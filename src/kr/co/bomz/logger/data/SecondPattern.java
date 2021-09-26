package kr.co.bomz.logger.date;

import java.util.Calendar;

/**
 * �� ����. ����(s)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class SecondPattern extends Pattern{

	@Override
	int getCalendarType() {
		return Calendar.SECOND;
	}
	
	@Override
	int estimateLength() {
		return 2;
	}

}
