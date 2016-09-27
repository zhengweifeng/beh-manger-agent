package com.bonc.transmit;

import net.sf.json.JSONObject;

public class HTTPExport implements IExport{

	public HTTPExport(Transmission tran) {
		// TODO Auto-generated constructor stub
	}

	public  HTTPExport() {
		// TODO Auto-generated constructor stub
	}
	public void init(Transmission tran) {}
	@Override
	public void export(JSONObject json) {
		// TODO Auto-generated method stub
		System.out.println("http : " + json);
	}

}
