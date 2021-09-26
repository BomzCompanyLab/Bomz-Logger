package kr.co.bomz.logger.date;

import java.util.Calendar;

/**
 * 시간 패턴. 0~11시간으로 표현. 약자(K)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class Hour12NPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.HOUR;
	}

	@Override
	int estimateLength() {
		return 2;
	}
}
