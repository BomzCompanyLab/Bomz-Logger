package kr.co.bomz.logger.appender;

/**
 * 
 * ����� �� �ִ� Appender ����
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public enum AppenderType {
	/**		�ܼ� ���		*/
	CONSOLE,
	
	/**		���� ����		*/
	FILE,
	
	/**		�ܺ� ������ TCP ����		*/
	TCP,
	
	/**		HTTP ����		*/
	HTTP,
	
	/**		����� ���� ���		*/
	CUSTOM
}
