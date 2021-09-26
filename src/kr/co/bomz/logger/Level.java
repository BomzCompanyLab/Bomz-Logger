package kr.co.bomz.logger;

/**
 * 
 * �α� ����
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 */
public enum Level {

	TRACE(0),
	
	DEBUG(1),
	
	INFO(2),
	
	WARN(3),
	
	ERROR(4),
	
	FATAL(5),
	
	OFF(6);	
	
	private final int level;
	
	private static final byte[] TRACE_BYTE = "TRACE".getBytes();
	private static final byte[] DEBUG_BYTE = "DEBUG".getBytes();
	private static final byte[] INFO_BYTE = "INFO".getBytes();
	private static final byte[] WARN_BYTE = "WARN".getBytes();
	private static final byte[] ERROR_BYTE = "ERROR".getBytes();
	private static final byte[] FATAL_BYTE = "FATAL".getBytes();
	private static final byte[] OFF_BYTE = "OFF".getBytes();
	
	private Level(int level){
		this.level = level;
	}
	
	/**
	 * �α� ������ ���Ѵ�
	 * 
	 * @param level ���� �α� ����
	 * @return true �� ��� �α� ���
	 */
	public boolean lessOrEqual(int level){
		return this.level <= level;
	}
	
	/**
	 * �α� ������ ���Ѵ�
	 * 
	 * @param level ���� �α� ����
	 * @return true �� ��� �α� ���
	 */
	public boolean lessOrEqual(Level level){
		return this.level <= level.level;
	}
		
	/**
	 * int ���� �´� Level ��ü ����
	 * @param level		int ���� Level ��
	 * @return				int ���� ���� Level ��ü. �߸��� ���� ��� null
	 */
	public static final Level getLevel(int level){
		switch( level ){
		case 0 : return TRACE;
		case 1 : return DEBUG;
		case 2 : return INFO;
		case 3 : return WARN;
		case 4 : return ERROR;
		case 5 : return FATAL;
		case 6 : return OFF;
		default : return null;
		}
	}
	
	/**	�α� ��¿� �α׷�����		*/
	public byte[] getNameToBytes(){
		switch( level ){
		case 0 : return TRACE_BYTE;
		case 1 : return DEBUG_BYTE;
		case 2 : return INFO_BYTE;
		case 3 : return WARN_BYTE;
		case 4 : return ERROR_BYTE;
		case 5 : return FATAL_BYTE;
		case 6 : return OFF_BYTE;
		default : return new byte[]{};
		}
	}
}
