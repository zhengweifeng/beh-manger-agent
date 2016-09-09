package com.bonc.entity;

public class WarnEntity {

	//告警名称
	private String name;
	//类型 软件 or 硬件
	private String type;
	//报警等级 告警 or 错误
	private String level;
	//原因
	private String reason;
	//来源
	private String source;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	
}
