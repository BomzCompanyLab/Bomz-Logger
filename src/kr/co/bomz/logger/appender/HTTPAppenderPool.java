package kr.co.bomz.logger.appender;

import kr.co.bomz.util.threadpool.BomzThread;
import kr.co.bomz.util.threadpool.BomzThreadPool;

/**
 * HTTP ���� �ӵ� ������ ���� ����ϴ� ������ Ǯ.<p>
 * ����� ���ؼ��� Bomz-util1.x.jar �̻��� �ʿ��ϴ�<p>
 * 
 * ��� ���� ��)<p>
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
	
	/**		������ ������ ���� �ʾ��� ��� ������ Ǯ �⺻ ũ��		*/
	public static final int DEFAULT_POOL_SIZE = 3;
	/**		����� �� �ִ� �ִ� ������ Ǯ ũ��		*/
	public static final int MAX_POOL_SIZE = 10;
	
	/**
	 * ThreadPool ����� ���� �⺻ ������
	 */
	public HTTPAppenderPool(){
		
	}
	
	/**
	 * HTTP Appender �� ������ Ǯ ���
	 * @param appender		HTTPAppender
	 * @param size				����� ������ Ǯ ũ��
	 * @throws Throwable		Bomz-util1.x.jar �� ���� ��� �߻�
	 */
	HTTPAppenderPool(HTTPAppender appender, int size) throws Throwable{
		this.pool = new BomzThreadPool(
				size > 0 && size <= MAX_POOL_SIZE ? size : DEFAULT_POOL_SIZE,
				size > 0 && size <= MAX_POOL_SIZE ? size : MAX_POOL_SIZE,
				HTTPAppenderPool.class, 
				appender
			);
	
	}
	
	/**			HTTP ����		*/
	void start(){
		this.pool.getThread().start();
	}
	
	/**			������ Ǯ ����		*/
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
