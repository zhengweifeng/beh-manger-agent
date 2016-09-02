package com.bonc.entity;

public class Network {
	/**
	 * 网络接口
	 */
	private String name;
	/**
	 * 接收大小
	 * byte
	 */
	private long receiveSize;
	/**
	 * 接收包数
	 */
	private long receivePacket;
	private long receiveErrs;
	private long receiveDrop;
	private long sendSize;
	private long sendPacket;
	private long sendErrs;
	private long sendDrop;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getReceiveSize() {
		return receiveSize;
	}
	public void setReceiveSize(long receiveSize) {
		this.receiveSize = receiveSize;
	}
	public long getReceivePacket() {
		return receivePacket;
	}
	public void setReceivePacket(long receivePacket) {
		this.receivePacket = receivePacket;
	}
	public long getReceiveErrs() {
		return receiveErrs;
	}
	public void setReceiveErrs(long receiveErrs) {
		this.receiveErrs = receiveErrs;
	}
	public long getReceiveDrop() {
		return receiveDrop;
	}
	public void setReceiveDrop(long receiveDrop) {
		this.receiveDrop = receiveDrop;
	}
	public long getSendSize() {
		return sendSize;
	}
	public void setSendSize(long sendSize) {
		this.sendSize = sendSize;
	}
	public long getSendPacket() {
		return sendPacket;
	}
	public void setSendPacket(long sendPacket) {
		this.sendPacket = sendPacket;
	}
	public long getSendErrs() {
		return sendErrs;
	}
	public void setSendErrs(long sendErrs) {
		this.sendErrs = sendErrs;
	}
	public long getSendDrop() {
		return sendDrop;
	}
	public void setSendDrop(long sendDrop) {
		this.sendDrop = sendDrop;
	}
	@Override
	public String toString() {
		return "Network [name=" + name + ", receiveSize=" + receiveSize + ", receivePacket=" + receivePacket
				+ ", receiveErrs=" + receiveErrs + ", receiveDrop=" + receiveDrop + ", sendSize=" + sendSize
				+ ", sendPacket=" + sendPacket + ", sendErrs=" + sendErrs + ", sendDrop=" + sendDrop + "]";
	}
	
	
	
}
