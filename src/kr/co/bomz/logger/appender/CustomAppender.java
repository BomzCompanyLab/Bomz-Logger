package kr.co.bomz.logger.appender;

/**
 * 
 * 사용자가 직접 Appender 구현 시 해당 클래스를 상속받아 구현해야 한다<p>
 * 로그 설정파일에 type 속성 값을 'custom 으로 설정 후<p>
 * param 엘리먼트 속성 name='class' , 값 'package.class' 로 선언하여 적용할 수 있다<p>
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
	 * CustomAppender 를 구현한 클래스의 경로를 설정한<p>
	 * 로거 설정 파일의 param 엘리먼트 name 속성 값
	 */
	public static final String CLASS = "class";
	
	public CustomAppender(){}
	
}
