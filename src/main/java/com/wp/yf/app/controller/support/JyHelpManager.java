package com.wp.yf.app.controller.support;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JyHelpManager {
	
	public static int item = 10;// 每页显示多少条
	public static int shouyeitem = 8;
	
	int n = 2;

	// 提取URL中的参数
	// 返回的是字符串形式的参数
	public String getUrlArgStr(String url) {
		String bUrl = url;
		String[] qs = bUrl.split("@");
		String argStr = "";
		if (qs != null) {
			for (int i = 0; i < qs.length; i++) {
				argStr += qs[i].substring(0, qs[i].indexOf("=")) + "=" + qs[i].substring(qs[i].indexOf("=") + 1) + "&";
			}
		}
		return argStr;
	}

	// 翻页 0totlePage：总页数、1totle：总条数、2current：第多少条、3pagenum：当前页数
	public int[] pages(String arg[], int totle) {
		// 当前页
		int pagenum = Integer.parseInt(arg[3].equals("0") ? "1" : arg[3]);
		int current = 0;
		int lastcount = 0;

		if (pagenum != 1) {
			current = (pagenum - 1) * item;
		}
		int totlePage = totle / item;
		int totlePageY = totle % item;
		if (totlePageY != 0) {
			totlePage = totlePage + 1;
		}

		if ((current + item) < totle) {
			lastcount = current + item;
		} else {
			lastcount = totle;
		}

		int[] page = new int[] { totlePage, totle, current, lastcount, pagenum };
		return page;

	}

	public static int[] pages(String startPage, int total) {
		int pagenum = Integer.parseInt(startPage.equals("0") ? "1" : startPage);
		int current = 0;
		int lastcount = 0;

		if (pagenum != 1) {
			current = (pagenum - 1) * item;
		}
		int totlePage = total / item;
		int totlePageY = total % item;
		if (totlePageY != 0) {
			totlePage = totlePage + 1;
		}

		if ((current + item) < total) {
			lastcount = current + item;
		} else {
			lastcount = total;
		}

		int[] page = new int[] { totlePage, total, current, lastcount, pagenum };
		return page;
	}

	public static boolean isImgUp(String str) {
		ArrayList<String> mode1List = new ArrayList<String>();

		mode1List.add("good_add");
		mode1List.add("good_modify");
		mode1List.add("seller_modify");
		mode1List.add("seller_modify_phone");

		return mode1List.contains(str);
	}

	//// 返利计算
	// 参数：rebate_reason：返利基数 rebate_money：返利金额
	public double order_rebate_money(double order_rebate) {
		double order_rebate1 = order_rebate / 100;

		double order_rebate_money = Math.floor(order_rebate1); // 向下取整
		return order_rebate_money;
	}

	// 获取系统当前时间
	public String getSystemTime() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		return time;
	}

	public String order_create() {
		String sdFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		return sdFormat;
	}

	public static String sb() {

		StringBuffer sb = new StringBuffer(4);
		for (int i = 0; i < 4; i++) {
			int n = (int) (Math.random() * 10);
			sb.append(n);
		}
		return sb.toString();
	}

	public static BufferedImage image(String validateCode) {

		BufferedImage image = new BufferedImage(80, 25, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 80, 25);
		g.setColor(Color.BLACK);

		g.drawString(validateCode, 10, 20);
		g.dispose();
		return image;
	}

	// 获取系统当前时间 用于期号
	public String getSystemTime2() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time = format.format(date);
		return time;
	}

}
