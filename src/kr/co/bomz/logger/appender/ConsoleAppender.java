package kr.co.bomz.logger.appender;

import java.util.Map;

import kr.co.bomz.logger.Level;

/**
 * 
 * 발생한 로그를 콘솔 출력
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	CustomAppender
 * @see	TCPAppender
 * @see	FileAppender
 * @see	HTTPAppender
 */
public class ConsoleAppender extends Appender{

	public ConsoleAppender(){}
	
	@Override
	public void write(Level level, StringBuilder dateBuffer,
			StringBuilder callStackBuffer, StringBuilder msgBuffer, StringBuilder errBuffer) {
	
		System.out.format("%s [%s] %s", dateBuffer, level.name(), callStackBuffer);
		if( msgBuffer == null ){
			if( errBuffer == null )		System.out.println();
			else								System.out.format(" %s\n", errBuffer);
		}else{
			System.out.format(" %s\n", msgBuffer);
			if( errBuffer != null )		System.out.println(errBuffer);
		}
	}

	@Override
	public void close(){}

	@Override
	public void setParameter(Map<String, String> parameters) {}

}
