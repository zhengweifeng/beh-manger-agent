package com.bonc.warn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import com.bonc.entity.RuleEntity;
import com.bonc.entity.WarnEntity;
import com.bonc.export.ExportThread;
import com.bonc.rule.IRule;

public class RuleExecutor {
	
	private static ArrayBlockingQueue<WarnEntity> messageQueue = new ArrayBlockingQueue<>(1000);
	static {
		ExportThread thread = new ExportThread(messageQueue);
		thread.setDaemon(true);
		thread.start();
	}
	/**
	 * 执行规则
	 * 
	 * @param rule
	 * @param rlist
	 */
	public void execute(IRule rule, Object obj, List<RuleEntity> rlist, String rule_name) {

		// 执行规则判断,产生告警数据
		WarnEntity warn = filterRule(rule.execute(obj), rlist, rule_name, rule);
		System.out.println(warn);
		try {
			if(warn != null) {				
				messageQueue.put(warn);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 发送告警数据
	}

	/**
	 * 执行规则
	 * 
	 * @param rule
	 * @param rlist
	 */
	public void execute(IRule rule, Object obj, List<RuleEntity> rlist, String rule_name, String errorMessage,
			String warnMessage) {

		// 执行规则判断,产生告警数据
		WarnEntity warn = filterRule(rule.execute(obj), rlist, rule_name, rule);
		if (warn == null) {
			return;
		}
		if (warn.getLevel().equals("WARN")) {
			warn.setReason(warnMessage);
		} else {
			warn.setReason(errorMessage);
		}
		System.out.println(warn);
		try {
			messageQueue.put(warn);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 发送告警数据
	}

	/**
	 * 规则发送
	 */
	public void send() {

	}

	private WarnEntity filterRule(float number, List<RuleEntity> rList, String rule_name, IRule rule) {
		int rs = 0;
		if (rList == null) {
			return null;
		}
		Map<String, RuleEntity> ruleMap = new HashMap<>();

		for (RuleEntity r : rList) {
			if (ruleMap.get(r.getName()) == null) {
				ruleMap.put(r.getName(), r);
			} else {
				RuleEntity r1 = ruleMap.get(r.getName());
				if (r1.getScope().equals("GLOBAL") && r.getScope().equals("GLOBAL")) {
					if (Integer.parseInt(r1.getId()) < Integer.parseInt(r.getId())) {
						ruleMap.put(r.getName(), r);
					}
				} else {
					ruleMap.put(r.getName(), r);
				}
			}
		}
		RuleEntity ex = ruleMap.get(rule_name);

		if (ex != null) {
			if (ex.getType().equals("number")) {
				rs = numberRuleExecute(number, ex);
			} else {
				// 执行boolean类型的判断
			}
		}
		// 对结果进行封装 warnentity对象
		return createResult(rs, rule_name, number, ex);
	}

	/**
	 * 封装结果对象
	 * 
	 * @param rs
	 * @param rule_name
	 * @param number
	 * @param rule
	 * @return
	 */
	public WarnEntity createResult(int rs, String rule_name, float number, RuleEntity rule) {
		// TODO Auto-generated method stub
		if (rs != 0) {
			WarnEntity warn = new WarnEntity();
			warn.setRule(rule);
			warn.setNumber(number);
			warn.setName(rule_name);
			if (rs == 2) {
				warn.setLevel("ERROR");
				warn.setReason(rule.getGoal() + " used " + number + " ,but error limit is " + rule.getErrorLimit());
			} else {
				warn.setLevel("WARN");
				warn.setReason(rule.getGoal() + " used " + number + " ,but error limit is " + rule.getErrorLimit()
						+ " , warn limit is " + rule.getWarnLimit());
			}
			return warn;
		} else {
			return null;
		}
	}

	public WarnEntity createResult(int rs, String rule_name, float number, RuleEntity rule, String errorMessage,
			String warnMessage) {
		// TODO Auto-generated method stub
		if (rs != 0) {
			WarnEntity warn = new WarnEntity();
			warn.setRule(rule);
			warn.setNumber(number);
			warn.setName(rule_name);
			if (rs == 2) {
				warn.setLevel("ERROR");
				warn.setReason(errorMessage);
			} else {
				warn.setLevel("WARN");
				warn.setReason(warnMessage);
			}
			return warn;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param number
	 * @param rule
	 * @return 2 error 1 warn 0 normal
	 */
	public int numberRuleExecute(float number, RuleEntity rule) {
		return determineBySign(number, rule.getSign(), rule.getWarnLimit(), rule.getErrorLimit());
	}

	@SuppressWarnings("unused")
	private boolean determineBySign(float number, String sign, float limit) {
		switch (sign) {
		case "GE":
			return number >= limit;
		case "GT":
			return number > limit;
		case "LE":
			return number <= limit;
		case "LT":
			return number < limit;
		case "EQ":
			return number == limit;
		default:
			System.out.println("无法识别判断规则中的sign符号[GT(>),GE(>=),LE(<=),LT(<),EQ(=)]");
			return false;
		}
	}

	/**
	 * 判断数值在什么范围
	 * 
	 * @param number
	 * @param sign
	 * @param limit1
	 * @param limit2
	 * @return 2 :[limit2 , float.max] 1 : [limit1,limit2] 0 : [float.min ,
	 *         limit1]
	 */
	private int determineBySign(float number, String sign, float limit1, float limit2) {
		switch (sign) {
		case "GE":
			if (number < limit1) {
				return 0;
			} else if (number < limit2 && number >= limit1) {
				return 1;
			} else {
				return 2;
			}
		case "GT":
			if (number <= limit1) {
				return 0;
			} else if (number <= limit2 && number > limit1) {
				return 1;
			} else {
				return 2;
			}
		case "LE":
			if (number > limit1) {
				return 0;
			} else if (number >= limit2 && number <= limit1) {
				return 1;
			} else {
				return 2;
			}
		case "LT":
			if (number >= limit1) {
				return 0;
			} else if (number > limit2 && number < limit1) {
				return 1;
			} else {
				return 2;
			}
		case "EQ":
			if (number == limit1) {
				return 1;
			} else {
				return 0;
			}
		default:
			System.out.println("无法识别判断规则中的sign符号[GT(>),GE(>=),LE(<=),LT(<),EQ(=)]");
			return 0;
		}
	}

}
