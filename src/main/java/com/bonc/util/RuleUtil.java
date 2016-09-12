package com.bonc.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bonc.entity.ConnectionEntity;
import com.bonc.entity.RuleEntity;

public class RuleUtil extends BaseConfigUtil {

	private static HashMap<String, List<RuleEntity>> ruleMap;
	private static Map<String,ConnectionEntity> cMap;
	public static final String fn = "agent-rule.properties";
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
		fileName = fn;
		confPath = path + "/" + fn;
		readPropBySort();
		createConnection();
		createRuleMap();
	}	

	public static void init() {
		fileName = fn;
		readPropBySort();
		createConnection();
		createRuleMap();
	}	
	private static void createConnection(){
		cMap = new HashMap<String, ConnectionEntity>();
		String[] ids = propMap.get("connection.id").split(",");
		for(String id : ids) {
			id = id.trim();
			ConnectionEntity conn = new ConnectionEntity();
			conn.setId(id);
			conn.setType(propMap.get("connection." + id + ".type"));
			conn.setUrl(propMap.get("connection." + id + ".url"));
			cMap.put(id, conn);
		}
	}
	
	private static void createRuleMap() {
		ruleMap = new HashMap<String, List<RuleEntity>>();
		String[] ids = propMap.get("rule.id").split(",");
		for(String id : ids) {
			id = id.trim();
			RuleEntity rule = new RuleEntity();
			rule.setId(id);
			String scope = propMap.get("rule." + id + ".scope");
			rule.setType(propMap.get("rule." + id + ".type"));
			rule.setGoal(propMap.get("rule." + id + ".goal"));
			rule.setName(propMap.get("rule." + id + ".name"));
			rule.setScope(scope);
			if(propMap.get("rule." + id + ".type").equals("number")) {
				rule.setSign(propMap.get("rule." + id + ".sign"));
				rule.setWarnLimit(propMap.get("rule." + id + ".warn.limit"));
				rule.setErrorLimit(propMap.get("rule." + id + ".error.limit"));
			}
			rule.setConn(cMap.get(propMap.get("rule." + id + ".connection.model")));
			//ruleMap.put(id, rule);
			if(ruleMap.get(scope) == null) {
				ruleMap.put(scope, new ArrayList<RuleEntity>());
			}
			ruleMap.get(scope).add(rule);
		}
	}

	
	public static void main(String[] args) {
		RuleUtil.init("/aa");
		for(RuleEntity en : RuleUtil.getRuleMap("hadoop089")) {
			System.out.println(en );
		}
	}
	
}
