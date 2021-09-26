package kr.co.bomz.logger.date;

import java.util.Calendar;
import java.util.Locale;

/**
 * 월 패턴. 약자(M)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class MonthPattern extends Pattern{

	private Locale locale;
	
	@Override
	void setLocale(Locale locale){
		this.locale = locale;
	}
	
	@Override
	int getCalendarType() {
		return Calendar.MONTH;
	}
	
	@Override
	int estimateLength() {
		return 2;
	}

	@Override
	void appendPatten(Calendar date, StringBuilder buffer){
		int intValue = date.get(Calendar.MONTH) + 1;
				
		switch( super.repeat ){
		case 1 :		// M
			buffer.append(intValue);		
			break;		
		case 2 :		// MM
			if( intValue < 10 )		buffer.append("0");		// 1 ~ 9월
			buffer.append(intValue);
			break;
		case 3 :		// MMM
			buffer.append( date.getDisplayName(Calendar.MONTH, Calendar.SHORT, this.locale) );
			break;
		default :		// MMMM ~
			buffer.append( date.getDisplayName(Calendar.MONTH, Calendar.LONG, this.locale) );
			break;
		}
		
	}
}
