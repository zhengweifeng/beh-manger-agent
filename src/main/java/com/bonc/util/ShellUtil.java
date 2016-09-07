package com.bonc.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class ShellUtil {

	private static long interval = 1000L;
	private static int timeout = 3000;
	private JSch jsch = null;
	private Session session = null;

	public ShellUtil() {
	}

	public ShellUtil(String host, String username, String passwd) {
		createSession(host, username, passwd);
	}

	public void createSession(String host, String username, String passwd) {
		jsch = new JSch();
		//System.out.println("create session : " + host + " " + username + " " + passwd) ;
		try {
			session = jsch.getSession(username, host, 22);
			session.setPassword(passwd);
			session.setConfig("StrictHostKeyChecking", "no");
			UserInfo ui = new MyUserInfo() {
				public void showMessage(String message) {
					JOptionPane.showMessageDialog(null, message);
				}

				public boolean promptYesNo(String message) {
					Object[] options = { "yes", "no" };
					int foo = JOptionPane.showOptionDialog(null, message, "Warning", JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE, null, options, options[0]);
					return foo == 0;
				}
			};

			session.setUserInfo(ui);
			session.connect();

		} catch (JSchException e) {
			System.out.println("create session exeception !");
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		ShellUtil util = new ShellUtil("115.28.90.38", "zhengweifeng", "hadoop");
		// util.createSession("localhost", "zwf", "zwf");
		try {
			List<String> list = util.exec("/bin/lsblk -b");
			for(String s : list) {
				System.out.println(s);
			}
			List<String> list2 = util.exec("/bin/df");
			for(String s : list2) {
				System.out.println(s);
			}
			util.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public long shell(String cmd, String outputFileName) throws Exception {
		long start = System.currentTimeMillis();
		ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
		PipedInputStream pipeIn = new PipedInputStream();
		PipedOutputStream pipeOut = new PipedOutputStream(pipeIn);
		// FileOutputStream fileOut = new FileOutputStream(outputFileName);
		channelShell.setInputStream(pipeIn);
		channelShell.setOutputStream(System.out);
		channelShell.connect(timeout);
		pipeOut.write(cmd.getBytes());

		Thread.sleep(interval);
		pipeOut.close();
		pipeIn.close();
		// fileOut.close();

		channelShell.disconnect();

		return System.currentTimeMillis() - start;
	}

	public List<String> exec(String cmd) throws Exception {
		ChannelExec openChannel = (ChannelExec) session.openChannel("exec");
		//System.out.println(cmd);
		openChannel.setCommand(cmd);
		//int exitStatus = openChannel.getExitStatus();
		//System.out.println(exitStatus);
		openChannel.connect();
		InputStream in = openChannel.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String buf = null;
		List<String> list = new ArrayList<String>();
		while ((buf = reader.readLine()) != null) {
			list.add(buf);
		}
		//System.out.println(buf.toString());
		openChannel.disconnect();
		return list;
	}

	public void close() {
		session.disconnect();
	}

	public static abstract class MyUserInfo implements UserInfo, UIKeyboardInteractive {
		public String getPassword() {
			return null;
		}

		public boolean promptYesNo(String str) {
			return false;
		}

		public String getPassphrase() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return false;
		}

		public boolean promptPassword(String message) {
			return false;
		}

		public void showMessage(String message) {
		}

		public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt,
				boolean[] echo) {
			return null;
		}
	}
}
