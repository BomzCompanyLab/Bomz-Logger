package kr.co.bomz.logger;

import java.io.File;

import kr.co.bomz.logger.date.DateFormat;

/**
 * 
 * 사용 예)<p>
 * 	<code>private static final Logger logger1 = Logger.getLogger("user_name");</code><p>
 * <code>private static final Logger logger2 = Logger.getLogger(user.TestMain.class);</code><p>
 * <code>logger1.debug("log test");</code><p>
 * <code>logger2.info("log test", 3, "End Test");</code><p>
 * <p><p>
 * 설정 파일 예)<p>
 * <code>&lt;logger name="user_name" type="console" level="debug"/&gt;</code><p>
 * <code>&lt;logger name="user_name" type="file" level="trace"&gt;</code><p>
 * <code>&nbsp;&nbsp;&nbsp;&lt;param name="dir"&gt;/log/mylog.log&lt;/param&gt;</code><p>
 * <code>&nbsp;&nbsp;&nbsp;&lt;param name="size"&gt;3&lt;/param&gt;</code><p>
 * <code>&nbsp;&nbsp;&nbsp;&lt;param name="period"&gt;day&lt;/param&gt;</code><p>
 * <code>&lt;/logger&gt;</code><p>
 * <code>&lt;logger name="user.TestMain" type="console" level="debug"/&gt;</code><p>
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public abstract class Logger {

	private static final String CLASS_NAME = Logger.class.getName();
	
	private static final Object[] NULL_OBJ_ARRAY = null;
	
	private static final String EXTENTION_PACKAGE = "kr.co.bomz.logging.extention";
	private static final String SLF4J_PACKAGE = "org.slf4j.impl.BomzLoggerAdapter";
	
	/**		
	 * 별도의 날짜패턴을 지정하지 않았을 경우 공통으로 사용하는 기본 형식의 로그 날짜 포맷 	
	 */
	protected DateFormat dateFormat = new DateFormat();

	Logger(){}
	
	/**
	 * 로거 요청
	 * 
	 * @param loggerName		로그 이름
	 * @return							로거
	 */
	public static final Logger getLogger(String loggerName){
		return LoggerManager.getInstance().getLogger(loggerName);
	}
	
	/**
	 * 로거 요청
	 * 
	 * @param loggerClass		로그로 등록할 클래스
	 * @return							로거
	 */
	public static final Logger getLogger(Class<?> loggerClass){
		return Logger.getLogger(loggerClass.getName());
	}
	
	/**
	 * 해당 로그 레벨이 사용되는지 여부
	 * @param level	로그 레벨
	 * @return 사용될 경우 true
	 */
	public abstract boolean isLoggable(Level level);
	
	/**		사용자 지정 Level 로그 출력		 */
	public void log(Level level, Object ... msg){
		this.log(level, null, msg);
	}
	/**		사용자 지정 Level 로그 출력		*/
	public void log(Level level, Throwable err){
		this.log(level, err, NULL_OBJ_ARRAY);
	}
		
	/**		trace Level 로그 출력		 */
	public void trace(Object ... msg){
		this.log(Level.TRACE, null, msg);
	}
	/**		trace Level 로그 출력		*/
	public void trace(Throwable err){
		this.log(Level.TRACE, err, NULL_OBJ_ARRAY);
	}
	/**		trace Level 로그 출력		*/
	public void trace(Throwable err, Object ... msg){
		this.log(Level.TRACE, err, msg);
	}
	
	/**		debug Level 로그 출력		 */
	public void debug(Object ... msg){
		this.log(Level.DEBUG, null, msg);
	}
	/**		debug Level 로그 출력		*/
	public void debug(Throwable err){
		this.log(Level.DEBUG, err, NULL_OBJ_ARRAY);
	}
	/**		debug Level 로그 출력		*/
	public void debug(Throwable err, Object ... msg){
		this.log(Level.DEBUG, err, msg);
	}
	
	/**		info Level 로그 출력		 */
	public void info(Object ... msg){
		this.log(Level.INFO, null, msg);
	}
	/**		info Level 로그 출력		*/
	public void info(Throwable err){
		this.log(Level.INFO, err, NULL_OBJ_ARRAY);
	}
	/**		info Level 로그 출력		*/
	public void info(Throwable err, Object ... msg){
		this.log(Level.INFO, err, msg);
	}
	
	/**		warn Level 로그 출력		 */
	public void warn(Object ... msg){
		this.log(Level.WARN, null, msg);
	}
	/**		warn Level 로그 출력		*/
	public void warn(Throwable err){
		this.log(Level.WARN, err, NULL_OBJ_ARRAY);
	}
	/**		warn Level 로그 출력		*/
	public void warn(Throwable err, Object ... msg){
		this.log(Level.WARN, err, msg);
	}
	
	/**		error Level 로그 출력		 */
	public void error(Object ... msg){
		this.log(Level.ERROR, null, msg);
	}
	/**		error Level 로그 출력		*/
	public void error(Throwable err){
		this.log(Level.ERROR, err, NULL_OBJ_ARRAY);
	}
	/**		error Level 로그 출력		*/
	public void error(Throwable err, Object ... msg){
		this.log(Level.ERROR, err, msg);
	}
	
	/**		fatal Level 로그 출력		 */
	public void fatal(Object ... msg){
		this.log(Level.FATAL, null, msg);
	}
	/**		fatal Level 로그 출력		*/
	public void fatal(Throwable err){
		this.log(Level.FATAL, err, NULL_OBJ_ARRAY);
	}
	/**		fatal Level 로그 출력		*/
	public void fatal(Throwable err, Object ... msg){
		this.log(Level.FATAL, err, msg);
	}
	
	/**
	 * 로그 출력
	 * 
	 * @param level		로그 레벨
	 * @param err			에러
	 * @param msg		로그 내용
	 */
	public abstract void log(Level level, Throwable err, Object ... msg);
	
	/**
	 * 예외 출력시 예외 내용 분석
	 * @param err		에러
	 * @return			에러 내용 분석 결과 
	 */
	protected StringBuilder getThrowableValue(Throwable err){
		if( err == null )		return null;
		
		StringBuilder buffer = new StringBuilder();
		StackTraceElement[] trace = err.getStackTrace();
		buffer.append(err.toString());
		for(StackTraceElement t : trace)	buffer.append("\n\tat " + t);
		buffer.append('\n');
		return buffer;
	}
	
	/**
	 * 로그를 호출한 스텍 정보 분석
	 * @param stack		로그 발생 스텍
	 * @return				로그 발생 스텍 분석 결과
	 */
	protected StringBuilder getCallStackTraceValue(StackTraceElement[] stack){
		String stackClassName;
		for(int i=3; i < stack.length; i++){
			stackClassName = stack[i].getClassName();
			if( stackClassName.startsWith(EXTENTION_PACKAGE) )		continue;
			if( stackClassName.startsWith(SLF4J_PACKAGE) )				continue;
			
			if( !stackClassName.equals(CLASS_NAME) )		return new StringBuilder(stack[i].toString());
		}
				
		// 원하는 내용을 찾지 못했을 경우
		return new StringBuilder(stack[stack.length -1].toString());
	}
	
	/**
	 * 로그 내용 분석
	 * 
	 * @param msg		로그 내용
	 * @return				로그 내용 분석 결과
	 */
	protected StringBuilder getMessageValue(Object ... msg){
		if( msg == null || msg.length <= 0 )		return null;
		
		StringBuilder buffer = new StringBuilder();
		int length = msg.length;
		for(int index = 0; index < length; index++){
			buffer.append(msg[index]);
		}
		
		return buffer;
	}
		
	/**
	 * 로그 파일 변경
	 * @param configFilePath		변경할 로그 파일 경로
	 */
	public static void setLogConfigFile(String configFilePath){
		Logger.setLogConfigFile(new File(configFilePath));
	}
	
	/**
	 * 로그 파일 변경
	 * @param configFile			변경할 로그 파일
	 */
	public static void setLogConfigFile(File configFile){
		LoggerManager.getInstance().setConfigFile(configFile);
	}

	public String getName(){
		return CLASS_NAME;
	}
	
}
