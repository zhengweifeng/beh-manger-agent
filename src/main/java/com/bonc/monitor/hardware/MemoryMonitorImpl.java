package com.bonc.monitor.hardware;

import com.bonc.entity.Memory;
import com.bonc.monitor.IMonitor;
import com.bonc.parse.MemoryParse;

import net.sf.json.JSONArray;

public class MemoryMonitorImpl implements IMonitor{

	private static MemoryParse parse ;
	{
		parse = new MemoryParse();
	}
	@Override
	public JSONArray getJsonData() {
		
		Memory mem = parse.readMemory();
		JSONArray array = new JSONArray();
		array.add(mem);
		return array;
	}

}
