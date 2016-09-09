/*
package com.bonc;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
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
import com.bonc.parse.CpuParse;
import com.bonc.parse.DiskParse;
import com.bonc.parse.MemoryParse;
import com.bonc.parse.NetworkParse;
import com.bonc.util.ConfigurationUtil;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;


@SuppressWarnings("deprecation")
public class ApplicationServer {

	private Connection conn;
	private  Table table;
	private static byte[] family ;
	public static void main(String[] args) {

		ApplicationServer server = new ApplicationServer();
		try {
			server.parseArgs(args);
			family = Bytes.toBytes(ConfigurationUtil.get("hbase.monitor.family"));
			server.checkTableAndCreate();
			
			long min_interval = Long.parseLong(ConfigurationUtil.get("collect.min.interval", "10000"));
			InetAddress netAddress = InetAddress.getLocalHost();
			String hostName = netAddress.getHostName();
			
			while (true) {
				long start_time = System.currentTimeMillis();
				byte[] row = Bytes.add(Bytes.toBytes(start_time),Bytes.toBytes(hostName));
				
				List<Put> putList = new ArrayList<Put>();
				
				//获取cpu信息
				CpuParse cp = new CpuParse();
				server.createPutFromObj(putList, cp.getCpuMonitor(),row,"cpu");
				//获取网络信息
				NetworkParse np = new NetworkParse();
				server.createPutFromList(putList, np.getNetSpeed(), row, "network");
				//获取内存信息
				MemoryParse mp = new MemoryParse();
				server.createPutFromObj(putList, mp.readMemory(),row,"memory");
				//获取硬盘信息。和IO信息
				DiskParse dp = new DiskParse();
				server.createPutFromList(putList, dp.getDiskStatus(), row, "disk");
				server.createPutFromList(putList, dp.getDiskIO(), row, "io");
				
				while ((System.currentTimeMillis() - start_time) < min_interval) {
					Thread.sleep(2000);
				}
				System.out.println(System.currentTimeMillis() - start_time);
				server.table.put(putList);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				server.table.close();
				server.conn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	private void createPutFromObj(List<Put> putList,Object ojb,byte[] row,String qualifier) {
		JSONObject cmJson = JSONObject.fromObject(ojb);;
		Put cmPut = new Put(row);
		cmPut.add(family, Bytes.toBytes(qualifier), Bytes.toBytes(cmJson.toString()));
		putList.add(cmPut);
	}
	private void createPutFromList(List<Put> putList,List<?> list,byte[] row,String qualifier) {
		JSONArray netArray = JSONArray.fromObject(list);
		//System.out.println(netArray);
		Put netPut = new Put(row);
		netPut.add(family, Bytes.toBytes(qualifier), Bytes.toBytes(netArray.toString()));
		putList.add(netPut);
	}
	
	// 获取表，并且检测表是否存在，如果不存在，创建表
	private void checkTableAndCreate() throws Exception {
		Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", ConfigurationUtil.get("hbase.zookeeper.quorum"));
		conf.set("hbase.zookeeper.property.clientPort",
				ConfigurationUtil.get("hbase.zookeeper.property.clientPort", "2181"));
		this.conn = ConnectionFactory.createConnection(conf);

		TableName tbName = TableName.valueOf(ConfigurationUtil.get("hbase.monitor.tablename","beh:monitor"));
		
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
			htd.addFamily(new HColumnDescriptor(Bytes.toBytes(ConfigurationUtil.get("hbase.monitor.family", "cf"))));
			admin.createTable(htd);
		}
		this.table = conn.getTable(tbName);
		admin.close();
	}

	private void parseArgs(String[] args) throws Exception {

		Options opt = new Options();
		opt.addOption("h", "help", false, "list help");
		opt.addOption("c", "conf",true, "set which config to read");

		HelpFormatter hf = new HelpFormatter();
		hf.setWidth(100);
		CommandLineParser parse = new PosixParser();

		CommandLine line = parse.parse(opt, args);
		if (line.hasOption("h")) {
			hf.printHelp("beh-manager-agent help", opt);
		}
		if (line.hasOption("c")) {
			ConfigurationUtil.init(line.getOptionValue("c"));
		}
		// 对于其他配置文件中参数解析暂时不支持，后续开发支持
		// 打印opts的名称和值
		System.out.println("-------------------------------------- ");
		Option[] opts = line.getOptions();
		if (opts != null) {
			for (Option opt1 : opts) {

				String name = opt1.getLongOpt();
				String value = opt1.getValue(null);
				if (name == null ||name.equals("h") || name.equals("c") || value == null) {
					continue;
				}
				System.out.println(name + "=>" + value);
			}
		}
	}

}
*/
