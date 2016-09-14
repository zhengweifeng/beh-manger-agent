package com.bonc.test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class FileTest {

	public static void main(String[] args) {

		Path myDir = Paths.get("/Users/zwf/","config","aa.log");
		
		try {
			WatchService watcher = myDir.getFileSystem().newWatchService();
			
			myDir.register(watcher,StandardWatchEventKinds.ENTRY_MODIFY);
		
			WatchKey watckKey = watcher.take();
			while(true){
				List<WatchEvent<?>> events = watckKey.pollEvents();
				
				for (WatchEvent<?> event : events) {
					if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
						System.out.println("Modify: " + event.context().toString());
						String fileName = event.context().toString();
						if(fileName.equals("aa.log")) {
							
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
	}

}
