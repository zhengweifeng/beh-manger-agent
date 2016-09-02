package com.bonc.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.bonc.entity.Memory;

public class MemoryParse {

	private String memoryFile = "/proc/meminfo";
	
	public static void main(String[] args) {
		MemoryParse parse = new MemoryParse();
		System.out.println(parse.readMemory());
	}
	
	public Memory readMemory(){
		
		File  file = new File(memoryFile);
		BufferedReader reader = null;
		Memory me = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			me = paresMemory(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(me);
		return me;
	}
	
	public Memory paresMemory(BufferedReader reader) throws IOException {
		
		String line = null;
		Memory me = new Memory();
		while((line = reader.readLine()) != null) {
			String[] kv = line.split(":");
			
			Long num = Long.parseLong(kv[1].trim().split(" ")[0].trim());
			if(kv[0].trim().equals("MemTotal")){
        		me.setMemTotal(num);
        	}else if(kv[0].trim().equals("MemFree")){
        		me.setMemFree(num);
        	}else if(kv[0].trim().equals("Buffers")){
        		me.setBuffers(num);
        	}else if(kv[0].trim().equals("Cached")){
        		me.setCached(num);
        	}else if(kv[0].trim().equals("SwapTotal")){
        		me.setSwapTotal(num);
        	}else if(kv[0].trim().equals("SwapFree")){
        		me.setSwapFree(num);
        	}
		}

		me.setMemUsed(me.getMemTotal() - me.getMemFree() - me.getBuffers() - me.getCached());
		return me;
	}
	
}
