package kr.co.bomz.logger.configure;

/**
 * �α� XML ���� �м� ���� �� �߻�
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
	 * �α� XML ���� �м� ���� �� �߻�
	 * @param err		���� ����
	 */
	public XMLParseException(String err){
		super(err);
	}
}
