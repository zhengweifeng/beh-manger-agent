package com.bonc.entity;

public class Network {
	/**
	 * 网卡名称
	 */
	private String name;
	/**
	 * 接收大小
	 * byte
	 */
	private String receiveSize;
	/**
	 * 接收包数
	 */
	private String receivePacket;
	private String receiveErrs;
	private String receiveDrop;
	private String sendSize;
	private String sendPacket;
	private String sendErrs;
	private String sendDrop;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReceiveSize() {
		return receiveSize;
	}
	public void setReceiveSize(String receiveSize) {
		this.receiveSize = receiveSize;
	}
	public String getReceivePacket() {
		return receivePacket;
	}
	public void setReceivePacket(String receivePacket) {
		this.receivePacket = receivePacket;
	}
	public String getReceiveErrs() {
		return receiveErrs;
	}
	public void setReceiveErrs(String receiveErrs) {
		this.receiveErrs = receiveErrs;
	}
	public String getReceiveDrop() {
		return receiveDrop;
	}
	public void setReceiveDrop(String receiveDrop) {
		this.receiveDrop = receiveDrop;
	}
	public String getSendSize() {
		return sendSize;
	}
	public void setSendSize(String sendSize) {
		this.sendSize = sendSize;
	}
	public String getSendPacket() {
		return sendPacket;
	}
	public void setSendPacket(String sendPacket) {
		this.sendPacket = sendPacket;
	}
	public String getSendErrs() {
		return sendErrs;
	}
	public void setSendErrs(String sendErrs) {
		this.sendErrs = sendErrs;
	}
	public String getSendDrop() {
		return sendDrop;
	}
	public void setSendDrop(String sendDrop) {
		this.sendDrop = sendDrop;
	}
	
	
	
}
