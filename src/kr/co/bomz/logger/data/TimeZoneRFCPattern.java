package kr.co.bomz.logger.date;

import java.util.Calendar;

/**
 * TimeZone 패턴. 약자(Z)
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class TimeZoneRFCPattern extends TimeZonePattern{
	
	@Override
	int getCalendarType() {
		return -1;
	}
	
	@Override
	int estimateLength() {
		return 5;
	}
	
	@Override
	protected String getTimeZoneValue(Calendar date){
		// apache lang3 project. FastDatePrinter.java source copy
		StringBuilder buffer = new StringBuilder(5);
		int offset = date.get(Calendar.ZONE_OFFSET) + date.get(Calendar.DST_OFFSET);
		if (offset < 0) {
			buffer.append('-');
			offset = -offset;
		} else {
			buffer.append('+');
		}

		final int hours = offset / (60 * 60 * 1000);
		buffer.append((char)(hours / 10 + '0'));
		buffer.append((char)(hours % 10 + '0'));

		final int minutes = offset / (60 * 1000) - 60 * hours;
		buffer.append((char)(minutes / 10 + '0'));
		buffer.append((char)(minutes % 10 + '0'));
		
		return buffer.toString();
	}
	
}
