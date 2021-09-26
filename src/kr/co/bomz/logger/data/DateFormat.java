package kr.co.bomz.logger.date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 
 * java.util.Calendar 값을 원하는 형식의 문자열로 변경.<p>
 * 
 * 사용 가능한 약자는 아래와 같으며, 여러 약자의 조합으로 원하는 날짜 출력형식을 지정한다
 * 
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Chart shows pattern letters, date/time component, presentation, and examples.">
 *     <tr bgcolor="#ccccff">
 *         <th align=left>약자</th>
 *         <th align=left>설명</th>
 *         <th align=left>리턴 타입</th>
 *         <th align=left>출력 예</th>
 *     </tr>
 *     <tr>
 *         <td><code>G</code></td>
 *         <td>Era designator</td>
 *         <td><a href="#text">Text</a></td>
 *         <td><code>AD</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>y</code></td>
 *         <td>Year</td>
 *         <td><a href="#year">Year</a></td>
 *         <td><code>1996</code>; <code>96</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>M</code></td>
 *         <td>Month in year</td>
 *         <td><a href="#month">Month</a></td>
 *         <td><code>July</code>; <code>Jul</code>; <code>07</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>w</code></td>
 *         <td>Week in year</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>27</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>W</code></td>
 *         <td>Week in month</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>2</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>D</code></td>
 *         <td>Day in year</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>189</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>d</code></td>
 *         <td>Day in month</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>10</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>F</code></td>
 *         <td>Day of week in month</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>2</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>E</code></td>
 *         <td>Day in week</td>
 *         <td><a href="#text">Text</a></td>
 *         <td><code>Tuesday</code>; <code>Tue</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>a</code></td>
 *         <td>Am/pm marker</td>
 *         <td><a href="#text">Text</a></td>
 *         <td><code>PM</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>H</code></td>
 *         <td>Hour in day (0-23)</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>0</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>k</code></td>
 *         <td>Hour in day (1-24)</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>24</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>K</code></td>
 *         <td>Hour in am/pm (0-11)</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>0</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>h</code></td>
 *         <td>Hour in am/pm (1-12)</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>12</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>m</code></td>
 *         <td>Minute in hour</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>30</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>s</code></td>
 *         <td>Second in minute</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>55</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>S</code></td>
 *         <td>Millisecond</td>
 *         <td><a href="#number">Number</a></td>
 *         <td><code>978</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>z</code></td>
 *         <td>Time zone</td>
 *         <td><a href="#timezone">General time zone</a></td>
 *         <td><code>Pacific Standard Time</code>; <code>PST</code>; <code>GMT-08:00</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>Z</code></td>
 *         <td>Time zone</td>
 *         <td><a href="#rfc822timezone">RFC 822 time zone</a></td>
 *         <td><code>-0800</code></td>
 *     </tr>
 * </table>
 * </blockquote>
 * 
 * <h4>사용 예제</h4>
 *
 * 실제 사용예제는 아래와 같으며 생성자에 원하는 출력 패턴을 설정하거나 format(...) 메소드에 원하는 패턴을 지정하여 사용할 수 있다
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Examples of date and time patterns interpreted in the U.S. locale">
 *     <tr bgcolor="#ccccff">
 *         <th align=left>예제</th>
 *         <th align=left>결과</th>
 *     </tr>
 *     <tr>
 *         <td><code>"yyyy.MM.dd G 'at' HH:mm:ss z"</code></td>
 *         <td><code>2001.07.04 AD at 12:08:56 PDT</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>"EEE, MMM d, ''yy"</code></td>
 *         <td><code>Wed, Jul 4, '01</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>"h:mm a"</code></td>
 *         <td><code>12:08 PM</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>"hh 'o''clock' a, zzzz"</code></td>
 *         <td><code>12 o'clock PM, Pacific Daylight Time</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>"K:mm a, z"</code></td>
 *         <td><code>0:08 PM, PDT</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>"yyyyy.MMMMM.dd GGG hh:mm aaa"</code></td>
 *         <td><code>02001.July.04 AD 12:08 PM</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>"EEE, d MMM yyyy HH:mm:ss Z"</code></td>
 *         <td><code>Wed, 4 Jul 2001 12:08:56 -0700</code></td>
 *     </tr>
 *     <tr bgcolor="#eeeeff">
 *         <td><code>"yyMMddHHmmssZ"</code></td>
 *         <td><code>010704120856-0700</code></td>
 *     </tr>
 *     <tr>
 *         <td><code>"yyyy-MM-dd'T'HH:mm:ss.SSSZ"</code></td>
 *         <td><code>2001-07-04T12:08:56.235-0700</code></td>
 *     </tr>
 * </table>
 * </blockquote>
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class DateFormat {

	private int patternLength = 0;
	private Pattern[] patterns;
	private Locale locale;
	private TimeZone timeZone;
	
	private static final String HYPHEN = "-";
	private static final String BLANK = " ";
	private static final String COLON = ":";
	
	public DateFormat(){
		this(null, null, null);
	}
		
	public DateFormat(String pattern, String locale, String timeZone){
		
		try{
			this.locale = locale == null ? Locale.getDefault() : new Locale(locale);
		}catch(Exception e){
			this.locale = Locale.getDefault();
		}
		
		try{
			this.timeZone = timeZone == null ? TimeZone.getDefault() : TimeZone.getTimeZone(timeZone);
		}catch(Exception e){
			this.timeZone = TimeZone.getDefault();
		}
		
		if( pattern != null )		this.parsePattern(pattern);
	}
		
	/**
	 * 지정한 날짜 패턴으로 시간 정보 변경
	 * @param date		시간 정보
	 * @return				지정한 패턴으로 변경된 시간 정보
	 */	
	public StringBuilder getDate(Calendar date){
		if( date == null )		throw new NullPointerException("date value is null");
		
		if( this.patterns == null ){
			return this.getDefaultDate(date);
		}else{
			return this.getPatternDate(date);
		}
	}
	
	public StringBuilder getDefaultDate(Calendar date){
		
		return new StringBuilder(
				date.get(Calendar.YEAR) + HYPHEN +
				(date.get(Calendar.MONTH) + 1) + HYPHEN +
				date.get(Calendar.DAY_OF_MONTH) + BLANK +
				date.get(Calendar.HOUR_OF_DAY) + COLON +
				date.get(Calendar.MINUTE) + COLON +
				date.get(Calendar.SECOND)
			);
	}
	
	// 지정된 패턴 정보로 시간 정보를 변경
	private StringBuilder getPatternDate(Calendar date){
				
		final StringBuilder buffer = new StringBuilder(this.patternLength);
		
		for(final Pattern pt : this.patterns){
			pt.appendPatten(date, buffer);
		}
		
		return buffer;
	}
		
	
	/*
	 * yy , yyyy: 년도
	 * M , MM : 월
	 * d , dd : 일 (월 단위)
	 * D , DD : 일 (년 단위)
	 * h , hh : 시 (1~12)
	 * K , KK : 시 (0~11)
	 * H , HH : 시(0~23)
	 * k , kk : 시(1~24)
	 * m , mm : 분
	 * s , ss : 초
	 * S : 밀리세컨드
	 * a : 오전/오후
	 * w : Week in year
	 * W : Week in month
	 * F : 요일 횟수 (월 기준)
	 * E : 요일명
	 * z : General Time Zone (ex:Pacific Standard Time; PST; GMT-08:00)
	 * Z : RFC822 Time Zone (ex:-0800); 
	 * 
	 */
	private void parsePattern(String pattern){
		
		ArrayList<Pattern> patternList = new ArrayList<Pattern>();
		
		int length = pattern.length();
		char charValue;
		try{
			for(int i=0; i < length; i++){
				charValue = pattern.charAt(i);
				switch(charValue){
				case 'y' :		// 년
					this.parsePattern(YearPattern.class, patternList);		break;
				case 'M' : 		// 월
					this.parsePattern(MonthPattern.class, patternList);		break;
				case 'd' :		// 월 기준 일
					this.parsePattern(DayInMonthPattern.class, patternList);		break;
				case 'D' :		// 년 기준 일
					this.parsePattern(DayInYearPattern.class, patternList);		break;
				case 'h' :		// 시 (1~12)
					this.parsePattern(Hour12Pattern.class, patternList);		break;
				case 'H' :		// 시 (0~23)
					this.parsePattern(Hour24Pattern.class, patternList);		break;
				case 'm' :		// 분
					this.parsePattern(MinutePattern.class, patternList);		break;
				case 's' :		// 초
					this.parsePattern(SecondPattern.class, patternList);		break;
				case 'a' : 		// 오전 오후
					this.parsePattern(MeridiemPattern.class, patternList);	break;
				case '\'' :		// 문자열
					i = this.parseString(pattern, i, patternList);		break;
				case 'G' :		// Era designator. AD or BC
					this.parsePattern(EraPattern.class, patternList);	break;
				case 'w' :		// Week In Year
					this.parsePattern(WeekInYearPattern.class, patternList);	break;
				case 'W' :		// Week In Month
					this.parsePattern(WeekInMonthPattern.class, patternList);	break;
				case 'F' :		// 해당 월의 해당 요일 반복 횟수
					this.parsePattern(DayOfWeekInMonthPattern.class, patternList);	break;
				case 'E' :		// 요일 이름
					this.parsePattern(DayNameInWeekPattern.class, patternList);	break;
				case 'k' :		// 시 (1~24)
					this.parsePattern(Hour24NPattern.class, patternList);	break;
				case 'K' :		// 시 (0~11)
					this.parsePattern(Hour12NPattern.class, patternList);	break;
				case 'S' :		// 밀리세컨드
					this.parsePattern(MillisecondPattern.class, patternList);	break;
				case 'z' :		// General Time Zone
					this.parsePattern(TimeZoneGeneralPattern.class, patternList);	break;
				case 'Z' :		// RFC Time Zone
					this.parsePattern(TimeZoneRFCPattern.class, patternList);	break;
				default :		// 그 외 문자열
					if( (charValue >= 'a' && charValue <= 'z') || (charValue >= 'A' && charValue <= 'Z') )
						throw new java.lang.IllegalArgumentException("Illegal pattern character '" + charValue + "'");
					i = this.otherString(pattern, i, patternList);
					break;
				}
			}
			
			// 패턴 분석이 정상적으로 이루어졌을 경우
			this.patterns = patternList.toArray(new Pattern[patternList.size()]);
			
			for(int i=0; i < this.patterns.length; i++)
				this.patternLength += this.patterns[i].estimateLength();
			
		}catch(Exception e){
			// 에러 날 경우 패턴 사용 안함
			System.err.println(e + " (정의되지 않은 날짜 패턴 '" + pattern + "')");
		}
		
	}
	
	// 작은 따옴표로 감싸지 않고 알파벳이 아닌 경우 처리
	private int otherString(String pattern, int i, List<Pattern> patternList){
		int start = i;
		int max = pattern.length();
		char charValue;
		while( true ){
			if( ++i >= max )		break;		// 끝까지 갔을 경우
			charValue = pattern.charAt(i);
			if( (charValue >= 'a' && charValue <= 'z') || (charValue >= 'A' && charValue <= 'Z') || (charValue == '\'') )		break;
		}
		
		this.appendStringPattern(patternList, pattern.substring(start, i));
		return --i;
	}
	
	// '\'' 로 패턴에 정의된 문자열 분석
	private int parseString(String pattern, int index, List<Pattern> patternList) throws Exception{
		try{
			int lastIndex = pattern.indexOf("'", index + 1);
			this.appendStringPattern(patternList, pattern.substring(index + 1, lastIndex));
			return lastIndex;
		}catch(Exception e){
			throw new DatePatternException("패턴 분석 오류", e);
		}
	}
	
	// 문자열 패턴일 경우 그 전 패턴이 문자열이었는지 확인 후 문자열이면 전 패턴과 합친다
	private void appendStringPattern(List<Pattern> patternList, String msg){
		int size = patternList.size();
		if( size == 0 ){
			patternList.add(new StringPattern(msg));
		}else{
			Pattern pattern = patternList.get( size - 1);
			if( pattern instanceof StringPattern ){
				// 마지막 패턴이 String 일 경우 문자 추가
				((StringPattern)pattern).append(msg);
			}else{
				// 마지막 패턴이 다른 패턴일 경우
				patternList.add(new StringPattern(msg));
			}
		}
	}
	
	private Pattern parsePattern(Class<? extends Pattern> clazz, List<Pattern> patternList) throws DatePatternException{
		int size = patternList.size();
		Pattern pattern = size == 0 ? null : patternList.get(size-1);
		
		if( pattern == null || pattern.getClass() != clazz){		
			// null 일 경우. 또는 현재 패턴과 종류가 다를 경우 새로 생성 후 리스트에 추가
			try {
				pattern = clazz.newInstance();
				pattern.setLocale(this.locale);
				pattern.setTimeZone(this.timeZone);
				patternList.add(pattern);
			} catch (Exception e) {
				throw new DatePatternException("DatePattern clsss 생성 오류 [" + clazz + "]");
			}
			
		}
		
		pattern.repeat();
		
		return pattern;
	}
		
}
