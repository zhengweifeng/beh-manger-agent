package org.BEH.Manager.Agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadFileLinux {
	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static MemInfo ReadLinuxFile(String fileName) {
	    File file = new File(fileName);
	    BufferedReader reader = null;
	    int ver = 0;
	    MemInfo me = new MemInfo();
	    try {
	        reader = new BufferedReader(new FileReader(file));
	        String tempString = null;
	        ver = ReadFileLinux.readLinuxVersion();
	        while ((tempString = reader.readLine()) != null) {
	        	String [] str = tempString.split(":");
	        	long num = Long.valueOf(str[1].trim().split(" ")[0]);
	        	if(str[0].equals("MemTotal")){
	        		me.setMemTotal(num);
	        	}else if(str[0].equals("MemFree")){
	        		me.setMemFree(num);
	        	}else if(str[0].equals("Buffers")){
	        		me.setBuffers(num);
	        	}else if(str[0].equals("Cached")){
	        		me.setCached(num);
	        	}else if(str[0].equals("SwapTotal")){
	        		me.setSwapTotal(num);
	        	}else if(str[0].equals("SwapFree")){
	        		me.setSwapFree(num);
	        	}
	        }
        	if(ver==6){
        		long MedUsed = me.getMemTotal()-me.getMemFree()-me.getBuffers()-me.getCached();
        		me.setMemUsed(MedUsed);
        		long MemFree = me.getMemFree()+me.getBuffers()+me.getCached();
        		me.setMemFree(MemFree);
        	}else if(ver == 7){
        		long MedUsed = me.getMemTotal()-me.getMemFree();
        		me.setMemUsed(MedUsed);
        	}
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return me;
	    } finally {
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
	    return me;
	}
	
	public static int readLinuxVersion(){
		File f = null;
		f = new File("D://linuxPath/etc/redhat-release");
		if(!f.exists()){
			f = new File("D://linuxPath/etc/system-release");
			if(!f.exists()){
				f = new File("D://linuxPath/etc/centos-release");
			}
		}
		int version = 0;
	    BufferedReader reader = null;
	    try {
	        reader = new BufferedReader(new FileReader(f));
	        String tempString = null;
	        while ((tempString = reader.readLine()) != null) {
	        	if(tempString.indexOf("7.")!=(-1)){
	        		version = 7;
	        		break;
	        	}else if(tempString.indexOf("6.")!=(-1)){
	        		version = 6;
	        		break;
	        	}
	        }
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("file does not exist");
	    } finally {
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e1) {
	            }
	        }
	    }
		return version;
	}
	public static void main(String[] args) {
		MemInfo me =  ReadFileLinux.ReadLinuxFile("D://linuxPath/proc/meminfo");
		System.out.println(me);
		int i = ReadFileLinux.readLinuxVersion();
		System.err.println(i);
	}
}

