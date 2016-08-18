package com.bonc.agent.impl;


import com.alibaba.fastjson.JSON;
import com.bonc.agent.IAgent;
import com.bonc.base.util.ConfigUtil;
import com.bonc.base.entity.ConfigurationItem;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtocolSignature;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class BehAgent implements IAgent {
	private Server server;
	private String hostname;
	private String ip;
	private int port;
	private String behClusterHostName;
	private int behClusterPort;
//	private ZooKeeper zk;
	private boolean isAlive = false;

	public BehAgent(int port, String shellPath, String behClusterHostName,int behClusterPort) {
		this.behClusterHostName=behClusterHostName;
		this.behClusterPort=behClusterPort;
		init(port, shellPath);
		try {

			this.server = new RPC.Builder(new Configuration()).
					setProtocol(this.getClass()).
					setInstance(this).
					setBindAddress(this.hostname).
					setPort(this.port).build();
			System.out.println(port);
			startAgent();
		} catch (IOException e) {

			e.printStackTrace();
			throw new RuntimeException("behagent代理初始化失败");
		}
	}

	private void startAgent() {
		synchronized (BehAgent.class) {
			if (isAlive) {
				throw new RuntimeException("behagent代理已经启动，请勿重复启动");
			} else {
				server.start();

			}
		}

	}

	/**
	 * 该方法用于远程关闭客户端代理
	 */
	public void stopAgent() {
		synchronized (BehAgent.class) {
			server.stop();
			this.isAlive = false;
			System.exit(0);
		}
	}

	private void init(int port2, String shellPath) {
		try {
			InetAddress address = InetAddress.getLocalHost();
			this.hostname = address.getHostName();

			this.ip = address.getHostAddress();
			this.port = port2;

		} catch (UnknownHostException e) {
			e.printStackTrace();
			throw new RuntimeException("无法获得主机的IP地址和主机名");

		}

	}



	public ProtocolSignature getProtocolSignature(String arg0, long arg1,
			int arg2) throws IOException {

		return new ProtocolSignature(IAgent.versionID, null);
	}

	public long getProtocolVersion(String str, long versionID)
			throws IOException {

		return IAgent.versionID;
	}

	public static void main(String[] args) {
		if (args.length != 4) {
			System.out.println("参数非法");
		} else {
			BehAgent agent = new BehAgent(Integer.parseInt(args[0]), args[1],args[2],Integer.parseInt(args[3]));
		}
	}


	public String readList(String path) {
		return JSON.toJSONString(ConfigUtil.readList(path));
	}
	public void saveList(String path, String configStr){
		try {
			List<ConfigurationItem> configurationItems = JSON.parseArray(configStr, ConfigurationItem.class);
			ConfigUtil.saveList(path, configurationItems);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
