package kr.co.bomz.logger.appender;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import kr.co.bomz.logger.Level;
import kr.co.bomz.logger.LoggerManager;

/**
 * 
 * TCP ������� �α� �����͸� �����ϸ�,<p>
 * ���� ���� �� ���� ���� ���� ��� �����͸� �����ϰ� ���� �ʴ´�.<p><p>
 * 
 * �ý����� Ŭ���̾�Ʈ ��Ȱ�� �����ϸ� TCP�������<p>
 * �����͸� ���۹��� ������ ������ �ʿ��ϴ�<p>
 * ���� param ������Ʈ�� ip �Ӽ��� port �Ӽ� ���� �ʼ��� �ʿ��ϸ�<p>
 * �ʿ信 ���� �������� ���۰� ���� ������ stx , etx ���� ������ �� �ִ�
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	ConsoleAppender
 * @see	CustomAppender
 * @see	FileAppender
 * @see	HTTPAppender
 */
public class TCPAppender extends Appender{

	/*		���� IP		*/
	private String ip;
	/*		���� PORT		*/
	private int port;
	/*		������ ���� �� ���� ǥ�� ���� (��������)		*/
	private byte[] stx;
	/*		������ ���� �� ���� ǥ�� ���� (��������)		*/
	private byte[] etx;
	
	/*		�������� ������ : ���� IP		*/
	private static final String PARAM_IP = "ip";
	/*		�������� ������ : ���� PORT		*/
	private static final String PARAM_PORT = "port";
	/*		�������� ������ : ������ ���� ǥ�� ����		*/
	private static final String PARAM_STX = "stx";
	/*		�������� ������ : ������ ���� ǥ�� ����		*/
	private static final String PARAM_ETX = "etx";
	
	/*		�α� �����͸� ������ ť�� �⺻ ũ��		*/
	private static final int DEFAULT_QUEUE_SIZE = 200;
	
	/*		����ȭ�� ���� lock		*/
	private static final Object lock = new Object();
	/*		���� �α� �����͸� ������ ������ 	*/
	private static TCPAppenderSub sub = null;
	/*		�ε� �����Ͱ� ����Ǵ� ť		*/
	private ArrayBlockingQueue<String> queue = null;
	
	/*		������ ����� Socket Channel		*/
	private SocketChannel sc;
	/*		������ �α� ������		*/
	private byte[] sendMsg;
	/*		������ �α� ������ ����		*/
	private ByteBuffer bb = ByteBuffer.allocate(BUFFER_SIZE);
	/*		�ٹٲ�		*/
	private final byte[] newLine = "\n".getBytes();
	
	public TCPAppender(){}
	
	@Override
	public void write(Level level, StringBuilder dateBuffer,
			StringBuilder callStackBuffer, StringBuilder msgBuffer, StringBuilder errBuffer) {
		
		// ������ ���� ���� ó��
		if( this.queue == null )		return;
		
		this.queue.offer(
				dateBuffer.toString() + LEVEL_STX + level.name() +LEVEL_ETX + callStackBuffer.toString() + BLANK +
				(msgBuffer == null ? "" : msgBuffer.toString() ) +
				(errBuffer == null ? "" : (msgBuffer != null ? NEW_LINE + errBuffer.toString() : errBuffer.toString()))
			);
	}

	@Override
	public void close() {
		synchronized ( lock ){
			if( sub != null ){
				sub.closeTCPAppender();
				sub = null;
			}
		}
		
	}

	@Override
	public void setParameter(Map<String, String> parameters) {
		if( !this.setParameterIsIp( parameters.get(PARAM_IP) ) )					return;
		if( !this.setParameterIsPort( parameters.get(PARAM_PORT) ) )		return;

		this.stx = parameters.get(PARAM_STX) == null ? null : parameters.get(PARAM_STX).getBytes();
		this.etx = parameters.get(PARAM_ETX) == null ? null : parameters.get(PARAM_ETX).getBytes();
		
		this.queue = new ArrayBlockingQueue<String>(DEFAULT_QUEUE_SIZE);
		
		synchronized(lock){
			if( sub == null ){
				sub = new TCPAppenderSub();
				sub.setDaemon(true);
				sub.start();
			}
			
			sub.addTCPAppender(this, this.queue);
		}
		
	}
	
