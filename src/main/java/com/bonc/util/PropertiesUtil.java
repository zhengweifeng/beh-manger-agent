package com.bonc.util;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 加载配置文件
 * @author zwf
 *
 */
public class PropertiesUtil {
	
	private static Log log = LogFactory.getLog(PropertiesUtil.class);
	private static PropertiesConfiguration conf;
	private static String configPath;
	private static String fileName = "agent-conf.properties";
	static{
		conf = init(fileName);
	}
	/**
	 * 
	 * @param cp
	 * @throws ConfigurationException 
	 */
	public static void setConfigPath(String cp) throws ConfigurationException {
		configPath = cp;
		conf = init(fileName);
	}
	
	private static PropertiesConfiguration init(String name) {
		PropertiesConfiguration c = new PropertiesConfiguration();
		try {
			if(configPath != null) {
				File configFile = new File(configPath + "/" + name);
				if(configFile.exists() && configFile.isFile()) {
					log.info("加载指定的配置文件:" + configFile.getPath());
					c.load(configFile);
				}
			} else {
				File file = new File(PropertiesUtil.class.getResource("/") + "/" + name);
				File file2 = new File( file.getParentFile().getParent() + "/" + name);
				if(file2.exists() && file2.isFile()) {
					log.info("加载包同目录配置:  " + file2.getPath());
					c.load(file2);
				} else {
					log.info("加载配置文件 : " + file.getPath());
					c.load(PropertiesUtil.class.getResourceAsStream("/" + name));						
				}
			}
			
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			log.error("init properties util expection:", e);
		}
		FileChangedReloadingStrategy strategy = new FileChangedReloadingStrategy();
		strategy.setRefreshDelay(5000);
		c.setReloadingStrategy(strategy);
		
		return c;
	}
	
	public static String get(String key){
		return conf.getString(key);
	}
	public static String get(String key,String defaultValue){
		String value = conf.getString(key);
		if(value == null) {
			return defaultValue;
		} 
		return value;
	}
	public static  PropertiesConfiguration getOtherConf(String fileName) {
		return init(fileName);
	}
	
	
	public static void main(String[] args) throws ConfigurationException {
		
		//PropertiesUtil.setConfigPath("/Users/zwf/BEH-Manager-Agent");
		//PropertiesConfiguration cf = PropertiesUtil.getOtherConf("agent-rule.properties");
		while(true) {
			System.out.println(PropertiesUtil.get("hadoop.home"));
			//System.out.println(cf.getStringArray("rule.id"));
			//System.out.println(cf.getProperty("rule.id"));
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
