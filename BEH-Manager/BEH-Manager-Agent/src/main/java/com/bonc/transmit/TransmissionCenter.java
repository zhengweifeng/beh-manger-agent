package com.bonc.transmit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TransmissionCenter implements Runnable{

	private Log log = LogFactory.getLog(TransmissionCenter.class);
	
	private ArrayBlockingQueue<JSONObject> errorQueue;
	public TransmissionCenter(ArrayBlockingQueue<JSONObject> errorQueue) {
		this.errorQueue = errorQueue;
	}
	
	@Override
	public void run() {
		
		while(true) {
			try {
				JSONObject errorJson = errorQueue.take();
				log.debug("导出数据" + errorJson);
				JSONArray array = errorJson.getJSONObject("rule").getJSONArray("transmitList");
				Iterator<String> iterator = array.iterator();
				while(iterator.hasNext()) {
					String tid = iterator.next();
					IExport export = new TransmissionProxy(tid);
					export.export(errorJson);
				}
			} catch (InterruptedException e) {
				log.error("从队列获取数据错误",e);
			}
		}
		
	}

}
