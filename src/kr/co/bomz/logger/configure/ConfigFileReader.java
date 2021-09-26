package kr.co.bomz.logger.configure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import kr.co.bomz.logger.Level;
import kr.co.bomz.logger.LoggerManager;
import kr.co.bomz.logger.LoggerModel;
import kr.co.bomz.logger.appender.AppenderType;

/**
 * 
 * �α� ���� ���� �м�
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class ConfigFileReader {
	
	// �⺻ �α� ���ϸ�
	public static final String DEFAULT_CONFIG_FILE = "/logger.xml";
	
	// �α� ����
	private File configFile;
	
	// ���� XML ���� ���Ǵ� Attribute ��Ī
	private static final String ATT_NAME = "name";
	private static final String ATT_TYPE = "type";
	private static final String ATT_LEVEL = "level";
	private static final String ATT_LEVEL_STX = "(";
	private static final String ATT_LEVEL_ETX = ")";
	private static final String ATT_OBSERVE = "observe";
	private static final String ATT_OBSERVE_ON = "on";
	
	// �α� ���� �ǽð� ���� ��� ����
	private boolean observe = false;
	
	public ConfigFileReader(){}
	
	/**
	 * �α� ���� ����
	 * @param configFile �ΰ� ���� ����. null �� ��� �⺻ ����� �⺻ ���ϸ� ���
	 */
	public File setConfigFile(File configFile){
		if( configFile == null ){
			try {
				this.configFile = new File(this.getClass().getResource(DEFAULT_CONFIG_FILE).toURI());
			} catch (Exception e) {
				return null;
			}
		}else{
			this.configFile = configFile;
		}
		
		return this.configFile;
	}
	
	/**
	 * �ΰ� XML ���� �м� �� ����
	 * @return	�α� XML �м� ���
	 */
	public List<LoggerModel> readConfigFile(){
		List<LoggerModel> modelList = new ArrayList<LoggerModel>();
		
		this.readConfigFile(this.configFile, modelList);
		
		return modelList;
	}
		
	/*		�α� ���� �м�		*/
	private void readConfigFile(File configFile, List<LoggerModel> modelList){
		if( configFile == null || !configFile.isFile() )		return;
		
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setValidating(false);
			
			DocumentBuilder builder = dbFactory.newDocumentBuilder();
			Document doc = builder.parse(configFile);
			
			this.parseRootElement(doc.getDocumentElement(), modelList);
			
		}catch(IOException e){
			LoggerManager.getInstance().getRootLogger().debug(e, "�α� ���� ���� �ε� �� ����. path=", configFile.getParent());
		}catch(Exception e){
			LoggerManager.getInstance().getRootLogger().debug(e, "�α� ���� ���� �м� ����. path=", configFile.getParent());
		}
	}
	
	/*		�α� ���� ��Ʈ ������Ʈ �м�		*/
	private void parseRootElement(Element element, List<LoggerModel> modelList){
		NodeList nodeList = element.getElementsByTagName("logger");
		if( nodeList == null )		return;
		
		int length = nodeList.getLength();
		for(int i=0; i < length; i++){
			// logger element �м� ��û
			LoggerModel model = this.parseElement( (Element)nodeList.item(i) );
			if( model != null )		modelList.add(model);
		}
		
		// observe attribute check
		this.checkObserveAttribute( element.getAttribute(ATT_OBSERVE).trim() );
	}
		
	/*		�α� �ǽð� ���� ��� ����		*/
	private void checkObserveAttribute(String value){
		if( value.equals(ATT_OBSERVE_ON) )		this.observe = true;
		
	}
	
	/*		logger Element �м�		*/
	private LoggerModel parseElement(Element element){
		try{
			String name = this.checkAttributeValue(element, ATT_NAME);
			String type = this.checkAttributeValue(element, ATT_TYPE);
			AppenderType appenderType = AppenderType.valueOf(type.toUpperCase());
			
			LoggerModel model = new LoggerModel(name, appenderType);
			
			this.parseLevel( this.checkAttributeValue(element, ATT_LEVEL), model);
			this.parseParamElement( element.getElementsByTagName("param"), model );
			
			return model;
		}catch(Exception e){
			LoggerManager.getInstance().getRootLogger().debug(e);
			return null;
		}
	}
	
	/*		logger element �� param child element �˻�		*/ 
	private void parseParamElement(NodeList nodeList, LoggerModel model) throws XMLParseException{
		if( nodeList == null )		return;
		int length = nodeList.getLength();
		Element element;
		String attValue, eleValue;
		for(int i=0; i < length; i++){
			element = (Element)nodeList.item(i);
			attValue = element.getAttribute("name").trim();
			eleValue = element.getTextContent().trim();
			if( attValue == null || attValue.equals("") )		throw new XMLParseException("param ������Ʈ�� name �Ӽ����� ������� ����");
			if( eleValue == null || eleValue.equals("") )		throw new XMLParseException("param ������Ʈ ���� ������� ����");
			model.addParameter(attValue, eleValue);
		}
	}
	
	/*		������Ʈ�� attribute �� ����		*/
	private String checkAttributeValue(Element element, String attName) throws XMLParseException{
		String value = element.getAttribute(attName).trim();
		if( value == null || value.equals("") )		throw new XMLParseException(attName + " ������Ʈ ���� ������� ����");
		return value;
	}
	
	/*		logger level �м�		*/
	private void parseLevel(String userLevel, LoggerModel model) throws Exception{
		
		if( userLevel.startsWith(ATT_LEVEL_STX) && userLevel.endsWith(ATT_LEVEL_ETX) ){
			// ���� ���� ����. ��> (DEBUG|WARN|FATAL)
			userLevel = userLevel.substring(1, userLevel.length()-1);
			String[] userLevels = userLevel.split("\\|");
			for(String lv : userLevels)		
				model.addLevel(Level.valueOf(lv.trim().toUpperCase()));
		}else{
			// �⺻ ���� ����. ��> DEBUG. ����� �̻��� ��� ����
			Level level = Level.valueOf(userLevel.toUpperCase());
			int last = Level.OFF.ordinal();
			for(int start = level.ordinal(); start < last; start++)
				model.addLevel( Level.getLevel(start) );
		}
	}
	
	/**
	 * �ǽð� �α� ���� ��� ����
	 * @return		����� ��� true
	 */
	public boolean isObserve(){
		return this.observe;
	}
	
}
