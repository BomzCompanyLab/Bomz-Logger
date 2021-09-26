package kr.co.bomz.logger.appender;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * 
 * 	HTTAppender 에서 큐에 쌓인 로그 데이터를 실제 전송 작업하는 스레드
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	HTTPAppender
 */
public class HTTPAppenderSub extends Thread{

	private boolean run = true;
	
	// HTTAppender 정보 저장 리스트
	private List<HTTPInfo> httpList = new ArrayList<HTTPInfo>();
	
	private int listSize = 0;
		
	/**
	 * HTTPAppender 의 큐에 쌓인 로그 데이터를 전송한다
	 */
	public void run(){
		
		// 안해도 상관없지만 첫 로딩 시간을 약간이라도 앞당기기 위해 사용
		try{		Thread.sleep(1000);		}catch(Exception e){}
		
		int index;
		HTTPInfo httpInfo;
		
		while( this.run ){
			for(index=0; index < this.listSize; index++){
				httpInfo = this.httpList.get(index);
				
				// 전송할 로그가 없을 경우 continue
				if( httpInfo.queue.isEmpty() )		continue;
				
				// 큐에 쌓인 데이터 전송
				httpInfo.httpAppender.writeMessage();
			}
			
			// CPU 100% 사용을 막기 위한 작업
			try{		Thread.sleep(1);		}catch(Exception e){}
		}
		
	}
		
	/**
	 * 새로운 HTTPAppender 생성 시 추가
	 * @param httpAppender		HTTPAppender
	 * @param queue					로그 데이터가 들어있는 큐
	 */
	synchronized void addHTTPAppender(HTTPAppender httpAppender, Queue<String> queue){
		this.httpList.add( new HTTPInfo(httpAppender, queue) );
		this.listSize ++;
	}
	
	/**
	 * 모든 TCPAppender 종료
	 */
	void closeHTTPAppender(){
		this.run = false;
	}
	
	/**
	 * HTTPAppender 의 요약정보
	 * @author Bomz
	 * @since 1.0
	 * @version 1.0
	 */
	class HTTPInfo{
		private final HTTPAppender httpAppender;
		private final Queue<String> queue;
		
		HTTPInfo(HTTPAppender httpAppender, Queue<String> queue){
			this.httpAppender = httpAppender;
			this.queue = queue;
		}
		
	}
	
}
