package com.bonc.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class PathWatcher  implements Runnable{

	private String path;
	private List<String> fileList;
	public PathWatcher(String path,List<String> fileList){
		this.path = path;
		this.fileList = fileList;
	}
	
	public void run() {
		
		Path myDir = Paths.get(path);
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
						if(fileList.contains(fileName)) {
							if(fileName.equals(ConfigurationUtil.fn)) {
								ConfigurationUtil.init();
							} else if(fileName.equals(RuleUtil.fn)) {
								ConfigurationUtil.init();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
		
		
	}

}
