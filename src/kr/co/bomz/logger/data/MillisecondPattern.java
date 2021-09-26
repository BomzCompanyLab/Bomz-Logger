package kr.co.bomz.logger.date;

import java.util.Calendar;

/**
 * �и������� ����. ����(S)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class MillisecondPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.MILLISECOND;
	}

	@Override
	int estimateLength() {
		return 3;
	}
}
