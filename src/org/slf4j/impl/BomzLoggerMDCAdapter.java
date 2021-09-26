package org.slf4j.impl;

import java.util.Map;

import org.slf4j.spi.MDCAdapter;

/**
 * Bomz Logger MDC
 * 
 * @author Bomz
 * @since 1.0
 *
 */
public class BomzLoggerMDCAdapter implements MDCAdapter{

	@Override
	public void clear() {}

	@Override
	public String get(String key) {
		return null;
	}

	@Override
	public Map<String, String> getCopyOfContextMap() {
		return null;
	}

	@Override
	public void put(String key, String value) {}

	@Override
	public void remove(String key) {}

	@Override
	public void setContextMap(Map<String, String> value) {}

}
