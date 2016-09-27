package com.bonc.transmit;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.util.RuleUtil;

import net.sf.json.JSONObject;

public class TransmissionProxy implements IExport{

	private static Log log = LogFactory.getLog(TransmissionProxy.class);
	private static Map<String, Transmission> tranMap = RuleUtil.getTransmitMap();
	private static  Map<String , IExport> exportMap = new HashMap<>();
	private static Map<String,Long> timeMap = new HashMap<>();
	private IExport source;
	private String key;
	
	
	public TransmissionProxy(String key) {
		this.source = exportMap.get(key);
		this.key = key;
	}
	
	@Override
	public void export(JSONObject json) {
		if(before(json)){			
			source.export(json);
		} else {
			log.debug("数据在时间间隔中 ,放弃数据入库 : " + json);
		}
	}
	private boolean before(JSONObject json){
		
		Transmission tran = tranMap.get(key);
		int ruleId = json.getJSONObject("rule").getInt("id");
		String timekey = tran.getId() + "_" + ruleId;
		if(timeMap.get(timekey) == null || timeMap.get(timekey) == 0) {
			long value = json.getLong("timesmap") + tran.getInterval();
			timeMap.put(timekey, value);
			return true;
		} else {
			long storeTime = timeMap.get(timekey);
			if(System.currentTimeMillis() >= storeTime) {
				return true;
			}
			return false;
		}
	}

	public static void register(String type, IExport export) {
		exportMap.put(type, export);
	}

	@Override
	public void init(Transmission tran) {
	};
	
}
