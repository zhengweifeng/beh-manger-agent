package com.bonc.transmit;

import net.sf.json.JSONObject;

public interface IExport {

	public void export(JSONObject json);
	public void init(Transmission tran);
}
