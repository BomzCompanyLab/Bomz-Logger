package kr.co.bomz.logger.appender;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Map;

import kr.co.bomz.logger.Level;
import kr.co.bomz.logger.LoggerManager;

/**
 * 
 * ���Ͽ� �α� ������ ����ϴ� Appender<p><p>
 * 
 * ������ ������ ����� param ������Ʈ�� dir �Ӽ� ���� �ʼ��� �ʿ��ϸ�<p>
 * ��Ÿ �������� period �Ӽ� (���� ���� �ֱ� day or hour) ���̳�<p>
 * size �Ӽ� (���� ũ�� ����:MB) ���� ������ �� �ִ�
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.1
 * @see	ConsoleAppender
 * @see	CustomAppender
 * @see	TCPAppender
 * @see	HTTPAppender
 *
 */
public class FileAppender extends Appender implements Runnable{

	/*		����ڰ� ������ ���� �ִ� ũ��		*/
	private long fileSize = -1;
	/*		����ڰ� ������ ���� ���		*/
	private String fileDir;
	/*		����ڰ� ������ ���� ���� �ֱ�		*/
	private Period filePeriod = Period.NON;
	
	/*		�������� ������ : ���� ���� �ֱ�		*/
	private static final String PARAM_PERIOD = "period";
	/*		�������� ������ : ���� ũ��(MB)		*/
	private static final String PARAM_SIZE = "size";
	/*		�������� ������ : ���� ���		*/
	private static final String PARAM_DIR = "dir";
	/*		�⺻ ���� ũ�� ���� ��		*/
	private static final long DEFAULT_FILE_SIZE = -1;
	
	
	/*		�ð����� ���� ���� �� �ð� ǥ�� ����		*/
	private static final String TIME = "T";
	/*		���ǿ� ���� �� ���� ���� �� �ѹ��� ǥ�� ���� ��ȣ		*/
	private static final char NUMBER_STX = '(';
	/*		���ǿ� ���� �� ���� ���� �� �ѹ��� ǥ�� ���� ��ȣ		*/
	private static final char NUMBER_ETX = ')';
	
	/*		���� �۾��ϴ� �α� ���� ��ü		*/
	private File file;
	/*		�α� ���ϸ�		*/
	private String fileName;
	/*		�α� ���� Ȯ����		*/
	private String fileNameExtension;
	/*		�α� ���� OutputStream		*/
	private FileOutputStream os;
	/*		�α� ���� Channel		*/
	private FileChannel fc;
	/*		�α� ���� ��� �� ��� �ϴ� ByteBuffer		*/
	private ByteBuffer bb = ByteBuffer.allocateDirect(BUFFER_SIZE);
	
	private int year = -1;		// �α� ���� ���� ��
	private int month;			// �α� ���� ���� ��
	private int day;					// �α� ���� ���� ��
	private int hour;				// �α� ���� ���� �ð�
	
	private int numbering = -1;		// ���� �̸� ��ȣ
	
	/*	�ӵ� ����� ���� ���� ����		*/
	private final StringBuilder buffer = new StringBuilder(BUFFER_SIZE);
	
	private final Thread shutdownHook = new Thread(this);
	
	private final Object lock = new Object();
	
	public FileAppender(){
		// ���ۿ� �ִ� ������ ���Ͽ� �߰��ϱ� ���� �߰�
		Runtime.getRuntime().addShutdownHook(this.shutdownHook);
		LoggerManager.getInstance().addFileAppender(this);
	}
	
	public void run(){
		/*
		 * �ý��� ���� �� ���ۿ� �ִ� �α� ������ ���Ͽ� �߰��ϱ� ���� ���
		 * �ý��� ���� �� ȣ�� ��
		 */
		final int length = this.buffer.length();
		if( length <= 0 )		return;
		
		try {
			synchronized (this.lock){
				super.write(this.bb, this.buffer.toString().getBytes());
				this.buffer.delete(0, length);
			}
		} catch (Exception e) {
			LoggerManager.getInstance().getRootLogger().error(e, "�α� ���Ͼ��� �� ����");
		}
	}
	
