package com.bonc.monitor.software;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import com.bonc.monitor.IMonitor;
import com.bonc.util.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HBASEMonitorImpl implements IMonitor {

	private Log log = LogFactory.getLog(HBASEMonitorImpl.class);
	
	private static Configuration conf;
	private Connection conn;
	static{
		conf = HBaseConfiguration.create();
		conf.addResource(new Path(PropertiesUtil.get("hbase.home") + "/conf/hbase-site.xml"));
	}
	public HBASEMonitorImpl() {
		try {
			conn = ConnectionFactory.createConnection(conf);
		} catch (IOException e) {
			log.error("can not conncection hbase ",e);
		}
	}
	@Override
	public JSONArray getJsonData() {
		JSONArray array = new JSONArray();
		array.add(getHbaseMetrices());
		System.out.println(array);
		return array;
	}

	public JSONObject getHbaseMetrices() {
		
		 Admin admin;
		try {
			admin = conn.getAdmin();
			ClusterStatus status = admin.getClusterStatus();
			
			JSONObject json = new JSONObject();
			json.put("state",JSONObject.fromObject(status));
			return json;
		} catch (IOException e) {
			log.error("get hbase admin exception :" ,e);
		}
		return null;
	}
	public static void main(String[] args) {
		IMonitor monitor = new HBASEMonitorImpl();
		monitor.getJsonData();
	}
}
