package kr.co.bomz.logger.date;

/**
 * 날짜 패턴 분석 실패 시 발생
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class DatePatternException extends Exception{

	private static final long serialVersionUID = 9139823577751459145L;

	public DatePatternException(String msg){
		super(msg);
	}
	
	public DatePatternException(String msg, Throwable err){
		super(msg, err);
	}
}
