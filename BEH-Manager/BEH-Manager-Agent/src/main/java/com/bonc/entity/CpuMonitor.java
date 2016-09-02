package com.bonc.entity;

public class CpuMonitor {
	/**
	 * 用户空间占用CPU百分比
	 */
	private int us;
	/**
	 * 内核空间占用CPU百分比
	 */
	private int sy;
	/**
	 * 用户进程空间内改变过优先级的进程占用CPU百分比
	 */
	private int ni;
	/**
	 * 空闲CPU百分比
	 */
	private int id;
	/**
	 * 等待输入输出的CPU时间百分比
	 */
	private int wa;
	/**
	 * 硬件CPU中断占用百分比
	 */
	private int hi;
	/**
	 * 软中断占用百分比
	 */
	private int si;
	/**
	 * 虚拟机占用百分比
	 */
	private int st;
	
	/**
	 * 获取sleep时间内的cpu使用情况
	 * cpu2 - cpu1
	 * cpu时间单位0.01秒 ＝ 10毫秒
	 */
	public void createCpuMonitor(Cpu cpu1,Cpu cpu2,int sleep_time) {
		
		this.us = (cpu2.getUserTime() - cpu1.getUserTime())*1000/sleep_time;
		this.sy = (cpu2.getSystemTime() - cpu1.getSystemTime())*1000/ sleep_time;
		this.ni = (cpu2.getNiceTime() - cpu1.getNiceTime())*1000 / sleep_time ;
		this.id = (cpu2.getFreeTime() - cpu1.getFreeTime())*1000 / sleep_time ;
		this.wa = (cpu2.getIowait() - cpu1.getIowait())*1000 / sleep_time ;
		this.hi = (cpu2.getIrq() - cpu1.getIrq())*1000 / sleep_time;
		this.si = (cpu2.getSoftirq() - cpu1.getSoftirq())*1000 / sleep_time;
		this.st = (cpu2.getSteal() - cpu1.getSteal())*1000 / sleep_time;
	}
	
	public int getUs() {
		return us;
	}
	public void setUs(int us) {
		this.us = us;
	}
	public int getSy() {
		return sy;
	}
	public void setSy(int sy) {
		this.sy = sy;
	}
	public int getNi() {
		return ni;
	}
	public void setNi(int ni) {
		this.ni = ni;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWa() {
		return wa;
	}
	public void setWa(int wa) {
		this.wa = wa;
	}
	public int getHi() {
		return hi;
	}
	public void setHi(int hi) {
		this.hi = hi;
	}
	public int getSi() {
		return si;
	}
	public void setSi(int si) {
		this.si = si;
	}
	public int getSt() {
		return st;
	}
	public void setSt(int st) {
		this.st = st;
	}

	@Override
	public String toString() {
		return "CpuMonitor [us=" + us + ", sy=" + sy + ", ni=" + ni + ", id=" + id + ", wa=" + wa + ", hi=" + hi
				+ ", si=" + si + ", st=" + st + "]";
	}
	
}
