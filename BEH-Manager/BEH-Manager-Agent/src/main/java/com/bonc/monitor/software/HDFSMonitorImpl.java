package com.bonc.monitor.software;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.ha.HAServiceProtocol;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.tools.NNHAServiceTarget;

import com.bonc.monitor.IMonitor;
import com.bonc.util.PropertiesUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HDFSMonitorImpl implements IMonitor{
	private FileSystem  fs ;
	private Log log = LogFactory.getLog(HDFSMonitorImpl.class);
	private DecimalFormat dformat = new DecimalFormat("#.00");
	private static Configuration conf;
	private static List<String> dataNodeList;
	static{
		conf = new Configuration();
		System.out.println(PropertiesUtil.get("hadoop.home") + "/etc/hadoop/hdfs-site.xml");
		conf.addResource(new Path(PropertiesUtil.get("hadoop.home") + "/etc/hadoop/hdfs-site.xml"));
		conf.addResource(new Path(PropertiesUtil.get("hadoop.home") + "/etc/hadoop/core-site.xml"));
	}
	public HDFSMonitorImpl(){
		try {
			this.fs = FileSystem.get(conf);
		} catch (IOException e) {
			log.error("connection hdfs error " , e);
		}
	}
	
	@Override
	public JSONArray getJsonData() {
		JSONArray array = new JSONArray();
		JSONObject json = new JSONObject();
		json.put("store", getHdfsCapacity());
		json.put("datanode", getDataNodeStatus());
		json.put("blackstatus", getBlockStatus());
		json.put("namenode", getNameNode());
		array.add(json);
		System.out.println(array);
		return array;
	}

	public JSONObject getHdfsCapacity(){
		JSONObject obj = null;
		try {
			FsStatus status = fs.getStatus();
			obj = new JSONObject();
			obj.put("unit", "MB");
			
			
			long capacity = status.getCapacity();
			long remain = status.getRemaining();
			long used = status.getUsed();
			long non_hdfs_used = capacity - remain -used;
			obj.put("capacity",dformat.format(capacity/1024d/1024d));
			obj.put("remain",dformat.format(remain/1024d/1024d));
			obj.put("used",dformat.format(used/1024d/1024d));
			obj.put("non_hdfs_used",dformat.format(non_hdfs_used/1024d/1024d));
			
		} catch (IOException e) {
			log.error("hdfs get status error : ",e);
		}
		return obj;
	}
	
	public JSONObject getBlockStatus(){
		JSONObject json = new JSONObject();
		if(fs instanceof DistributedFileSystem) {
			DistributedFileSystem dfs = (DistributedFileSystem) fs;
			try {
				//所有副本都丢失的块数
				json.put("missingblock", dfs.getMissingBlocksCount());
				//至少丢失一个副本的快数
				json.put("missingRep", dfs.getUnderReplicatedBlocksCount());
			} catch (IOException e) {
				log.error("获取hdfs block 情况出错");
			}
		} else {
			log.error(" the FileSystem you get is not a DistributedFileSystem but is  " + fs.getClass());
		}
		return json;
	}
	
	public JSONArray getDataNodeStatus(){
		getSlaveList();
		JSONArray array = null;
		
		if(fs instanceof DistributedFileSystem) {
			DistributedFileSystem dfs = (DistributedFileSystem) fs;
			
			List<String> hostNameList = new ArrayList<>();
			array = new JSONArray();
			try {
				DatanodeInfo[] nodeList = dfs.getDataNodeStats();
				for(DatanodeInfo info : nodeList) {
					JSONObject json = new JSONObject();
					json.put("state", info.getAdminState());
					json.put("hostname", info.getHostName());
					json.put("unit", "MB");
					json.put("capacity", dformat.format(info.getCapacity()/1024D/1024D));
					json.put("used", dformat.format(info.getDfsUsed()/1024D/1024D));
					json.put("non_hdfs_used", dformat.format(info.getNonDfsUsed()/1024D/1024D));
					json.put("remaining", dformat.format(info.getRemaining()/1024D/1024D));
					json.put("block_pool_used", dformat.format(info.getBlockPoolUsed()/1024D/1024D));
					json.put("block_pool_percent", info.getBlockPoolUsedPercent());
					json.put("version", info.getSoftwareVersion());
					json.put("last_connection", info.getLastUpdate());
					array.add(json);
					hostNameList.add(info.getHostName());
				}
				for(String hostname : dataNodeList) {
					if(!hostNameList.contains(hostname)) {
						JSONObject json = new JSONObject();
						json.put("state", "lost");
						array.add(json);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			log.error(" the FileSystem you get is not a DistributedFileSystem but is  " + fs.getClass());
		}
		return array;
	}
	
	public void getSlaveList(){
		if(dataNodeList == null) {
			File file = new File(PropertiesUtil.get("hadoop.home") + "/etc/hadoop/slaves");
			BufferedReader reader = null;
			if(file.exists()) {
				try {
					reader = new BufferedReader(new FileReader(file));
					String line = null;
					dataNodeList = new ArrayList<>();
					while((line = reader.readLine()) != null) {
						dataNodeList.add(line.trim());
					}
				} catch (FileNotFoundException e) {
					log.error("file not found exception", e);
				} catch (IOException e) {
					log.error("read file slaves excepion :",e);
				}finally {
					try {
						reader.close();
					} catch (IOException e) {
						reader = null;
						log.error("close io exception",e);
					}
				}
			}else {
				log.error("can not get hdfs slaves file :" + file.getPath());
			} 
		}
	}

	public JSONArray getNameNode() {
		JSONArray array = new JSONArray();
		String[] namespaces = conf.get("dfs.nameservices").split(",");
		for(String ns : namespaces) {
			String[] namenodes = conf.get("dfs.ha.namenodes." + ns).trim().split(",");
			for(String nn : namenodes) {
				String hostName = conf.get("dfs.namenode.rpc-address." + ns + "." + nn).split(":")[0];
				try {
					HAServiceProtocol proto = new NNHAServiceTarget(conf, null,nn)
							.getProxy(conf, CommonConfigurationKeys.HA_FC_CLI_CHECK_TIMEOUT_DEFAULT);
					
					JSONObject json = new JSONObject();
					json.put("hostName", hostName);
					json.put("namespaceid", ns);
					json.put("namenodeid", nn);
					json.put("state", proto.getServiceStatus().getState());
					array.add(json);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.error("get namenode status exception",e);
				}
			}
			
		}
		return array;
	}
	public static void main(String[] args) {
		
		HDFSMonitorImpl hmi = new HDFSMonitorImpl();
		hmi.getJsonData();
		
	}
	
}
