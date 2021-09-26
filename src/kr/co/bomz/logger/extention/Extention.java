package kr.co.bomz.logger.extention;

/**
 * �ܺ� �����ӿ�ũ �α� ���� ���
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	MyBatisLog
 * @see	ApacheCommonsLog
 */
public class Extention {

	// ����� �� �ִ� �ܺ� ���� �α� ���
	private static final String[] list = new String[]{
			"kr.co.bomz.logger.extention.MyBatisLog",					// MyBatis
			"kr.co.bomz.logger.extention.ApacheCommonsLog"	// Apache Commons Log
	};
	
	/**
	 * �ܺ� ���� �α׸� ����� �� �ְԲ� ��� ó��
	 */
	public static final void checkExtention(){
		for(String el : list){
			try{
				((ExtentionLogger)Class.forName(el).newInstance()).useLogger();
			}catch(Throwable e){
				// �ش� ���̺귯���� �߰����� �ʾҴٸ� �߻��Ѵ�
			}
		}
	}
	
}
