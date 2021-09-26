package kr.co.bomz.logger.appender;

import java.io.File;

/**
 * 
 * FileAppender 사용 시 사용되며, 시스템 재시작 시<p>
 * 마지막에 사용한 로그 번호를 로딩하는 기능 수행 
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class FileAppenderFilter{

	private final String fileName;
	private final String fileNameExtension;
	
	private final int fileNameLength;
	private final int fileNameExtensionLength;
	
	/**
	 * FileAppender 에 의해 사용된다
	 * @param fileName		파일 명
	 * @param fileNameExtension		파일 확장자
	 */
	FileAppenderFilter(String fileName, String fileNameExtension){
		this.fileName = fileName;
		this.fileNameExtension = fileNameExtension;
		this.fileNameLength = this.fileName.length();
		this.fileNameExtensionLength = this.fileNameExtension.length();
	}
	
	/**
	 * 해당 경로에 있는 파일 중 지정한 파일명과 확장자에 맞는<p>
	 * 파일 중 마지막에 사용된 번호를 검색한다
	 * 
	 * @param dir	경로
	 * @return		마지막으로 사용된 번호
	 */
	public int filter(File dir) {
		String[] files = dir.list();
		
		int result = 0;
		if( files == null )		return result;
		
		int value;
		int length = files.length;
		for(int i=0; i < length; i++){
			if( !files[i].startsWith(this.fileName) )			continue;
			if( !files[i].endsWith(this.fileNameExtension) )		continue;
			
			try{
				value = Integer.parseInt( files[i].substring(this.fileNameLength, files[i].length() - this.fileNameExtensionLength) );
				if( result < value )				result = value;
			}catch(Exception e){
				continue;
			}
		}
		
		// 마지막 숫자에 + 1을 한다
		return ++result;
	}

}
