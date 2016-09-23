package com.bonc.monitor;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import com.bonc.util.HbaseUtil;
import com.bonc.util.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MonitorCenter implements Runnable {
	private Log log = LogFactory.getLog(MonitorCenter.class);
	
	private static Map<String, IMonitor> monitorMap;
	private LinkedBlockingQueue<JSONObject> queue;

	public static void register(String name, IMonitor monitor) {
		if (monitorMap == null) {
			monitorMap = new HashMap<>();
		}
		monitorMap.put(name, monitor);
	}

	public MonitorCenter(LinkedBlockingQueue<JSONObject> queue) {
		// TODO Auto-generated constructor stub
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			log.info("---------------------启动监控线程------------------");
			String connKey = UUID.randomUUID().toString();
			HbaseUtil.register(PropertiesUtil.get("hbase.monitor.family"), PropertiesUtil.get("hbase.zookeeper.quorum"),
					PropertiesUtil.get("hbase.zookeeper.property.clientPort"), PropertiesUtil.get("hbase.monitor.tablename"), connKey);

			String hostName = InetAddress.getLocalHost().getHostName();
			byte[] family = Bytes.toBytes(PropertiesUtil.get("hbase.monitor.family"));
			long min_interval = Long.parseLong(PropertiesUtil.get("collect.min.interval"));
			while (true) {
				long start_time = System.currentTimeMillis();
				byte[] row = Bytes.add(Bytes.toBytes(start_time), Bytes.toBytes(hostName));

				List<Put> putList = new ArrayList<Put>();
				for (String key : monitorMap.keySet()) {
					Put put = new Put(row);
					JSONArray array = monitorMap.get(key).getJsonData();
					put.addColumn(family, Bytes.toBytes(key), Bytes.toBytes(array.toString()));
					putList.add(put);
					//将监控数据发送到告警数据队列
					JSONObject json = new JSONObject();
					json.put("name", key);
					json.put("data", array);
					json.put("timesmap", start_time);
					json.put("hostName", hostName);
					queue.put(json);
					log.debug("发送数据到告警数据队列:" + json);
					log.debug("告警数据队列大小:" + queue.size());
				}

				while ((System.currentTimeMillis() - start_time) < min_interval) {
					Thread.sleep(2000);
				}
				Connection conn = HbaseUtil.getConnection(connKey);
				Table table = conn.getTable(TableName.valueOf(PropertiesUtil.get("hbase.monitor.tablename")));
				table.put(putList);
				table.close();
				log.debug("监控执行真实间隔时间: " +( System.currentTimeMillis() - start_time));
			}
		} catch (Exception e) {
			log.error("监控线程错误",e);
		}
	}
}
