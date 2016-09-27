package com.bonc.transmit;

import net.sf.json.JSONObject;

public class MessageExport implements IExport{

	public MessageExport(Transmission tran) {
		
	}

	public  MessageExport() {
		// TODO Auto-generated constructor stub
	}
	
	public void init(Transmission tran){}
	@Override
	public void export(JSONObject json) {
		// TODO Auto-generated method stub
		System.out.println("message :" + json);
	}

}
