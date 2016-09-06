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
		
		return createCpuMonitor(cpu1, cpu2);
	}
	/**
	 * 获取sleep时间内的cpu使用情况
	 * cpu2 - cpu1
	 * cpu时间单位0.01秒 ＝ 10毫秒
	 *  *10 单位换算成 毫秒
	 *  ＊100 百分比 
	 */
	public CpuMonitor createCpuMonitor(Cpu cpu1,Cpu cpu2) {
		CpuMonitor monitor = new CpuMonitor();
		long totalTime = cpu2.getUserTime() + cpu2.getSystemTime() + cpu2.getNiceTime() + 
				cpu2.getFreeTime() + cpu2.getIowait() + cpu2.getIrq() +  cpu2.getSoftirq() + cpu2.getSteal() -
				(cpu1.getUserTime() + cpu1.getSystemTime() + cpu1.getNiceTime() + 
						cpu1.getFreeTime() + cpu1.getIowait() + cpu1.getIrq() +  cpu1.getSoftirq() + cpu1.getSteal());
		monitor.setUs((cpu2.getUserTime() - cpu1.getUserTime())*100.0F/totalTime);
		monitor.setSy((cpu2.getSystemTime() - cpu1.getSystemTime())*100.0F/totalTime);
		monitor.setNi((cpu2.getNiceTime() - cpu1.getNiceTime())*100.0F / totalTime) ;
		monitor.setId((cpu2.getFreeTime() - cpu1.getFreeTime())*100.0F / totalTime) ;
		monitor.setWa((cpu2.getIowait() - cpu1.getIowait())*1000 / totalTime) ;
		monitor.setHi((cpu2.getIrq() - cpu1.getIrq())*1000 / totalTime);
		monitor.setSi((cpu2.getSoftirq() - cpu1.getSoftirq())*1000 / totalTime);
		monitor.setSt((cpu2.getSteal() - cpu1.getSteal())*1000 / totalTime);
		return monitor;
	}
	
	public static void main(String[] args) {
		
		CpuParse parse = new CpuParse();
		System.out.println(parse.getCpuMonitor());
	}
	
}
