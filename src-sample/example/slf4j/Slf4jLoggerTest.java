package example.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * logger setting file : ./conf/slf4j_logger.xml
 */
public class Slf4jLoggerTest {

	private static final Logger logger = LoggerFactory.getLogger(Slf4jLoggerTest.class);
	
	public static void main(String[] args){
		kr.co.bomz.logger.Logger.setLogConfigFile("./conf/slf4j_logger.xml");
		
		logger.trace("trace simple logger {}", "TraceTest");
		logger.debug("debug simple logger {}", "DebugTest");
		logger.info("info simple logger {}", "InfoTest");
		logger.warn("warn simple logger {}", "WarnTest");
		logger.error("error simple loggert {}", "ErrorTest");
	}
}
