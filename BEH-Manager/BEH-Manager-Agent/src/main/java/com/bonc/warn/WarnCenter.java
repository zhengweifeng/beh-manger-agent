package com.bonc.warn;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.util.WarnFactory;

import net.sf.json.JSONObject;

public class WarnCenter implements Runnable {

	private Log log = LogFactory.getLog(WarnCenter.class);
	private LinkedBlockingQueue<JSONObject> queue;
	private static Map<String, IRuleExecutor> executorMap;
	private ArrayBlockingQueue<JSONObject> errorQueue;

	public WarnCenter(LinkedBlockingQueue<JSONObject> queue, ArrayBlockingQueue<JSONObject> errorQueue) {
		// TODO Auto-generated constructor stub
		this.queue = queue;
		this.errorQueue = errorQueue;
	}

	@Override
	public void run() {
		log.info("启动告警线程,开始过滤告警信息");
		while (true) {
			try {
				JSONObject json = queue.take();
				Map<String, Regulation> ruleMap = WarnFactory.getRule(json.getString("name"));
				if (ruleMap == null) {
					log.warn("不能获取数据对应的规则:" + json);
					continue;
				}
				for (String key : ruleMap.keySet()) {
					Regulation rule = ruleMap.get(key);
					
					IRuleExecutor exec = executorMap.get(rule.getType());
					if (exec == null) {
						log.warn("不能识别规则类型:" + rule);
						continue;
					}
					JSONObject obj = exec.execute(json, rule);
					if (obj != null) {
						errorQueue.put(obj);
					}
				}
			} catch (InterruptedException e) {
				log.error("从队列获取元素错误:", e);
			}

		}
	}

	public static void register(String key, IRuleExecutor value) {
		if (executorMap == null) {
			executorMap = new HashMap<>();
		}
		executorMap.put(key, value);
	}

}
