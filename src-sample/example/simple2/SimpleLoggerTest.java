package example.simple2;

import kr.co.bomz.logger.Logger;

/*
 * logger setting file : ./conf/simple2.xml
 */
public class SimpleLoggerTest {

	private static final Logger logger = Logger.getLogger(SimpleLoggerTest.class);
	
	public static void main(String[] args){
		Logger.setLogConfigFile("./conf/simple2.xml");
		
		logger.trace("trace simple logger");
		logger.debug("debug simple logger");
		logger.info("info simple logger");
		logger.warn("warn simple logger");
		logger.error("error simple loggert");
		logger.fatal("fatal simple logger");
	}
}
