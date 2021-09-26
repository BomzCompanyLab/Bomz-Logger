package kr.co.bomz.logger.appender;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * 
 * 	HTTAppender ���� ť�� ���� �α� �����͸� ���� ���� �۾��ϴ� ������
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	HTTPAppender
 */
public class HTTPAppenderSub extends Thread{

	private boolean run = true;
	
	// HTTAppender ���� ���� ����Ʈ
	private List<HTTPInfo> httpList = new ArrayList<HTTPInfo>();
	
	private int listSize = 0;
		
	/**
	 * HTTPAppender �� ť�� ���� �α� �����͸� �����Ѵ�
	 */
	public void run(){
		
		// ���ص� ��������� ù �ε� �ð��� �ణ�̶� �մ��� ���� ���
		try{		Thread.sleep(1000);		}catch(Exception e){}
		
		int index;
		HTTPInfo httpInfo;
		
		while( this.run ){
			for(index=0; index < this.listSize; index++){
				httpInfo = this.httpList.get(index);
				
				// ������ �αװ� ���� ��� continue
				if( httpInfo.queue.isEmpty() )		continue;
				
				// ť�� ���� ������ ����
				httpInfo.httpAppender.writeMessage();
			}
			
			// CPU 100% ����� ���� ���� �۾�
			try{		Thread.sleep(1);		}catch(Exception e){}
		}
		
	}
		
	/**
	 * ���ο� HTTPAppender ���� �� �߰�
	 * @param httpAppender		HTTPAppender
	 * @param queue					�α� �����Ͱ� ����ִ� ť
	 */
	synchronized void addHTTPAppender(HTTPAppender httpAppender, Queue<String> queue){
		this.httpList.add( new HTTPInfo(httpAppender, queue) );
		this.listSize ++;
	}
	
	/**
	 * ��� TCPAppender ����
	 */
	void closeHTTPAppender(){
		this.run = false;
	}
	
	/**
	 * HTTPAppender �� �������
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
