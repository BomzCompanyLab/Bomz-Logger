package kr.co.bomz.logger.date;

import java.util.Locale;
import java.util.TimeZone;

/**
 * 
 * 캐쉬 사용을 위한 패턴 정보
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class PatternInfo {

	private final String pattern;
	private final Locale locale;
	private final TimeZone timeZone;
	
	PatternInfo(String pattern, Locale locale, TimeZone timeZone){
		this.pattern = pattern;
		this.locale = locale;
		this.timeZone = timeZone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		result = prime * result
				+ ((timeZone == null) ? 0 : timeZone.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PatternInfo other = (PatternInfo) obj;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		if (timeZone == null) {
			if (other.timeZone != null)
				return false;
		} else if (!timeZone.equals(other.timeZone))
			return false;
		return true;
	}

}
