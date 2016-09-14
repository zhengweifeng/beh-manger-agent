package com.bonc;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import com.bonc.entity.CpuMonitor;
import com.bonc.entity.Disk;
import com.bonc.entity.IO;
import com.bonc.entity.Memory;
import com.bonc.entity.NetworkMonitor;
import com.bonc.entity.RuleEntity;
import com.bonc.entity.WarnEntity;
import com.bonc.export.ExportInterface;
import com.bonc.rule.DiskRule;
import com.bonc.rule.IRule;
import com.bonc.util.RuleUtil;
import com.bonc.warn.RuleExecutor;

/**
 * 产生告警信息
 * @author zwf
 *
 */
public class WarningServer implements Runnable {
	
	private String hostName;

	/**
	 * 数据传输队列
	 */
	private ArrayBlockingQueue<Object> queue;

	private RuleExecutor execute;
	public WarningServer(ArrayBlockingQueue<Object> queue,String hostName) {
		this.queue = queue;
		this.hostName = hostName;
		execute = new RuleExecutor();
	}
	
	public void run() {
		
		while(true){
			try {
				Object obj = queue.take();
				System.out.println("数据产生队列累集数据量:" + queue.size());
				System.out.println(obj);
				
				if(obj instanceof Disk) {
					//发送到硬盘监控判断
					execute.execute(new DiskRule(), obj, RuleUtil.getRuleMap("disk"), "disk_used");
				} else if(obj instanceof CpuMonitor) {
					//发送到cpu判断
					execute.execute(new IRule() {
						@Override
						public float execute(Object obj) {
							CpuMonitor cpu = (CpuMonitor)obj;
							return cpu.getUs();
						}
					}, obj, RuleUtil.getRuleMap("cpu"), "cpu_used");
				} else if(obj instanceof IO) {
					//发送到 io判断
					execute.execute(new IRule() {
						@Override
						public float execute(Object obj) {
							IO io = (IO)obj;
							return io.getWriteSpeed();
						}
					}, obj, RuleUtil.getRuleMap("io"), "io_write");
				} else if(obj instanceof Memory){
					//判断内存
					execute.execute(new IRule() {
						@Override
						public float execute(Object obj) {
							Memory mem = (Memory)obj;
							return mem.getMemUsed()/mem.getMemTotal();
						}
					}, obj, RuleUtil.getRuleMap("memory"), "memory_used");
				} else if(obj instanceof NetworkMonitor) {
					//判断网络
					execute.execute(new IRule() {
						@Override
						public float execute(Object obj) {
							NetworkMonitor net = (NetworkMonitor)obj;
							return net.getDownSpeed();
						}
					}, obj, RuleUtil.getRuleMap("network"), "network_down");
				} else {
					createSoftWarning(obj);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void createSoftWarning(Object obj){
		System.out.println(" soft + " + obj);
	}
	
	
}
