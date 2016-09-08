package com.bonc;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import com.bonc.entity.CpuMonitor;
import com.bonc.entity.Disk;
import com.bonc.entity.IO;
import com.bonc.entity.Memory;
import com.bonc.entity.NetworkMonitor;
import com.bonc.entity.WarnEntity;
import com.bonc.export.ExportInterface;

/**
 * 产生告警信息
 * @author zwf
 *
 */
public class WarningServer implements Runnable {

	/**
	 * 告警规则列表
	 */
	private Map<String, Object> map ;
	
	/**
	 * interface 数据发送接口,或者列表
	 */
	private List<ExportInterface> exports;
	/**
	 * 数据传输队列
	 */
	private ArrayBlockingQueue<Object> queue;
	/**
	 * 需要输出的map对象
	 */
	private Map<String ,WarnEntity> outputMap;
	public WarningServer(ArrayBlockingQueue<Object> queue,List<ExportInterface> exports) {
		this.queue = queue;
		this.exports = exports;
	}
	
	public void run() {
		while(true){
			try {
				Object obj = queue.take();
				if(obj instanceof ExportInterface) {
					createOneNodeWarning(obj);
				} else {
					createSoftWarning(obj);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public void createOneNodeWarning(Object obj) {
		System.out.println(obj);
		if(obj instanceof Disk) {
			//发送到硬盘监控判断
		} else if(obj instanceof CpuMonitor) {
			//发送到cpu判断
		} else if(obj instanceof IO) {
			//发送到 io判断
		} else if(obj instanceof Memory){
			//判断内存
		} else if(obj instanceof NetworkMonitor) {
			//判断网络
		} else {
			System.out.println("无法判断数据类型");
		}
	}
	
	public void createSoftWarning(Object obj){
		System.out.println(" soft + " + obj);
	}
	
	
}
