package org.slf4j.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * slf4j ������ ���� �ΰ� ������
 * @author Bomz
 * @since 1.0
 *
 */
public class BomzLoggerFactory implements ILoggerFactory{

	/**
	 * 	BomzLogger ��ü�� slf4j ������ �ΰŷ� �����Ͽ� ����
	 * 
	 * 	Key		:	logger name
	 * 	Value	:	BomzLoggerAdapter
	 */
	private final ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();
	
	public BomzLoggerFactory(){}
	
	@Override
	public Logger getLogger(String name) {
		if( this.loggerMap.containsKey(name) ){
			// ���� ��ȯ ó���� �ΰŰ� ���� ���
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
