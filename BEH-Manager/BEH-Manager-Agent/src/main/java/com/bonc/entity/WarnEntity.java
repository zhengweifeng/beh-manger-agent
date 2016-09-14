package com.bonc.entity;

public class WarnEntity {

	//告警名称
	private String name;
	
	private float number;
	//报警等级 告警 or 错误
	private String level;
	//原因
	private String reason;
	//来源
	private RuleEntity rule;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public RuleEntity getRule() {
		return rule;
	}
	public void setRule(RuleEntity rule) {
		this.rule = rule;
	}
	
	public float getNumber() {
		return number;
	}
	public void setNumber(float number) {
		this.number = number;
	}
	@Override
	public String toString() {
		return "WarnEntity [name=" + name + ", number=" + number + ", level=" + level + ", reason=" + reason + ", rule="
				+ rule + "]";
	}
	
}
