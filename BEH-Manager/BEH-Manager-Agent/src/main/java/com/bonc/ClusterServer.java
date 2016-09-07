package com.bonc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import com.bonc.util.ConfigurationUtil;
import com.google.gson.JsonObject;

public class ClusterServer {

	private static byte[] family = Bytes.toBytes(ConfigurationUtil.get("hbase.monitor.family"));
	
	public static void main(String[] args) throws Exception {
		
		long sleep_time = Long.parseLong(ConfigurationUtil.get("collect.min.interval"));
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", ConfigurationUtil.get("hbase.zookeeper.quorum"));
		conf.set("hbase.zookeeper.property.clientPort",
				ConfigurationUtil.get("hbase.zookeeper.property.clientPort", "2181"));
		Connection conn = ConnectionFactory.createConnection(conf);
		TableName tbName = TableName.valueOf(ConfigurationUtil.get("hbase.monitor.tablename","beh:monitor"));
		long start_time = System.currentTimeMillis();
		System.out.println("----------" + (start_time - sleep_time) +"--------------");
		byte[] startRow = Bytes.toBytes(0);
		Table table = conn.getTable(tbName);
		ResultScanner rs = table.getScanner(new Scan(startRow));
		Put put = new Put(Bytes.toBytes(start_time));
		ClusterServer server = new ClusterServer();
		server.createDiskPut(rs, put);
		
	}
	
	public void createDiskPut(ResultScanner resultScanner,Put put) {
		
		Iterator<Result> it = resultScanner.iterator();
		Map<String, JsonObject> diskMap = new HashMap<String, JsonObject>();
		while(it.hasNext()) {
			Result r = it.next();
			byte[] row = r.getRow();
			long timestamp = Bytes.toLong(Bytes.copy(row, 0, 8));
			String host = Bytes.toString(Bytes.copy(row, 8, row.length-8));
			//diskMap.put(host, value)
			
		}
	}
	
}
