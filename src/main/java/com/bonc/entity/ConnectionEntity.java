package com.bonc.entity;

public class ConnectionEntity {

	private String id;
	private String type;
	private String url;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "ConnectionEntity [id=" + id + ", type=" + type + ", url=" + url + "]";
	}
	
	
}
