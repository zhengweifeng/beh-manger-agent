package com.bonc.monitor;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import com.bonc.util.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MonitorCenter implements Runnable {
	private Log log = LogFactory.getLog(MonitorCenter.class);
	private Connection conn;
	private Table table;
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
			checkTableAndCreate();

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
				log.debug("监控执行真实间隔时间: " +( System.currentTimeMillis() - start_time));
				table.put(putList);
			}
		} catch (Exception e) {
			log.error("监控线程错误",e);
		}
	}

	// 获取表，并且检测表是否存在，如果不存在，创建表
	private void checkTableAndCreate() throws Exception {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", PropertiesUtil.get("hbase.zookeeper.quorum"));
		conf.set("hbase.zookeeper.property.clientPort",
				PropertiesUtil.get("hbase.zookeeper.property.clientPort", "2181"));
		this.conn = ConnectionFactory.createConnection(conf);

		TableName tbName = TableName.valueOf(PropertiesUtil.get("hbase.monitor.tablename", "beh:monitor"));

		Admin admin = conn.getAdmin();
		// 如果表不存在，创建hbase表
		if (!admin.tableExists(tbName)) {
			String spaceName = tbName.getNamespaceAsString();
			log.warn("检查到" + tbName + "不存在,开始检查命名空间"+ spaceName +"是否存在");
			// 如果namespace不存在，创建namespace
			try {
				admin.getNamespaceDescriptor(spaceName);
			} catch (Exception e) {
				// TODO: handle exception
				log.warn("检查到命名空间" + spaceName + " 不存在,正在尝试创建....");
				admin.createNamespace(NamespaceDescriptor.create(spaceName).build());
				
			}

			HTableDescriptor htd = new HTableDescriptor(tbName);
			htd.addFamily(new HColumnDescriptor(Bytes.toBytes(PropertiesUtil.get("hbase.monitor.family", "cf"))));
			admin.createTable(htd);
			log.info("成功创建命名空间和表");
		}
		this.table = conn.getTable(tbName);
		admin.close();
	}
}
