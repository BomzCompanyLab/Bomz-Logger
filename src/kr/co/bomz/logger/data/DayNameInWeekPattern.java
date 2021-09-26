package kr.co.bomz.logger.date;

import java.util.Calendar;
import java.util.Locale;

/**
 * 요일 이름 패턴. 약자(E)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class DayNameInWeekPattern extends Pattern{

	private Locale locale;
	
	@Override
	void setLocale(Locale locale){
		this.locale = locale;
	}
	
	@Override
	int getCalendarType() {
		return Calendar.DAY_OF_WEEK;
	}
	
	@Override
	int estimateLength() {
		return super.repeat <= 3 ? 3 : 6;
	}
	
	@Override
	void appendPatten(Calendar date, StringBuilder buffer){
		buffer.append(
				date.getDisplayName(
						Calendar.DAY_OF_WEEK,
						super.repeat <= 3 ? Calendar.SHORT : Calendar.LONG,
						this.locale
					)
				);
	}
}
