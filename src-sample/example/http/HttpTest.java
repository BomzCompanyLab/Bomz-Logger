package example.http;

import java.io.File;

import kr.co.bomz.logger.Logger;

/*
 * HTTP Appender Test.
 * 
 * Request method is GET or POST
 */
public class HttpTest {

	public static void main(String[] args){
		Logger.setLogConfigFile(new File("./conf/http1.xml"));
		
		Logger logger = Logger.getLogger("httpLog");
		
		String name="bomz";
		int age = 30;
		String url="bomz.co.kr";
		
		logger.debug("http log test. my name is ", name);
		logger.warn(name, " site url : ", url);
		logger.error(new Exception("test exception"), "exception example. my age is ", age, " and my name is ", name);
				
		try{		Thread.sleep(5000);		}catch(Exception e){}
		
		logger.debug("end http test");
	}
}
