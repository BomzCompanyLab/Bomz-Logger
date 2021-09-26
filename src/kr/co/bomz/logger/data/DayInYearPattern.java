package kr.co.bomz.logger.date;

import java.util.Calendar;

/**
 * �� ���� �� ����. ����(D)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class DayInYearPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.DAY_OF_YEAR;
	}

	@Override
	int estimateLength() {
		return 3;
	}

}
