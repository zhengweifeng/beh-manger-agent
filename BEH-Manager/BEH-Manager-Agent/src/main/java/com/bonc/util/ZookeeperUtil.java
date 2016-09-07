package com.bonc.util;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperUtil {

	private static String zkhost = ConfigurationUtil.get("hbase.zookeeper.quorum", "localhost");
	private static String zkport = ConfigurationUtil.get("hbase.zookeeper.property.clientPort", "2181");

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

		String[] hosts = zkhost.split(",");
		StringBuffer buffer = new StringBuffer("");
		for (String h : hosts) {
			buffer.append(h + ":" + zkport);
		}
		ZooKeeper zk = new ZooKeeper(buffer.toString(), 3000, new Watcher() {
			public void process(WatchedEvent event) {
				System.out.println("已触发" + event.toString() + "事件！");
			}
		});
		zk.create("/testRootPath", "zwf".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		// 创建一个子目录节点
		zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
		System.out.println(new String(zk.getData("/testRootPath", false, null)));

		// 取出子目录节点列表
		System.out.println(zk.getChildren("/testRootPath", true));

		// 创建另外一个子目录节点
		zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(), Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
		System.out.println(zk.getChildren("/testRootPath", true));

		// 修改子目录节点数据
		zk.setData("/testRootPath/testChildPathOne", "hahahahaha".getBytes(), -1);
		byte[] datas = zk.getData("/testRootPath/testChildPathOne", true, null);
		String str = new String(datas, "utf-8");
		System.out.println(str);

		// 删除整个子目录 -1代表version版本号，-1是删除所有版本
		zk.delete("/testRootPath/testChildPathOne", -1);
		System.out.println(zk.getChildren("/testRootPath", true));
		System.out.println(str);

	}

}
