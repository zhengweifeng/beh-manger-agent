package com.bonc.monitor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.entity.IO;
import com.bonc.parse.DiskParse;

import net.sf.json.JSONArray;

public class IOMonitor implements IMonitor{
	
	private Log log = LogFactory.getLog(IOMonitor.class);
	private static DiskParse parse;
	{
		parse = new DiskParse();
	}
	@Override
	public JSONArray getJsonData() {
		
		List<IO> list = null;
		try {
			list = parse.getDiskIO();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
		}
	 	JSONArray array = new JSONArray();
	 	for(IO io : list) {
	 		array.add(io);
	 	}
		return array;
	}

}