	/*
	 *		���� ���Ͽ��� ip ���� �м�/�����Ѵ�
	 *		@return	���� ���� �� true 
	 */
	private boolean setParameterIsIp(String value){
		if( value == null || value.equals("") ){
			LoggerManager.getInstance().getRootLogger().error("�α� TCP ���� �ʼ� ���� 'IP' �� ������� ����");
			return false;
		}else{
			this.ip = value;
			return true;
		}
	}
	
	/*
	 *		���� ���Ͽ��� port ���� �м�/�����Ѵ�
	 *		@return	���� ���� �� true 
	 */
	private boolean setParameterIsPort(String value){
		if( value == null || value.equals("") ){
			LoggerManager.getInstance().getRootLogger().error("�α� TCP ���� �ʼ� ���� 'PORT' �� ������� ����");
			return false;
		}else{
			try{
				this.port = Integer.parseInt(value);
				return true;
			}catch(Exception e){
				LoggerManager.getInstance().getRootLogger().error("�α� TCP ���� �ʼ� ���� 'PORT' �� �߸� ������ (", value , ")");
				return false;
			}
		}
	}
	
	/**
	 * �α� �����͸� ������ ������ �����Ѵ�
	 * @return		���� ���� �� true
	 */
	boolean connect(){
		if( this.sc != null )		return true;
		
		try{
			this.sc = SocketChannel.open(new InetSocketAddress(this.ip, this.port));
			this.sc.configureBlocking(false);
			LoggerManager.getInstance().getRootLogger().debug("TCPAppender ���� ���� (IP:", this.ip, " , PORT:", this.port, ")");
			return true;
		}catch(Exception e){
			LoggerManager.getInstance().getRootLogger().warn(e, "TCPAppender ���� ���� (IP:", this.ip, " , PORT:", this.port, ")");
			this.closeConnect();
			return false;
		}
	}
	
	/**
	 * �α� �����͸� ������ �������� ������ ���� �����Ѵ�
	 */
	void closeConnect(){
		
		this.queue.clear();
		
		if( this.sc != null ){
			try{
				this.sc.close();
			}catch(Exception e){}
			this.sc = null;
		}
		
		LoggerManager.getInstance().getRootLogger().debug("TCPAppender ���� ���� (IP:", this.ip, " , PORT:", this.port, ")");
	}
	
	/**
	 * ������ ť�� ���� �α� �����͸� ����
	 */
	void writeMessage(){
		int size = this.queue.size();
		for(int i=0; i < size; i++){
			this.sendMsg = this.queue.isEmpty() ? null : this.queue.poll().getBytes();
			if( this.sendMsg == null )		break;
			
			try{
				// STX ����
				if( this.stx != null )		this.writeSTX();
				// ���� ���� ����
				super.write(this.bb, this.sendMsg);
				// ETX ����
				if( this.etx != null )		this.writeETX();
				// �ٹٲ�
				this.writeNewLine();
			}catch(Exception e){
				this.closeConnect();
				break;
			}
			
		}
		
		this.sendMsg = null;
	}
	
	private void writeNewLine() throws Exception{
		this.bb.clear();
		this.bb.put(this.newLine);
		this.bb.flip();
		this.sc.write(this.bb);
	}
	
	/*		STX ���� �����Ǿ� ���� ��� STX ����		*/
	private void writeSTX() throws Exception{
		this.bb.clear();
		this.bb.put(this.stx);
		this.bb.flip();
		this.sc.write(this.bb);
	}
	
	/*		ETX ���� �����Ǿ� ���� ��� ETX ����		*/
	private void writeETX() throws Exception{
		this.bb.clear();
		this.bb.put(this.etx);
		this.bb.flip();
		this.sc.write(this.bb);
	}
	
	@Override
	protected void write(ByteBuffer bb) throws Exception{
		this.sc.write(bb);
	}
	
}
