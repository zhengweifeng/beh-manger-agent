package com.bonc.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import com.bonc.entity.Disk;
import com.bonc.entity.IO;
import com.bonc.util.ConfigurationUtil;
import com.bonc.util.ShellUtil;

public class DiskParse {

	private static String DISK_PARTITION_FILE = "/proc/partitions";
	private static String DISK_STAT_FILE = "/proc/diskstats";
	private static String LSBLK_NLB = "/bin/lsblk -nlb";
	private static String DF = "/bin/df";
	private String hostName = ConfigurationUtil.get("ssh.auth.localhost");
	private String userName = ConfigurationUtil.get("ssh.auth.userName");
	private String passwd = ConfigurationUtil.get("ssh.auth.passwd");
	private static long SLEEP_TIME = 1000;

	public static void main(String[] args) throws Exception {
		DiskParse parse = new DiskParse();
		parse.getDiskStatus();
	}

	/**
	 * 获取硬盘io情况
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<IO> getDiskIO() throws Exception {
		List<Disk> list1 = getDiskStat();
		Thread.sleep(SLEEP_TIME);
		List<Disk> list2 = getDiskStat();
		List<IO> list = new ArrayList<IO>();
		for (Disk d1 : list1) {
			IO io = new IO();
			for (Disk d2 : list2) {
				if (d1.getName().equals(d2.getName())) {
					io.setDiskName(d1.getName());
					io.setUnit("kb/s");
					io.setReadSpeed((d2.getSelectorRead() - d1.getSelectorRead()) / 2 / (SLEEP_TIME / 1000));
					io.setWriteSpeed((d2.getSelectorWrite() - d1.getSelectorWrite()) / 2 / (SLEEP_TIME / 1000));
					io.setRequest(d2.getRead() + d2.getWrite() - d1.getRead() - d1.getWrite());
				}
			}
			//System.out.println(io);
			list.add(io);
		}
		return list;
	}

	/**
	 * 获取硬盘的名称，以及开机到现在读写次数
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Disk> getDiskStat() throws Exception {

		File stat = new File(DISK_STAT_FILE);
		BufferedReader reader = new BufferedReader(new FileReader(stat));
		String line = null;
		List<Disk> list = new ArrayList<Disk>();
		List<Disk> usefulList = getDiskStatus();
		while ((line = reader.readLine()) != null) {
			
			String[] arr = line.trim().split(" +");
			if (arr.length >= 14) {
				String name = arr[2].trim();
				for (Disk usefulDisk : usefulList) {
					if (usefulDisk.getName().equals(name)) {
						Disk disk = new Disk();
						disk.setMajor(Integer.parseInt(arr[0]));
						disk.setMinor(Integer.parseInt(arr[1]));
						disk.setName(arr[2].trim());
						disk.setRead(Long.parseLong(arr[3]));
						disk.setMergedRead(Long.parseLong(arr[4]));
						disk.setSelectorRead(Long.parseLong(arr[5]));
						disk.setSelectorReadTime(Long.parseLong(arr[6]));
						disk.setWrite(Long.parseLong(arr[7]));
						disk.setMergedWrite(Long.parseLong(arr[8]));
						disk.setSelectorWrite(Long.parseLong(arr[9]));
						disk.setSelectorWriteTime(Long.parseLong(arr[10]));
						disk.setRequest(Long.parseLong(arr[11]));
						disk.setRequestTime(Long.parseLong(arr[12]));
						disk.setRequestComTime(Long.parseLong(arr[13]));
						//System.out.println(disk);
						list.add(disk);
					}
				}
			}
		}
		reader.close();
		return list;
	}
	/**
	 * 获取分区使用情况 
	 * 命令： df
	 * Filesystem     1K-blocks      Used Available Use% Mounted on
	 * /dev/xvda1      20510332   8106576  11355232  42% /
	 * tmpfs             960384         0    960384   0% /dev/shm
	 * /dev/xvdb1     412712844 179810424 211931172  46% /data
	 * @return
	 */
	public List<Disk> getDiskStatus() {
		
		ShellUtil util = new ShellUtil();
		util.createSession(hostName, userName, passwd);
		List<String> list;
		List<Disk> diskList = getDiskAndPart();
		try {
			list = util.exec(DF);
			for(String str : list) {
				//System.out.println(str);
				String[] arr = str.trim().split(" +");
				if(arr[0].equals("Filesystem")) {
					continue;
				}
				for(Disk disk : diskList) {
					if(("/dev/" + disk.getName()).equals(arr[0])) {
						disk.setUsed(Long.parseLong(arr[2]));
						//System.out.println(disk);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			util.close();
		}
		
		return diskList;
	}
	
	
	/**
	 * 获取硬盘大小 和硬盘拥有的分区情况 </br>
	 * 相关命令：lsblk -nlb </br>
	 * 返回:硬盘和分区组成的list列表，包含 类型 硬盘或分区名称，大小，分区所在硬盘，挂在点</br>
	 * @throws Exception
	 */
	public List<Disk> getDiskAndPart() {

		ShellUtil util = new ShellUtil();
		util.createSession(hostName, userName, passwd);
		List<String> list;
		List<Disk> diskList = new ArrayList<Disk>();
		try {
			list = util.exec(LSBLK_NLB);

			String diskName = null;
			for (String line : list) {
				//System.out.println(line);
				String[] arr = line.trim().split(" +");
				if (arr.length >= 6) {

					Disk disk = new Disk();
					disk.setName(arr[0]);
					if (arr[5].equals("disk")) {
						diskName = arr[0];
						disk.setType("DISK");
					} else {
						disk.setType("PARTITION");
						disk.setOwner(diskName);
						disk.setMount(arr[6]);
					}
					disk.setSize(Long.parseLong(arr[3])/1024L);
					disk.setMajor(Integer.parseInt(arr[1].split(":")[0]));
					disk.setMinor(Integer.parseInt(arr[1].split(":")[1]));
					diskList.add(disk);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {			
			util.close();
		}
		return diskList;
	}

	/**
	 * 获取硬盘和分区情况 只是封装了硬盘，块，还需要获取块大小，计算容量，和已使用大小
	 * 相关命令： cat /proc/partitions
	 * 返回： 硬盘名称，主设备ID，次设备ID，block块数
	 * @throws Exception
	 */
	public List<Disk> getDisk() throws Exception {

		File file = new File(DISK_PARTITION_FILE);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		List<Disk> list = new ArrayList<Disk>();
		while ((line = reader.readLine().trim()) != null) {
			if (line.startsWith("major") || line.trim().equals("")) {
				continue;
			}
			String[] str = line.split(" +");
			if (str.length >= 4) {
				Disk d = new Disk();
				d.setMajor(Integer.parseInt(str[0]));
				d.setMinor(Integer.parseInt(str[1]));
				d.setBlocksNum(Long.parseLong(str[2]));
				d.setName(str[3]);
				list.add(d);
			}
		}
		reader.close();
		return list;
	}
}
