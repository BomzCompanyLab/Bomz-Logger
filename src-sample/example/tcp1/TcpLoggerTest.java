package example.tcp1;

import java.util.Random;

import kr.co.bomz.logger.Level;
import kr.co.bomz.logger.Logger;

public class TcpLoggerTest {

	public static void main(String[] args) {
		Logger.setLogConfigFile("./conf/tcp1.xml");

		Logger logger = Logger.getLogger("tcpLogger");
		
		Random random = new Random();
		
		while( true ){
			logger.log(
					Level.getLevel(random.nextInt(6)),		// log level
					"my log random value[",
					random.nextDouble(),
					"]"
				);
			
			try{		Thread.sleep(1000);		}catch(Exception e){}
		}
	}

}
