package kr.co.bomz.logger.appender;

import java.io.File;

/**
 * 
 * FileAppender ��� �� ���Ǹ�, �ý��� ����� ��<p>
 * �������� ����� �α� ��ȣ�� �ε��ϴ� ��� ���� 
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
	 * FileAppender �� ���� ���ȴ�
	 * @param fileName		���� ��
	 * @param fileNameExtension		���� Ȯ����
	 */
	FileAppenderFilter(String fileName, String fileNameExtension){
		this.fileName = fileName;
		this.fileNameExtension = fileNameExtension;
		this.fileNameLength = this.fileName.length();
		this.fileNameExtensionLength = this.fileNameExtension.length();
	}
	
	/**
	 * �ش� ��ο� �ִ� ���� �� ������ ���ϸ�� Ȯ���ڿ� �´�<p>
	 * ���� �� �������� ���� ��ȣ�� �˻��Ѵ�
	 * 
	 * @param dir	���
	 * @return		���������� ���� ��ȣ
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
		
		// ������ ���ڿ� + 1�� �Ѵ�
		return ++result;
	}

}
