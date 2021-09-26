package kr.co.bomz.logger.extention;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import kr.co.bomz.logger.Logger;
import kr.co.bomz.logger.LoggerManager;
import kr.co.bomz.logger.configure.ConfigFileReader;


/**
 * 
 * 웹프로젝트에서 로그를 사용할 경우 web.xml 에 아래 서블릿을 추가하여 초기화 작업을 수행한다<p>
 * 생략 가능하지만 생략할 경우 사용자 로그를 제외한 시스템 및 기타 프레임워크의 로그가 적용되지 않을 수 있다<p>
 * 또한 &lt;load-on-startup&gt;1&lt;/load-on-startup&gt; 값은 1로하여 우선 초기화 시키며, 다른 서블릿은 2 이상의 값으로 한다<p>
 * <br>
 * 
 * 1. 기본 로그 파일 /logger.xml 을 이용할 경우<p>
 * <code>
 * &lt;servlet&gt;<p>
 * &nbsp;&nbsp;&lt;servlet-name&gt;BomzServlet&lt;/servlet-name&gt;<p>
 * &nbsp;&nbsp;&lt;servlet-class&gt;kr.co.bomz.logging.extention.BomzLogServlet&lt;/servlet-value&gt;<p>
 * &nbsp;&nbsp;&lt;load-on-startup&gt;1&lt;/load-on-startup&gt;<p>
 * &lt;/servlet&gt;<p>
 * </code>
 * <br>
 * 2. 다른 경로의 로그 파일을 이용할 경우<p>
 * <code>
 * &lt;servlet&gt;<p>
 * &nbsp;&nbsp;&lt;servlet-name&gt;BomzServlet&lt;/servlet-name&gt;<p>
 * &nbsp;&nbsp;&lt;servlet-class&gt;kr.co.bomz.logging.extention.BomzLogServlet&lt;/servlet-value&gt;<p>
 * &nbsp;&nbsp;&lt;init-param&gt;<p>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-name&gt;BomzLogConfigLocation&lt;/param-name&gt;<p>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;param-value&gt;/logger.xml&lt;/param-value&gt;<p>
 * &nbsp;&nbsp;&lt;/init-param&gt;<p>
 * &nbsp;&nbsp;&lt;load-on-startup&gt;1&lt;/load-on-startup&gt;<p>
 * &lt;/servlet&gt;<p>
 * </code>
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 *
 */
public class BomzLogServlet implements Servlet{

	private final String DEFAULT_LOG_FILE_PARAM = "BomzLogConfigLocation";

	@Override
	public void destroy() {
		
	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public String getServletInfo() {
		return "Bomz logger web servlet";
	}

	@Override
	public void init(ServletConfig sc) throws ServletException {
		String param = sc.getInitParameter(DEFAULT_LOG_FILE_PARAM);
		String configFile;
		if( param == null ){
			Logger.setLogConfigFile(ConfigFileReader.DEFAULT_CONFIG_FILE);
			configFile = ConfigFileReader.DEFAULT_CONFIG_FILE;
		}else{
			Logger.setLogConfigFile(param);
			configFile = param;
		}
		
		Logger logger = LoggerManager.getInstance().getRootLogger();
		logger.debug("Bomz logger listener call. config file:", configFile);
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {}


}
