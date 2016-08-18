package com.bonc.agent;

import org.apache.hadoop.ipc.VersionedProtocol;


public interface IAgent extends VersionedProtocol {
	public static final long versionID=1L;
	
    void stopAgent();
//    String[] readHDFSAuditLog(String path, long starttime, long endtime);
//    String[] readHBASEAuditLog(String path, long starttime, long endtime);
//    String[] readConfiguration(String path);
    String readList(String path);
    void saveList(String path, String configStr);
}
