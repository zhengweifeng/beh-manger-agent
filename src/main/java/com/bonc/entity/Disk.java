package com.bonc.entity;

import java.util.List;

public class Disk  {
	/**
	 * 主设备编号
	 */
	private int major;
	/**
	 * 次设备编号
	 */
	private int minor;
	/**
	 * block 数
	 */
	private long blocksNum;
	/**
	 * 设备名称
	 */
	private String name;
	
	/**
	 * 读完成次数
	 */
	private long read;
	/**
	 * 合并读完成次数
	 */
	private long mergedRead;
	/**
	 * 读扇区次数
	 */
	private long selectorRead;
	/**
	 * 读扇区时间
	 */
	private long selectorReadTime;
	/**
	 * 写完成次数
	 */
	private long write;
	/**
	 * 合并写完成次数
	 */
	private long mergedWrite;
	/**
	 * 写扇区次数
	 */
	private long selectorWrite;
	/**
	 * 写扇区消耗时间
	 */
	private long selectorWriteTime;
	/**
	 * 正在处理请求数
	 */
	private long request;
	/**
	 * I/O操作时间
	 */
	private long requestTime;
	/**
	 * I／O操作时间，包含io打开，结束时间
	 *
	 */
	private long requestComTime;
	/**
	 * 硬盘大小
	 * 单位KB
	 */
	private long size;
	/**
	 * 硬盘使用量
	 * 单位KB
	 */
	private long used;
	/**
	 *  类型，是硬盘还是分区
	 */
	private String type;

	/**
	 * 分区 归属的硬盘
	 */
	private String owner;
	
	private String mount;
	
	public long getRead() {
		return read;
	}
	public void setRead(long read) {
		this.read = read;
	}
	public long getMergedRead() {
		return mergedRead;
	}
	public void setMergedRead(long mergedRead) {
		this.mergedRead = mergedRead;
	}
	public long getSelectorRead() {
		return selectorRead;
	}
	public void setSelectorRead(long selectorRead) {
		this.selectorRead = selectorRead;
	}
	public long getSelectorReadTime() {
		return selectorReadTime;
	}
	public void setSelectorReadTime(long selectorReadTime) {
		this.selectorReadTime = selectorReadTime;
	}
	public long getWrite() {
		return write;
	}
	public void setWrite(long write) {
		this.write = write;
	}
	public long getMergedWrite() {
		return mergedWrite;
	}
	public void setMergedWrite(long mergedWrite) {
		this.mergedWrite = mergedWrite;
	}
	public long getSelectorWrite() {
		return selectorWrite;
	}
	public void setSelectorWrite(long selectorWrite) {
		this.selectorWrite = selectorWrite;
	}
	public long getSelectorWriteTime() {
		return selectorWriteTime;
	}
	public void setSelectorWriteTime(long selectorWriteTime) {
		this.selectorWriteTime = selectorWriteTime;
	}
	public long getRequest() {
		return request;
	}
	public void setRequest(long request) {
		this.request = request;
	}
	public long getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}
	public long getRequestComTime() {
		return requestComTime;
	}
	public void setRequestComTime(long requestComTime) {
		this.requestComTime = requestComTime;
	}
	public int getMajor() {
		return major;
	}
	public void setMajor(int major) {
		this.major = major;
	}
	public int getMinor() {
		return minor;
	}
	public void setMinor(int minor) {
		this.minor = minor;
	}
	public long getBlocksNum() {
		return blocksNum;
	}
	public void setBlocksNum(long blocksNum) {
		this.blocksNum = blocksNum;
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getUsed() {
		return used;
	}
	public void setUsed(long used) {
		this.used = used;
	}


	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getMount() {
		return mount;
	}
	public void setMount(String mount) {
		this.mount = mount;
	}
	@Override
	public String toString() {
		return "Disk [major=" + major + ", minor=" + minor + ", blocksNum=" + blocksNum + ", name=" + name + ", read="
				+ read + ", mergedRead=" + mergedRead + ", selectorRead=" + selectorRead + ", selectorReadTime="
				+ selectorReadTime + ", write=" + write + ", mergedWrite=" + mergedWrite + ", selectorWrite="
				+ selectorWrite + ", selectorWriteTime=" + selectorWriteTime + ", request=" + request + ", requestTime="
				+ requestTime + ", requestComTime=" + requestComTime + ", size=" + size + ", used=" + used + ", type="
				+ type + ", owner=" + owner + ", mount=" + mount + "]";
	}
	
	
	
}
