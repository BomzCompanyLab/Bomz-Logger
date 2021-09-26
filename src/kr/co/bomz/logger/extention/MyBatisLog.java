package kr.co.bomz.logger.extention;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import kr.co.bomz.logger.Logger;

/**
 * Apache MyBatis 연동 로그
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	ApacheCommonsLog
 *
 */
public class MyBatisLog implements ExtentionLogger, Log{
	
	private final Logger logger;
	private Logger subLogger = null;		// query name logger (namespace + queryname)
	
	/**
	 * Apache MyBatis Logger 사용 여부 검사를 위한 기본 생성자
	 */
	public MyBatisLog(){
		this.logger = null;
	}
	
	/**
	 * Apache MyBatis Logger 사용 시 로거 등록을 위해 사용되는 생성자
	 * @param clazz		로거명
	 */
	public MyBatisLog(String clazz){
		if( clazz.startsWith("org.apache.ibatis") ){
			this.logger = Logger.getLogger(clazz);
		}else{
			this.logger = Logger.getLogger("org.apache.ibatis");
			this.subLogger = Logger.getLogger(clazz);
		}
	}
	
	@Override
	public void useLogger() throws Throwable{
		LogFactory.useCustomLogging(MyBatisLog.class);
	}
	
	@Override
	public void debug(String msg) {
		this.logger.debug(msg);
		if( this.subLogger != null )		this.subLogger.debug(msg);
	}

	@Override
	public void error(String msg) {
		this.logger.error(msg);
		if( this.subLogger != null )		this.subLogger.error(msg);
	}

	@Override
	public void error(String msg, Throwable err) {
		this.logger.error(err, msg);
		if( this.subLogger != null )		this.subLogger.error(err, msg);
	}

	@Override
	public void trace(String msg) {
		this.logger.trace(msg);
		if( this.subLogger != null )		this.subLogger.trace(msg);
	}

	@Override
	public void warn(String msg) {
		this.logger.warn(msg);
		if( this.subLogger != null )		this.subLogger.warn(msg);
	}

	@Override
	public boolean isDebugEnabled() {
//		쿼리 설정파일에서 (네임스페이스.쿼리명) 으로 된 로그 처리를 위해 이렇게 함 
//		return logger.isLoggable(Level.DEBUG);
		return true;
	}

	@Override
	public boolean isTraceEnabled() {
//		쿼리 설정파일에서 (네임스페이스.쿼리명) 으로 된 로그 처리를 위해 이렇게 함
//		return logger.isLoggable(Level.TRACE);
		return true;
	}
}
