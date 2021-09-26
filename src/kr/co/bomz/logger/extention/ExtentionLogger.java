package kr.co.bomz.logger.extention;

/**
 * �ܺ� ���� ���̺귯�� �α� ó�������� �������̽�
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see	MyBatisLog
 * @see	ApacheCommonsLog
 *
 */
public interface ExtentionLogger {

	/**
	 * Bomz logger �� ����� �� �ְ� ��� ��û�Ѵ�
	 * @throws Throwable		�ش� ���̺귯���� ����Ʈ���� �ʾ��� ��� �߻�
	 */
	void useLogger() throws Throwable;
}
