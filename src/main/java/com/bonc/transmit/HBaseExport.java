package com.bonc.transmit;

import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

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

import com.bonc.util.HbaseUtil;

import net.sf.json.JSONObject;

public class HBaseExport implements IExport {
	private Log log = LogFactory.getLog(HBaseExport.class);
	private String key;
	private byte[] family;
	private String tableName;
	@Override
	public void export(JSONObject json) {
		log.debug(" export data to hbase  " + json);
		//时间戳 + 类型 作为rowkey
		
		byte[] row =Bytes.add(Bytes.toBytes(json.getLong("timesmap")), Bytes.toBytes(json.getString("name"))) ;
		Put put = new Put(row);
		put.addColumn(family, Bytes.toBytes("data"), Bytes.toBytes(json.toString()));
		try {
			Connection conn = HbaseUtil.getConnection(key);
			
			Table table = conn.getTable(TableName.valueOf(tableName));
			table.put(put);
			table.close();
		} catch (IOException e) {
			log.error("export data to hbase has exception : " , e);
		} 
		
	}
	public HBaseExport(Transmission trans) {
		tableName = trans.getOtherMap().get("connection."+ trans.getId() +".hbase.tablename");
		key = UUID.randomUUID().toString();
		family = Bytes.toBytes(trans.getOtherMap().get("connection." + trans.getId() + ".hbase.family"));
		HbaseUtil.register(trans, key);
	}

	

}
