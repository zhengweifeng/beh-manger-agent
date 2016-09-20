package com.bonc.monitor;

import java.util.List;

import com.bonc.entity.NetworkMonitor;
import com.bonc.parse.NetworkParse;

import net.sf.json.JSONArray;

public class NetworkMonitorImpl implements IMonitor{

	private static NetworkParse parse;
	{
		parse = new NetworkParse();
	}
	
	@Override
	public JSONArray getJsonData() {
		// TODO Auto-generated method stub
		List<NetworkMonitor> list = parse.getNetSpeed();
		JSONArray array = new JSONArray();
		for(NetworkMonitor net : list) {
			array.add(net);
		}
		return array;
	}

	
}
