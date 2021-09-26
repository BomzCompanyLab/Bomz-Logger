package example.tcp1;

import java.io.BufferedInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * run...
 * 
 * java example.tcp1.SimpleServer 10111
 * java example.tcp1.SimpleServer 15555
 * 
 * @author bomz
 *
 */
public class SimpleServer extends Thread{

	private int port;
	private ServerSocket sSocket;
	private Socket socket;
	private BufferedInputStream bis;	
	private byte[] data = new byte[1000];
	
	public SimpleServer(int port){
		this.port = port;
	}
	
	public static void main(String[] args) throws Exception{
		int port = Integer.parseInt(args[0]);
		
		SimpleServer server = new SimpleServer(port);
		server.start();
	}

	public void run(){
		while( true ){
			if( !this.openServer() )		continue;
			
			if( !this.accept() )		continue;
			
			this.read();
		}
	}
	
	// read message
	private void read(){
		try{
			int size = this.bis.read(this.data);
			if( size == -1 ){
				System.err.println("client disconnect..." + this.port);
				this.close();
			}else if( size != 0){
				System.out.print(new String(this.data, 0, size));
			}
		}catch(Exception e){
			System.err.println("client disconnect : " + e.getMessage());
			this.close();
		}
	}
	
	// client accept
	private boolean accept(){
		if( this.socket != null )		return true;
		
		try{
			this.socket = this.sSocket.accept();
			this.bis = new BufferedInputStream(this.socket.getInputStream());
			System.out.println("client accept : " + this.port);
			return true;
		}catch(Exception e){
			System.err.println("client accept exception : " +e.getMessage());
			this.close();
			try{		Thread.sleep(1000);		}catch(Exception e1){}
			return false;
		}
	}
		
	// open server
	private boolean openServer(){
		if( this.sSocket != null )		return true;
		
		try{
			this.sSocket = new ServerSocket(this.port);
			System.out.println("server open : " + this.port);
			return true;
		}catch(Exception e){
			System.err.println("server open exception : " +e.getMessage());
			try{		Thread.sleep(1000);		}catch(Exception e1){}
			return false;
		}
	}
	
	private void close(){
		if( this.bis != null ){
			try{		this.bis.close();		}catch(Exception e){}
			this.bis = null;
		}
		
		if( this.socket != null ){
			try{		this.socket.close();		}catch(Exception e){}
			this.socket = null;
		}
	}
	
}
