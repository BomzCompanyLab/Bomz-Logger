package example.file1;

import java.io.File;

import kr.co.bomz.logger.Logger;

/*
 * logger setting file : ./conf/file1.xml
 */
public class FileLoggerTest {

	public static void main(String[] args){
		Logger.setLogConfigFile(new File("./conf/file1.xml"));
		Logger logger = Logger.getLogger(FileLoggerTest.class);
		
		logger.info("file logger example start...");
		new DayPeriodThread().start();
		new HourPeriodThread().start();
	}
}
