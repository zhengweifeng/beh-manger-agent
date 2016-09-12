package com.bonc.util;

public class ConfigurationUtil extends BaseConfigUtil {

	public static final String fn = "agent-conf.properties";
	
	public static String get(String key) {
		if(propMap == null) {
			readPropBySort();
		}
		return propMap.get(key);
	}
	public static String get(String key,String defaultValue) {
		if(propMap == null) {
			readPropBySort();
		}
		String value = propMap.get(key);
		if(value == null || value.equals("")) {
			return defaultValue;
		}
		return value;
	}
	public static void init(String path) {
		fileName = fn;
		confPath = path + "/" + fn;
		readPropBySort();
	}	
	public static void init() {
		fileName = fn;
		readPropBySort();
	}	
}
