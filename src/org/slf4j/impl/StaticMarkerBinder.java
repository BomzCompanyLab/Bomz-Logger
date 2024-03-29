package org.slf4j.impl;

import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.spi.MarkerFactoryBinder;

/**
 * slf4j ���� ����
 * 
 * @author Bomz
 * @since 1.0
 *
 */
public class StaticMarkerBinder implements MarkerFactoryBinder {

    public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinder();

    private final IMarkerFactory markerFactory = new BasicMarkerFactory();
    
    private StaticMarkerBinder() {}

    public static StaticMarkerBinder getSingleton() {
        return SINGLETON;
    }

    public IMarkerFactory getMarkerFactory() {
        return this.markerFactory;
    }

    public String getMarkerFactoryClassStr() {
        return BasicMarkerFactory.class.getName();
    }

}
