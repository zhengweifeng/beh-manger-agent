package com.bonc.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.event.ConfigurationEvent;
import org.apache.commons.configuration.event.ConfigurationListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.transmit.Transmission;
import com.bonc.warn.Regulation;

public class WarnFactory {

	private static Log log = LogFactory.getLog(WarnFactory.class);
	
	private static PropertiesConfiguration conf;
	private static String  fileName= "agent-rule.properties";
	private static Map<String ,Map<String,Regulation>> ruleMap;
	private static Map<String, Transmission> transmitMap;
	static{
		init();
	};
	public static void  init() {
		conf = PropertiesUtil.getOtherConf(fileName);
		log.info("读取agent-rule配置文件完成");
		//监听conf对象
		conf.addConfigurationListener(new ConfigurationListener() {
			
			@Override
			public void configurationChanged(ConfigurationEvent event) {
				log.info("监听到agent-rule配置文件已经修改 " + event.getPropertyValue() + " : " + event.getPropertyName());
				createConnection();
				createRuleMap();
			}
		});
		createConnection();
		createRuleMap();
	}
	/**
	 * 获取本机对应对象的告警规则
	 * @param key
	 * @return
	 */
	public static Map<String,Regulation> getRule(String key){
		return ruleMap.get(key);
	}
	public static Map<String, Transmission> getTransmitMap() {
		return transmitMap;
	}
	private static void createConnection(){
		transmitMap = new HashMap<String, Transmission>();
		List<Object> ids = conf.getList("connection.id");
		for(Object id : ids) {
			String idstr = id.toString().trim();
			Transmission tran = new Transmission();
			tran.setId(idstr);
			tran.setType(conf.getString("connection." + idstr + ".type"));
			tran.setInterval(conf.getLong("connection." + idstr + ".interval",120000L));
			Iterator<String> it = conf.getKeys("connection." + idstr);
			Map<String, String> otherMap = new HashMap<>();
			while(it.hasNext()) {
				String key = it.next();
				otherMap.put(key, conf.getString(key));
			}
			tran.setOtherMap(otherMap);
			transmitMap.put(idstr, tran);
		}
	}
	/**
	 * 初始化规则列表 反回 <goal : rule>
	 * 根据规则作用的对象返回,如果配置了多个规则,应用的等级:
	 * hostname + id大小进行比较<只初始化本机的规则,因此只会比较本机或对整体的规则>
	 * GLOBAL 等级最低
	 */
	private static void createRuleMap() {
		ruleMap = new HashMap<String ,Map<String,Regulation>>();
		List<Object> ids = conf.getList("rule.id");
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			log.error("解析配置文件是 获取主机名错误",e);
		}
		for(Object id : ids) {
			//解析配置文件,装配到regulation中
			String idStr = id.toString().trim();
			Regulation rule = new Regulation();
			rule.setId(Integer.parseInt(idStr));
			String goal =  conf.getString("rule." + idStr + ".goal");
			String name = conf.getString("rule." + idStr + ".name");
			String scope = conf.getString("rule." + idStr + ".scope");
			
			rule.setType(conf.getString("rule." + idStr + ".type"));
			rule.setGoal(goal);
			rule.setCount(conf.getString("rule." + idStr + ".count"));
			rule.setScope(scope);
			rule.setName(name);
			if(conf.getString("rule." + idStr + ".type").equals("number")) {
				rule.setSign(conf.getString("rule." + idStr + ".sign"));
				rule.setWarnLimit(conf.getFloat("rule." + idStr + ".warn.limit"));
				rule.setErrorLimit(conf.getFloat("rule." + idStr + ".error.limit"));
			}
			List<Object> tmids = conf.getList("rule." + idStr + ".connection.model");
			List<String> tList = new ArrayList<>();
			for(Object tid : tmids) {
				tList.add(tid.toString());
			}
			rule.setTransmitList(tList);
			log.info("解析到rule : " + rule);
			//分析配置文件,装在到map中
			Map<String,Regulation> regul = ruleMap.get(goal);
			if(regul == null) {
				Map<String,Regulation> map = new HashMap<>();
				map.put(name, rule);
				ruleMap.put(goal, map);
			} else if(rule.getScope().equals(hostName) ||rule.getScope().equals("GLOBAL")) {
				if(regul.size() == 0 ) {
					regul.put(name, rule);
					continue;
				}
				Regulation rtmp = regul.get(name);
				if(rtmp == null) {
					regul.put(name, rule);
					continue;
				} 
				if(rule.getScope().equals(hostName)&&rtmp.getScope().equals("GLOBAL")) {
					regul.put(name, rule);
				} else if(rule.getScope().equals(hostName)&&rtmp.getScope().equals(hostName)) {
					if(rule.getId() > rtmp.getId()) {
						regul.put(name, rule);
					}
				} else if(rule.getScope().equals("GLOBAL")&&rtmp.getScope().equals("GLOBAL")) {
					if(rule.getId() > rtmp.getId()) {
						regul.put(name, rule);
					}
				}
			}
		}
	}
	public static void main(String[] args) throws Exception {
		
		while(true) {
			Map<String, Regulation> map = WarnFactory.getRule("io");
			for(String key : map.keySet()) {
				System.out.println(key + " : " + map.get(key));
			}
			System.out.println("-------------------------");
			Thread.sleep(2000);
		}
	}
	
}
