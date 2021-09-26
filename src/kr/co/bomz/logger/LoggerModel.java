package kr.co.bomz.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.co.bomz.logger.appender.AppenderType;

/**
 * �ΰ� ���� ���� �м� ����
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class LoggerModel {

	// �ΰ� ��
	private final String loggerName;
	// �ΰ� ����
	private List<Level> levelList = new ArrayList<Level>(3);
	// Appender Ÿ��
	private final AppenderType appenderType;
	// �Ķ����
	private Map<String, String> parameters;
	
	/** 
	 * @param loggerName		�ΰŸ�
	 * @param appenderType		Appender Ÿ��
	 */
	public LoggerModel(String loggerName, AppenderType appenderType){
		this.loggerName = loggerName;
		this.appenderType = appenderType;
	}

	/**
	 * �ΰ� �� ��û
	 * @return	�ΰŸ�
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * �ΰ� ���� ��� ��û
	 * @return		�ΰ� ���� ���
	 */
	public List<Level> getLevelList() {
		return levelList;
	}

	/**
	 * �ΰ� ���� ���
	 * @param level		�ΰ� ����
	 */
	public void addLevel(Level level) {
		if( level == null )		return;
		this.levelList.add(level);
	}

	/**
	 * �Ķ���� ��û
	 * @return		�Ķ���� ����. �Ķ���Ͱ� ���� ��� null
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}

	/**
	 * �Ķ���� ���
	 * @param key		�Ķ���͸�
	 * @param value	�Ķ���Ͱ�
	 */
	public void addParameter(String key, String value) {
		if( this.parameters == null ){
			this.parameters = new java.util.HashMap<String, String>(3);
		}
		
		this.parameters.put(key, value);
	}
	
	/**
	 * Ư�� �Ķ���� �� ��û
	 * @param key		�Ķ���͸�
	 * @return			�Ķ���Ͱ�
	 */
	public String getParameter(String key){
		if( this.parameters == null )		return null;
		return this.parameters.get(key);
	}

	/**
	 * Appender Ÿ�� ��û
	 * @return		Appender Ÿ��
	 */
	public AppenderType getAppenderType() {
		return appenderType;
	}
	
}
