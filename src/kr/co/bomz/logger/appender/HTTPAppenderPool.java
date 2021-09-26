package kr.co.bomz.logger.appender;

import kr.co.bomz.util.threadpool.BomzThread;
import kr.co.bomz.util.threadpool.BomzThreadPool;

/**
 * HTTP 전송 속도 보완을 위해 사용하는 스레드 풀.<p>
 * 사용을 위해서는 Bomz-util1.x.jar 이상이 필요하다<p>
 * 
 * 사용 설정 예)<p>
 * <code>
 * 		... <br>
 * 		&lt;logger name="root" type="http" level="(debug|error)"&gt;<br>
 *		&nbsp;&nbsp;&nbsp;&lt;param name="url"&gt;http://www.bomz.co.kr&lt;/param&gt;<br>
 *		&nbsp;&nbsp;&nbsp;&lt;param name="method"&gt;POST&lt;/param&gt;<br>
 *		&nbsp;&nbsp;&nbsp;&lt;param name="poolUse"&gt;true&lt;/param&gt;<br>
 *		&nbsp;&nbsp;&nbsp;&lt;param name="poolSize"&gt;5&lt;/param&gt;<br>
 *		&lt;/logger&gt;<br>
 *		... <br>
 * </code>
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class HTTPAppenderPool extends BomzThread{

	private BomzThreadPool pool;
	
	private HTTPAppender appender;
	
	/**		별도의 설정을 하지 않았을 경우 스레드 풀 기본 크기		*/
	public static final int DEFAULT_POOL_SIZE = 3;
	/**		사용할 수 있는 최대 스레드 풀 크기		*/
	public static final int MAX_POOL_SIZE = 10;
	
	/**
	 * ThreadPool 사용을 위한 기본 생성자
	 */
	public HTTPAppenderPool(){
		
	}
	
	/**
	 * HTTP Appender 의 스레드 풀 사용
	 * @param appender		HTTPAppender
	 * @param size				사용할 스레드 풀 크기
	 * @throws Throwable		Bomz-util1.x.jar 가 없을 경우 발생
	 */
	HTTPAppenderPool(HTTPAppender appender, int size) throws Throwable{
		this.pool = new BomzThreadPool(
				size > 0 && size <= MAX_POOL_SIZE ? size : DEFAULT_POOL_SIZE,
				size > 0 && size <= MAX_POOL_SIZE ? size : MAX_POOL_SIZE,
				HTTPAppenderPool.class, 
				appender
			);
	
	}
	
	/**			HTTP 전송		*/
	void start(){
		this.pool.getThread().start();
	}
	
	/**			스레드 풀 종료		*/
	void closeThreadPool(){
		this.pool.closeThreadPool();
	}
	
	@Override
	protected void execute() throws Exception {
		this.appender.write();		// http send
	}
	
	@Override
	protected void constructorParameter(Object ... param){
		this.appender = (HTTPAppender)param[0];
	}
		
}
