package org.slf4j.impl;

import org.slf4j.spi.MDCAdapter;

/**
 * slf4j 연동 설정
 * 
 * @author Bomz
 * @since 1.0
 *
 */
public class StaticMDCBinder {

    public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

    private StaticMDCBinder() {}

    public static final StaticMDCBinder getSingleton() {
        return SINGLETON;
    }

    public MDCAdapter getMDCA() {
        return new BomzLoggerMDCAdapter();
    }

    public String getMDCAdapterClassStr() {
        return BomzLoggerMDCAdapter.class.getName();
    }
}
