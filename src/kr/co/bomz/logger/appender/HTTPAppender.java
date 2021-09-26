package kr.co.bomz.logger.appender;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import kr.co.bomz.logger.Level;
import kr.co.bomz.logger.LoggerManager;

/**
 * 
 * HTTP 방식으로 로그 데이터를 전송하며,<p>
 * 전송 실패 및 오류 등이 났을 경우 데이터를 보관하고 있지 않는다.<p><p>
 * 
 * 시스템은 클라이언트 역활을 수행하며 HTTP 방식으로<p>
 * 데이터를 전송받을 별도의 서버가 필요하다<p>
 * 따라서 param 엘리먼트의 url 속성 값이 필수로 필요하며<p>
 * 필요에 따라 method(GET or POST) , timeOut , id(ParameterID) , encoding , poolUse , poolSize 를 설정할 수 있다
 * 
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * 
 * @see	ConsoleAppender
 * @see	CustomAppender
 * @see	FileAppender
 * @see	TCPAppender
 *
 */
public class HTTPAppender extends Appender{
	// 서버의 URL
	private String url;
	// Method 타입 여부. GET 일 경우 true
	private boolean requestMethodIsGet;
	
	/*		설정파일 설정명 : ID		*/
	private static final String PARAM_REQUEST_ID = "id";
	/*		설정파일 설정명 : ENCODING		*/
	private static final String PARAM_REQUEST_ENCODING = "encoding";
	/*		설정파일 설정명 : REQUEST_METHOD		*/
	private static final String PARAM_REQUEST_METHOD = "method";
	/*		설정파일 설정명 : REQUEST_URL		*/
	private static final String PARAM_REQUEST_URL = "url";
	/*		설정파일 설정명 : REQUEST_TIMEOUT		*/
	private static final String PARAM_REQUEST_TIMEOUT = "timeOut";
	
	/*		로그 데이터를 저장할 큐의 기본 크기		*/
	private static final int DEFAULT_QUEUE_SIZE = 400;
	/*		동기화를 위한 lock		*/
	private static final Object lock = new Object();
	/*		실제 로그 데이터를 전송할 스레드 	*/
	private static HTTPAppenderSub sub = null;
	
	// commons
	private static final String DEFAULT_PARAM_ID = "logMsg";
	private static final String DEFAULT_PARAM_SIGN = "=";
	private static final byte[] DEFAULT_PARAM_SIGN_BYTE = DEFAULT_PARAM_SIGN.getBytes();
	private static final int DEFAULT_TIMEOUT = 1500;
	private int timeOut;
	
	// post
	private byte[] postParamId;
	private URL postURL;
	
	// get
	private String getParamId;
	
	// encoding
	private static final String DEFAULT_ENCODING = "UTF-8";
	private String encoding;
	
	private ArrayBlockingQueue<String> queue = null;
	private String tmpWriteMessage;
	private int tmpResponseCode;
	
	// Thread Pooling
	private HTTPAppenderPool pool = null;
	/*		설정파일 설정명 : POOL_USE		*/
	private static final String PARAM_POOL_USE = "poolUse";
	/*		설정파일 설정명 : POOL_SIZE		*/
	private static final String PARAM_POOL_SIZE = "poolSize";
	
	@Override
	public void write(Level level, StringBuilder dateBuffer,
			StringBuilder callStackBuffer, StringBuilder msgBuffer, StringBuilder errBuffer) {
		
		// 만약을 위해 예외 처리
		if( this.queue == null )		return;
		
		this.queue.offer(
				dateBuffer.toString() + LEVEL_STX + level.name() + LEVEL_ETX + callStackBuffer.toString() + BLANK +
				(msgBuffer == null ? "" : msgBuffer.toString() ) +
				(errBuffer == null ? "" : (msgBuffer != null ? NEW_LINE + errBuffer.toString() : errBuffer.toString()))
			);
		
	}

	@Override
	public void close() {
		synchronized ( lock ){
			if( sub != null ){
				sub.closeHTTPAppender();
				sub = null;
			}
		}
		
		if( this.pool != null ){
			this.pool.closeThreadPool();
		}
	}

	@Override
	public void setParameter(Map<String, String> parameters) {
		this.setParameterIsEncoding(parameters.get(PARAM_REQUEST_ENCODING));				// 1번
		this.setParameterIsMethod(parameters.get(PARAM_REQUEST_METHOD));	// 2번
		if( !this.setParameterIsUrl(parameters.get(PARAM_REQUEST_URL)) )		return;		// 3번
		this.setParameterIsId(parameters.get(PARAM_REQUEST_ID));			// 4번
		this.setParameterIsTimeOut(parameters.get(PARAM_REQUEST_TIMEOUT));			// 5번
		
		this.queue = new ArrayBlockingQueue<String>(DEFAULT_QUEUE_SIZE);
		
		// thread pool
		this.setPool(parameters.get(PARAM_POOL_USE), parameters.get(PARAM_POOL_SIZE));
		
		synchronized(lock){
			if( sub == null ){
				sub = new HTTPAppenderSub();
				sub.setDaemon(true);
				sub.start();
			}
			
			sub.addHTTPAppender(this, this.queue);
		}
	}

	private void setPool(String use, String size){
		if( use == null )		return;		// pool non use
		
		try{
			boolean useB = Boolean.parseBoolean(use);
			if( !useB )		return;		// pool 사용하지 않으므로 리턴
		}catch(Exception e){
			// true / false 가 아닐 경우
			LoggerManager.getInstance().getRootLogger().debug("HTTPAppender pool use value error (true or false) : ", use);
			return;
		}
		
		int sizeValue;
		try{
			sizeValue = size == null ? HTTPAppenderPool.DEFAULT_POOL_SIZE : Integer.parseInt(size);		
		}catch(Exception e){
			sizeValue = HTTPAppenderPool.DEFAULT_POOL_SIZE;
		}
		
		// pool 사용할 경우
		try{
			this.pool = new HTTPAppenderPool(this, sizeValue);
		}catch(Throwable e){
			// Bomz-util1.x.jar 가 포함되어있지 않았을 경우 발생
			LoggerManager.getInstance().getRootLogger().debug(e, "non import Bomz-util1.x.jar");
		}
		
	}
	
