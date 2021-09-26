package example.date;

import java.io.File;

import kr.co.bomz.logger.Level;
import kr.co.bomz.logger.Logger;


/*
 * logger setting file : ./conf/date1.xml
 */
public class DateFormat1 {

	private Logger logger1 = Logger.getLogger(DateFormat1.class);
	private Logger logger2 = Logger.getLogger("dateLog");
	private Logger logger3 = Logger.getLogger("consoleAndFileLog");
	
	public static void main(String[] args){
		Logger.setLogConfigFile(new File("./conf/date1.xml"));
		
		DateFormat1 dateFormat = new DateFormat1();
		
		while( true ){
			for(int i=0; i <= 6; i++){
				switch( i ){
				case 0 : dateFormat.printLog(Level.TRACE);		break;
				case 1 : dateFormat.printLog(Level.DEBUG);		break;
				case 3 : dateFormat.printLog(Level.INFO);		break;
				case 4 : dateFormat.printLog(Level.WARN);		break;
				case 5 : dateFormat.printLog(Level.ERROR);		break;
				case 6 : dateFormat.printLog(Level.FATAL);		break;
				}
			}
			
			try{		Thread.sleep(3000);		}catch(Exception e){}
		}
		
	}
	
	private void printLog(Level level){
		logger1.log(level, "Date format print test");
		logger2.log(level, "Date format print test");
		logger3.log(level, "Date format print test");
	}
}
