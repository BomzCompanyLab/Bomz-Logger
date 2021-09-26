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
 * HTTP ������� �α� �����͸� �����ϸ�,<p>
 * ���� ���� �� ���� ���� ���� ��� �����͸� �����ϰ� ���� �ʴ´�.<p><p>
 * 
 * �ý����� Ŭ���̾�Ʈ ��Ȱ�� �����ϸ� HTTP �������<p>
 * �����͸� ���۹��� ������ ������ �ʿ��ϴ�<p>
 * ���� param ������Ʈ�� url �Ӽ� ���� �ʼ��� �ʿ��ϸ�<p>
 * �ʿ信 ���� method(GET or POST) , timeOut , id(ParameterID) , encoding , poolUse , poolSize �� ������ �� �ִ�
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
	// ������ URL
	private String url;
	// Method Ÿ�� ����. GET �� ��� true
	private boolean requestMethodIsGet;
	
	/*		�������� ������ : ID		*/
	private static final String PARAM_REQUEST_ID = "id";
	/*		�������� ������ : ENCODING		*/
	private static final String PARAM_REQUEST_ENCODING = "encoding";
	/*		�������� ������ : REQUEST_METHOD		*/
	private static final String PARAM_REQUEST_METHOD = "method";
	/*		�������� ������ : REQUEST_URL		*/
	private static final String PARAM_REQUEST_URL = "url";
	/*		�������� ������ : REQUEST_TIMEOUT		*/
	private static final String PARAM_REQUEST_TIMEOUT = "timeOut";
	
	/*		�α� �����͸� ������ ť�� �⺻ ũ��		*/
	private static final int DEFAULT_QUEUE_SIZE = 400;
	/*		����ȭ�� ���� lock		*/
	private static final Object lock = new Object();
	/*		���� �α� �����͸� ������ ������ 	*/
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
	/*		�������� ������ : POOL_USE		*/
	private static final String PARAM_POOL_USE = "poolUse";
	/*		�������� ������ : POOL_SIZE		*/
	private static final String PARAM_POOL_SIZE = "poolSize";
	
	@Override
	public void write(Level level, StringBuilder dateBuffer,
			StringBuilder callStackBuffer, StringBuilder msgBuffer, StringBuilder errBuffer) {
		
		// ������ ���� ���� ó��
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
		this.setParameterIsEncoding(parameters.get(PARAM_REQUEST_ENCODING));				// 1��
		this.setParameterIsMethod(parameters.get(PARAM_REQUEST_METHOD));	// 2��
		if( !this.setParameterIsUrl(parameters.get(PARAM_REQUEST_URL)) )		return;		// 3��
		this.setParameterIsId(parameters.get(PARAM_REQUEST_ID));			// 4��
		this.setParameterIsTimeOut(parameters.get(PARAM_REQUEST_TIMEOUT));			// 5��
		
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
			if( !useB )		return;		// pool ������� �����Ƿ� ����
		}catch(Exception e){
			// true / false �� �ƴ� ���
			LoggerManager.getInstance().getRootLogger().debug("HTTPAppender pool use value error (true or false) : ", use);
			return;
		}
		
		int sizeValue;
		try{
			sizeValue = size == null ? HTTPAppenderPool.DEFAULT_POOL_SIZE : Integer.parseInt(size);		
		}catch(Exception e){
			sizeValue = HTTPAppenderPool.DEFAULT_POOL_SIZE;
		}
		
		// pool ����� ���
		try{
			this.pool = new HTTPAppenderPool(this, sizeValue);
		}catch(Throwable e){
			// Bomz-util1.x.jar �� ���ԵǾ����� �ʾ��� ��� �߻�
			LoggerManager.getInstance().getRootLogger().debug(e, "non import Bomz-util1.x.jar");
		}
		
	}
	
	// �Ķ���� 'url' ����. �ʼ� ��
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
	
	// �Ķ���� 'id' ����
	private void setParameterIsTimeOut(String timeOut){
		if( timeOut == null || timeOut.equals("") ){
			this.timeOut = DEFAULT_TIMEOUT;
		}else{
			try{
				this.timeOut = Integer.parseInt(timeOut);
			}catch(Exception e){
				LoggerManager.getInstance().getRootLogger().debug("HTTPAppender �߸��� ������. timeOut value is '", timeOut, "'");
				this.timeOut = DEFAULT_TIMEOUT;
			}
		}
	}
	
	// �Ķ���� 'id' ����
	private void setParameterIsId(String id){
		if( id == null || id.equals("") )			id = DEFAULT_PARAM_ID;
		
		if( this.requestMethodIsGet ){		// GET
			this.getParamId = id;
		}else{		// POST
			this.postParamId =  id.getBytes(); 
		}
		
	}
	
	// �Ķ���� 'method' ����
	private void setParameterIsMethod(String method){
		if( method == null || method.equals("") ){
			this.requestMethodIsGet = true;
		}else{
			if( method.equalsIgnoreCase("get") ){
				this.requestMethodIsGet = true;
			}else if( method.equalsIgnoreCase("post") ){
				this.requestMethodIsGet = false;
			}else{
				LoggerManager.getInstance().getRootLogger().debug("HTTPAppender �߸��� ������. method value is '", method, "'");
				this.requestMethodIsGet = true;
			}
		}
	}
	
	// �Ķ���� 'encoding' ����
	private void setParameterIsEncoding(String encoding){
		if( encoding == null || encoding.equals("") ){
			this.encoding = DEFAULT_ENCODING;
		}else{
			this.encoding = encoding;
		}
	}
	
	/**
	 * �α� �����͸� ������ ������ �����Ѵ�
	 * @return		���� ���� �� true
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
	 * Thread Pool ��� ���ο� ���� HTTP ���� ó��
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
	 * ������ ť�� ���� �α� �����͸� ����
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
				LoggerManager.getInstance().getRootLogger().debug("HTTPAppender Response ����. URL=", this.url, " , ResponseCode=", this.tmpResponseCode);
				
		}catch(Exception e){
			LoggerManager.getInstance().getRootLogger().warn(e, "HTTPAppender ���� ����. URL=", this.url);
			this.queue.clear();		// ������ �� ���� ��� ť�� ���� ��� ����
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
	
	// ����Ʈ Ÿ���� ��� �Ķ���� ����
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
