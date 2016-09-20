package com.bonc.transmit;

import java.io.IOException;
import java.util.Iterator;

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
import net.sf.json.JSONObject;

public class HBaseExport implements IExport {
	private Log log = LogFactory.getLog(HBaseExport.class);
	private Connection conn;
	private Table table;
	private byte[] family;

	/**
	 * {"name":"disk","data":[
	 *		{"type":"warn","limit":10,"count":"used/size*100","data":{"type":"PARTITION","name":"vda1","mount":"/boot","owner":"vda","size":253871,"used":32538},"num":12.816745512484687}
	 *		],"timesmap":1474279915905,"hostName":"hadoop031",
	 *		"rule":{
	 *			"count":"used/size*100","errorLimit":99,"goal":"disk","id":1,"scope":"GLOBAL","sign":"GE","transmitList":[m10,m11] 
	 *		}
	 *	}
	 */
	@Override
	public void export(JSONObject json) {
		log.debug(" export data to hbase  " + json);
		//时间戳 + 类型 作为rowkey
		
		byte[] row = Bytes.toBytes(json.getString("timesmap") + json.getString("name"));
		Put put = new Put(row);
		put.addColumn(family, Bytes.toBytes("data"), Bytes.toBytes(json.toString()));
		try {
			table.put(put);
		} catch (IOException e) {
			log.error("export data to hbase has exception : " , e);
		}
		
	}
	public HBaseExport(Transmission trans) {
		init(trans);
	}

	// 获取表，并且检测表是否存在，如果不存在，创建表
	public void init(Transmission trans) {
		log.info("初始化导出数据的hbase连接");
		String ids = trans.getId();
		family = Bytes.toBytes(trans.getOtherMap().get("connection." + ids + ".hbase.family"));
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", trans.getOtherMap().get("connection." + ids + ".zookeeper"));
		conf.set("hbase.zookeeper.property.clientPort",
				trans.getOtherMap().get("connection." + ids + ".zookeeper.port"));
		try {
			conn = ConnectionFactory.createConnection(conf);

			TableName tbName = TableName.valueOf(trans.getOtherMap().get("connection." + ids + ".hbase.tablename"));

			Admin admin = conn.getAdmin();
			// 如果表不存在，创建hbase表
			if (!admin.tableExists(tbName)) {
				String spaceName = tbName.getNamespaceAsString();
				// 如果namespace不存在，创建namespace
				try {
					admin.getNamespaceDescriptor(spaceName);
				} catch (Exception e) {
					log.warn("未找到指定的命名空间,尝试创建中" + spaceName);
					admin.createNamespace(NamespaceDescriptor.create(spaceName).build());
				}

				HTableDescriptor htd = new HTableDescriptor(tbName);
				htd.addFamily(new HColumnDescriptor(family));
				admin.createTable(htd);
				log.info("创建成功 : " + tbName);
			}
			table = conn.getTable(tbName);
			admin.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			log.error("初始化hbase连接错误", e1);
		}
	}

}
