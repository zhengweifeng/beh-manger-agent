package com.bonc.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import org.apache.hadoop.hbase.util.Bytes;

import com.bonc.transmit.Transmission;

public class HbaseUtil {
	private static Log log = LogFactory.getLog(HbaseUtil.class);
	private static Map<String, Connection> connMap = new HashMap<>();

	public static Connection getConnection(String key){
		Connection conn = connMap.get(key);
		if(conn != null) {
			if(conn.isClosed()) {
				try {
					conn = ConnectionFactory.createConnection(conn.getConfiguration());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.info("重新连接 hbase 错误",e );
				}
				
			}
			return conn;
		}
		return null;
	}
	public static void releaseTable(String key){
		 Connection conn = connMap.get(key);
		 connMap.remove(key);
		 if(!conn.isClosed()) {
			 try {
				conn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.info("hbase连接关闭异常",e);
			}
		 }
	}
	
	public static void register(String familyString,String quorum,String clientPort,String tableName,String key) {
		log.info("初始化导出数据的hbase连接");
		
		byte[] family = Bytes.toBytes(familyString);
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", quorum);
		conf.set("hbase.zookeeper.property.clientPort",clientPort);
		try {
			Connection conn = ConnectionFactory.createConnection(conf);

			TableName tbName = TableName.valueOf(tableName);

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
			admin.close();
			connMap.put(key, conn);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			log.error("初始化hbase连接错误", e1);
		}
	}
	
	// 获取表，并且检测表是否存在，如果不存在，创建表
	public static void register(Transmission trans,String key) {
		String ids = trans.getId();
		register(trans.getOtherMap().get("connection." + ids + ".hbase.family"),
				trans.getOtherMap().get("connection." + ids + ".zookeeper")
				, trans.getOtherMap().get("connection." + ids + ".zookeeper.port"), 
				trans.getOtherMap().get("connection." + ids + ".hbase.tablename"), key);
	
	}
}
