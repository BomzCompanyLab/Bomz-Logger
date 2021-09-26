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
 * ��������Ʈ���� �α׸� ����� ��� web.xml �� �Ʒ� ������ �߰��Ͽ� �ʱ�ȭ �۾��� �����Ѵ�<p>
 * ���� ���������� ������ ��� ����� �α׸� ������ �ý��� �� ��Ÿ �����ӿ�ũ�� �αװ� ������� ���� �� �ִ�<p>
 * ���� &lt;load-on-startup&gt;1&lt;/load-on-startup&gt; ���� 1���Ͽ� �켱 �ʱ�ȭ ��Ű��, �ٸ� ������ 2 �̻��� ������ �Ѵ�<p>
 * <br>
 * 
 * 1. �⺻ �α� ���� /logger.xml �� �̿��� ���<p>
 * <code>
 * &lt;servlet&gt;<p>
 * &nbsp;&nbsp;&lt;servlet-name&gt;BomzServlet&lt;/servlet-name&gt;<p>
 * &nbsp;&nbsp;&lt;servlet-class&gt;kr.co.bomz.logging.extention.BomzLogServlet&lt;/servlet-value&gt;<p>
 * &nbsp;&nbsp;&lt;load-on-startup&gt;1&lt;/load-on-startup&gt;<p>
 * &lt;/servlet&gt;<p>
 * </code>
 * <br>
 * 2. �ٸ� ����� �α� ������ �̿��� ���<p>
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
