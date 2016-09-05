package com.bonc.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.bonc.entity.Cpu;
import com.bonc.entity.CpuMonitor;

public class CpuParse {

	private static String statFile = "/proc/stat";
	private static int sleep_time = 1000;
	/**
	 * 读取/proc/stat文件获取cpu信息
	 * @return
	 */
	public Cpu readerProcStat() {
		
		File stat = new File(statFile);
		BufferedReader reader = null;
		Cpu cpu = null;
		try {
			reader = new BufferedReader(new FileReader(stat));
			cpu = parseProcStat(reader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				reader = null;
			}
		}
		return cpu;
	}
	/**
	 * 解析/proc/stat读取的内容
	 * @param reader
	 * @return Cpu 封装成cpu对象
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public Cpu parseProcStat(BufferedReader reader) throws NumberFormatException, IOException {
		Cpu cpu = new Cpu();
		String line = null;
		while((line = reader.readLine()) != null) {
			String[] pam = line.split(" +");
			//System.out.println(line + " : " +pam.length);
			if(pam.length >= 10 && pam[0].trim().equals("cpu")) {
				cpu.setUserTime(Integer.parseInt(pam[1]));
				cpu.setNiceTime(Integer.parseInt(pam[2]));
				cpu.setSystemTime(Integer.parseInt(pam[3]));
				cpu.setFreeTime(Integer.parseInt(pam[4]));
				cpu.setIowait(Integer.parseInt(pam[5]));
				cpu.setIrq(Integer.parseInt(pam[6]));
				cpu.setSoftirq(Integer.parseInt(pam[7]));
				cpu.setSteal(Integer.parseInt(pam[8]));
			} else if(pam.length ==2 && pam[0].trim().equals("ctxt")) {
				cpu.setCtxt(Integer.parseInt(pam[1]));
			} else if(pam.length ==2 && pam[0].trim().equals("processes")) {
				cpu.setProcesses(Integer.parseInt(pam[1]));
			} else if(pam.length == 2 && pam[0].trim().equals("procs_running")) {
				cpu.setProcs_running(Integer.parseInt(pam[1]));
			} else if(pam.length ==2 && pam[0].trim().equals("procs_blocked")) {
				cpu.setProcs_blocked(Integer.parseInt(pam[1]));
			}
			
		}
		//System.out.println(cpu);
		return cpu;
	}
	/**
	 * 获取cpu使用百分比
	 * @return
	 */
	public CpuMonitor getCpuMonitor() {
		Cpu cpu1 = readerProcStat();
		try {
			Thread.sleep(sleep_time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cpu cpu2 = readerProcStat();
		CpuMonitor monitor = new CpuMonitor();
		monitor.createCpuMonitor(cpu1, cpu2, sleep_time);
		//System.out.println(monitor);
		return monitor;
	}
	
	
	public static void main(String[] args) {
		
		CpuParse parse = new CpuParse();
		System.out.println(parse.getCpuMonitor());
	}
	
}
