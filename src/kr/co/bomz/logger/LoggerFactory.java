package kr.co.bomz.logger;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import kr.co.bomz.logger.appender.Appender;
import kr.co.bomz.logger.appender.AppenderType;
import kr.co.bomz.logger.appender.ConsoleAppender;
import kr.co.bomz.logger.appender.CustomAppender;
import kr.co.bomz.logger.appender.FileAppender;
import kr.co.bomz.logger.appender.HTTPAppender;
import kr.co.bomz.logger.appender.TCPAppender;

/**
 * �߻�Ŭ������ ������ kr.co.bomz.logger.Logger ��ü�� ���� ��<p>
 * 
 * Appender �� �����ϴ� ��Ȱ ����
 * 
 * 
 * @author Bomz
 * @since 1.0
 * @version 1.0
 * @see		Logger
 *
 */
public class LoggerFactory {

	LoggerFactory(){}
	
	/**
	 * ���ο� �ΰ� ����
	 * @return		�ΰ� ��ü
	 */
	Logger createLogger(){
		return new LoggerImpl();
	}
	
	/**
	 * �ý��� �ΰ� ��ü ����
	 * @return		�ΰ� ��ü
	 */
	Logger createDefaultLogger(){
		LoggerImpl logger = new LoggerImpl();
		LoggerModel model = new LoggerModel(LoggerManager.ROOT_LOGGER_NAME, AppenderType.CONSOLE);
		
		int defaultLevel = Level.DEBUG.ordinal();
		int offLevel = Level.OFF.ordinal();
		for(;defaultLevel < offLevel; defaultLevel++)
			model.addLevel( Level.getLevel(defaultLevel) );
		
		this.addAppender(logger, model);
		return logger;
	}
	
	/**
	 * �ΰ��� Appender ���� �� ���� ó��
	 * @param logger		�ΰ� ��ü
	 */
	void closeAppender(Logger logger){
		if( !(logger instanceof LoggerImpl) )		return;
		
		LoggerImpl impl = (LoggerImpl)logger;
		Set<Appender> appenderSet = new HashSet<Appender>();
		int length = impl.appenders.length;
		int aLength;
		for( int i=0; i < length; i++){
			if( impl.appenders[i] == null )	continue;
			for(aLength=0; aLength < impl.appenders[i].length; aLength++)
				appenderSet.add(impl.appenders[i][aLength]);
		}
		
		java.util.Iterator<Appender> keys = appenderSet.iterator();
		while( keys.hasNext() )		keys.next().close();
		
		int off = Level.OFF.ordinal();
		for(int i=0; i < off; i++)		impl.appenders[i] = null;
	}
	
	/**
	 * �ΰſ� ���ο� Appender �߰�
	 * @param logger		�ΰ� ��ü
	 * @param model		�ΰ� ����
	 */
	void addAppender(Logger logger, LoggerModel model){
		if( !(logger instanceof LoggerImpl) )		return;
		
		LoggerImpl impl = (LoggerImpl)logger;
		impl.loggerModel = model;
		
		java.util.List<Level> levelList = model.getLevelList();
		int size = levelList.size();
		Appender appender = this.createAppender(model);
		for(int i=0; i < size; i++)
			impl.addAppender(levelList.get(i),  appender);
	}
	
	/**
	 * Ÿ�Կ� �´� ���ο� Appender ����
	 * @param model		�ΰ� ����
	 * @return				Appender
	 */
	private Appender createAppender(LoggerModel model){
		Appender appender = this.createAppender(model.getAppenderType(), model);
		if( appender == null )		return null;
		
		// ��¥ ���� ����
		appender.setDatePattern(model.getParameter("date"), model.getParameter("locale"), model.getParameter("timeZone"));
		
		java.util.Map<String, String> paramMap = model.getParameters();
		if( paramMap != null )		appender.setParameter(paramMap);
		
		return appender;
	}
	
