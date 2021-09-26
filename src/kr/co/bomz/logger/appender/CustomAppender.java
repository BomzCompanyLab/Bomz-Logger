package kr.co.bomz.logger.appender;

/**
 * 
 * ����ڰ� ���� Appender ���� �� �ش� Ŭ������ ��ӹ޾� �����ؾ� �Ѵ�<p>
 * �α� �������Ͽ� type �Ӽ� ���� 'custom ���� ���� ��<p>
 * param ������Ʈ �Ӽ� name='class' , �� 'package.class' �� �����Ͽ� ������ �� �ִ�<p>
 *  
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	ConsoleAppender
 * @see	TCPAppender
 * @see	FileAppender
 * @see	HTTPAppender
 *
 */
public abstract class CustomAppender extends Appender{

	/**
	 * CustomAppender �� ������ Ŭ������ ��θ� ������<p>
	 * �ΰ� ���� ������ param ������Ʈ name �Ӽ� ��
	 */
	public static final String CLASS = "class";
	
	public CustomAppender(){}
	
}
