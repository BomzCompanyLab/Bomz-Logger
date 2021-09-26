package kr.co.bomz.logger.date;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 약자 'z' 또는 'Z' 사용을 위한 추상클래스
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	TimeZoneGeneralPattern
 * @see	TimeZoneRFCPattern
 *
 */
public abstract class TimeZonePattern extends Pattern{
	
	protected static final HashMap<TimeZoneInfo, String> cachePattern = new HashMap<TimeZoneInfo, String>();
	
	protected Locale locale;
	protected TimeZone timeZone;
	
	@Override
	void appendPatten(Calendar date, StringBuilder buffer){
		
		TimeZoneInfo timeZoneInfo = new TimeZoneInfo(this.locale, this.timeZone);
		String value = cachePattern.get(timeZoneInfo);
		
		if( value != null ){
			buffer.append( value );
		}else{
			value = this.getTimeZoneValue(date);
			cachePattern.put(timeZoneInfo, value);
			buffer.append( value );
		}
				
	}
	
	/**
	 * 약자 'z' 또는 'Z' 에 맞는 문자 정보 생성
	 * 
	 * @param date		형식을 변경할 날짜 정보
	 * @return				약자 'z' 또는 'Z' 에 맞는 문자 정보
	 */
	protected abstract String getTimeZoneValue(Calendar date); 
	
	@Override
	void setLocale(Locale locale){
		this.locale = locale;
	}
	
	@Override
	void setTimeZone(TimeZone timeZone){
		this.timeZone = timeZone;
	}
	
	/**
	 * 캐쉬 사용을 위한 TimeZone 결과 정보
	 * @author Bomz
	 * @since 1.0
	 * @version 1.0
	 *
	 */
	protected class TimeZoneInfo{
		private Locale locale;
		private TimeZone timeZone;
		
		/**
		 * 캐쉬 사용을 위한 TimeZone 정보
		 * @param locale				지역정보
		 * @param timeZone		표준시간대
		 */
		protected TimeZoneInfo(Locale locale, TimeZone timeZone){
			this.locale = locale;
			this.timeZone = timeZone;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((locale == null) ? 0 : locale.hashCode());
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
			TimeZoneInfo other = (TimeZoneInfo) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (locale == null) {
				if (other.locale != null)
					return false;
			} else if (!locale.equals(other.locale))
				return false;
			if (timeZone == null) {
				if (other.timeZone != null)
					return false;
			} else if (!timeZone.equals(other.timeZone))
				return false;
			return true;
		}

		private TimeZonePattern getOuterType() {
			return TimeZonePattern.this;
		}
		
	}
	
}
