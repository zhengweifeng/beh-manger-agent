package com.bonc.transmit;

import net.sf.json.JSONObject;

public class MailExport implements IExport{

	public MailExport(Transmission tran) {
		// TODO Auto-generated constructor stub
	}

	public  MailExport() {
		// TODO Auto-generated constructor stub
	}
	public void init(Transmission tran){
		
	}
	
	@Override
	public void export(JSONObject json) {
		// TODO Auto-generated method stub
		System.out.println("mail : " + json);
	}

}
