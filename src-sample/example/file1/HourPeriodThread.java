package example.file1;

import kr.co.bomz.logger.Logger;

/*
 * period : HOUR
 * file name : hour.log
 * level : debug & error & fatal
 */
public class HourPeriodThread extends Thread{

	private static final Logger logger = Logger.getLogger("hourPeriod");
	
	public HourPeriodThread(){
		logger.trace("start hourPeriodThread");
	}
	
	public void run(){
		int index = 0;
		while( true ){
			switch(index++){
			case 0 :		logger.trace("hour trace level log", index);		break;
			case 1 :		logger.debug("hour debug level log", index);		break;
			case 2 :		logger.info("hour info level log", index);		break;
			case 3 :		logger.warn("hour warn level log", index);		break;
			case 4 :		logger.error("hour error level log", index);		break;
			case 5 :		logger.fatal("hour fatal level log", index);		index = 0;		break;
			}
			
			try{		Thread.sleep(100);		}catch(Exception e){}
		}
	}
}
