package example.custom;

import kr.co.bomz.logger.Logger;

public class CustomLoggerTest {

	public static void main(String[] args) {
		Logger.setLogConfigFile("./conf/custom1.xml");
		Logger logger = Logger.getLogger("myLogger");
		
		logger.trace("test trace log");
		logger.debug("test ", "debug ", "log", 1);
		logger.info("test info log");
		
		int age = 21;
		logger.warn("my age is ", age, ".");
		
		logger.error("test error log");
		logger.fatal("last log level fatal");
		
	}

}
