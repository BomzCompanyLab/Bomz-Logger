package kr.co.bomz.logger.appender;

/**
 * 
 * 사용할 수 있는 Appender 종류
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public enum AppenderType {
	/**		콘솔 출력		*/
	CONSOLE,
	
	/**		파일 저장		*/
	FILE,
	
	/**		외부 서버로 TCP 전송		*/
	TCP,
	
	/**		HTTP 전송		*/
	HTTP,
	
	/**		사용자 구현 방식		*/
	CUSTOM
}
