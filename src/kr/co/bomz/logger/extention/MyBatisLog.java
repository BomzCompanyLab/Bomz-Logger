package kr.co.bomz.logger.extention;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import kr.co.bomz.logger.Logger;

/**
 * Apache MyBatis ���� �α�
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
	 * Apache MyBatis Logger ��� ���� �˻縦 ���� �⺻ ������
	 */
	public MyBatisLog(){
		this.logger = null;
	}
	
	/**
	 * Apache MyBatis Logger ��� �� �ΰ� ����� ���� ���Ǵ� ������
	 * @param clazz		�ΰŸ�
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
//		���� �������Ͽ��� (���ӽ����̽�.������) ���� �� �α� ó���� ���� �̷��� �� 
//		return logger.isLoggable(Level.DEBUG);
		return true;
	}

	@Override
	public boolean isTraceEnabled() {
//		���� �������Ͽ��� (���ӽ����̽�.������) ���� �� �α� ó���� ���� �̷��� ��
//		return logger.isLoggable(Level.TRACE);
		return true;
	}
}
