package kr.co.bomz.logger.date;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 년, 월, 일, 시, 분, 초 등 패턴에 대한 기본 추상 클래스
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public abstract class Pattern{
	/**
	 *  패턴의 연속 반복 횟수.
	 */
	protected int repeat = 0;
	
	/**
	 * 패턴의 연속 반복 횟수 증가
	 */
	protected void repeat(){
		this.repeat++;
	}
	
	/**
	 * 패턴의 연속 반복 횟수 리턴
	 * @return		패턴의 연속 반복 횟수
	 */
	protected int getRepeat(){
		return this.repeat;
	}
		
	/**
	 * 인트형의 자릿수
	 * @param value		값
	 * @return				값의 자릿수. 예) 값이 1994 일 경우 4 리턴
	 */
	protected int intLength(int value){
		if( value <= 9 )						return 1;
		else if( value <= 99 )				return 2;
		else if( value <= 999 )			return 3;
		else if( value <= 9999 )			return 4;
		else if( value <= 99999 )		return 5;
		else		return Integer.toString(value).length();
	}
	
	/**
	 * 지정한 자릿수에 맞게 빈 자리를 채운다
	 * @param size		지정한 자릿수
	 * @param buffer		표현할 데이터
	 */
	protected void appendEmptyData(int size, StringBuilder buffer){
		if( size <= 0 )		return;
		switch( size ){
		case 1 :		buffer.append("0");		break;
		case 2 :		buffer.append( "00");		break;
		case 3 :		buffer.append( "000");		break;
		case 4 :		buffer.append( "0000");		break;
		case 5 :		buffer.append( "00000");		break;
		case 6 :		buffer.append( "000000");		break;
		case 7 :		buffer.append( "0000000");		break;
		case 8 :		buffer.append( "00000000");		break;
		default :
			for(int i=0; i < size; i++){
				buffer.append("0");
			}
		}
	}
	
	/**
	 * 표현할 날짜 패턴 종류
	 * @return
	 */
	abstract int getCalendarType();
		
	/**
	 * 패턴에 맞는 날짜 데이터 추가
	 * @param date
	 * @param buffer
	 */
	void appendPatten(Calendar date, StringBuilder buffer){
		
		int value = date.get( this.getCalendarType() );
		
		int intLength = this.intLength(value);
		if( intLength < this.repeat )		this.appendEmptyData(this.repeat - intLength, buffer);
		
		buffer.append( value );
	}
	
	/**
	 * 지역 정보 설정
	 * @param locale		지역정보
	 */
	void setLocale(Locale locale){}
	
	/**
	 * 표준 시간대 설정
	 * @param timeZone		표준 시간대
	 */
	void setTimeZone(TimeZone timeZone){}
	
	/**
	 * 문자열 예상 길이
	 * @return		문자열 예상 길이
	 */
	abstract int estimateLength();
}
