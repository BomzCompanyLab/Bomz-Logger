package kr.co.bomz.logger.appender;

import java.nio.ByteBuffer;
import java.util.Map;

import kr.co.bomz.logger.Level;
import kr.co.bomz.logger.date.DateFormat;

/**
 * 
 * Console , File , TCP �� ��¹�Ŀ� ���� ������ �ֻ��� Appender  
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	ConsoleAppender
 * @see	CustomAppender
 * @see	TCPAppender
 * @see	FileAppender
 * @see	HTTPAppender
 *
 */
public abstract class Appender {
	

	/**		���� ��� �� ���� ����		*/
	protected static final String LEVEL_STX = " [";
	/**		���� ��� �� ���� ����		*/
	protected static final String LEVEL_ETX = "] ";
	/**		�ٹٲ� ����		*/
	protected static final String NEW_LINE = "\n";
	/**		���� ����		*/
	protected static final String BLANK = " ";
	
	/**
	 * FileAppender , TCPAppeder ���� ���Ǵ� ���� ũ��
	 */
	protected static final int BUFFER_SIZE = 500;
	
	/**		
	 * ������ ��¥������ �������� �� ����ϴ� �α� ��¥ ���� 	
	 */
	public DateFormat dateFormat = null;
	
	/**
	 * �⺻ ������
	 */
	Appender(){}
	
	public void setDatePattern(String datePattern, String locale, String timeZone){
		if( datePattern != null ){
			try {
				this.dateFormat = new DateFormat(datePattern, locale, timeZone);
			} catch (Exception e) {}
		}
	}
	
	/**
	 * �߻��� �α� ��� �޼ҵ�
	 * 
	 * @param level		�α� ����
	 * @param dateBuffer		�α� �߻� ����
	 * @param callStackBuffer		�α� �߻� ��ġ
	 * @param msgBuffer		�α� ����
	 * @param errBuffer			���� ����
	 */
	public abstract void write(Level level, StringBuilder dateBuffer, 
			StringBuilder callStackBuffer, StringBuilder msgBuffer, StringBuilder errBuffer);
	
	/**
	 * Appender ����
	 */
	public abstract void close();
	
	/**
	 * �α� ���� ���Ͽ� ������ �Ķ���� �� ����
	 * @param parameters		���� ���Ͽ� ������ ��
	 */
	public abstract void setParameter(Map<String, String> parameters);
		
	/**
	 * TCPAppender , FileAppender ���� ���Ǵ� ������ ���� �޼ҵ�
	 * 
	 * @param bb		����ִ� ����
	 * @param msg	�α� ����
	 * @throws Exception	TCP / File ��� ���� �� �߻�
	 */
	protected void write(ByteBuffer bb, byte[] msg) throws Exception{
		if( msg == null )			return;
		int length = msg.length;
		if( length <= 0 )				return;
		
		// ������ ���� ��츦 ����� ������ ������ ó��
		int width =  length>BUFFER_SIZE?BUFFER_SIZE:length;
		int index = 0;
		while( true ){
			if( width <= 0 )		break;
			
			bb.clear();
			bb.put( msg, index, width );
			bb.flip();
			write( bb );
			index = index + width;
			length = length - width;
			width = length > BUFFER_SIZE ? BUFFER_SIZE : length;
		}
		
	}
	
	/**
	 * TCPAppender , FileAppender ���� ���Ǵ� ������ ���� �޼ҵ�
	 * @param bb		�����Ͱ� �߰��� ����
	 * @throws Exception		TCP �Ǵ� File �� ������ ��� ���� �� �߻�
	 */
	protected void write(ByteBuffer bb) throws Exception{}
	
}
