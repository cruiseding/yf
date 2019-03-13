package com.wp.yf.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JyClusterSync {

	/**
	 * 同步图片
	 */
	public static void syncImages() {
		Runtime rt = Runtime.getRuntime();
		String cmd = "/data/wwwroot/qrcode-cluster.sh syncimg";
		try {
			Process proc = rt.exec(cmd);
			proc.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			String result = sb.toString();
			System.out.println("--------- 同步图片 ---------");
			System.out.println(result);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void syncModal() {
		Runtime rt = Runtime.getRuntime();
		String cmd = "/data/wwwroot/qrcode-cluster.sh syncmodal";
		try {
			Process proc = rt.exec(cmd);
			proc.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			String result = sb.toString();
			System.out.println("--------- 同步模板 ---------");
			System.out.println(result);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void syncJsp() {
		Runtime rt = Runtime.getRuntime();
		String cmd = "/data/wwwroot/qrcode-cluster.sh syncjsp";
		try {
			Process proc = rt.exec(cmd);
			proc.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			String result = sb.toString();
			System.out.println("--------- 同步JSP页面 ---------");
			System.out.println(result);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}
}
