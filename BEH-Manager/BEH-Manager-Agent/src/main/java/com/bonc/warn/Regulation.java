package com.bonc.warn;

import java.util.List;

import com.bonc.transmit.Transmission;

/**
 * 规则类型
 * @author zwf
 *
 */
public class Regulation {

	private int id;
	//规则类型,数值型,还是 boolean行
	private String type;
	//规则对应对象
	private String goal;
	//计算规则
	private String count;
	//作用范围,GLOBAL还是 主机名
	private String scope;
	//计算符号
	private String sign;
	//告警阀值
	private float warnLimit;
	//错误阀值
	private float errorLimit;
	//发送位置
	private List<String> transmitList;
	
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

	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
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
	public void setWarnLimit(float warnLimit) {
		this.warnLimit = warnLimit;
	}
	public float getErrorLimit() {
		return errorLimit;
	}
	public void setErrorLimit(float errorLimit) {
		this.errorLimit = errorLimit;
	}
	
	public List<String> getTransmitList() {
		return transmitList;
	}
	public void setTransmitList(List<String> transmitList) {
		this.transmitList = transmitList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Regulation [id=" + id + ", type=" + type + ", goal=" + goal + ", count=" + count + ", scope=" + scope
				+ ", sign=" + sign + ", warnLimit=" + warnLimit + ", errorLimit=" + errorLimit + ", transmitList="
				+ transmitList + "]";
	}
	
	
}
