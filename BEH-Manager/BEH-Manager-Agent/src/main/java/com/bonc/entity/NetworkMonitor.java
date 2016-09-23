package com.bonc.entity;
/**
 * 封装网络上传和下载的速度
 * @author zwf
 *
 */
public class NetworkMonitor {
	private String name;
	private String unit = "byte/s";
	private long downSpeed;
	private long downPack;
	private long downErr;
	private long downDrop;
	private long uploadSpeed;
	private long uploadPack;
	private long uploadErr;
	private long uploadDrop;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getDownSpeed() {
		return downSpeed;
	}
	public void setDownSpeed(long downSpeed) {
		this.downSpeed = downSpeed;
	}
	public long getDownPack() {
		return downPack;
	}
	public void setDownPack(long downPack) {
		this.downPack = downPack;
	}
	public long getDownErr() {
		return downErr;
	}
	public void setDownErr(long downErr) {
		this.downErr = downErr;
	}
	public long getDownDrop() {
		return downDrop;
	}
	public void setDownDrop(long downDrop) {
		this.downDrop = downDrop;
	}
	public long getUploadSpeed() {
		return uploadSpeed;
	}
	public void setUploadSpeed(long uploadSpeed) {
		this.uploadSpeed = uploadSpeed;
	}
	public long getUploadPack() {
		return uploadPack;
	}
	public void setUploadPack(long uploadPack) {
		this.uploadPack = uploadPack;
	}
	public long getUploadErr() {
		return uploadErr;
	}
	public void setUploadErr(long uploadErr) {
		this.uploadErr = uploadErr;
	}
	public long getUploadDrop() {
		return uploadDrop;
	}
	public void setUploadDrop(long uploadDrop) {
		this.uploadDrop = uploadDrop;
	}
	@Override
	public String toString() {
		return "NetworkMonitor [name=" + name + ", downSpeed=" + downSpeed + ", downPack=" + downPack + ", downErr="
				+ downErr + ", downDrop=" + downDrop + ", uploadSpeed=" + uploadSpeed + ", uploadPack=" + uploadPack
				+ ", uploadErr=" + uploadErr + ", uploadDrop=" + uploadDrop + "]";
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
	
}
