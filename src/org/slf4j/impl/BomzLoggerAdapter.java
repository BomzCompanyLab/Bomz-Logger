package org.slf4j.impl;

import java.io.Serializable;

import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.spi.LocationAwareLogger;

import kr.co.bomz.logger.Level;
import kr.co.bomz.logger.Logger;

/**
 * 	BomzLogger 를 slf4j Logger 형식으로 변환
 * 
 * @author Bomz
 * @since 1.0
 *
 */
public class BomzLoggerAdapter extends MarkerIgnoringBase implements LocationAwareLogger, Serializable {

	private static final long serialVersionUID = -5899984408871340648L;
	
	/**		BomzLogger		*/
	private final Logger logger;
	
	BomzLoggerAdapter(Logger logger){
		this.logger = logger;
	}
	
	
	@Override
	public void trace(String msg) {
		if( !this.logger.isLoggable(Level.TRACE) )		return;
		
		this.logger.trace(msg);
	}

	@Override
	public void trace(String format, Object arg) {
		if( !this.logger.isLoggable(Level.TRACE) )		return;
			
		FormattingTuple ft = MessageFormatter.format(format, arg);
		this.logger.trace(ft.getThrowable(), ft.getMessage());
	}

	@Override
	public void trace(String format, Object... arg) {
		if( !this.logger.isLoggable(Level.TRACE) )		return;
		
		FormattingTuple ft = MessageFormatter.arrayFormat(format, arg);
		this.logger.trace(ft.getThrowable(), ft.getMessage());
	}

	@Override
	public void trace(String msg, Throwable error) {
		if( !this.logger.isLoggable(Level.TRACE) )		return;
				
		this.logger.trace(error, msg);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		if( !this.logger.isLoggable(Level.TRACE) )		return;
		
		 FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		 this.logger.trace(ft.getThrowable(), ft.getMessage());
	}

	
	
	
	@Override
	public void debug(String msg) {
		if( !this.logger.isLoggable(Level.DEBUG) )		return;
		
		this.logger.debug(msg);
	}

	@Override
	public void debug(String format, Object arg) {
		if( !this.logger.isLoggable(Level.DEBUG) )		return;
			
		FormattingTuple ft = MessageFormatter.format(format, arg);
		this.logger.debug(ft.getThrowable(), ft.getMessage());
	}

	@Override
	public void debug(String format, Object... arg) {
		if( !this.logger.isLoggable(Level.DEBUG) )		return;
		
		FormattingTuple ft = MessageFormatter.arrayFormat(format, arg);
		this.logger.debug(ft.getThrowable(), ft.getMessage());
	}

	@Override
	public void debug(String msg, Throwable error) {
		if( !this.logger.isLoggable(Level.DEBUG) )		return;
				
		this.logger.debug(error, msg);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		if( !this.logger.isLoggable(Level.DEBUG) )		return;
		
		 FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		 this.logger.debug(ft.getThrowable(), ft.getMessage());
	}
	
	
	
	@Override
	public void info(String msg) {
		if( !this.logger.isLoggable(Level.INFO) )		return;
		
		this.logger.info(msg);
	}

	@Override
	public void info(String format, Object arg) {
		if( !this.logger.isLoggable(Level.INFO) )		return;
			
		FormattingTuple ft = MessageFormatter.format(format, arg);
		this.logger.info(ft.getThrowable(), ft.getMessage());
	}

	@Override
	public void info(String format, Object... arg) {
		if( !this.logger.isLoggable(Level.INFO) )		return;
		
		FormattingTuple ft = MessageFormatter.arrayFormat(format, arg);
		this.logger.info(ft.getThrowable(), ft.getMessage());
	}

	@Override
	public void info(String msg, Throwable error) {
		if( !this.logger.isLoggable(Level.INFO) )		return;
				
		this.logger.info(error, msg);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		if( !this.logger.isLoggable(Level.INFO) )		return;
		
		 FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		 this.logger.info(ft.getThrowable(), ft.getMessage());
	}

	
	
	
	
	@Override
	public void warn(String msg) {
		if( !this.logger.isLoggable(Level.WARN) )		return;
		
		this.logger.warn(msg);
	}

	@Override
	public void warn(String format, Object arg) {
		if( !this.logger.isLoggable(Level.WARN) )		return;
			
		FormattingTuple ft = MessageFormatter.format(format, arg);
		this.logger.warn(ft.getThrowable(), ft.getMessage());
	}

	@Override
	public void warn(String format, Object... arg) {
		if( !this.logger.isLoggable(Level.WARN) )		return;
		
		FormattingTuple ft = MessageFormatter.arrayFormat(format, arg);
		this.logger.warn(ft.getThrowable(), ft.getMessage());
	}

	@Override
	public void warn(String msg, Throwable error) {
		if( !this.logger.isLoggable(Level.WARN) )		return;
				
		this.logger.warn(error, msg);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		if( !this.logger.isLoggable(Level.WARN) )		return;
		
		 FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		 this.logger.warn(ft.getThrowable(), ft.getMessage());
	}

	
	
	
	@Override
	public void error(String msg) {
		if( !this.logger.isLoggable(Level.ERROR) )		return;
		
		this.logger.error(msg);
	}

	@Override
	public void error(String format, Object arg) {
		if( !this.logger.isLoggable(Level.ERROR) )		return;
			
		FormattingTuple ft = MessageFormatter.format(format, arg);
		this.logger.error(ft.getThrowable(), ft.getMessage());
	}

	@Override
	public void error(String format, Object... arg) {
		if( !this.logger.isLoggable(Level.ERROR) )		return;
		
		FormattingTuple ft = MessageFormatter.arrayFormat(format, arg);
		this.logger.error(ft.getThrowable(), ft.getMessage());
	}

	@Override
	public void error(String msg, Throwable error) {
		if( !this.logger.isLoggable(Level.ERROR) )		return;
				
		this.logger.error(error, msg);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		if( !this.logger.isLoggable(Level.ERROR) )		return;
		
		 FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
		 this.logger.error(ft.getThrowable(), ft.getMessage());
	}

	
	
	
	@Override
	public void log(Marker marker, String callerFQCN, int level, String msg, Object[] argArray, Throwable t) {
		if( t == null )	this.logger.log(this.toBomzLoggerLevel(level), msg);
		else					this.logger.log(this.toBomzLoggerLevel(level), t, msg);
	}

	/**		slf4j 로거 레벨을 BomzLogger 레벨에 맞게 변환		*/
	private Level toBomzLoggerLevel(int level) {
        switch (level) {
        case LocationAwareLogger.TRACE_INT:		return Level.TRACE;
        case LocationAwareLogger.DEBUG_INT:		return Level.DEBUG;
        case LocationAwareLogger.INFO_INT:			return Level.INFO;
        case LocationAwareLogger.WARN_INT:		return Level.WARN;
        case LocationAwareLogger.ERROR_INT:		return Level.ERROR;
        default:		throw new IllegalStateException("Level number " + level + " is not recognized.");
        }
    }
	
	@Override
	public boolean isTraceEnabled() {
		return this.logger.isLoggable(Level.TRACE);
	}
	
	@Override
	public boolean isDebugEnabled() {
		return this.logger.isLoggable(Level.DEBUG);
	}

	@Override
	public boolean isInfoEnabled() {
		return this.logger.isLoggable(Level.INFO);
	}

	@Override
	public boolean isWarnEnabled() {
		return this.logger.isLoggable(Level.WARN);
	}
	
	@Override
	public boolean isErrorEnabled() {
		return this.logger.isLoggable(Level.ERROR);
	}

}
