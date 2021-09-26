package kr.co.bomz.logger.date;

import java.util.Calendar;

/**
 * 시간 패턴. 0 ~ 23으로 표현. 약자(H)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class Hour24Pattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.HOUR_OF_DAY;
	}

	@Override
	int estimateLength() {
		return 2;
	}
}
