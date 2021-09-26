package kr.co.bomz.logger.appender;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * 
 * 	TCPAppender 에서 큐에 쌓인 로그 데이터를 실제 전송 작업하는 스레드
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	TCPAppender
 */
public class TCPAppenderSub extends Thread{

	private boolean run = true;
	
	// TCPAppender 정보 저장 리스트
	private List<TCPInfo> tcpList = new ArrayList<TCPInfo>();
	
	private int listSize = 0;
	
	/**
	 * TCPAppender 의 큐에 쌓인 로그 데이터를 전송한다
	 */
	public void run(){
		
		// 안해도 상관없지만 첫 로딩 시간을 약간이라도 앞당기기 위해 사용
		try{		Thread.sleep(1000);		}catch(Exception e){}
		
		int index;
		TCPInfo tcpInfo;
		
		while( this.run ){
			for(index=0; index < this.listSize; index++){
				tcpInfo = this.tcpList.get(index);
				
				// 전송할 로그가 없을 경우 continue
				if( tcpInfo.queue.isEmpty() )		continue;
				// 서버 연결 실패 시 continue
				if( !tcpInfo.tcpAppender.connect() )		continue;
				
				// 큐에 쌓인 데이터 전송
				tcpInfo.tcpAppender.writeMessage();
			}
			
			// CPU 100% 사용을 막기 위한 작업
			try{		Thread.sleep(100);		}catch(Exception e){}
		}
		
		// 서버와의 연결 정상 종료
		this.closeAll();
	}
	
	/*		서버와의 연결 정상 종료		*/
	private void closeAll(){
		int size = this.tcpList.size();
		for(int i=0; i < size; i++)		this.tcpList.get(i).close();
	}
	
	/**
	 * 새로운 TCPAppender 생성 시 추가
	 * @param tcpAppender		TCPAppender
	 * @param queue					로그 데이터가 들어있는 큐
	 */
	synchronized void addTCPAppender(TCPAppender tcpAppender, Queue<String> queue){
		this.tcpList.add( new TCPInfo(tcpAppender, queue) );
		this.listSize ++;
	}
	
	/**
	 * 모든 TCPAppender 종료
	 */
	void closeTCPAppender(){
		this.run = false;
	}
	
	/**
	 * TCPAppender 의 요약정보
	 * @author Bomz
	 * @since 1.0
	 * @version 1.0
	 */
	class TCPInfo{
		private final TCPAppender tcpAppender;
		private final Queue<String> queue;
		
		TCPInfo(TCPAppender tcpAppender, Queue<String> queue){
			this.tcpAppender = tcpAppender;
			this.queue = queue;
		}
		
		private void close(){
			this.tcpAppender.closeConnect();
		}
	}
	
}
