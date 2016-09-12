package com.bonc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public abstract class BaseConfigUtil {
	protected static Map<String, String> propMap;
	protected static String confPath ;
	protected static String fileName;

	public static void reload() {
		readPropBySort();
	}
	/**
	 * 顺序读取配置文件，顺序：
	 * 1.项目class同目录下
	 * 2.项目根目录下
	 * 3.命令行传递的参数
	 */
	protected static void readPropBySort(){

		propMap =  new HashMap<String, String>();
		try {
			readPropFile(ConfigurationUtil.class.getResourceAsStream("/" + fileName));
			readPropFile(new File(System.getProperties().getProperty("user.dir") + "/" + fileName));
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
		System.out.println("read file : " + f.getPath());
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
