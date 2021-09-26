package org.slf4j.impl;

import org.slf4j.ILoggerFactory;

/**
 * slf4j 연동 설정
 * 
 * @author Bomz
 * @since 1.0
 *
 */
public class StaticLoggerBinder {

    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    private final ILoggerFactory loggerFactory;
    
    public static final StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    public static String REQUESTED_API_VERSION = "1.6.99"; // !final

    private StaticLoggerBinder() {
    	 this.loggerFactory = new BomzLoggerFactory();
    }

    public ILoggerFactory getLoggerFactory() {
        return this.loggerFactory;
    }

    public String getLoggerFactoryClassStr() {
        return BomzLoggerFactory.class.getName();
    }
}
