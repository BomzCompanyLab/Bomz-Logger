package kr.co.bomz.logger.extention;

/**
 * 외부 연동 라이브러리 로그 처리를위한 인터페이스
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	MyBatisLog
 * @see	ApacheCommonsLog
 *
 */
public interface ExtentionLogger {

	/**
	 * Bomz logger 를 사용할 수 있게 등록 요청한다
	 * @throws Throwable		해당 라이브러리가 임포트되지 않았을 경우 발생
	 */
	void useLogger() throws Throwable;
}
