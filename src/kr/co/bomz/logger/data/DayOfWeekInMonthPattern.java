package kr.co.bomz.logger.date;

import java.util.Calendar;

/**
 * 월 기준 주 반혹 횟수 패턴. 약자(F)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class DayOfWeekInMonthPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.DAY_OF_WEEK_IN_MONTH;
	}
	
	@Override
	int estimateLength() {
		return 2;
	}

}
