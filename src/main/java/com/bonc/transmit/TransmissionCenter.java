package com.bonc.transmit;

import java.util.Iterator;
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
				log.info("导出数据" + errorJson);
				//{"name":"disk","data":[
				//	{"type":"warn","limit":10,"count":"used/size*100","data":{"type":"PARTITION","name":"vda1","mount":"/boot","owner":"vda","size":253871,"used":32538},"num":12.816745512484687}
				//],
				//"timesmap":1474279915905,"hostName":"hadoop031",
				//"rule":{"count":"used/size*100","errorLimit":99,"goal":"disk","id":1,"scope":"GLOBAL","sign":"GE",
				//"transmitList":[m10,m11]}}
				JSONArray array = errorJson.getJSONObject("rule").getJSONArray("transmitList");
				Iterator<String> iterator = array.iterator();
				while(iterator.hasNext()) {
					String tid = iterator.next();
					IExport export = TransmissionFactory.getExport(tid);
					export.export(errorJson);
				}
			} catch (InterruptedException e) {
				log.error("从队列获取数据错误",e);
			}
		}
		
	}

}