	// 파라메터 'url' 설정. 필수 값
	private boolean setParameterIsUrl(String url){
		if( url == null || url.equals("") )		return false;
		
		char lastChar = url.charAt( url.length() - 1 );
		if( this.requestMethodIsGet ){		// GET
			this.url = (lastChar == '?') ? url : url + "?";
		}else{
			this.url = url;
			try {
				this.postURL = new URL( (lastChar == '/') ? url : url + "/" );
			} catch (MalformedURLException e) {
				LoggerManager.getInstance().getRootLogger().debug(e, "Post URL create error. url=", url);
				return false;
			}
		}
		
		return true;
	}
	
	// 파라메터 'id' 설정
	private void setParameterIsTimeOut(String timeOut){
		if( timeOut == null || timeOut.equals("") ){
			this.timeOut = DEFAULT_TIMEOUT;
		}else{
			try{
				this.timeOut = Integer.parseInt(timeOut);
			}catch(Exception e){
				LoggerManager.getInstance().getRootLogger().debug("HTTPAppender 잘못된 설정값. timeOut value is '", timeOut, "'");
				this.timeOut = DEFAULT_TIMEOUT;
			}
		}
	}
	
	// 파라메터 'id' 설정
	private void setParameterIsId(String id){
		if( id == null || id.equals("") )			id = DEFAULT_PARAM_ID;
		
		if( this.requestMethodIsGet ){		// GET
			this.getParamId = id;
		}else{		// POST
			this.postParamId =  id.getBytes(); 
		}
		
	}
	
	// 파라메터 'method' 설정
	private void setParameterIsMethod(String method){
		if( method == null || method.equals("") ){
			this.requestMethodIsGet = true;
		}else{
			if( method.equalsIgnoreCase("get") ){
				this.requestMethodIsGet = true;
			}else if( method.equalsIgnoreCase("post") ){
				this.requestMethodIsGet = false;
			}else{
				LoggerManager.getInstance().getRootLogger().debug("HTTPAppender 잘못된 설정값. method value is '", method, "'");
				this.requestMethodIsGet = true;
			}
		}
	}
	
	// 파라메터 'encoding' 설정
	private void setParameterIsEncoding(String encoding){
		if( encoding == null || encoding.equals("") ){
			this.encoding = DEFAULT_ENCODING;
		}else{
			this.encoding = encoding;
		}
	}
	
	/**
	 * 로그 데이터를 전송할 서버에 접속한다
	 * @return		접속 성공 시 true
	 */
	private HttpURLConnection connect(HttpURLConnection conn) throws Exception{				
		if( this.requestMethodIsGet ){		// GET
			conn.setRequestMethod("GET");
		}else{		// POST
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
		}

		conn.setDoInput(true);
		conn.setDefaultUseCaches(false);
		conn.setUseCaches(false);
		
		return conn;
	}
	
	/**
	 * Thread Pool 사용 여부에 따라 HTTP 전송 처리
	 */
	void writeMessage(){
		if( this.pool == null ){
			// non pool
			this.write();
		}else{
			this.pool.start();
		}
	}
	
	/**
	 * 서버로 큐에 쌓인 로그 데이터를 전송
	 */
	void write(){
		this.tmpWriteMessage= this.queue.poll();
		if( this.tmpWriteMessage == null )		return;
		
		HttpURLConnection conn = null;
		
		try{
			URL url = this.getURL();
			
			// connect
			conn = this.connect((HttpURLConnection)url.openConnection());
			conn.setConnectTimeout(this.timeOut);
			
			// post parameter
			if( !this.requestMethodIsGet )
				this.postWriteMessage( conn.getOutputStream() );
			
			// response
			this.tmpResponseCode = conn.getResponseCode();
			if( this.tmpResponseCode != HttpURLConnection.HTTP_OK )
				LoggerManager.getInstance().getRootLogger().debug("HTTPAppender Response 오류. URL=", this.url, " , ResponseCode=", this.tmpResponseCode);
				
		}catch(Exception e){
			LoggerManager.getInstance().getRootLogger().warn(e, "HTTPAppender 접속 오류. URL=", this.url);
			this.queue.clear();		// 연결할 수 없을 경우 큐의 내용 모두 삭제
		}finally{
			if( conn != null )		conn.disconnect();
		}
		
	}
		
	// GET or POST URL
	private URL getURL() throws Exception{
		if( this.requestMethodIsGet ){
			return new URL(
					this.url + 
					this.getParamId + 
					DEFAULT_PARAM_SIGN + 
					URLEncoder.encode( this.tmpWriteMessage, this.encoding)
				);
		}else{
			return this.postURL;
		}
	}
	
	// 포스트 타입일 경우 파라메터 전송
	private void postWriteMessage(OutputStream os) throws Exception{
		BufferedOutputStream bos = null;
		try{
			bos = new BufferedOutputStream(os);
			bos.write( this.postParamId );
			bos.write( DEFAULT_PARAM_SIGN_BYTE );
			bos.write( URLEncoder.encode(this.tmpWriteMessage, this.encoding).getBytes() );
			bos.flush();
		}finally{
			if( bos != null )				try{		bos.close();		}catch(Exception e){}
			if( os != null )				try{		os.close();		}catch(Exception e){}
		}
	}
		
}
