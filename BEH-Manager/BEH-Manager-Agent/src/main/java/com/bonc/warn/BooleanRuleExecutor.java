package com.bonc.warn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BooleanRuleExecutor implements IRuleExecutor {

	private Log log = LogFactory.getLog(BooleanRuleExecutor.class);
	@Override
	public JSONObject execute(JSONObject json, Regulation rule) {
		//String determineString = getShellString(rule);
		
		JSONArray array = json.getJSONArray("data");
		JSONArray resultArray = new JSONArray();
		for(int i=0 ; i<array.size(); i++) {
			try{
				JSONObject data = array.getJSONObject(i);
				Binding bind = new Binding();
				for(Object key : data.keySet()) {
					bind.setVariable(key.toString(), data.getString(key.toString()));
				}
				GroovyShell shell = new GroovyShell(bind);
				boolean num = (boolean)shell.evaluate(rule.getCount());
				if(num == Boolean.parseBoolean(rule.getSign())) {
					JSONObject rs1 = new JSONObject();
					rs1.put("type", "warn");
					rs1.put("limit", rule.getSign());
					rs1.put("count", rule.getCount());
					rs1.put("data", data);
					rs1.put("num", num);
					resultArray.add(rs1);
				} else {
					continue;
				}
			} catch(Exception e){
				log.warn("规则执行错误 : ",e);
			}
		}
		
		if(resultArray.size() == 0) {
			return null;
		} else {
			json.put("data", resultArray);
			json.put("rule", rule);
			return json;
		}
	}
	
}
