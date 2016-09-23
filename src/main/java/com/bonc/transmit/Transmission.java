package com.bonc.transmit;

import java.util.Map;

public class Transmission {

	private String id;
	private String type;
	private long interval;
	private Map<String, String> otherMap;
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
	public Map<String, String> getOtherMap() {
		return otherMap;
	}
	public void setOtherMap(Map<String, String> otherMap) {
		this.otherMap = otherMap;
	}
	
	public long getInterval() {
		return interval;
	}
	public void setInterval(long interval) {
		this.interval = interval;
	}
	@Override
	public String toString() {
		return "Transmission [id=" + id + ", type=" + type + ", interval=" + interval + ", otherMap=" + otherMap + "]";
	}
	
}
