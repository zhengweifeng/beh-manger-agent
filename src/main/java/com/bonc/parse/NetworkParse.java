package com.bonc.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.bonc.entity.Network;
import com.bonc.entity.NetworkMonitor;

public class NetworkParse {

	private static String PROC_NET_DEV = "/proc/net/dev";
	private static long SLEEP_TIME= 1000;
	public static void main(String[] args) {
		NetworkParse net = new NetworkParse();
		net.getNetSpeed();
	}
	
	public List<NetworkMonitor> getNetSpeed() {
		List<NetworkMonitor> list = new ArrayList<NetworkMonitor>();
		List<Network> list1 = readProcNetDev();
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Network> list2 = readProcNetDev();
		for(Network net1 : list1) {
			for(Network net2 : list2) {
				if(net1.getName().equals(net2.getName())) {
					NetworkMonitor monitor = new NetworkMonitor();
					monitor.setName(net1.getName());
					monitor.setDownSpeed(net2.getReceiveSize() - net1.getReceiveSize()/(SLEEP_TIME/1000));
					monitor.setDownPack(net2.getReceivePacket() - net1.getReceivePacket()/(SLEEP_TIME/1000));
					monitor.setDownErr(net2.getReceiveErrs()- net1.getReceiveErrs()/(SLEEP_TIME/1000));
					monitor.setDownDrop(net2.getReceiveDrop() - net1.getReceiveDrop()/(SLEEP_TIME/1000));
					monitor.setUploadSpeed(net2.getSendSize() - net1.getSendSize()/(SLEEP_TIME/1000));
					monitor.setUploadPack(net2.getSendPacket() - net1.getSendPacket()/(SLEEP_TIME/1000));
					monitor.setUploadErr(net2.getSendErrs() - net1.getSendErrs()/(SLEEP_TIME/1000));
					monitor.setUploadDrop(net2.getSendDrop() - net1.getSendDrop()/(SLEEP_TIME/1000));
					list.add(monitor);
					//System.out.println(monitor);
				}
			}
		}
		return list;
	}
	
	
	
	public List<Network> readProcNetDev() {
		BufferedReader reader = null;
		List<Network> list = new ArrayList<Network>();
		try {
			reader = new BufferedReader(new FileReader(new File(PROC_NET_DEV)));
			String line = null;
			while((line = reader.readLine()) != null) {
				//System.out.println(line);
				if(line.startsWith("Inter-|") || line.trim().startsWith("face |bytes")) {
					continue;
				} 
				String arr[] = line.trim().split(" +");
				Network net = new Network();
				String name = arr[0].trim().replace(":", "");
				net.setName(name);
				net.setReceiveSize(Long.parseLong(arr[1]));
				net.setReceivePacket(Long.parseLong(arr[2]));
				net.setReceiveErrs(Long.parseLong(arr[3]));
				net.setReceiveDrop(Long.parseLong(arr[4]));
				net.setSendSize(Long.parseLong(arr[9]));
				net.setSendPacket(Long.parseLong(arr[10]));
				net.setSendErrs(Long.parseLong(arr[11]));
				net.setSendDrop(Long.parseLong(arr[12]));
				list.add(net);
				//System.out.println(net);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