	@Override
	public void write(Level level, StringBuilder dateBuffer,
			StringBuilder callStackBuffer, StringBuilder msgBuffer, StringBuilder errBuffer) {
		
		/*
		 *  ������ ������ ��¥ ��
		 *  ������ �ֱⰡ �����ٸ� ���� ������ ���ϸ� ���� �� ���ο� ���� ����
		 *  ���ο� ���Ͽ� ����
		 */
		if( !this.createOrReplaceFile() )		return;		// �α� ������ ���� �����ϰų� ���� ������ �̸��� �����Ѵ�
		
		/*
		 * ���� ũ�� ���ؼ� ���� ũ�� �������� �Ѿ����� ���ϸ� ����
		 */
		if( !this.checkMaxFileSize() )		return;
			
		
		try {
			synchronized(this.lock){
				// [��¥] [����] [�ҽ���ġ]
				this.buffer.append(dateBuffer).append(LEVEL_STX)
								.append(level.name()).append(LEVEL_ETX)
								.append(callStackBuffer).append(BLANK);
				
				if( msgBuffer != null )		this.buffer.append(msgBuffer);
				this.buffer.append(NEW_LINE);
				if( errBuffer != null )		this.buffer.append(errBuffer);
				
				int length = this.buffer.length();
				if( length > BUFFER_SIZE ){
					super.write(this.bb, this.buffer.toString().getBytes());
					this.buffer.delete(0, length);
				}
			}
		} catch (Exception e) {
			LoggerManager.getInstance().getRootLogger().error(e, "�α� ���Ͼ��� �� ����");
		}
	}
	
	@Override
	protected void write(ByteBuffer bb) throws Exception{
		this.fc.write( this.bb );
	}
	
	@Override
	public void close() {
		
		this.run();		// ���ۿ� �����ִ� �α׳��� ���Ͽ� ����
		
		Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
		LoggerManager.getInstance().removeFileAppender(this);
		
		this.fileClose();
		
	}

	/**		�α� ���� ����		*/
	private void fileClose(){
		if( this.fc != null ){
			try{		this.fc.close();		}catch(Exception e){}
			this.fc = null;
		}
		
		if( this.os != null ){
			try{		this.os.close();		}catch(Exception e){}
			this.os = null;
		}
	}
	
	@Override
	public void setParameter(Map<String, String> parameters) {
		if( parameters == null )		return;
	
		if( !this.setParameterIsDir(parameters.get(PARAM_DIR))	 )	return;		
		this.setParameterIsSize(parameters.get(PARAM_SIZE));
		this.setParameterIsPeriod(parameters.get(PARAM_PERIOD));
		
		if( !this.makeDirs() )		this.fileClose();
	}
	
	/**		���ۿ� ����� �α� ������ ���Ͽ� ���		*/
	public final void flush(){
		this.run();
	}
	
	/*
	 *		���� ���Ͽ��� dir ���� �м�/�����Ѵ�
	 *		@return	���� ���� �� true 
	 */
	private boolean setParameterIsDir(String value){
		if( value == null ){
			LoggerManager.getInstance().getRootLogger().error("�α� ���ϼ��� �ʼ� ���� 'dir' �� ������� ����");
			this.fileDir = null;
			return false;
		}else{
			this.file = new File( value );
			String fileName = this.file.getName();
			this.setParameterIsDir( fileName.split("\\.") );
			this.fileDir = value.substring(0, value.length() - fileName.length());
			return true;
		}
	}
	
	/*
	 *		���� ���Ͽ��� dir ���� �м�/�����Ѵ�
	 *		@return	���� ���� �� true 
	 */
	private void setParameterIsDir(String[] fileNames){
		int length = fileNames.length;
		if( length == 1 ){
			this.fileName = fileNames[0];
			this.fileNameExtension = "";
		}else{
			length--;
			StringBuilder buffer = new StringBuilder();
			for(int i=0; i < length; i++){
				buffer.append(fileNames[i]);
				if( i+1 < length )		buffer.append(".");		
			}
			
			this.fileName = buffer.toString();
			this.fileNameExtension = fileNames[length];
		}
	}
	
