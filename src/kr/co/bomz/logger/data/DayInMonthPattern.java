package kr.co.bomz.logger.date;

import java.util.Calendar;

/**
 * �� ���� �� ����. ����(d)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class DayInMonthPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.DAY_OF_MONTH;
	}

	@Override
	int estimateLength() {
		return 2;
	}

}
