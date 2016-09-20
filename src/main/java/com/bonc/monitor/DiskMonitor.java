package com.bonc.monitor;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.entity.Disk;
import com.bonc.parse.DiskParse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DiskMonitor implements IMonitor{
	private Log log = LogFactory.getLog(IOMonitor.class);
	private static DiskParse parse;
	{
		parse = new DiskParse();
	}
	@Override
	public JSONArray getJsonData() {
		DiskParse diskParse = new DiskParse();
	 	List<Disk> list = diskParse.getDiskStatus();
	 	JSONArray json = new JSONArray();
	 	for(Disk disk : list) {
	 		JSONObject obj = new JSONObject();
	 		obj.put("type",disk.getType());
	 		obj.put("name", disk.getName());
	 		obj.put("mount", disk.getMount());
	 		obj.put("owner", disk.getOwner());
	 		obj.put("size", disk.getSize());
	 		obj.put("used", disk.getUsed());
	 		json.add(obj);
	 	}
		return json;
	}
	

	
	
}