	/*
	 *		���� ���Ͽ��� period ���� �����Ѵ�
	 *		@return	���� ���� �� true 
	 */
	private void setParameterIsPeriod(String value){
		if( value == null ){
			this.filePeriod = Period.NON;
		}else{
			try{
				this.filePeriod = Period.valueOf(value.toUpperCase());
			}catch(Exception e){
				LoggerManager.getInstance().getRootLogger().warn(e, "�α� ���� period �� ����");
				this.filePeriod = Period.NON;
			}
		}
	}
	
	/*
	 *		���� ���Ͽ��� size ���� �����Ѵ�
	 *		@return	���� ���� �� true 
	 */
	private void setParameterIsSize(String value){
		if( value == null ){
			this.fileSize = DEFAULT_FILE_SIZE;
		}else{
			try{
				int intValue = Integer.parseInt(value);
				if( intValue > 0 )		this.fileSize = intValue * 1024 * 1024;
				else			LoggerManager.getInstance().getRootLogger().warn("�α� ���� ���� ũ��� 1 �̻���� ���� ���� [value=", value, "]");					
			}catch(Exception e){
				LoggerManager.getInstance().getRootLogger().warn("�߸� ������ �α� ���� ũ�� [value=", value, "]");
				this.fileSize = DEFAULT_FILE_SIZE;
			}
		}
	}
	
	
	/*		
	 * 		�α� ������ ���� ��� ���� �����ϸ�
	 * 		���� ��� �ð��� ���Ͽ� ���� �Ⱓ�� �������� �̸� ���� ��
	 * 		�ٽ� ���ο� ������ �����Ѵ� 
	 */
	private boolean createOrReplaceFile(){
		if( !this.file.isFile() ){		// ������ ���� ��� ���� ����
			return this.createNewFile();
	
		}else{		// ������ ���� ��� ������ �ð��� ���� �ֱ⸦ ���Ͽ� ó��
			if( this.year == -1 ){
				Calendar date = Calendar.getInstance();
				date.setTimeInMillis( this.file.lastModified() );
				this.settingLastModify(date);
			}
			return this.createOrReplaceFile(Calendar.getInstance());
		}
		
	}
	
	/*		���ο� �α� ���� ����		*/
	private boolean createNewFile(){
		
		this.fileClose();
		
		try {
			if( !this.file.createNewFile() ){
				// ���ο� ���� ���� ����
				LoggerManager.getInstance().getRootLogger().error("�α� ���� ���� ���� (����=", this.file.getCanonicalFile(), ")");
				return false;
			}
			
			return this.createWriter();
		} catch (IOException e) {
			LoggerManager.getInstance().getRootLogger().error(e, "�α� ���� ���� ���� (����=", this.file.getName(), ")");
			return false;
		}
	}
	
	/*
	 * 	�α� ���� ����� ���� File Channel �� �����Ѵ�
	 */
	private boolean createWriter(){
		
		if( this.os != null )		return true;
		
		try{
			this.os = new FileOutputStream(this.file, true);
			this.fc = this.os.getChannel();
			return true;
		}catch(IOException e){
			LoggerManager.getInstance().getRootLogger().error(e, "���� ���� ä�� �������� ����");
			return false;
		}
	}
	
	/*
	 * �α� ������ ������ ����� ���丮 ����
	 */
	private boolean makeDirs(){
		try{
			String[] args = file.getCanonicalPath().split("\\\\");
			int length = args.length - 1;
			
			StringBuilder buffer = new StringBuilder();
			for(int i=0; i < length; i++)
				buffer.append(args[i] + File.separatorChar);
			
			File dirFile = new File(buffer.toString());
			dirFile.mkdirs();
			
			return true;
		}catch(Exception e){
			LoggerManager.getInstance().getRootLogger().error(e, "�α� ���丮 ��� �˻� ����");
			return false;
		}
	}
	
	/*		�α� ������ ������ ���� �ð� ����		*/
	private void settingLastModify(Calendar date){
		this.year = date.get(Calendar.YEAR);
		this.month = date.get(Calendar.MONTH) + 1;
		this.day = date.get(Calendar.DAY_OF_MONTH);
		this.hour = date.get(Calendar.HOUR_OF_DAY);
	}
	
