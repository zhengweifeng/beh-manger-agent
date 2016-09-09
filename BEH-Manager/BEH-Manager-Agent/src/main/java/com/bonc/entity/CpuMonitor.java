package com.bonc.entity;

public class CpuMonitor implements ExportEntityInterface {
	/**
	 * 用户空间占用CPU百分比
	 */
	private float us;
	/**
	 * 内核空间占用CPU百分比
	 */
	private float sy;
	/**
	 * 用户进程空间内改变过优先级的进程占用CPU百分比
	 */
	private float ni;
	/**
	 * 空闲CPU百分比
	 */
	private float id;
	/**
	 * 等待输入输出的CPU时间百分比
	 */
	private float wa;
	/**
	 * 硬件CPU中断占用百分比
	 */
	private float hi;
	/**
	 * 软中断占用百分比
	 */
	private float si;
	/**
	 * 虚拟机占用百分比
	 */
	private float st;


	public float getUs() {
		return us;
	}


	public void setUs(float us) {
		this.us = us;
	}


	public float getSy() {
		return sy;
	}


	public void setSy(float sy) {
		this.sy = sy;
	}


	public float getNi() {
		return ni;
	}


	public void setNi(float ni) {
		this.ni = ni;
	}


	public float getId() {
		return id;
	}


	public void setId(float id) {
		this.id = id;
	}


	public float getWa() {
		return wa;
	}


	public void setWa(float wa) {
		this.wa = wa;
	}


	public float getHi() {
		return hi;
	}


	public void setHi(float hi) {
		this.hi = hi;
	}


	public float getSi() {
		return si;
	}


	public void setSi(float si) {
		this.si = si;
	}


	public float getSt() {
		return st;
	}


	public void setSt(float st) {
		this.st = st;
	}


	@Override
	public String toString() {
		return "CpuMonitor [us=" + us + ", sy=" + sy + ", ni=" + ni + ", id=" + id + ", wa=" + wa + ", hi=" + hi
				+ ", si=" + si + ", st=" + st + "]";
	}
	
}
