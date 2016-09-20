package com.bonc.transmit;

import net.sf.json.JSONObject;

public class MessageExport implements IExport{

	public MessageExport(Transmission tran) {
		
	}

	@Override
	public void export(JSONObject json) {
		// TODO Auto-generated method stub
		System.out.println("message :" + json);
	}

}
