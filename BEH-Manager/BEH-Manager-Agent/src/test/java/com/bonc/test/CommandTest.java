package com.bonc.test;

import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import com.bonc.entity.Network;

public class CommandTest {
	public static void main(String[] args) throws Exception {
		String line = " lo:2607992753 16346184    0    0    0     0          0         0 2607992753 16346184    0    0    0     0       0          0";
		String arr[] = line.trim().split(" +");
		if(arr.length <= 16) {
			arr = line.trim().split("\\s+|:");
		}
		System.out.println(arr[0] + " length:" + arr.length);
		Options options = new Options();
		// true 需要带参数，false 不用参数
		options.addOption("t", true, "display current time");// 参数不可用
		options.addOption("p", true, "person owner");// 参数可用
		options.addOption("h", false, "help");// 参数可用
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);
		if (cmd.hasOption("p")) {
			String code = cmd.getOptionValue("p");
			System.out.println("输入参数 p：" + code);
		}
		if (cmd.hasOption("t")) {
			String code = cmd.getOptionValue("t");
			System.out.println("输入参数t:" + code + "," + new Date());
		}
		if (cmd.hasOption("h")) {
			System.out.println("输入参数h：帮助：[-t][-c][-h]");
		}

	}
}
