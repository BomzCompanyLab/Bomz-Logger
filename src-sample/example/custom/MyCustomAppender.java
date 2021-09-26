package example.custom;

import java.util.Map;

import kr.co.bomz.logger.Level;
import kr.co.bomz.logger.appender.CustomAppender;

/*
 * user custom appender example
 */
public class MyCustomAppender extends CustomAppender{

	private String myName;
	
	@Override
	public void write(Level level, StringBuilder date, StringBuilder callStack, StringBuilder msg, StringBuilder err) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Nm[").append(this.myName).append("]");
		buffer.append(" Lv[").append(level.name()).append("]");		// name is not null
		buffer.append(" DATE[").append(date).append("]");			// date is not null
		buffer.append(" [").append(callStack).append("] ");				// callStack is not null
		
		if( msg != null )	buffer.append(msg);								// msg is null or not null
		if( err != null )		buffer.append( msg==null?"":"\n").append(err);		// err is null or not null
		
		System.out.println( buffer.toString() );
	}

	@Override
	public void close() {
		this.myName = null;
	}

	@Override
	public void setParameter(Map<String, String> param) {
		this.myName = param.get("myName");
		if( this.myName == null )		this.myName = "testName";
	}


}
