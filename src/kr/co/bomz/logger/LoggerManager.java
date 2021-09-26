package kr.co.bomz.logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.co.bomz.logger.LoggerFactory.LoggerImpl;
import kr.co.bomz.logger.appender.FileAppender;
import kr.co.bomz.logger.configure.ConfigFileObserve;
import kr.co.bomz.logger.configure.ConfigFileReader;
import kr.co.bomz.logger.extention.Extention;

/**
 * 
 * bomz-logger 의 전체적인 동작 관리 수행
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public final class LoggerManager implements Runnable{
	
	// 등록되어 있는 모든 로거 관리
	private final Map<String, Logger> loggerMap = Collections.synchronizedMap(new HashMap<String, Logger>());
	
	// FileAppender 관리용
	private final List<FileAppender> fileAppenderList = Collections.synchronizedList(new ArrayList<FileAppender>());
	
	// 시스템 로거 아이디
	public static final String ROOT_LOGGER_NAME = "root";
		
	// 로거 생성자
	private final LoggerFactory factory = new LoggerFactory();
	
	// 로거 설정파일 변경여부를 관찰하는 옵저버
	private ConfigFileObserve observe;
	// 로거 파일
	private File configFile;
	// 초기화 여부
	private boolean init = false;
	
	private static final LoggerManager _this = new LoggerManager();
		
	private LoggerManager(){
		Thread th = new Thread(this);
		th.setDaemon(true);
		th.start();
	}
			
	/**
	 * 	주기적으로 FileAppender 의 버퍼 내용을 파일로 출력하게끔 한다
	 */
	public void run(){
		while( true ){
			try{		Thread.sleep(7000);		}catch(Exception e){}
			
			for(FileAppender fileAppender : this.fileAppenderList){
				fileAppender.flush();
			}
		}
	}
	
	/**		버퍼 관리를 위해 FileAppender 등록		*/
	public final void addFileAppender(FileAppender appender){
		if( appender == null )		return;
		this.fileAppenderList.add(appender);
	}
	
	/**		버퍼 관리를 위해 등록한 FileAppender 삭제		*/
	public void removeFileAppender(FileAppender appender){
		if( appender == null )		return;
		this.fileAppenderList.remove(appender);
	}
	
	/**
	 * 싱글톤으로 이루어진 LoggerManager 객체 요청
	 * @return		LoggerManager
	 */
	public static final LoggerManager getInstance(){
		return _this;
	}
	
	/**
	 * 로거 설정 파일 변경
	 * @param configFile		변경할 로거 파일
	 */
	final void setConfigFile(File configFile){
		if( configFile == null || !configFile.isFile() )		return;
		this.init(configFile);
	}
	
	/**
	 * bomz-logger 초기화 수행
	 * @param configFile		설정 파일. null 일 경우 기본 설정파일(/logger.xml) 이용
	 */
	private void init(File configFile){	
		this.init = true;		// 초기화 호출되었으므로 상태값 변경
		
		ConfigFileReader cfReader = new ConfigFileReader();
		// 로거 설정 파일 경로 알려줌
		this.configFile = cfReader.setConfigFile(configFile);
		if( this.configFile == null ){
			LoggerManager.getInstance().getRootLogger().debug( 
					"로그 설정 파일 검색 실패 (path=", 
					configFile == null ? "logger.xml" : configFile.getAbsolutePath(), 
					")"
				);
			return;
		}
		
		// 로거 설정 파일 분석 결과
		java.util.List<LoggerModel> list = cfReader.readConfigFile();
		synchronized( this.loggerMap ){
			this.clearLoggeMap();		// 현재 가지고 있는 정보 초기화
			this.newInstanceLoggers(list);		// 새로운 로그 객체 생성
			this.observe(cfReader.isObserve());		// 옵저버 사용여부 검사
		}
	}
	
	/**
	 * bomz-logger 재시작
	 */
	public void reset(){
		this.init(this.configFile);
	}
		
	/*		로거 설정파일 변경여부 실시간 감시 사용 여부		*/
	private void observe(boolean observe){
		if( !observe ){
			if( this.observe != null ){
				this.observe.observe(false);
				this.observe = null;
			}
		}else{
			if( this.observe == null )		this.observe = new ConfigFileObserve();
			this.observe.setConfigFile(this.configFile);
			this.observe.observe(true);
		}
	}
	
	/*		로거 정보 관리 맵 초기화 및 기존 로그 Appender 종료		*/
	private void clearLoggeMap(){
		java.util.Iterator<Logger> values = this.loggerMap.values().iterator();
		while( values.hasNext() ){
			this.factory.closeAppender( values.next() );
		}
	}
	
	/*		새로운 로그 생성	및 등록		*/
	private void newInstanceLoggers(java.util.List<LoggerModel> list){
		
		// 나중에 설정파일 내용이 바뀌었을 경우 기존 정보와 동기화 처리를 위해 사용
		Map<String, Logger> tmpLoggerMap = new HashMap<String, Logger>(this.loggerMap.size());
		tmpLoggerMap.putAll(this.loggerMap);
		// synchronizationLogger(...) 에서 중복 처리를 막기 위해 사용
		Set<String> loggerNameSet = new HashSet<String>(list.size());
		
		// 새로운 로그 내용 추가
		this.newInstanceLoggers(list, list.size(), loggerNameSet);
		
		// 기존 내용이 있을 경우 로그 객체 동기화
		this.synchronizationLogger(tmpLoggerMap, loggerNameSet);
		
		// 외부 로그 연동모듈 검사
		Extention.checkExtention();
		
	}
	
	/*		기존 로그 내용이 있을 경우를 위한 동기화 처리		*/
	private void synchronizationLogger(Map<String, Logger> tmpLoggerMap, Set<String> loggerNameSet){
		if( tmpLoggerMap.isEmpty() )		return;		// 들어있는 내용이 없을 경우
		
		java.util.Iterator<String> keys = tmpLoggerMap.keySet().iterator();
		String key;
		LoggerImpl newLoggerImpl;
		while( keys.hasNext() ){
			key = keys.next();
			
			// 동일한 이름이 있다면 newInstanceLoggers(list, listSize) 에서 처리됨
			if( loggerNameSet.contains(key) )		continue;
			
			newLoggerImpl = (LoggerImpl)this.selectLoggerToPackageName(key, true);
			if( newLoggerImpl == null )		continue;
			
			this.factory.addAppender(tmpLoggerMap.get(key), newLoggerImpl.getLoggerModel());
		}
		
	}
	
	/*		로그 설정파일에 따라 맵에 등록		*/
	private void newInstanceLoggers(java.util.List<LoggerModel> list, final int listSize, Set<String> loggerNameSet){
		// 새로운 정보 추가
		LoggerModel model;
		Logger logger;
		String loggerName;
		for(int i=0; i < listSize; i++){
			model = list.get(i);
			loggerName = model.getLoggerName();
			loggerNameSet.add(loggerName);
			logger = this.loggerMap.get( loggerName );
			if( logger == null ){
				logger = this.factory.createLogger();
				this.loggerMap.put(loggerName, logger);
			}
			this.factory.addAppender(logger, model);
			
		}
	}
	
	/**
	 * 로거 초기화 여부 검사
	 */
	private synchronized void checkInit(){
		if( this.init )		return;
		
		this.init(null);
	}
	
	/**
	 * 로거 정보 리턴
	 * @param loggerName		로거 명
	 * @return							로거 객체
	 */
	Logger getLogger(String loggerName){
		// 초기화 여부 검사 후 초기화 수행
		this.checkInit();
		
		// 기능이 없는 기본 로거 리턴
		if( loggerName == null )		return this.factory.createLogger();
		
		Logger logger = this.loggerMap.get(loggerName);
		if( logger != null )		return logger;
		
		// 패키지명으로 되어있을 경우의 이름 찾기 
		logger = this.selectLoggerToPackageName(loggerName, false);
		
		if( logger != null ){
			return logger;
		}else{
			logger = this.factory.createLogger();
			this.loggerMap.put(loggerName, logger);
			return logger;
		}
	}
	
	/*		class 로 로그명이 설정되어있을 경우를 위해 피키지명으로 로그설정 검색		*/
	private Logger selectLoggerToPackageName(String loggerName, boolean isSynchronization){
		Logger logger;
		String[] loggerNames = loggerName.split("\\.");
		int length = loggerNames.length;
		
		if( length == 0 && !isSynchronization )	{		// 패키지명이 아닐 경우 처리
			logger = this.factory.createLogger();
			this.loggerMap.put(loggerName, logger);
			return logger;
		}
		
		StringBuilder buffer = new StringBuilder();
		while( length-- > 1 ){
			for(int i=0; i < length; i++){
				buffer.append(loggerNames[i]);
				if( (i+1) < length )		buffer.append(".");
			}
			logger = this.loggerMap.get(buffer.toString());
			if( logger != null )		return logger;
			buffer.delete(0, buffer.capacity());
		}
		
		return null;
	}
	
	/**
	 * 시스템 로그 요청
	 * @return		시스템 로거
	 */
	public Logger getRootLogger(){
		// 초기화 여부 검사 후 초기화 수행
		this.checkInit();
				
		Logger logger = this.loggerMap.get(ROOT_LOGGER_NAME);
		if( logger == null ){
			logger = this.factory.createDefaultLogger();
		}
		
		return logger;
	}
	
}
