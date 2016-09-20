package com.bonc.warn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class NumberRuleExecutor implements IRuleExecutor{

	private Log log = LogFactory.getLog(BooleanRuleExecutor.class);
	@Override
	public JSONObject execute(JSONObject json, Regulation rule) {
		String determineString = getShellString(rule);
		
		JSONArray array = json.getJSONArray("data");
		JSONArray resultArray = new JSONArray();
		for(int i=0 ; i<array.size(); i++) {
			try{
				JSONObject data = array.getJSONObject(i);
				Binding bind = new Binding();
				for(Object key : data.keySet()) {
					try {
						double value = Double.parseDouble(data.getString(key.toString()));
						bind.setVariable(key.toString(), value);
						log.debug("bind key : " + key + " -- " + value);
					} catch (Exception e) {
						continue;
					}
				}
				GroovyShell shell = new GroovyShell(bind);
				log.debug("execute : " + rule.getCount() + " \n " + data);
				Double num = (Double)shell.evaluate(rule.getCount());
				bind.setVariable("num", num);
				bind.setVariable("errorlimit", rule.getErrorLimit());
				bind.setVariable("warnlimit", rule.getWarnLimit());
				int result = (Integer)shell.evaluate(determineString);
				if(result == 0) {
					continue;
				} else if(result == 1 || result == 2) {
					JSONObject rs1 = new JSONObject();
					if(result == 1) {
						rs1.put("type", "warn");						
						rs1.put("limit", rule.getWarnLimit());
					} else {
						rs1.put("type", "error");												
						rs1.put("limit", rule.getErrorLimit());
					}
					rs1.put("count", rule.getCount());
					rs1.put("data", data);
					rs1.put("num", num);
					resultArray.add(rs1);
				}  else {
					log.warn("不识别规则执行结果 : " + result);
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
	
	
	private String  getShellString(Regulation rule) {
	
			switch (rule.getSign()) {
			case "GE":
				return " if( num >= errorlimit) { return 2 } "
						+ " else if( num < errorlimit && num >= warnlimit)  { return 1 } "
						+ " else { return 0 }";
			case "GT":
				return " if( num > errorlimit) { return 2 } "
						+ " else if( num =< errorlimit && num > warnlimit)  { return 1 } "
						+ " else { return 0 }";
			case "LE":
				return " if( num <= errorlimit) { return 2 } "
						+ " else if( num > errorlimit && num =< warnlimit)  { return 1 } "
						+ " else { return 0 }";
			case "LT":
				return " if( num < errorlimit) { return 2 } "
						+ " else if( num >= errorlimit && num < warnlimit)  { return 1 } "
						+ " else { return 0 }";
			case "EQ":
				return " if( num == errorlimit) { return 2 } "
						+ " else if( num == warnlimit)  { return 1 } "
						+ " else { return 0 }";
			default:
				log.warn("无法识别判断规则中的sign符号[GT(>),GE(>=),LE(<=),LT(<),EQ(=)]");
				return null;
			}
	}
}
