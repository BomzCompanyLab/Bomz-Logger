package kr.co.bomz.logger.configure;

/**
 * 로그 XML 설정 분석 실패 시 발생
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	ConfigFileReader
 *
 */
public class XMLParseException extends Exception{

	private static final long serialVersionUID = 1188388031837161376L;

	/**
	 * 로그 XML 설정 분석 실패 시 발생
	 * @param err		오류 내용
	 */
	public XMLParseException(String err){
		super(err);
	}
}
