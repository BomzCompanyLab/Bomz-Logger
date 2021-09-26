package example.file1;

import kr.co.bomz.logger.Logger;

/*
 * period : DAY
 * max file size : 3MB
 * file name : day.log
 * level : trace ~
 */
public class DayPeriodThread extends Thread{

	private static final Logger logger = Logger.getLogger("dayPeriod");
	
	private String name = "Hong min";
	private int age = 26;
	
	public DayPeriodThread(){
		logger.debug("start Day Period");
	}
	
	public void run(){
		for(int i=0; i > -1; i++){
			logger.info("�̸�=", this.name, " , ����=", this.age, " , ��ȣ=", i);
			if( i % 3 == 0 ){
				// make exception
				try{
					int a = i/0;
				}catch(Exception e){
					logger.error(e);
					logger.error(e, "exception test. �̸�=", this.name);
				}
			}
			
			try{		Thread.sleep(100);		}catch(Exception e){}
		}
	}
}
