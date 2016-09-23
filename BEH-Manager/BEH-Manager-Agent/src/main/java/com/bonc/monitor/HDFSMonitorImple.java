package com.bonc.monitor;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileSystem.Statistics;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.viewfs.ViewFileSystem;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.DistributedFileSystem.DiskStatus;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;

import com.bonc.util.PropertiesUtil;

import net.sf.json.JSONArray;

public class HDFSMonitorImple implements IMonitor{

	@Override
	public JSONArray getJsonData() {
		// TODO Auto-generated method stub
		return null;
	}

	public void connectionHdfs() {
		
		Configuration conf = new Configuration();
		conf.addResource(new Path(PropertiesUtil.get("hadoop.home") + "/etc/hadoop/hdfs-site.xml"));
		conf.addResource(new Path(PropertiesUtil.get("hadoop.home") + "/etc/hadoop/core-site.xml"));
		try {
			FileSystem fs = FileSystem.get(conf);
			System.out.println(fs.getClass());
			DistributedFileSystem dfs = (DistributedFileSystem) fs;
			//HDFS容量 及使用情况
			FsStatus status = dfs.getStatus();
			System.out.println("capacity  : "+ status.getCapacity()/1024/1024/1024 + " G");
			System.out.println("used  : "+ status.getUsed()/1024/1024 +" M");
			System.out.println("remain : "+ status.getRemaining()/1024/1024/1024 + " G");
			
			ContentSummary content = dfs.getContentSummary(new Path("/"));
			System.out.println(" content summary : " + content.toString());
			System.out.println("dir count : " + content.getDirectoryCount());
			System.out.println("file count : " + content.getFileCount());
			
			//hdfs 损坏的块个数
			System.out.println(" 至少一个副本被损坏的块数 : " + dfs.getCorruptBlocksCount());
			System.out.println(" 没有完好副本的快: " + dfs.getMissingBlocksCount());
			System.out.println(" 只有一个副本,并且已经丢失的块数 : " + dfs.getMissingReplOneBlocksCount());
			System.out.println(" 至少丢失一个副本的快数 : " + dfs.getUnderReplicatedBlocksCount());
			//获取hdfs中线程的读写,请求状态
			List<Statistics> allstat = dfs.getAllStatistics();
			
			for(Statistics stat : allstat) {
				System.out.println(stat.toString());
				//System.out.println(stat.getScheme());
				
			}
			DatanodeInfo[] datainfos = dfs.getDataNodeStats();
			for(DatanodeInfo info : datainfos) {
				System.out.println("----------------------");
				System.out.println(" admin state " + info.getAdminState());
				System.out.println(" hostname " + info.getHostName());
				System.out.println(" block pool used " + info.getBlockPoolUsed()/1024/1024 + "M");
				System.out.println(" block pool percent " + info.getBlockPoolUsedPercent());
				System.out.println(" rew capacity  " + info.getCapacity());
				System.out.println(" cache capacity " + info.getCacheCapacity()/1024/1204 + "M");
				System.out.println(" parent " + info.getParent());
				
			}
			DFSClient client = dfs.getClient();
			System.out.println(client.getClientName());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static void main(String[] args) {
		HDFSMonitorImple hm = new HDFSMonitorImple();
		hm.connectionHdfs();
	}
}
