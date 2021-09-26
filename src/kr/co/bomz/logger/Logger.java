package kr.co.bomz.logger;

import java.io.File;

import kr.co.bomz.logger.date.DateFormat;

/**
 * 
 * ��� ��)<p>
 * 	<code>private static final Logger logger1 = Logger.getLogger("user_name");</code><p>
 * <code>private static final Logger logger2 = Logger.getLogger(user.TestMain.class);</code><p>
 * <code>logger1.debug("log test");</code><p>
 * <code>logger2.info("log test", 3, "End Test");</code><p>
 * <p><p>
 * ���� ���� ��)<p>
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
	 * ������ ��¥������ �������� �ʾ��� ��� �������� ����ϴ� �⺻ ������ �α� ��¥ ���� 	
	 */
	protected DateFormat dateFormat = new DateFormat();

	Logger(){}
	
	/**
	 * �ΰ� ��û
	 * 
	 * @param loggerName		�α� �̸�
	 * @return							�ΰ�
	 */
	public static final Logger getLogger(String loggerName){
		return LoggerManager.getInstance().getLogger(loggerName);
	}
	
	/**
	 * �ΰ� ��û
	 * 
	 * @param loggerClass		�α׷� ����� Ŭ����
	 * @return							�ΰ�
	 */
	public static final Logger getLogger(Class<?> loggerClass){
		return Logger.getLogger(loggerClass.getName());
	}
	
	/**
	 * �ش� �α� ������ ���Ǵ��� ����
	 * @param level	�α� ����
	 * @return ���� ��� true
	 */
	public abstract boolean isLoggable(Level level);
	
	/**		����� ���� Level �α� ���		 */
	public void log(Level level, Object ... msg){
		this.log(level, null, msg);
	}
	/**		����� ���� Level �α� ���		*/
	public void log(Level level, Throwable err){
		this.log(level, err, NULL_OBJ_ARRAY);
	}
		
	/**		trace Level �α� ���		 */
	public void trace(Object ... msg){
		this.log(Level.TRACE, null, msg);
	}
	/**		trace Level �α� ���		*/
	public void trace(Throwable err){
		this.log(Level.TRACE, err, NULL_OBJ_ARRAY);
	}
	/**		trace Level �α� ���		*/
	public void trace(Throwable err, Object ... msg){
		this.log(Level.TRACE, err, msg);
	}
	
	/**		debug Level �α� ���		 */
	public void debug(Object ... msg){
		this.log(Level.DEBUG, null, msg);
	}
	/**		debug Level �α� ���		*/
	public void debug(Throwable err){
		this.log(Level.DEBUG, err, NULL_OBJ_ARRAY);
	}
	/**		debug Level �α� ���		*/
	public void debug(Throwable err, Object ... msg){
		this.log(Level.DEBUG, err, msg);
	}
	
	/**		info Level �α� ���		 */
	public void info(Object ... msg){
		this.log(Level.INFO, null, msg);
	}
	/**		info Level �α� ���		*/
	public void info(Throwable err){
		this.log(Level.INFO, err, NULL_OBJ_ARRAY);
	}
	/**		info Level �α� ���		*/
	public void info(Throwable err, Object ... msg){
		this.log(Level.INFO, err, msg);
	}
	
	/**		warn Level �α� ���		 */
	public void warn(Object ... msg){
		this.log(Level.WARN, null, msg);
	}
	/**		warn Level �α� ���		*/
	public void warn(Throwable err){
		this.log(Level.WARN, err, NULL_OBJ_ARRAY);
	}
	/**		warn Level �α� ���		*/
	public void warn(Throwable err, Object ... msg){
		this.log(Level.WARN, err, msg);
	}
	
	/**		error Level �α� ���		 */
	public void error(Object ... msg){
		this.log(Level.ERROR, null, msg);
	}
	/**		error Level �α� ���		*/
	public void error(Throwable err){
		this.log(Level.ERROR, err, NULL_OBJ_ARRAY);
	}
	/**		error Level �α� ���		*/
	public void error(Throwable err, Object ... msg){
		this.log(Level.ERROR, err, msg);
	}
	
	/**		fatal Level �α� ���		 */
	public void fatal(Object ... msg){
		this.log(Level.FATAL, null, msg);
	}
	/**		fatal Level �α� ���		*/
	public void fatal(Throwable err){
		this.log(Level.FATAL, err, NULL_OBJ_ARRAY);
	}
	/**		fatal Level �α� ���		*/
	public void fatal(Throwable err, Object ... msg){
		this.log(Level.FATAL, err, msg);
	}
	
	/**
	 * �α� ���
	 * 
	 * @param level		�α� ����
	 * @param err			����
	 * @param msg		�α� ����
	 */
	public abstract void log(Level level, Throwable err, Object ... msg);
	
	/**
	 * ���� ��½� ���� ���� �м�
	 * @param err		����
	 * @return			���� ���� �м� ��� 
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
	 * �α׸� ȣ���� ���� ���� �м�
	 * @param stack		�α� �߻� ����
	 * @return				�α� �߻� ���� �м� ���
	 */
	protected StringBuilder getCallStackTraceValue(StackTraceElement[] stack){
		String stackClassName;
		for(int i=3; i < stack.length; i++){
			stackClassName = stack[i].getClassName();
			if( stackClassName.startsWith(EXTENTION_PACKAGE) )		continue;
			if( stackClassName.startsWith(SLF4J_PACKAGE) )				continue;
			
			if( !stackClassName.equals(CLASS_NAME) )		return new StringBuilder(stack[i].toString());
		}
				
		// ���ϴ� ������ ã�� ������ ���
		return new StringBuilder(stack[stack.length -1].toString());
	}
	
	/**
	 * �α� ���� �м�
	 * 
	 * @param msg		�α� ����
	 * @return				�α� ���� �м� ���
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
	 * �α� ���� ����
	 * @param configFilePath		������ �α� ���� ���
	 */
	public static void setLogConfigFile(String configFilePath){
		Logger.setLogConfigFile(new File(configFilePath));
	}
	
	/**
	 * �α� ���� ����
	 * @param configFile			������ �α� ����
	 */
	public static void setLogConfigFile(File configFile){
		LoggerManager.getInstance().setConfigFile(configFile);
	}

	public String getName(){
		return CLASS_NAME;
	}
	
}
