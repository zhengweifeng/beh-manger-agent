package com.bonc.entity;

public class IO  implements ExportEntityInterface{
	
	private String diskName;
	private long readSpeed;
	private long writeSpeed;
	private String unit;
	private long request;
	public String getDiskName() {
		return diskName;
	}
	public void setDiskName(String diskName) {
		this.diskName = diskName;
	}
	public long getReadSpeed() {
		return readSpeed;
	}
	public void setReadSpeed(long readSpeed) {
		this.readSpeed = readSpeed;
	}
	public long getWriteSpeed() {
		return writeSpeed;
	}
	public void setWriteSpeed(long writeSpeed) {
		this.writeSpeed = writeSpeed;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public long getRequest() {
		return request;
	}
	public void setRequest(long request) {
		this.request = request;
	}
	@Override
	public String toString() {
		return "IO [diskName=" + diskName + ", readSpeed=" + readSpeed + ", writeSpeed=" + writeSpeed + ", unit=" + unit
				+ ", request=" + request + "]";
	}
	
}