	/*		��¥�� ���Ͽ� ���� ����Ǿ��� ��� ���� �̸� ���� �� ���� ����		*/
	private boolean createOrReplaceFile(Calendar date){
		if( this.year != date.get(Calendar.YEAR) || this.month != (date.get(Calendar.MONTH) + 1) ||
				this.day != date.get(Calendar.DAY_OF_MONTH) ){
			// ��¥�� ����Ǿ��� ��� ���� ������ �ٸ� �̸����� ���� �� ���ο� ���� ����
			try{
				return this.replaceFile(date);
			}finally{
				this.numbering = 1;
			}
		}else{
			// ��¥�� �Ȱ��� ��� �ð����� �α� �����̵Ǿ� �ִ��� �˻�
			if(this.filePeriod == Period.DAY)		return this.createWriter(); 
			if(this.hour == date.get(Calendar.HOUR_OF_DAY))							return this.createWriter();
			
			// �ð����� �α� ������ �Ǿ��ְ� ������ �ð��� ���� �ð��� �ٸ��ٸ� ���� ������
			// �ٸ� �̸����� ����  �� ���ο� ���� ����
			try{
				return this.replaceFile(date);
			}finally{
				this.numbering = 1;
			}
		}
	}
	
	/*		
	 * 		���� ��� �Ⱓ�� ������ ��� ������ ����ϴ� ������ ����� ����, 
	 * 		���ϸ��� ����, ���ο� ���� ������ ������ ó���Ѵ�
	 */
	private boolean replaceFile(Calendar date){
		this.run();				// ������ �����ϱ� �� ���ۿ� �����ִ� ������ �߰��Ѵ�
		this.fileClose();		// �������� ���� ����
		if( this.numbering == -1 )		this.loadingNumbering();
		
		File renameFile = this.getRenameFile(date);
		if( !this.file.renameTo(renameFile) ){
			// ���� �̸� ���� ����
			LoggerManager.getInstance().getRootLogger().error("�α� ���� �̸� ���� ���� (����=" , renameFile.getName(), ")");
			return false;
		}
		
		this.settingLastModify(Calendar.getInstance());
		return this.createNewFile();
	}
	
	/*
	 * ������ �α� ��ȣ�� �м��Ѵ�
	 */
	private void loadingNumbering(){
		FileAppenderFilter filter = new FileAppenderFilter(
				this.getDefaultFileName() + NUMBER_STX,
				NUMBER_ETX + "." + this.fileNameExtension
			);
		this.numbering = filter.filter( new File(this.fileDir) );
	}
	
	/*		�α� ��ȣ�� 3�ڸ� ���ڷ� ���� �� ����		*/
	private String getNumbering(){
		if( this.numbering < 10 )				return "00" + this.numbering++;
		else if( this.numbering < 100)		return "0" + this.numbering++;
		else												return "" + this.numbering++;
	}
	
	/*		������ ��� �Ⱓ�� ������ ��� ������ ���� ���� ����		*/
	private File getRenameFile(Calendar date){
		return new File(
				this.fileDir + this.getDefaultFileName() + 
				NUMBER_STX + getNumbering() + NUMBER_ETX +
				"." + this.fileNameExtension 
			);
	}
	
	/*		�⺻ ���� �� ����		*/
	private String getDefaultFileName(){
		return this.fileName + "." + this.year + "-" + this.month + "-" + this.day + 
		(this.filePeriod == Period.DAY ? "" : TIME + this.hour); 
	}
		
	/*
	 * ���� �ִ� ũ�Ⱑ ���������� ��� ���� ���� ũ�� �� ��
	 * ���� ũ�⺸�� ũ�ٸ� ���� ���� ����
	 */
	private boolean checkMaxFileSize(){
		// ���� �ִ� ũ�Ⱑ �����Ǿ� ���� ���� ���
		if( this.fileSize == DEFAULT_FILE_SIZE )		return true;
		
		// ������ ���� �ִ� ũ�Ⱑ �� Ŭ ��� 
		if ( this.fileSize > this.file.length() )		return true;

		// �α����� ũ�Ⱑ ������ �ִ� ũ�⸦ �Ѱ��� ��� ���� ���� ����
		return this.replaceFile(Calendar.getInstance());
	}
	
}
