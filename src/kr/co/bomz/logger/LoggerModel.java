package kr.co.bomz.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.co.bomz.logger.appender.AppenderType;

/**
 * 로거 설정 파일 분석 정보
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class LoggerModel {

	// 로거 명
	private final String loggerName;
	// 로거 레벨
	private List<Level> levelList = new ArrayList<Level>(3);
	// Appender 타입
	private final AppenderType appenderType;
	// 파라메터
	private Map<String, String> parameters;
	
	/** 
	 * @param loggerName		로거명
	 * @param appenderType		Appender 타입
	 */
	public LoggerModel(String loggerName, AppenderType appenderType){
		this.loggerName = loggerName;
		this.appenderType = appenderType;
	}

	/**
	 * 로거 명 요청
	 * @return	로거명
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * 로거 레벨 목록 요청
	 * @return		로거 레벨 목록
	 */
	public List<Level> getLevelList() {
		return levelList;
	}

	/**
	 * 로거 레벨 등록
	 * @param level		로거 레벨
	 */
	public void addLevel(Level level) {
		if( level == null )		return;
		this.levelList.add(level);
	}

	/**
	 * 파라메터 요청
	 * @return		파라메터 정보. 파라메터가 없을 경우 null
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * 파라메터 등록
	 * @param key		파라메터명
	 * @param value	파라메터값
	 */
	public void addParameter(String key, String value) {
		if( this.parameters == null ){
			this.parameters = new java.util.HashMap<String, String>(3);
		}
		
		this.parameters.put(key, value);
	}
	
	/**
	 * 특정 파라메터 값 요청
	 * @param key		파라메터명
	 * @return			파라메터값
	 */
	public String getParameter(String key){
		if( this.parameters == null )		return null;
		return this.parameters.get(key);
	}

	/**
	 * Appender 타입 요청
	 * @return		Appender 타입
	 */
	public AppenderType getAppenderType() {
		return appenderType;
	}
	
}
