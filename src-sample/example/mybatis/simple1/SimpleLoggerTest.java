package example.simple1;

import kr.co.bomz.logger.Logger;

/*
 * logger setting file : /src/logger.xml
 */
public class SimpleLoggerTest {

	private static final Logger logger = Logger.getLogger("simple");
	
	public static void main(String[] args){
		logger.info("start simple logger test");
		
		MyObject obj = new MyObject();
		obj.info();
		obj.error();
		
		logger.info("end simple logger test");
	}
}
