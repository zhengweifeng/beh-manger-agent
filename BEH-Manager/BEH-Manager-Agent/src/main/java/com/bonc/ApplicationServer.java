package com.bonc;

import com.bonc.parse.CpuParse;
import com.bonc.parse.DiskParse;
import com.bonc.parse.MemoryParse;
import com.bonc.parse.NetworkParse;

public class ApplicationServer {

	public static void main(String[] args) throws Exception {
		
		CpuParse cp = new CpuParse();
		cp.getCpuMonitor();
		
		NetworkParse np = new NetworkParse();
		np.getNetSpeed();
		
		
		MemoryParse mp = new MemoryParse();
		mp.readMemory();
		
		DiskParse dp = new DiskParse();
		dp.getDiskStatus();
		dp.getDiskIO();
		
	}
	
}
