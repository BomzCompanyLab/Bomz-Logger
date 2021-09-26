package org.slf4j.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * slf4j 연동을 위한 로거 생성용
 * @author Bomz
 * @since 1.0
 *
 */
public class BomzLoggerFactory implements ILoggerFactory{

	/**
	 * 	BomzLogger 객체를 slf4j 형태의 로거로 저장하여 관리
	 * 
	 * 	Key		:	logger name
	 * 	Value	:	BomzLoggerAdapter
	 */
	private final ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();
	
	public BomzLoggerFactory(){}
	
	@Override
	public Logger getLogger(String name) {
		if( this.loggerMap.containsKey(name) ){
			// 기존 변환 처리된 로거가 있을 경우
			return this.loggerMap.get(name);
			
		}else{
			kr.co.bomz.logger.Logger bomzLogger = (name.equalsIgnoreCase(Logger.ROOT_LOGGER_NAME)) ?
						kr.co.bomz.logger.Logger.getLogger(kr.co.bomz.logger.LoggerManager.ROOT_LOGGER_NAME) :
						kr.co.bomz.logger.Logger.getLogger(name);
			
			Logger slf4jLogger = new BomzLoggerAdapter(bomzLogger);
			this.loggerMap.put(name, slf4jLogger);
			
			return slf4jLogger;
		}
	}

}
