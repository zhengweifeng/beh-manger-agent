package com.bonc.entity;

import java.io.Serializable;
/**
 * 获取数据地址：／proc／stat
 * 其中的时间都是从系统启动开始到当前的时间
 * @author zwf
 *
 */
public class Cpu implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**
	 * 用户态cpu时间
	 */
	private int userTime;
	/**
	 * 系统态cpu时间
	 */
	private int systemTime;
	/**
	 * 低优先级程序所占用的用户态的cpu时间。
	 */
	private int niceTime;
	/**
	 * CPU空闲的时间（不包含IO等待）
	 */
	private int freeTime;
	/**
	 * 等待IO响应的时间
	 */
	private int iowait;
	/**
	 * 处理硬件中断的时间
	 */
	private int irq;
	/**
	 *  从系统启动开始累计到当前时刻，软中断时间（单位：jiffies）
	 */
	private int softirq;
	//其他系统所花的时间（个人理解是针对虚拟机）
	private int steal;
	/**
	 * 这行展示自系统启动以来CPU发生的上下文交互的次数
	 */

	private int ctxt;
	/**
	 * 这行展示自系统启动以来所创建的任务的个数
	 */
	private int processes;
	/**
	 * 这行显示当前运行队列的任务数目
	 */
	private int procs_running;
	/**
	 * 这行显示当前被阻塞的任务数目
	 */
	private int procs_blocked;
	
	public int getUserTime() {
		return userTime;
	}

	public int getSystemTime() {
		return systemTime;
	}

	public int getNiceTime() {
		return niceTime;
	}

	public int getFreeTime() {
		return freeTime;
	}

	public int getIowait() {
		return iowait;
	}

	public int getIrq() {
		return irq;
	}

	public int getCtxt() {
		return ctxt;
	}

	public int getProcesses() {
		return processes;
	}

	public int getProcs_running() {
		return procs_running;
	}

	public int getProcs_blocked() {
		return procs_blocked;
	}

	public Cpu() {}

	public void setUserTime(int userTime) {
		this.userTime = userTime;
	}
	public void setSystemTime(int systemTime) {
		this.systemTime = systemTime;
	}
	public void setNiceTime(int niceTime) {
		this.niceTime = niceTime;
	}
	public void setFreeTime(int freeTime) {
		this.freeTime = freeTime;
	}
	public void setIowait(int iowait) {
		this.iowait = iowait;
	}
	public void setIrq(int irq) {
		this.irq = irq;
	}
	public void setCtxt(int ctxt) {
		this.ctxt = ctxt;
	}
	public void setProcesses(int processes) {
		this.processes = processes;
	}
	public void setProcs_running(int procs_running) {
		this.procs_running = procs_running;
	}
	public void setProcs_blocked(int procs_blocked) {
		this.procs_blocked = procs_blocked;
	}

	@Override
	public String toString() {
		return "Cpu [userTime=" + userTime + ", systemTime=" + systemTime + ", niceTime=" + niceTime + ", freeTime="
				+ freeTime + ", iowait=" + iowait + ", irq=" + irq + ", ctxt=" + ctxt + ", processes=" + processes
				+ ", procs_running=" + procs_running + ", procs_blocked=" + procs_blocked + "]";
	}

	public int getSoftirq() {
		return softirq;
	}

	public void setSoftirq(int softirq) {
		this.softirq = softirq;
	}

	public int getSteal() {
		return steal;
	}

	public void setSteal(int steal) {
		this.steal = steal;
	}
	
	
}
