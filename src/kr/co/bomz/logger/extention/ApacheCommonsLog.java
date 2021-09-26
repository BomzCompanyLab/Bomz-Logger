package kr.co.bomz.logger.extention;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import kr.co.bomz.logger.Level;
import kr.co.bomz.logger.Logger;

/**
 * Apache Commons Logger 연동 로그
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	MyBatisLog
 *
 */
public class ApacheCommonsLog implements ExtentionLogger, Log, Serializable{

	private static final long serialVersionUID = -7926647339891222950L;

	private final Logger logger;
	
	/**
	 * Apache Commons Logger 사용 여부 검사를 위한 기본 생성자
	 */
	ApacheCommonsLog(){
		this.logger = null;
	}
	
	/**
	 * Apache Commons Logger 사용 시 로거 등록을 위해 사용되는 생성자
	 * @param loggerName		로거명
	 */
	public ApacheCommonsLog(String loggerName){
		this.logger = Logger.getLogger(loggerName);
	}
	
	@Override
	public void useLogger() throws Throwable{
		LogFactoryImpl.releaseAll();
		System.setProperty(LogFactoryImpl.LOG_PROPERTY, ApacheCommonsLog.class.getName());
	}
	
	@Override
	public void debug(Object msg) {
		logger.debug(msg);
	}

	@Override
	public void debug(Object msg, Throwable err) {
		this.logger.debug(err, msg);
	}

	@Override
	public void error(Object msg) {
		this.logger.error(msg);
	}

	@Override
	public void error(Object msg, Throwable err) {
		this.logger.error(err, msg);
	}

	@Override
	public void fatal(Object msg) {
		this.logger.fatal(msg);
	}

	@Override
	public void fatal(Object msg, Throwable err) {
		this.logger.fatal(err, msg);
	}

	@Override
	public void info(Object msg) {
		this.logger.info(msg);
	}

	@Override
	public void info(Object msg, Throwable err) {
		this.logger.info(err, msg);
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isLoggable(Level.DEBUG);
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isLoggable(Level.ERROR);
	}

	@Override
	public boolean isFatalEnabled() {
		return logger.isLoggable(Level.FATAL);
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isLoggable(Level.INFO);
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isLoggable(Level.TRACE);
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isLoggable(Level.WARN);
	}

	@Override
	public void trace(Object msg) {
		this.logger.trace(msg);
	}

	@Override
	public void trace(Object msg, Throwable err) {
		this.logger.trace(err, msg);
	}

	@Override
	public void warn(Object msg) {
		this.logger.warn(msg);
	}

	@Override
	public void warn(Object msg, Throwable err) {
		this.logger.warn(err, msg);
	}

}
