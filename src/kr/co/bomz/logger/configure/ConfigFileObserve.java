package kr.co.bomz.logger.configure;

import java.io.File;

import kr.co.bomz.logger.LoggerManager;

/**
 * 
 * �α� ���������� ���� ���� �ǽð� ����
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		ConfigFileReader
 *
 */
public class ConfigFileObserve extends Thread{

	// ������ ��� ����
	private boolean run = false;
	// ���� ���� �� ����
	private boolean isOn;
	
	// �α� ����
	private File configFile;
	// �α� ���� ������ ����Ǿ��� ��� ����� �α� ����
	private File newConfigFile;
	// �α� ���� ���� ���� ������ �˻� �ð�
	private long configFileLastModified;
	// ����ȭ�� ���� ��
	private final Object lock = new Object();
	
	public ConfigFileObserve(){}
	
	public void run(){
		long lastModified;
		while( this.isOn ){
			// 3�ʸ��� �α� ���� ���濩�� �˻� ����
			try{		Thread.sleep(3000);		}catch(Exception e){}
			
			// ����ڰ� �α����� ��θ� �����ߴ��� Ȯ��
			this.checkNewConfigFile();
			
			synchronized( this.lock ){
				// ������ ���� ���� ó��. 
				if( this.configFile == null )		continue;
				
				lastModified = this.configFile.lastModified();		// ������ ���� �ð�
				if( lastModified <= this.configFileLastModified )		continue;
				
				// �ΰ� ���� ������ ����Ǿ��� ���
				this.configFileLastModified = lastModified;
				LoggerManager.getInstance().reset();		// �α� �ʱ�ȭ
			}
			
		}
	}
		
	/*		����ڰ� �α� ���� ��θ� �����ߴ��� �˻�		*/
	private void checkNewConfigFile(){
		if( this.newConfigFile == null )		return;
		
		this.configFile = this.newConfigFile;
		this.configFileLastModified = this.configFile.lastModified();
		this.newConfigFile = null;
	}
	
	/**
	 * �α� ���� ���� ���� �ǽð� �˻� ����<p>
	 * �ѹ� ������Ų ��� �絿�� ��ų �� ����
	 * @param isOn		true �� ��� �ǽð� �˻� ����
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
	 * �α� ���� ��� ����
	 * @param configFile		������ �α� ����
	 */
	public void setConfigFile(File configFile){
		synchronized ( this.lock ){
			if( configFile == null )		return;
			
			this.newConfigFile = configFile;
		}
	}
	
}
