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
 * 파일에 로그 내용을 출력하는 Appender<p><p>
 * 
 * 파일을 저장할 경로인 param 엘리먼트의 dir 속성 값이 필수로 필요하며<p>
 * 기타 설정으로 period 속성 (파일 생성 주기 day or hour) 값이나<p>
 * size 속성 (파일 크기 단위:MB) 값을 설정할 수 있다
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

	/*		사용자가 설정한 파일 최대 크기		*/
	private long fileSize = -1;
	/*		사용자가 설정한 파일 경로		*/
	private String fileDir;
	/*		사용자가 설정한 파일 생성 주기		*/
	private Period filePeriod = Period.NON;
	
	/*		설정파일 설정명 : 파일 생성 주기		*/
	private static final String PARAM_PERIOD = "period";
	/*		설정파일 설정명 : 파일 크기(MB)		*/
	private static final String PARAM_SIZE = "size";
	/*		설정파일 설정명 : 파일 경로		*/
	private static final String PARAM_DIR = "dir";
	/*		기본 파일 크기 설정 값		*/
	private static final long DEFAULT_FILE_SIZE = -1;
	
	
	/*		시간으로 파일 관리 시 시간 표시 문자		*/
	private static final String TIME = "T";
	/*		조건에 따라 새 파일 생성 시 넘버링 표시 시작 기호		*/
	private static final char NUMBER_STX = '(';
	/*		조건에 따라 새 파일 생성 시 넘버링 표시 종료 기호		*/
	private static final char NUMBER_ETX = ')';
	
	/*		현재 작업하는 로그 파일 객체		*/
	private File file;
	/*		로그 파일명		*/
	private String fileName;
	/*		로그 파일 확장자		*/
	private String fileNameExtension;
	/*		로그 파일 OutputStream		*/
	private FileOutputStream os;
	/*		로그 파일 Channel		*/
	private FileChannel fc;
	/*		로그 파일 출력 시 사용 하는 ByteBuffer		*/
	private ByteBuffer bb = ByteBuffer.allocateDirect(BUFFER_SIZE);
	
	private int year = -1;		// 로그 파일 수정 년
	private int month;			// 로그 파일 수정 월
	private int day;					// 로그 파일 수정 일
	private int hour;				// 로그 파일 수정 시간
	
	private int numbering = -1;		// 파일 이름 번호
	
	/*	속도 향상을 위한 파일 버퍼		*/
	private final StringBuilder buffer = new StringBuilder(BUFFER_SIZE);
	
	private final Thread shutdownHook = new Thread(this);
	
	private final Object lock = new Object();
	
	public FileAppender(){
		// 버퍼에 있는 내용을 파일에 추가하기 위해 추가
		Runtime.getRuntime().addShutdownHook(this.shutdownHook);
		LoggerManager.getInstance().addFileAppender(this);
	}
	
	public void run(){
		/*
		 * 시스템 종료 시 버퍼에 있는 로그 내용을 파일에 추가하기 위해 사용
		 * 시스템 종료 시 호출 됨
		 */
		final int length = this.buffer.length();
		if( length <= 0 )		return;
		
		try {
			synchronized (this.lock){
				super.write(this.bb, this.buffer.toString().getBytes());
				this.buffer.delete(0, length);
			}
		} catch (Exception e) {
			LoggerManager.getInstance().getRootLogger().error(e, "로그 파일쓰기 중 오류");
		}
	}
	
	@Override
	public void write(Level level, StringBuilder dateBuffer,
			StringBuilder callStackBuffer, StringBuilder msgBuffer, StringBuilder errBuffer) {
		
		/*
		 *  파일의 마지막 날짜 비교
		 *  지정한 주기가 지났다면 기존 파일의 파일명 변경 후 새로운 파일 생성
		 *  새로운 파일에 쓰기
		 */
		if( !this.createOrReplaceFile() )		return;		// 로그 파일을 새로 생성하거나 기존 파일의 이름을 변경한다
		
		/*
		 * 파일 크기 비교해서 파일 크기 설정값을 넘었으면 파일명 변경
		 */
		if( !this.checkMaxFileSize() )		return;
			
		
		try {
			synchronized(this.lock){
				// [날짜] [레벨] [소스위치]
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
			LoggerManager.getInstance().getRootLogger().error(e, "로그 파일쓰기 중 오류");
		}
	}
	
	@Override
	protected void write(ByteBuffer bb) throws Exception{
		this.fc.write( this.bb );
	}
	
	@Override
	public void close() {
		
		this.run();		// 버퍼에 남아있는 로그내역 파일에 쓰기
		
		Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
		LoggerManager.getInstance().removeFileAppender(this);
		
		this.fileClose();
		
	}

	/**		로그 파일 종료		*/
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
	
	/**		버퍼에 저장된 로그 내용을 파일에 출력		*/
	public final void flush(){
		this.run();
	}
	
	/*
	 *		설정 파일에서 dir 값을 분석/설정한다
	 *		@return	설정 성공 시 true 
	 */
	private boolean setParameterIsDir(String value){
		if( value == null ){
			LoggerManager.getInstance().getRootLogger().error("로그 파일설정 필수 값인 'dir' 이 선언되지 않음");
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
	 *		설정 파일에서 dir 값을 분석/설정한다
	 *		@return	설정 성공 시 true 
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
	 *		설정 파일에서 period 값을 설정한다
	 *		@return	설정 성공 시 true 
	 */
	private void setParameterIsPeriod(String value){
		if( value == null ){
			this.filePeriod = Period.NON;
		}else{
			try{
				this.filePeriod = Period.valueOf(value.toUpperCase());
			}catch(Exception e){
				LoggerManager.getInstance().getRootLogger().warn(e, "로그 설정 period 값 오류");
				this.filePeriod = Period.NON;
			}
		}
	}
	
	/*
	 *		설정 파일에서 size 값을 설정한다
	 *		@return	설정 성공 시 true 
	 */
	private void setParameterIsSize(String value){
		if( value == null ){
			this.fileSize = DEFAULT_FILE_SIZE;
		}else{
			try{
				int intValue = Integer.parseInt(value);
				if( intValue > 0 )		this.fileSize = intValue * 1024 * 1024;
				else			LoggerManager.getInstance().getRootLogger().warn("로그 설정 파일 크기는 1 이상부터 설정 가능 [value=", value, "]");					
			}catch(Exception e){
				LoggerManager.getInstance().getRootLogger().warn("잘못 설정된 로그 파일 크기 [value=", value, "]");
				this.fileSize = DEFAULT_FILE_SIZE;
			}
		}
	}
	
	
	/*		
	 * 		로그 파일이 없을 경우 새로 생성하며
	 * 		있을 경우 시간을 비교하여 일정 기간이 지났으면 이름 변경 후
	 * 		다시 새로운 파일을 생성한다 
	 */
	private boolean createOrReplaceFile(){
		if( !this.file.isFile() ){		// 파일이 없을 경우 새로 생성
			return this.createNewFile();
	
		}else{		// 파일이 있을 경우 파일의 시간과 설정 주기를 비교하여 처리
			if( this.year == -1 ){
				Calendar date = Calendar.getInstance();
				date.setTimeInMillis( this.file.lastModified() );
				this.settingLastModify(date);
			}
			return this.createOrReplaceFile(Calendar.getInstance());
		}
		
	}
	
	/*		새로운 로그 파일 생성		*/
	private boolean createNewFile(){
		
		this.fileClose();
		
		try {
			if( !this.file.createNewFile() ){
				// 새로운 파일 생성 실패
				LoggerManager.getInstance().getRootLogger().error("로그 파일 생성 실패 (파일=", this.file.getCanonicalFile(), ")");
				return false;
			}
			
			return this.createWriter();
		} catch (IOException e) {
			LoggerManager.getInstance().getRootLogger().error(e, "로그 파일 생성 실패 (파일=", this.file.getName(), ")");
			return false;
		}
	}
	
	/*
	 * 	로그 파일 출력을 위한 File Channel 을 생성한다
	 */
	private boolean createWriter(){
		
		if( this.os != null )		return true;
		
		try{
			this.os = new FileOutputStream(this.file, true);
			this.fc = this.os.getChannel();
			return true;
		}catch(IOException e){
			LoggerManager.getInstance().getRootLogger().error(e, "파일 쓰기 채널 가져오기 실패");
			return false;
		}
	}
	
	/*
	 * 로그 파일을 생성할 경로의 디렉토리 생성
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
			LoggerManager.getInstance().getRootLogger().error(e, "로그 디렉토리 경로 검색 오류");
			return false;
		}
	}
	
	/*		로그 파일의 마지막 수정 시간 설정		*/
	private void settingLastModify(Calendar date){
		this.year = date.get(Calendar.YEAR);
		this.month = date.get(Calendar.MONTH) + 1;
		this.day = date.get(Calendar.DAY_OF_MONTH);
		this.hour = date.get(Calendar.HOUR_OF_DAY);
	}
	
	/*		날짜를 비교하여 일이 변경되었을 경우 파일 이름 변경 후 새로 생성		*/
	private boolean createOrReplaceFile(Calendar date){
		if( this.year != date.get(Calendar.YEAR) || this.month != (date.get(Calendar.MONTH) + 1) ||
				this.day != date.get(Calendar.DAY_OF_MONTH) ){
			// 날짜가 변경되었을 경우 기존 파일을 다른 이름으로 변경 후 새로운 파일 생성
			try{
				return this.replaceFile(date);
			}finally{
				this.numbering = 1;
			}
		}else{
			// 날짜가 똑같을 경우 시간으로 로그 설정이되어 있는지 검사
			if(this.filePeriod == Period.DAY)		return this.createWriter(); 
			if(this.hour == date.get(Calendar.HOUR_OF_DAY))							return this.createWriter();
			
			// 시간으로 로그 설정이 되어있고 마지막 시간과 현재 시간이 다르다면 기존 파일을
			// 다른 이름으로 변경  후 새로운 파일 생성
			try{
				return this.replaceFile(date);
			}finally{
				this.numbering = 1;
			}
		}
	}
	
	/*		
	 * 		파일 사용 기간이 지났을 경우 이전에 사용하던 파일의 사용을 종료, 
	 * 		파일명을 변경, 새로운 파일 생성의 순으로 처리한다
	 */
	private boolean replaceFile(Calendar date){
		this.run();				// 파일을 변경하기 전 버퍼에 남아있는 내용을 추가한다
		this.fileClose();		// 기존파일 연결 종료
		if( this.numbering == -1 )		this.loadingNumbering();
		
		File renameFile = this.getRenameFile(date);
		if( !this.file.renameTo(renameFile) ){
			// 파일 이름 변경 실패
			LoggerManager.getInstance().getRootLogger().error("로그 파일 이름 변경 실패 (파일=" , renameFile.getName(), ")");
			return false;
		}
		
		this.settingLastModify(Calendar.getInstance());
		return this.createNewFile();
	}
	
	/*
	 * 마지막 로그 번호를 분석한다
	 */
	private void loadingNumbering(){
		FileAppenderFilter filter = new FileAppenderFilter(
				this.getDefaultFileName() + NUMBER_STX,
				NUMBER_ETX + "." + this.fileNameExtension
			);
		this.numbering = filter.filter( new File(this.fileDir) );
	}
	
	/*		로그 번호를 3자리 숫자로 생성 후 리턴		*/
	private String getNumbering(){
		if( this.numbering < 10 )				return "00" + this.numbering++;
		else if( this.numbering < 100)		return "0" + this.numbering++;
		else												return "" + this.numbering++;
	}
	
	/*		파일의 사용 기간이 지났을 경우 변경할 파일 정보 리턴		*/
	private File getRenameFile(Calendar date){
		return new File(
				this.fileDir + this.getDefaultFileName() + 
				NUMBER_STX + getNumbering() + NUMBER_ETX +
				"." + this.fileNameExtension 
			);
	}
	
	/*		기본 파일 명 생성		*/
	private String getDefaultFileName(){
		return this.fileName + "." + this.year + "-" + this.month + "-" + this.day + 
		(this.filePeriod == Period.DAY ? "" : TIME + this.hour); 
	}
		
	/*
	 * 파일 최대 크기가 정해져있을 경우 현재 파일 크기 비교 후
	 * 지정 크기보다 크다면 파일 새로 생성
	 */
	private boolean checkMaxFileSize(){
		// 파일 최대 크기가 설정되어 있지 않을 경우
		if( this.fileSize == DEFAULT_FILE_SIZE )		return true;
		
		// 지정된 파일 최대 크기가 더 클 경우 
		if ( this.fileSize > this.file.length() )		return true;

		// 로그파일 크기가 지정된 최대 크기를 넘겼을 경우 파일 새로 생성
		return this.replaceFile(Calendar.getInstance());
	}
	
}
