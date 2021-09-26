package kr.co.bomz.logger.date;

import java.util.Calendar;

/**
 * 년도 패턴. 약자(y)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class YearPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.YEAR;
	}

	@Override
	int estimateLength() {
		return super.repeat;
	}
	
	@Override
	void appendPatten(Calendar date, StringBuilder buffer){
		int pow = 1;
		for(int i=0; i < super.repeat; i++){
			pow *= 10;
			if( pow <= 0 )		break;		// y 를 너무 많이 써서 인트형의 최대 값을 넘었을 경우
		}
		
		
//		String value = pow <= 0 ?
//					Integer.toString(date.get( Calendar.YEAR )) :		// y 를 너무 많이 썼을 경우
//					Integer.toString(date.get( Calendar.YEAR ) % pow);
	
		int value = pow <= 0 ?
				date.get( Calendar.YEAR ) :		// y 를 너무 많이 썼을 경우
				date.get( Calendar.YEAR ) % pow;
		
		this.appendEmptyData(this.repeat - super.intLength(value), buffer);
		
		buffer.append( value );
	}
	
}
