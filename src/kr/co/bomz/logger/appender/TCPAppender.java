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
 * TCP 방식으로 로그 데이터를 전송하며,<p>
 * 전송 실패 및 오류 등이 났을 경우 데이터를 보관하고 있지 않는다.<p><p>
 * 
 * 시스템은 클라이언트 역활을 수행하며 TCP방식으로<p>
 * 데이터를 전송받을 별도의 서버가 필요하다<p>
 * 따라서 param 엘리먼트의 ip 속성과 port 속성 값이 필수로 필요하며<p>
 * 필요에 따라 데이터의 시작과 끝을 선언할 stx , etx 값을 설정할 수 있다
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

	/*		서버 IP		*/
	private String ip;
	/*		서버 PORT		*/
	private int port;
	/*		데이터 전송 시 시작 표시 문자 (생략가능)		*/
	private byte[] stx;
	/*		데이터 전송 시 종료 표시 문자 (생략가능)		*/
	private byte[] etx;
	
	/*		설정파일 설정명 : 서버 IP		*/
	private static final String PARAM_IP = "ip";
	/*		설정파일 설정명 : 서버 PORT		*/
	private static final String PARAM_PORT = "port";
	/*		설정파일 설정명 : 데이터 시작 표시 문자		*/
	private static final String PARAM_STX = "stx";
	/*		설정파일 설정명 : 데이터 종료 표시 문자		*/
	private static final String PARAM_ETX = "etx";
	
	/*		로그 데이터를 저장할 큐의 기본 크기		*/
	private static final int DEFAULT_QUEUE_SIZE = 200;
	
	/*		동기화를 위한 lock		*/
	private static final Object lock = new Object();
	/*		실제 로그 데이터를 전송할 스레드 	*/
	private static TCPAppenderSub sub = null;
	/*		로드 데이터가 저장되는 큐		*/
	private ArrayBlockingQueue<String> queue = null;
	
	/*		서버와 연결된 Socket Channel		*/
	private SocketChannel sc;
	/*		전송할 로그 데이터		*/
	private byte[] sendMsg;
	/*		전송할 로그 데이터 버퍼		*/
	private ByteBuffer bb = ByteBuffer.allocate(BUFFER_SIZE);
	/*		줄바꿈		*/
	private final byte[] newLine = "\n".getBytes();
	
	public TCPAppender(){}
	
	@Override
	public void write(Level level, StringBuilder dateBuffer,
			StringBuilder callStackBuffer, StringBuilder msgBuffer, StringBuilder errBuffer) {
		
		// 만약을 위해 예외 처리
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
	 *		설정 파일에서 ip 값을 분석/설정한다
	 *		@return	설정 성공 시 true 
	 */
	private boolean setParameterIsIp(String value){
		if( value == null || value.equals("") ){
			LoggerManager.getInstance().getRootLogger().error("로그 TCP 설정 필수 값인 'IP' 가 선언되지 않음");
			return false;
		}else{
			this.ip = value;
			return true;
		}
	}
	
	/*
	 *		설정 파일에서 port 값을 분석/설정한다
	 *		@return	설정 성공 시 true 
	 */
	private boolean setParameterIsPort(String value){
		if( value == null || value.equals("") ){
			LoggerManager.getInstance().getRootLogger().error("로그 TCP 설정 필수 값인 'PORT' 가 선언되지 않음");
			return false;
		}else{
			try{
				this.port = Integer.parseInt(value);
				return true;
			}catch(Exception e){
				LoggerManager.getInstance().getRootLogger().error("로그 TCP 설정 필수 값인 'PORT' 가 잘못 설정됨 (", value , ")");
				return false;
			}
		}
	}
	
	/**
	 * 로그 데이터를 전송할 서버에 접속한다
	 * @return		접속 성공 시 true
	 */
	boolean connect(){
		if( this.sc != null )		return true;
		
		try{
			this.sc = SocketChannel.open(new InetSocketAddress(this.ip, this.port));
			this.sc.configureBlocking(false);
			LoggerManager.getInstance().getRootLogger().debug("TCPAppender 접속 성공 (IP:", this.ip, " , PORT:", this.port, ")");
			return true;
		}catch(Exception e){
			LoggerManager.getInstance().getRootLogger().warn(e, "TCPAppender 접속 오류 (IP:", this.ip, " , PORT:", this.port, ")");
			this.closeConnect();
			return false;
		}
	}
	
	/**
	 * 로그 데이터를 전송할 서버와의 연결을 정상 종료한다
	 */
	void closeConnect(){
		
		this.queue.clear();
		
		if( this.sc != null ){
			try{
				this.sc.close();
			}catch(Exception e){}
			this.sc = null;
		}
		
		LoggerManager.getInstance().getRootLogger().debug("TCPAppender 접속 종료 (IP:", this.ip, " , PORT:", this.port, ")");
	}
	
	/**
	 * 서버로 큐에 쌓인 로그 데이터를 전송
	 */
	void writeMessage(){
		int size = this.queue.size();
		for(int i=0; i < size; i++){
			this.sendMsg = this.queue.isEmpty() ? null : this.queue.poll().getBytes();
			if( this.sendMsg == null )		break;
			
			try{
				// STX 전송
				if( this.stx != null )		this.writeSTX();
				// 본문 내용 전송
				super.write(this.bb, this.sendMsg);
				// ETX 전송
				if( this.etx != null )		this.writeETX();
				// 줄바꿈
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
	
	/*		STX 값이 설정되어 있을 경우 STX 전송		*/
	private void writeSTX() throws Exception{
		this.bb.clear();
		this.bb.put(this.stx);
		this.bb.flip();
		this.sc.write(this.bb);
	}
	
	/*		ETX 값이 설정되어 있을 경우 ETX 전송		*/
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
