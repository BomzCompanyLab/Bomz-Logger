package example.simple1;

import kr.co.bomz.logger.Logger;

public class MyObject {

	private static final Logger logger = Logger.getLogger("simple_object");
	
	public MyObject(){
		logger.debug("call MyObject Constructor");
	}
	
	public void info(){
		logger.info("call info level");
	}
	
	public void error(){
		logger.error("call error level");
	}
}
