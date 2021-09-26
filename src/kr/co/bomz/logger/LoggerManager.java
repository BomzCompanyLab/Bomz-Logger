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
 * bomz-logger �� ��ü���� ���� ���� ����
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public final class LoggerManager implements Runnable{
	
	// ��ϵǾ� �ִ� ��� �ΰ� ����
	private final Map<String, Logger> loggerMap = Collections.synchronizedMap(new HashMap<String, Logger>());
	
	// FileAppender ������
	private final List<FileAppender> fileAppenderList = Collections.synchronizedList(new ArrayList<FileAppender>());
	
	// �ý��� �ΰ� ���̵�
	public static final String ROOT_LOGGER_NAME = "root";
		
	// �ΰ� ������
	private final LoggerFactory factory = new LoggerFactory();
	
	// �ΰ� �������� ���濩�θ� �����ϴ� ������
	private ConfigFileObserve observe;
	// �ΰ� ����
	private File configFile;
	// �ʱ�ȭ ����
	private boolean init = false;
	
	private static final LoggerManager _this = new LoggerManager();
		
	private LoggerManager(){
		Thread th = new Thread(this);
		th.setDaemon(true);
		th.start();
	}
			
	/**
	 * 	�ֱ������� FileAppender �� ���� ������ ���Ϸ� ����ϰԲ� �Ѵ�
	 */
	public void run(){
		while( true ){
			try{		Thread.sleep(7000);		}catch(Exception e){}
			
			for(FileAppender fileAppender : this.fileAppenderList){
				fileAppender.flush();
			}
		}
	}
	
	/**		���� ������ ���� FileAppender ���		*/
	public final void addFileAppender(FileAppender appender){
		if( appender == null )		return;
		this.fileAppenderList.add(appender);
	}
	
	/**		���� ������ ���� ����� FileAppender ����		*/
	public void removeFileAppender(FileAppender appender){
		if( appender == null )		return;
		this.fileAppenderList.remove(appender);
	}
	
	/**
	 * �̱������� �̷���� LoggerManager ��ü ��û
	 * @return		LoggerManager
	 */
	public static final LoggerManager getInstance(){
		return _this;
	}
	
	/**
	 * �ΰ� ���� ���� ����
	 * @param configFile		������ �ΰ� ����
	 */
	final void setConfigFile(File configFile){
		if( configFile == null || !configFile.isFile() )		return;
		this.init(configFile);
	}
	
	/**
	 * bomz-logger �ʱ�ȭ ����
	 * @param configFile		���� ����. null �� ��� �⺻ ��������(/logger.xml) �̿�
	 */
	private void init(File configFile){	
		this.init = true;		// �ʱ�ȭ ȣ��Ǿ����Ƿ� ���°� ����
		
		ConfigFileReader cfReader = new ConfigFileReader();
		// �ΰ� ���� ���� ��� �˷���
		this.configFile = cfReader.setConfigFile(configFile);
		if( this.configFile == null ){
			LoggerManager.getInstance().getRootLogger().debug( 
					"�α� ���� ���� �˻� ���� (path=", 
					configFile == null ? "logger.xml" : configFile.getAbsolutePath(), 
					")"
				);
			return;
		}
		
		// �ΰ� ���� ���� �м� ���
		java.util.List<LoggerModel> list = cfReader.readConfigFile();
		synchronized( this.loggerMap ){
			this.clearLoggeMap();		// ���� ������ �ִ� ���� �ʱ�ȭ
			this.newInstanceLoggers(list);		// ���ο� �α� ��ü ����
			this.observe(cfReader.isObserve());		// ������ ��뿩�� �˻�
		}
	}
	
	/**
	 * bomz-logger �����
	 */
	public void reset(){
		this.init(this.configFile);
	}
		
	/*		�ΰ� �������� ���濩�� �ǽð� ���� ��� ����		*/
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
	
	/*		�ΰ� ���� ���� �� �ʱ�ȭ �� ���� �α� Appender ����		*/
	private void clearLoggeMap(){
		java.util.Iterator<Logger> values = this.loggerMap.values().iterator();
		while( values.hasNext() ){
			this.factory.closeAppender( values.next() );
		}
	}
	
	/*		���ο� �α� ����	�� ���		*/
	private void newInstanceLoggers(java.util.List<LoggerModel> list){
		
		// ���߿� �������� ������ �ٲ���� ��� ���� ������ ����ȭ ó���� ���� ���
		Map<String, Logger> tmpLoggerMap = new HashMap<String, Logger>(this.loggerMap.size());
		tmpLoggerMap.putAll(this.loggerMap);
		// synchronizationLogger(...) ���� �ߺ� ó���� ���� ���� ���
		Set<String> loggerNameSet = new HashSet<String>(list.size());
		
		// ���ο� �α� ���� �߰�
		this.newInstanceLoggers(list, list.size(), loggerNameSet);
		
		// ���� ������ ���� ��� �α� ��ü ����ȭ
		this.synchronizationLogger(tmpLoggerMap, loggerNameSet);
		
		// �ܺ� �α� ������� �˻�
		Extention.checkExtention();
		
	}
	
	/*		���� �α� ������ ���� ��츦 ���� ����ȭ ó��		*/
	private void synchronizationLogger(Map<String, Logger> tmpLoggerMap, Set<String> loggerNameSet){
		if( tmpLoggerMap.isEmpty() )		return;		// ����ִ� ������ ���� ���
		
		java.util.Iterator<String> keys = tmpLoggerMap.keySet().iterator();
		String key;
		LoggerImpl newLoggerImpl;
		while( keys.hasNext() ){
			key = keys.next();
			
			// ������ �̸��� �ִٸ� newInstanceLoggers(list, listSize) ���� ó����
			if( loggerNameSet.contains(key) )		continue;
			
			newLoggerImpl = (LoggerImpl)this.selectLoggerToPackageName(key, true);
			if( newLoggerImpl == null )		continue;
			
			this.factory.addAppender(tmpLoggerMap.get(key), newLoggerImpl.getLoggerModel());
		}
		
	}
	
	/*		�α� �������Ͽ� ���� �ʿ� ���		*/
	private void newInstanceLoggers(java.util.List<LoggerModel> list, final int listSize, Set<String> loggerNameSet){
		// ���ο� ���� �߰�
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
	 * �ΰ� �ʱ�ȭ ���� �˻�
	 */
	private synchronized void checkInit(){
		if( this.init )		return;
		
		this.init(null);
	}
	
	/**
	 * �ΰ� ���� ����
	 * @param loggerName		�ΰ� ��
	 * @return							�ΰ� ��ü
	 */
	Logger getLogger(String loggerName){
		// �ʱ�ȭ ���� �˻� �� �ʱ�ȭ ����
		this.checkInit();
		
		// ����� ���� �⺻ �ΰ� ����
		if( loggerName == null )		return this.factory.createLogger();
		
		Logger logger = this.loggerMap.get(loggerName);
		if( logger != null )		return logger;
		
		// ��Ű�������� �Ǿ����� ����� �̸� ã�� 
		logger = this.selectLoggerToPackageName(loggerName, false);
		
		if( logger != null ){
			return logger;
		}else{
			logger = this.factory.createLogger();
			this.loggerMap.put(loggerName, logger);
			return logger;
		}
	}
	
	/*		class �� �α׸��� �����Ǿ����� ��츦 ���� ��Ű�������� �α׼��� �˻�		*/
	private Logger selectLoggerToPackageName(String loggerName, boolean isSynchronization){
		Logger logger;
		String[] loggerNames = loggerName.split("\\.");
		int length = loggerNames.length;
		
		if( length == 0 && !isSynchronization )	{		// ��Ű������ �ƴ� ��� ó��
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
	 * �ý��� �α� ��û
	 * @return		�ý��� �ΰ�
	 */
	public Logger getRootLogger(){
		// �ʱ�ȭ ���� �˻� �� �ʱ�ȭ ����
		this.checkInit();
				
		Logger logger = this.loggerMap.get(ROOT_LOGGER_NAME);
		if( logger == null ){
			logger = this.factory.createDefaultLogger();
		}
		
		return logger;
	}
	
}
