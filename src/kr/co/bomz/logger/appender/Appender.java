package kr.co.bomz.logger.appender;

import java.nio.ByteBuffer;
import java.util.Map;

import kr.co.bomz.logger.Level;
import kr.co.bomz.logger.date.DateFormat;

/**
 * 
 * Console , File , TCP 등 출력방식에 따라 구현된 최상위 Appender  
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	ConsoleAppender
 * @see	CustomAppender
 * @see	TCPAppender
 * @see	FileAppender
 * @see	HTTPAppender
 *
 */
public abstract class Appender {
	

	/**		레벨 출력 시 시작 문자		*/
	protected static final String LEVEL_STX = " [";
	/**		레벨 출력 시 종료 문자		*/
	protected static final String LEVEL_ETX = "] ";
	/**		줄바꿈 문자		*/
	protected static final String NEW_LINE = "\n";
	/**		공백 문자		*/
	protected static final String BLANK = " ";
	
	/**
	 * FileAppender , TCPAppeder 에서 사용되는 버퍼 크기
	 */
	protected static final int BUFFER_SIZE = 500;
	
	/**		
	 * 별도의 날짜패턴을 지정했을 때 사용하는 로그 날짜 포맷 	
	 */
	public DateFormat dateFormat = null;
	
	/**
	 * 기본 생성자
	 */
	Appender(){}
	
	public void setDatePattern(String datePattern, String locale, String timeZone){
		if( datePattern != null ){
			try {
				this.dateFormat = new DateFormat(datePattern, locale, timeZone);
			} catch (Exception e) {}
		}
	}
	
	/**
	 * 발생한 로그 출력 메소드
	 * 
	 * @param level		로그 레벨
	 * @param dateBuffer		로그 발생 일자
	 * @param callStackBuffer		로그 발생 위치
	 * @param msgBuffer		로그 내용
	 * @param errBuffer			에러 내용
	 */
	public abstract void write(Level level, StringBuilder dateBuffer, 
			StringBuilder callStackBuffer, StringBuilder msgBuffer, StringBuilder errBuffer);
	
	/**
	 * Appender 종료
	 */
	public abstract void close();
	
	/**
	 * 로그 설정 파일에 설정된 파라메터 값 설정
	 * @param parameters		설정 파일에 설정된 값
	 */
	public abstract void setParameter(Map<String, String> parameters);
		
	/**
	 * TCPAppender , FileAppender 에서 사용되는 데이터 전송 메소드
	 * 
	 * @param bb		비어있는 버퍼
	 * @param msg	로그 내용
	 * @throws Exception	TCP / File 출력 실패 시 발생
	 */
	protected void write(ByteBuffer bb, byte[] msg) throws Exception{
		if( msg == null )			return;
		int length = msg.length;
		if( length <= 0 )				return;
		
		// 내용이 많을 경우를 대비해 여러번 나눠서 처리
		int width =  length>BUFFER_SIZE?BUFFER_SIZE:length;
		int index = 0;
		while( true ){
			if( width <= 0 )		break;
			
			bb.clear();
			bb.put( msg, index, width );
			bb.flip();
			write( bb );
			index = index + width;
			length = length - width;
			width = length > BUFFER_SIZE ? BUFFER_SIZE : length;
		}
		
	}
	
	/**
	 * TCPAppender , FileAppender 에서 사용되는 데이터 전송 메소드
	 * @param bb		데이터가 추가된 버퍼
	 * @throws Exception		TCP 또는 File 로 데이터 출력 실패 시 발생
	 */
	protected void write(ByteBuffer bb) throws Exception{}
	
}
