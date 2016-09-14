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
	private String type;
	private String goal;
	private String scope;
	private String sign;
	private float warnLimit;
	private float errorLimit;
	private ConnectionEntity conn;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public float getWarnLimit() {
		return warnLimit;
	}
	public void setWarnLimit(String warnLimit) {
	
		this.warnLimit = Float.parseFloat(warnLimit);
	}
	public float getErrorLimit() {
		return errorLimit;
	}
	public void setErrorLimit(String errorLimit) {
		this.errorLimit = Float.parseFloat(errorLimit);
	}
	public ConnectionEntity getConn() {
		return conn;
	}
	public void setConn(ConnectionEntity conn) {
		this.conn = conn;
	}
	@Override
	public String toString() {
		return "RuleEntity [id=" + id + ", name=" + name + ", type=" + type + ", goal=" + goal + ", scope=" + scope
				+ ", sign=" + sign + ", warnLimit=" + warnLimit + ", errorLimit=" + errorLimit + ", conn=" + conn + "]";
	}
	
	
}
