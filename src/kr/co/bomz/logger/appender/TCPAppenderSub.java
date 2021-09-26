package kr.co.bomz.logger.appender;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * 
 * 	TCPAppender ���� ť�� ���� �α� �����͸� ���� ���� �۾��ϴ� ������
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	TCPAppender
 */
public class TCPAppenderSub extends Thread{

	private boolean run = true;
	
	// TCPAppender ���� ���� ����Ʈ
	private List<TCPInfo> tcpList = new ArrayList<TCPInfo>();
	
	private int listSize = 0;
	
	/**
	 * TCPAppender �� ť�� ���� �α� �����͸� �����Ѵ�
	 */
	public void run(){
		
		// ���ص� ��������� ù �ε� �ð��� �ణ�̶� �մ��� ���� ���
		try{		Thread.sleep(1000);		}catch(Exception e){}
		
		int index;
		TCPInfo tcpInfo;
		
		while( this.run ){
			for(index=0; index < this.listSize; index++){
				tcpInfo = this.tcpList.get(index);
				
				// ������ �αװ� ���� ��� continue
				if( tcpInfo.queue.isEmpty() )		continue;
				// ���� ���� ���� �� continue
				if( !tcpInfo.tcpAppender.connect() )		continue;
				
				// ť�� ���� ������ ����
				tcpInfo.tcpAppender.writeMessage();
			}
			
			// CPU 100% ����� ���� ���� �۾�
			try{		Thread.sleep(100);		}catch(Exception e){}
		}
		
		// �������� ���� ���� ����
		this.closeAll();
	}
	
	/*		�������� ���� ���� ����		*/
	private void closeAll(){
		int size = this.tcpList.size();
		for(int i=0; i < size; i++)		this.tcpList.get(i).close();
	}
	
	/**
	 * ���ο� TCPAppender ���� �� �߰�
	 * @param tcpAppender		TCPAppender
	 * @param queue					�α� �����Ͱ� ����ִ� ť
	 */
	synchronized void addTCPAppender(TCPAppender tcpAppender, Queue<String> queue){
		this.tcpList.add( new TCPInfo(tcpAppender, queue) );
		this.listSize ++;
	}
	
	/**
	 * ��� TCPAppender ����
	 */
	void closeTCPAppender(){
		this.run = false;
	}
	
	/**
	 * TCPAppender �� �������
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
