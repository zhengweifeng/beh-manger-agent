package com.bonc.transmit;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.util.WarnFactory;

public class TransmissionFactory {

	private static Log log = LogFactory.getLog(TransmissionFactory.class);
	private static Map<String, Transmission> tranMap = WarnFactory.getTransmitMap();
	private static  Map<String , IExport> exportMap = new HashMap<>();
	static {
		
		for(String key : tranMap.keySet()) {
			Transmission tran = tranMap.get(key);
			switch(tran.getType()){
			case "hbase":
				exportMap.put(key, new HBaseExport(tran));
				break;
			case "http":
				exportMap.put(key, new HTTPExport(tran));
				break;
			case "mail":
				exportMap.put(key, new MailExport(tran));
				break;
			case "message":
				exportMap.put(key, new MessageExport(tran));
				break;
			default:
				log.error("不能识别告警数据导出类型: " + tran);
			}
		}
		
	}
	public static IExport getExport(String key){
		return exportMap.get(key);
	}
	
}
