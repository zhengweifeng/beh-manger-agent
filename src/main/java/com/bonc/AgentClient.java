package com.bonc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.bonc.monitor.CpuMonitorImpl;
import com.bonc.monitor.DiskMonitor;
import com.bonc.monitor.IOMonitor;
import com.bonc.monitor.MemoryMonitorImpl;
import com.bonc.monitor.MonitorCenter;
import com.bonc.monitor.NetworkMonitorImpl;
import com.bonc.transmit.TransmissionCenter;
import com.bonc.util.PropertiesUtil;
import com.bonc.util.WarnFactory;
import com.bonc.warn.BooleanRuleExecutor;
import com.bonc.warn.NumberRuleExecutor;
import com.bonc.warn.WarnCenter;

import net.sf.json.JSONObject;

public class AgentClient {

	private static Log log = LogFactory.getLog(AgentClient.class);

	public static void main(String[] args) {
		
		AgentClient client = new AgentClient();
		try {
			log.info("获取参数,并解析参数");
			client.parseArgs(args);
		} catch (Exception e) {
			log.error("解析参数错误: " + e.toString(),e);
		}
		log.info("注册采集服务...");
		MonitorCenter.register("disk", new DiskMonitor());
		MonitorCenter.register("io", new IOMonitor());
		MonitorCenter.register("network", new NetworkMonitorImpl());
		MonitorCenter.register("memory", new MemoryMonitorImpl());
		MonitorCenter.register("cpu", new CpuMonitorImpl());
		log.info("启动采集线程.......");
		LinkedBlockingQueue<JSONObject> queue = new LinkedBlockingQueue<>();
		MonitorCenter center = new MonitorCenter(queue);
		Thread monitorThread = new Thread(center);
		monitorThread.setDaemon(true);
		monitorThread.start();
		
		log.info("启动告警进程.....");
		ArrayBlockingQueue<JSONObject> errorQueue = new ArrayBlockingQueue<>(1000);
		WarnCenter.register("number", new NumberRuleExecutor());
		WarnCenter.register("boolean", new BooleanRuleExecutor());
		WarnCenter warnCenter = new WarnCenter(queue,errorQueue);
		Thread warnThread = new Thread(warnCenter);
		warnThread.setDaemon(true);
		warnThread.start();
		
		log.info("启动告警发送线程....");
		TransmissionCenter tc = new TransmissionCenter(errorQueue);
		Thread tcThread = new Thread(tc);
		tcThread.setDaemon(true);
		tcThread.start();
		try {
			Thread.sleep(Long.MIN_VALUE-1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void parseArgs(String[] args) throws Exception {

		Options opt = new Options();
		opt.addOption("h", "help", false, "list help");
		opt.addOption("c", "conf",true, "set which config path to read ");
		
		HelpFormatter hf = new HelpFormatter();
		hf.setWidth(100);
		CommandLineParser parse = new PosixParser();

		CommandLine line = parse.parse(opt, args);
		if (line.hasOption("h")) {
			hf.printHelp("beh-manager-agent help", opt);
			System.exit(-1);
		}
		if (line.hasOption("c")) {
			String cp = line.getOptionValue("c");
			PropertiesUtil.setConfigPath(cp);
			WarnFactory.init();
			PropertyConfigurator.configure(cp + "/agent-log4j.properties");
		}
	}
	
}
