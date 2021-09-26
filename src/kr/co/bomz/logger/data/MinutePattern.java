package kr.co.bomz.logger.date;

import java.util.Calendar;

/**
 * �� ����. ����(m)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class MinutePattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.MINUTE;
	}

	@Override
	int estimateLength() {
		return 2;
	}
}
