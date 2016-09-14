package com.bonc.export;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.httpclient.HttpClient;
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
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.bonc.entity.WarnEntity;
import com.bonc.util.ConfigurationUtil;

import net.sf.json.JSONObject;

public class ExportThread extends Thread {

	private ArrayBlockingQueue<WarnEntity> messageQueue;
	private Connection conn;
	private Table table;
	private byte[] family = Bytes.toBytes(ConfigurationUtil.get("hbase.warn.family", "cf"));
	private String url = ConfigurationUtil.get("warn.http.rest.url");

	public ExportThread(ArrayBlockingQueue<WarnEntity> messageQueue) {
		this.messageQueue = messageQueue;
	}

	@Override
	public void run() {
		try {
			checkTableAndCreate();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("get or create table expection for export warn in hbase ,"
					+ "please check agent-conf.properties and check habae normal ");
		}
		while (true) {
			try {
				WarnEntity warn = messageQueue.take();
				long create_time = System.currentTimeMillis();
				putToHbase(warn, create_time);
				sendToRest(warn, create_time);
				//可以添加报警时间间隔
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void sendToRest(WarnEntity warn, long create_time) throws InterruptedException {

		String urltmp = url + "?dataId=" + create_time + warn.getName();
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(urltmp);
		HttpResponse response = null;
		int i = 3;
		while (i > 0) {
			try {
				response = httpClient.execute(request);
			} catch (Exception e) {
				System.out.println(
						"http get : " + url + " has expection " + e.toString() + " ,try again after sleep 10s");
				Thread.sleep(10000);
				i--;
				continue;
			}
			if (response.getStatusLine().getStatusCode() == 200) {
				break;
			} else {
				System.out.println("http get : " + url + " has error code " + response.getStatusLine().getStatusCode()
						+ ", try again after sleep 10s");
				Thread.sleep(10000);
				i--;
			}

		}

	}

	private void putToHbase(WarnEntity warn, long create_time) throws IOException {
		// 时间戳 + 规则名
		Put put = new Put(Bytes.toBytes(create_time + warn.getName()));
		put.addColumn(family, Bytes.toBytes("data"), Bytes.toBytes(JSONObject.fromObject(warn).toString()));
		table.put(put);
	}

	// 获取表，并且检测表是否存在，如果不存在，创建表
	private void checkTableAndCreate() throws Exception {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", ConfigurationUtil.get("hbase.zookeeper.quorum"));
		conf.set("hbase.zookeeper.property.clientPort",
				ConfigurationUtil.get("hbase.zookeeper.property.clientPort", "2181"));
		this.conn = ConnectionFactory.createConnection(conf);

		TableName tbName = TableName.valueOf(ConfigurationUtil.get("hbase.warn.tablename", "beh:warn"));

		Admin admin = conn.getAdmin();
		// 如果表不存在，创建hbase表
		if (!admin.tableExists(tbName)) {
			String spaceName = tbName.getNamespaceAsString();
			// 如果namespace不存在，创建namespace
			try {
				admin.getNamespaceDescriptor(spaceName);
			} catch (Exception e) {
				// TODO: handle exception
				admin.createNamespace(NamespaceDescriptor.create(spaceName).build());
			}

			HTableDescriptor htd = new HTableDescriptor(tbName);
			htd.addFamily(new HColumnDescriptor(Bytes.toBytes(ConfigurationUtil.get("hbase.warn.family", "cf"))));
			admin.createTable(htd);
		}
		this.table = conn.getTable(tbName);
		admin.close();
	}
}
