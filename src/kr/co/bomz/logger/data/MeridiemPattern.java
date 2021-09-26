package kr.co.bomz.logger.date;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * 오전,오후 패턴. 약자(a)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class MeridiemPattern extends Pattern{

	private String[] amOrPm;
	
	private static final HashMap<String, String[]> cacheMap = new HashMap<String, String[]>();
	
	@Override
	void setLocale(Locale locale){
		this.amOrPm = cacheMap.get(locale.getCountry());
		
		if( this.amOrPm == null ){
			final DateFormatSymbols symbols = new DateFormatSymbols(locale);
			this.amOrPm = symbols.getAmPmStrings();
			cacheMap.put(locale.getCountry(), this.amOrPm);
		}
	}
	
	@Override
	int getCalendarType() {
		return Calendar.AM_PM;
	}
	
	@Override
	int estimateLength() {
		return 2;
	}
	
	@Override
	void appendPatten(Calendar date, StringBuilder buffer){
		buffer.append( this.amOrPm[ date.get(Calendar.AM_PM) ] );
	}
}
