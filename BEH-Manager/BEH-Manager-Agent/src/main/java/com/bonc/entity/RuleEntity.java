package com.bonc.entity;
/**
 * 报警规则对象
 * @author zwf
 *
 */
public class RuleEntity {
	// 规则id
	private String id;
	//规则名称
	private String name;
	//规则类型
	private RuleType type;
	//规则作用范围 single or GLOGAL
	private RuleScope scope;
	//single 规则 对应主机
	private String hostName;
	//规则计算符号
	private RuleSign sign;
	//规则限制范围
	private double limit;
	//报警级别 警告,还是 错误
	private RuleLevel level;
	//报警方式 邮件 or hbase or 短信 or http
	private RuleMode mode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public RuleType getType() {
		return type;
	}
	public void setType(RuleType type) {
		this.type = type;
	}
	public RuleScope getScope() {
		return scope;
	}
	public void setScope(RuleScope scope) {
		this.scope = scope;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public RuleSign getSign() {
		return sign;
	}
	public void setSign(RuleSign sign) {
		this.sign = sign;
	}
	public double getLimit() {
		return limit;
	}
	public void setLimit(double limit) {
		this.limit = limit;
	}

	public RuleMode getMode() {
		return mode;
	}
	public void setMode(RuleMode mode) {
		this.mode = mode;
	}
	public RuleLevel getLevel() {
		return level;
	}
	public void setLevel(RuleLevel level) {
		this.level = level;
	}
	enum RuleScope{
		SINGLE,GLOGAL
	}
	enum RuleSign{
		LG(">"),GE(">=");
		private String sign;
		RuleSign(String sign){
			this.setSign(sign);
		}
		public String getSign() {
			return sign;
		}
		public void setSign(String sign) {
			this.sign = sign;
		}
		
	}
	enum RuleType{
		DISK,CPU,IO,MEMORY,NETWORK
	}
	enum RuleLevel{
		WARN,ERROR
	}
	enum RuleMode{
		MAIL,HBASE,MESSAGE,HTTP
	}
}
