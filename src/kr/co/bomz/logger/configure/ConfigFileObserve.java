package kr.co.bomz.logger.configure;

import java.io.File;

import kr.co.bomz.logger.LoggerManager;

/**
 * 
 * 로그 설정파일의 변경 여부 실시간 감시
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		ConfigFileReader
 *
 */
public class ConfigFileObserve extends Thread{

	// 옵저버 사용 여부
	private boolean run = false;
	// 현재 동작 중 여부
	private boolean isOn;
	
	// 로그 파일
	private File configFile;
	// 로그 파일 내용이 변경되었을 경우 변경된 로그 파일
	private File newConfigFile;
	// 로그 파일 변경 여부 마지막 검사 시간
	private long configFileLastModified;
	// 동기화를 위한 락
	private final Object lock = new Object();
	
	public ConfigFileObserve(){}
	
	public void run(){
		long lastModified;
		while( this.isOn ){
			// 3초마다 로그 파일 변경여부 검사 수행
			try{		Thread.sleep(3000);		}catch(Exception e){}
			
			// 사용자가 로그파일 경로를 변경했는지 확인
			this.checkNewConfigFile();
			
			synchronized( this.lock ){
				// 만약을 위한 예외 처리. 
				if( this.configFile == null )		continue;
				
				lastModified = this.configFile.lastModified();		// 마지막 수정 시간
				if( lastModified <= this.configFileLastModified )		continue;
				
				// 로거 설정 파일이 변경되었을 경우
				this.configFileLastModified = lastModified;
				LoggerManager.getInstance().reset();		// 로그 초기화
			}
			
		}
	}
		
	/*		사용자가 로그 파일 경로를 변경했는지 검사		*/
	private void checkNewConfigFile(){
		if( this.newConfigFile == null )		return;
		
		this.configFile = this.newConfigFile;
		this.configFileLastModified = this.configFile.lastModified();
		this.newConfigFile = null;
	}
	
	/**
	 * 로그 파일 변경 여부 실시간 검사 여부<p>
	 * 한번 정지시킨 경우 재동작 시킬 수 없다
	 * @param isOn		true 일 경우 실시간 검사 수행
	 */
	public synchronized void observe(boolean isOn){
		this.isOn = isOn;
		if( this.isOn && !this.run ){
			super.setDaemon(true);
			start();
			this.run = true;
		}
	}
	
	/**
	 * 로그 파일 경로 변경
	 * @param configFile		변경할 로그 파일
	 */
	public void setConfigFile(File configFile){
		synchronized ( this.lock ){
			if( configFile == null )		return;
			
			this.newConfigFile = configFile;
		}
	}
	
}
