package com.bonc.monitor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bonc.parse.CpuParse;

import net.sf.json.JSONArray;

public class CpuMonitorImpl implements IMonitor {

	private Log log = LogFactory.getLog(CpuMonitorImpl.class);
	private static CpuParse parse;
	{
		parse = new CpuParse();
	}
	@Override
	public JSONArray getJsonData() {
		JSONArray array = new JSONArray();
		array.add(parse.getCpuMonitor());
		return array;
	}

}
