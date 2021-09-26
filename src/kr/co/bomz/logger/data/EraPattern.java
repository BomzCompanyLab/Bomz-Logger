package kr.co.bomz.logger.date;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * 
 * AD or BC. ¾àÀÚ(G)
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class EraPattern extends Pattern{

	private String[] eraValues;
	
	private static final HashMap<String, String[]> cacheEras = new HashMap<String, String[]>(1);
	
	@Override
	void setLocale(Locale locale){
		this.eraValues = cacheEras.get(locale.getCountry());
		
		if( this.eraValues == null ){
			final DateFormatSymbols symbols = new DateFormatSymbols(locale);
			this.eraValues = symbols.getEras();
			cacheEras.put(locale.getCountry(), this.eraValues);
		}
		
	}
	
	@Override
	int getCalendarType() {
		return Calendar.ERA;
	}
	
	@Override
	int estimateLength() {
		return 2;
	}
	
	@Override
	void appendPatten(Calendar date, StringBuilder buffer){
		buffer.append( this.eraValues[ date.get(Calendar.ERA) ] );
	}
	
}
