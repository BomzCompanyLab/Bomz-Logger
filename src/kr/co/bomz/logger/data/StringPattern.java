package kr.co.bomz.logger.date;

import java.util.Calendar;

/**
 * 일반 문자열 패턴.
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class StringPattern extends Pattern{

	private final StringBuilder msg;
	
	StringPattern(String msg){
		this.msg = new StringBuilder(msg);
	}
	
	/**
	 * 기존 문자열에 새로운 문자열 추가
	 * @param msg		추가할 문자열
	 */
	void append(String msg){
		this.msg.append(msg);
	}
	
	@Override
	int getCalendarType() {
		return -1;
	}
	
	@Override
	int estimateLength() {
		return this.msg.length();
	}

	@Override
	void appendPatten(Calendar date, StringBuilder buffer){
		buffer.append( this.msg );
	}
}
