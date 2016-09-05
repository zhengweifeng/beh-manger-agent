package com.bonc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationUtil {

	private static Map<String, String> propMap;
	private static String confPath ;
	
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
		confPath = path;
		readPropBySort();
	}
	
	public static void main(String[] args) {
		
		ConfigurationUtil.init("/aaa");
		System.out.println(ConfigurationUtil.class.getResource("/").getFile().toString());
		System.out.println(ConfigurationUtil.get("ssh.auth.userName"));
		
	}
	

	/**
	 * 顺序读取配置文件，顺序：
	 * 1.项目class同目录下
	 * 2.项目根目录下
	 * 3.命令行传递的参数
	 */
	private static void readPropBySort(){
		propMap =  new HashMap<String, String>();
		try {
			readPropFile(ConfigurationUtil.class.getResourceAsStream("/agent-conf.properties"));
			readPropFile(new File(System.getProperties().getProperty("user.dir") + "/agent-conf.properties"));
			if(confPath != null) {				
				readPropFile(new File(confPath));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 读取配置文件到 propMap中
	 * @param f
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void readPropFile(File f) throws FileNotFoundException, IOException {
		
		if(!f.exists() && !f.isFile()) {
			System.out.println("ERROR : the path specified  " + f.getAbsolutePath()  +" no exists or is not a file");
			return ;
		}
		Properties prop = new Properties();
		prop.load(new FileInputStream(f));
		for(Object key : prop.keySet()) {
			propMap.put(key.toString(), prop.getProperty(key.toString()));
		}
	}
	
	/**
	 * 读取配置文件到 propMap中
	 * @param f
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static void readPropFile(InputStream in) throws FileNotFoundException, IOException {
		
		Properties prop = new Properties();
		prop.load(in);
		for(Object key : prop.keySet()) {
			propMap.put(key.toString(), prop.getProperty(key.toString()));
		}
	}
	
}
