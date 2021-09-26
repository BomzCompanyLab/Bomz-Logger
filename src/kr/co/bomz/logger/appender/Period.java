package kr.co.bomz.logger.appender;

/**
 * FileAppender 사용 시 사용되는 설정 값</p>
 * 로그 파일 생성 주기
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	FileAppender
 */
public enum Period {
	/**		별도의 주기를 선언하지 않는다		*/
	NON,
	
	/**		시간 단위로 로그 파일 생성		*/
	HOUR,
	
	/**		일 단위로 로그 파일 생성		*/
	DAY
}
