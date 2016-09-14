package com.bonc.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bonc.entity.ConnectionEntity;
import com.bonc.entity.RuleEntity;

public class RuleUtil {

	private static HashMap<String, List<RuleEntity>> ruleMap;
	private static Map<String,ConnectionEntity> cMap;
	public static final String fn = "agent-rule.properties";
	private static BaseConfigUtil base;
	static {
		base = new BaseConfigUtil();
	}
	
	public static List<RuleEntity> getRuleMap(String key) {
		if(ruleMap == null) {
			init();
		}
		return ruleMap.get(key);
	}
	public static Map<String,ConnectionEntity> getConnMap() {
		if(cMap == null) {
			init();
		}
		return getConnMap();
	}
	public static void init(String path) {
		base.fileName = fn;
		base.confPath = path + "/" + fn;
		base.readPropBySort();
		createConnection();
		createRuleMap();
	}	

	public static void init() {
		base.fileName = fn;
		base.readPropBySort();
		createConnection();
		createRuleMap();
	}	
	private static void createConnection(){
		cMap = new HashMap<String, ConnectionEntity>();
		String[] ids = base.propMap.get("connection.id").split(",");
		for(String id : ids) {
			id = id.trim();
			ConnectionEntity conn = new ConnectionEntity();
			conn.setId(id);
			conn.setType(base.propMap.get("connection." + id + ".type"));
			conn.setUrl(base.propMap.get("connection." + id + ".url"));
			cMap.put(id, conn);
		}
	}
	
	private static void createRuleMap() {
		ruleMap = new HashMap<String, List<RuleEntity>>();
		String[] ids = base.propMap.get("rule.id").split(",");
		for(String id : ids) {
			id = id.trim();
			RuleEntity rule = new RuleEntity();
			rule.setId(id);
			String goal =  base.propMap.get("rule." + id + ".goal");
			rule.setType(base.propMap.get("rule." + id + ".type"));
			rule.setGoal(base.propMap.get("rule." + id + ".goal"));
			rule.setName(base.propMap.get("rule." + id + ".name"));
			rule.setScope( base.propMap.get("rule." + id + ".scope"));
			if(base.propMap.get("rule." + id + ".type").equals("number")) {
				rule.setSign(base.propMap.get("rule." + id + ".sign"));
				rule.setWarnLimit(base.propMap.get("rule." + id + ".warn.limit"));
				rule.setErrorLimit(base.propMap.get("rule." + id + ".error.limit"));
			}
			rule.setConn(cMap.get(base.propMap.get("rule." + id + ".connection.model")));
			//ruleMap.put(id, rule);
			if(ruleMap.get(goal) == null) {
				ruleMap.put(goal, new ArrayList<RuleEntity>());
			}
			ruleMap.get(goal).add(rule);
		}
	}

	
	public static void main(String[] args) {
		RuleUtil.init("/aa");
		for(RuleEntity en : RuleUtil.getRuleMap("disk")) {
			System.out.println(en );
		}
	}
	
}
