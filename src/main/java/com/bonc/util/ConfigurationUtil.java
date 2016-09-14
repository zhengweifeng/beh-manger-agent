package com.bonc.util;

public class ConfigurationUtil {

	public static final String fn = "agent-conf.properties";
	private static BaseConfigUtil base;
	static {
		base = new BaseConfigUtil();
		
	}
	public static String get(String key) {
		if(base.propMap == null) {
			init();
		}
		return base.propMap.get(key);
	}
	public static String get(String key,String defaultValue) {
		if(base.propMap == null) {
			init();
		}
		String value = base.propMap.get(key);
		if(value == null || value.equals("")) {
			return defaultValue;
		}
		return value;
	}
	public static void init(String path) {
		base.fileName = fn;
		base.confPath = path + "/" + fn;
		base.readPropBySort();
	}	
	public static void init() {
		base.fileName = fn;
		base.readPropBySort();
	}	
	
	public static void main(String[] args) {
		ConfigurationUtil.init("/");
		System.out.println(ConfigurationUtil.get("hbase.monitor.family"));
		
	}
}
