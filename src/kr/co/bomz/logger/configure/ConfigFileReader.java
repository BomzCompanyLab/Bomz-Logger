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
 * 로그 설정 파일 분석
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class ConfigFileReader {
	
	// 기본 로그 파일명
	public static final String DEFAULT_CONFIG_FILE = "/logger.xml";
	
	// 로그 파일
	private File configFile;
	
	// 설정 XML 에서 사용되는 Attribute 명칭
	private static final String ATT_NAME = "name";
	private static final String ATT_TYPE = "type";
	private static final String ATT_LEVEL = "level";
	private static final String ATT_LEVEL_STX = "(";
	private static final String ATT_LEVEL_ETX = ")";
	private static final String ATT_OBSERVE = "observe";
	private static final String ATT_OBSERVE_ON = "on";
	
	// 로그 파일 실시간 감시 사용 여부
	private boolean observe = false;
	
	public ConfigFileReader(){}
	
	/**
	 * 로그 파일 설정
	 * @param configFile 로거 설정 파일. null 일 경우 기본 경로의 기본 파일명 사용
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
	 * 로거 XML 파일 분석 후 리턴
	 * @return	로그 XML 분석 결과
	 */
	public List<LoggerModel> readConfigFile(){
		List<LoggerModel> modelList = new ArrayList<LoggerModel>();
		
		this.readConfigFile(this.configFile, modelList);
		
		return modelList;
	}
		
	/*		로그 파일 분석		*/
	private void readConfigFile(File configFile, List<LoggerModel> modelList){
		if( configFile == null || !configFile.isFile() )		return;
		
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setValidating(false);
			
			DocumentBuilder builder = dbFactory.newDocumentBuilder();
			Document doc = builder.parse(configFile);
			
			this.parseRootElement(doc.getDocumentElement(), modelList);
			
		}catch(IOException e){
			LoggerManager.getInstance().getRootLogger().debug(e, "로그 설정 파일 로드 중 오류. path=", configFile.getParent());
		}catch(Exception e){
			LoggerManager.getInstance().getRootLogger().debug(e, "로그 설정 파일 분석 오류. path=", configFile.getParent());
		}
	}
	
	/*		로그 파일 루트 엘리먼트 분석		*/
	private void parseRootElement(Element element, List<LoggerModel> modelList){
		NodeList nodeList = element.getElementsByTagName("logger");
		if( nodeList == null )		return;
		
		int length = nodeList.getLength();
		for(int i=0; i < length; i++){
			// logger element 분석 요청
			LoggerModel model = this.parseElement( (Element)nodeList.item(i) );
			if( model != null )		modelList.add(model);
		}
		
		// observe attribute check
		this.checkObserveAttribute( element.getAttribute(ATT_OBSERVE).trim() );
	}
		
	/*		로그 실시간 감시 사용 여부		*/
	private void checkObserveAttribute(String value){
		if( value.equals(ATT_OBSERVE_ON) )		this.observe = true;
		
	}
	
	/*		logger Element 분석		*/
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
	
	/*		logger element 의 param child element 검사		*/ 
	private void parseParamElement(NodeList nodeList, LoggerModel model) throws XMLParseException{
		if( nodeList == null )		return;
		int length = nodeList.getLength();
		Element element;
		String attValue, eleValue;
		for(int i=0; i < length; i++){
			element = (Element)nodeList.item(i);
			attValue = element.getAttribute("name").trim();
			eleValue = element.getTextContent().trim();
			if( attValue == null || attValue.equals("") )		throw new XMLParseException("param 엘리먼트의 name 속성값이 선언되지 않음");
			if( eleValue == null || eleValue.equals("") )		throw new XMLParseException("param 엘리먼트 값이 선언되지 않음");
			model.addParameter(attValue, eleValue);
		}
	}
	
	/*		엘리먼트의 attribute 값 리턴		*/
	private String checkAttributeValue(Element element, String attName) throws XMLParseException{
		String value = element.getAttribute(attName).trim();
		if( value == null || value.equals("") )		throw new XMLParseException(attName + " 엘리먼트 값이 선언되지 않음");
		return value;
	}
	
	/*		logger level 분석		*/
	private void parseLevel(String userLevel, LoggerModel model) throws Exception{
		
		if( userLevel.startsWith(ATT_LEVEL_STX) && userLevel.endsWith(ATT_LEVEL_ETX) ){
			// 지정 레벨 설정. 예> (DEBUG|WARN|FATAL)
			userLevel = userLevel.substring(1, userLevel.length()-1);
			String[] userLevels = userLevel.split("\\|");
			for(String lv : userLevels)		
				model.addLevel(Level.valueOf(lv.trim().toUpperCase()));
		}else{
			// 기본 레벨 설정. 예> DEBUG. 디버그 이상의 모든 레벨
			Level level = Level.valueOf(userLevel.toUpperCase());
			int last = Level.OFF.ordinal();
			for(int start = level.ordinal(); start < last; start++)
				model.addLevel( Level.getLevel(start) );
		}
	}
	
	/**
	 * 실시간 로그 감시 사용 여부
	 * @return		사용할 경우 true
	 */
	public boolean isObserve(){
		return this.observe;
	}
	
}