	/**
	 * Ÿ�Կ� �´� ���ο� Appender ����
	 * @param type		Appender Ÿ��
	 * @param model		�ΰ� ����
	 * @return				Appender
	 */
	private Appender createAppender(AppenderType type, LoggerModel model){
		if( type == null )		return  null;
		
		switch( type ){
		case CONSOLE:	return new ConsoleAppender();
		case FILE:				return new FileAppender();
		case TCP:				return new TCPAppender();
		case HTTP:			return new HTTPAppender();
		case CUSTOM:		return this.createCustomAppender(model);
		default:				return null;
		}
	}
	
	/**
	 * ����ڰ� ������ �ΰſ� Appender ����
	 * @param model		�ΰ� ����
	 * @return				�ΰ� ��ü
	 * @see		kr.co.bomz.logger.appender.CustomAppender
	 */
	private CustomAppender createCustomAppender(LoggerModel model){
		String className = model.getParameter(CustomAppender.CLASS);
		if( className == null )	{
			LoggerManager.getInstance().getRootLogger().debug("Custom Logger ��� �� class �Ӽ����� �ʿ��մϴ�. \n\t��) <param name=\"class\">packageName.className</param>");
			return null;
		}
		
		try{
			@SuppressWarnings("unchecked")
			Class<CustomAppender> clazz = (Class<CustomAppender>)Class.forName(className);
			return clazz.newInstance();
		}catch(Exception e){
			LoggerManager.getInstance().getRootLogger().debug(e, "Custom Logger ���� ����");	
			return null;
		}
	}
		
	/**
	 * �⺻���� �ΰ� ����ü
	 * @author Bomz
	 * @since 1.0
	 * @version 1.0
	 *
	 */
	class LoggerImpl extends Logger{
				
		private final Appender[][] appenders = new Appender[Level.FATAL.ordinal() + 1][];
		
		private LoggerModel loggerModel;
		
		public LoggerModel getLoggerModel(){
			return this.loggerModel;
		}
		
		private void addAppender(Level level, Appender appender){
			if( level == Level.OFF )		return;		// OFF �� ����� �ʿ䰡 ����
			if( appender == null )		return;
			
			this.addAppender(level.ordinal(), appender);
			
		}
		
		private void addAppender(int ordinal, Appender appender){
			Appender[] appenders = this.appenders[ordinal];
			if( appenders == null ){
				appenders = new Appender[]{appender};
			}else{
				int length = appenders.length;
				appenders = java.util.Arrays.copyOf(appenders, length + 1);
				appenders[length] = appender;
			}
			
			this.appenders[ordinal] = appenders; 
		}
		
		@Override
		public void log(Level level, Throwable err, Object ... msg){
			if( level == null )			return;
			Appender[] appenders = this.appenders[level.ordinal()];
			if( appenders == null )	return;
			
			StringBuilder dateBuffer = null;
			StringBuilder msgBuffer = super.getMessageValue(msg);
			StringBuilder callStackBuffer = super.getCallStackTraceValue( Thread.currentThread().getStackTrace() );
			StringBuilder errBuffer = super.getThrowableValue(err);
			
			Calendar date = Calendar.getInstance();
			
			int length = appenders.length;
			for(int index=0; index < length; index++){
				if( appenders[index].dateFormat == null ){
					// ������ ��¥ ���� ������ �������� �ʾ��� ��� �⺻ �������� ���
					if( dateBuffer == null )		dateBuffer = super.dateFormat.getDefaultDate(date);
					appenders[index].write(level, dateBuffer, callStackBuffer, msgBuffer, errBuffer);
				}else{
					// ������ ��¥ ���� ������ �������� ���
					appenders[index].write(level, appenders[index].dateFormat.getDate(date), callStackBuffer, msgBuffer, errBuffer);
				}
				
			}
		}

		@Override
		public boolean isLoggable(Level level) {
			Appender[] appender = appenders[level.ordinal()];
			if( appender == null )		return false;
			
			return appender.length > 0;
		}
	}
}
