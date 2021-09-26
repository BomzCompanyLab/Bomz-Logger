package kr.co.bomz.logger.date;

import java.util.Calendar;

/**
 * �� ���� ���� ��. ����(W)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class WeekInMonthPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.WEEK_OF_MONTH;
	}
	
	@Override
	int estimateLength() {
		return 2;
	}
}
