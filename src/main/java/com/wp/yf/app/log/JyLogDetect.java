package com.wp.yf.app.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 服务版探针： 当前版本号 1.0.02
 * 
 * 1.0.01：使用&lt;和&gt;替换字符串数据中的尖括号"<"、">"那样就可以在htm中以文本的形式显示尖括号
 * 1.0.02：把每条数据分行写入htm文件(为探针过滤做铺垫) 1. 每条数据末尾追加"\r\n"换行字符，便于按行对每条数据进行过滤 2.
 * 添加System1类，使用System1.out.println(String)打印字符串
 * 注：System1类的引入，会导致System1.out.println()方法的调用文件和行的判断不准确，需要修正 3.
 * append(DataType, String, Object)和send(DataType, String, Object)方法
 * 加同步标志，因为两个方法有颜色设置和恢复动作，如果不使用同步处理可能会造成颜色无法恢复。 4.
 * 在每条探针数据的日期和文件名中间添加[ip:sessionx]字段，用来作为jsp的轨迹追踪过滤条件 5.
 * 添加文件分割功能，当探针文件存放两天后将前半部分(按体积容量)分割出来单独文件并以日期后缀备份起来，
 * 
 * @author Administrator
 *
 */
public class JyLogDetect {

	// 日志文本处理相关
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
	private static SimpleDateFormat dfback = new SimpleDateFormat("yyyyMMdd-HHmmss");
	private static StringBuffer totalString = new StringBuffer(); // 所有累加字符串数据
	private static StringBuffer lineString = new StringBuffer(); // 一条字符串数据
	
	private File dirLogs; // 探针文件所在的目录
	private File logFile; // 探针文件
	private Date startDate;
	private DataType enumDataType = DataType.noType;
	private String dataType = "notype"; // 数据类型,默认为无类型
	
	// 关键字粉红色高亮显示
	private static Set<String> keyWords = new HashSet<String>(); // 存放关键字
	
	// 不同的sessionID对应一个不同的index号
	private static int sessionIndex = 1;
	private static Map<String, String> sessionMap = new HashMap<String, String>();
	private HttpServletRequest request;

	/**
	 * 探针初始化 userId: 调用者的id号 file: 探针文件存放的路径,包括文件名
	 */
	public JyLogDetect() {

		// 获取webcontent路径
		String path = this.getClass().getResource("/").getFile();
		File file1 = new File(path.substring(1, path.length() - 1));
		File file2 = new File(new File(file1.getParent()).getParent());
		dirLogs = new File(file2, "logs");
		boolean bNew = false;
		// 检测Logs目录，没有就新建
		if (!dirLogs.exists()) {
			dirLogs.mkdir();
		}
		// 检测日志文件是否存在，没有就新建
		logFile = new File(dirLogs, "log1.htm");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
				bNew = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 初始化写文件对象
		try {
			if (bNew) { // log.htm文件是否新建
				try (BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(logFile, true), "UTF-8"))) {
					writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n"); // 写入UTF-8头
					writer.flush();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JyLogDetect(HttpServletRequest req) {
		this();
		request = req;
	}

	/**
	 * 将对象转化为字符串存入totalString 可以识别数组、ArrayList、HashMap、String、int等基本类型
	 * 和泛型的组合，对于自定义类型需要程序员自己实现toString()方法
	 * 
	 * @param obj
	 */
	private void recursion(Object obj) {

		if (obj == null) {
			lineString.append("null");
			return;
		}
		// String类型
		if (obj instanceof String) {
			lineString.append(((String) obj).replaceAll("<", "&lt;").replaceAll(">", "&gt;")); // 将字符串中的加括号替换成html中可显示的形式
			// int类型
		} else if (obj instanceof Integer) {
			lineString.append(obj);
			// 数组类型
		} else if (obj.getClass().isArray()) {
			int length = Array.getLength(obj);
			lineString.append("[");
			for (int i = 0; i < length; i++) {
				recursion(Array.get(obj, i));

				if (i < length - 1) {
					lineString.append(",");
				}
			}
			lineString.append("]");
			// ArrayList类型
		} else if (obj instanceof ArrayList) {
			ArrayList<?> arrayList = (ArrayList<?>) obj;
			int length = arrayList.size();
			lineString.append("[");
			for (int i = 0; i < length; i++) {
				recursion(arrayList.get(i));
				if (i < length - 1) {
					lineString.append(",");
				}
			}
			lineString.append("]");
			// HashMap类型
		} else if (obj instanceof HashMap) {
			HashMap<?, ?> map = (HashMap<?, ?>) obj;
			int length = map.size();
			int index = 0;
			lineString.append("[");
			for (Object key : map.keySet()) {
				index++;
				lineString.append("(");
				recursion(key);
				lineString.append(",");
				recursion(map.get(key));

				if (index < length) {
					lineString.append("),");
				} else {
					lineString.append(")");
				}
			}
			lineString.append("]");
			// 异常类型
		} else if (obj instanceof Exception) {
			lineString.append(getEinfo((Exception) obj));
			// 其他自定义类型，需要程序员重写toString()方法
		} else {
			lineString.append(obj.toString());
		}
	}

	private String getObjString(Object obj) {
		recursion(obj);

		String returnString = lineString.toString();
		lineString.setLength(0); // 清空lineString

		return returnString;
	}

	/**
	 * 获取异常的字符串信息
	 * 
	 * @param ex 异常对象
	 * @return 返回异常字符串
	 */
	private String getEinfo(Exception ex) {
		String sOut = "";
		sOut += ex.getClass() + "\t" + ex.getMessage() + "<br>";
		StackTraceElement[] trace = ex.getStackTrace();
		for (StackTraceElement s : trace) {
			sOut += "\tat " + s + "<br>";
		}
		return sOut;
	}

	/**
	 * 探针数据累加到totalString
	 * 
	 * @param userID 程序员工号
	 * @param tag    此条日志前缀(程序员自定义解释性字符串)
	 * @param obj    一条探针数据的实际内容
	 */
	public void append(String userID, String tag, Object obj) {

		boolean bContain = false;
		// ---------------------------------------
		// 识别当前类名和行号
		int stackDeepth = Thread.currentThread().getStackTrace().length; // 获取调用栈深度
		int stackIndex = 0;
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		for (int i = 1; i <= stackDeepth; i++) {
			String filename = elements[stackDeepth - i].getFileName();
			if (filename != null && (filename.equals("JyLogDetect.java") || filename.equals("System1.java"))) { // 有些情况filename为null
				stackIndex = i - 1;
				break;
			}
		}
		String className = Thread.currentThread().getStackTrace()[stackDeepth - stackIndex].getFileName().split("\\.")[0];
		int lineNum = Thread.currentThread().getStackTrace()[stackDeepth - stackIndex].getLineNumber();

		// totalString是静态的，多对象共享，在追加数据要同步进行，否则在多线程的情况下会发生数据混乱
		synchronized (JyLogDetect.class) {
			// 获取对象解析的字符串
			String strLine = getObjString(obj);
			// 进行关键字比对
			for (String str : keyWords) {
				if (strLine.contains(str)) {
					bContain = true;
					strLine = strLine.replaceAll(str, "<font color=\"blue\">" + str + "</font>"); // 将关键字添加背景蓝色标签
					break;
				}
			}

			// 获取IP和session相关
			String strIp = null;
			String strSession = null;
			String sessionid = null;
			if (request != null) {
				strSession = null;
				sessionid = request.getSession().getId();
				// 获取IP地址
				try {
					strIp = getIpAddress(request);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 获取session
				if (sessionMap.containsKey(sessionid)) {
					strSession = sessionMap.get(sessionid);
				} else {
					strSession = "session" + (sessionIndex++);
					sessionMap.put(sessionid, strSession);
				}
			}

			// 如果包含关键字则粉红色显示
			if (bContain) {
				if (request != null) {
					totalString.append("<font color=\"#f31af3\">" + df.format(new Date()) + ": " + "[" + strIp + ":"
							+ strSession + "] " + "[" + userID + "-" + className + "-" + lineNum + "] " + tag);
				} else {
					totalString.append("<font color=\"#f31af3\">" + df.format(new Date()) + ": " + "[" + userID + "-"
							+ className + "-" + lineNum + "] " + tag);
				}
			} else {
				if (request != null) {
					totalString.append("<font color=\"" + dataType + "\">" + df.format(new Date()) + ": " + "[" + strIp
							+ ":" + strSession + "] " + "[" + userID + "-" + className + "-" + lineNum + "] " + tag);
				} else {
					totalString.append("<font color=\"" + dataType + "\">" + df.format(new Date()) + ": " + "[" + userID
							+ "-" + className + "-" + lineNum + "] " + tag);
				}
			}
			totalString.append(strLine);
			totalString.append("</font><br>\r\n");
		}
	}

	/**
	 * 探针数据累加到totalString
	 * 
	 * @param request HttpServletRequest对象，用来获取IP和session信息
	 * @param userID  程序员工号
	 * @param tag     此条日志前缀(程序员自定义解释性字符串)
	 * @param obj     一条探针数据的实际内容
	 */
	public void append(HttpServletRequest request, String userID, String tag, Object obj) {
		this.request = request;
		append(userID, tag, obj);
	}

	/**
	 * 将探针数据缓存和当前对象内容写入相应的htm文件
	 * 
	 * @param userID 程序员工号
	 * @param tag    此条日志前缀(程序员自定义解释性字符串)
	 * @param obj    一条探针数据的实际内容
	 */
	public void send(String userID, String tag, Object obj) {
		append(userID, tag, obj);
		flush();
	}

	/**
	 * 将探针数据缓存和当前对象内容写入相应的htm文件
	 * 
	 * @param request HttpServletRequest对象,用来获取IP和session信息
	 * @param userID  程序员工号
	 * @param tag     此条日志前缀(程序员自定义解释性字符串)
	 * @param obj     一条探针数据的实际内容
	 */
	public void send(HttpServletRequest request, String userID, String tag, Object obj) {
		append(request, userID, tag, obj);
		flush();
	}

	/**
	 * 探针数据累加到totalString
	 * 
	 * @param type   数据类型，用来设置文本颜色
	 * @param userID 程序员工号
	 * @param tag    此条日志前缀(程序员自定义解释性字符串)
	 * @param obj    一条探针数据的实际内容
	 */
	public synchronized void append(DataType type, String userID, String tag, Object obj) {
		DataType oldType = setDataType(type);
		append(userID, tag, obj);
		setDataType(oldType);
	}

	/**
	 * 将探针数据缓存和当前对象内容写入相应的htm文件
	 * 
	 * @param type   数据类型，用来设置文本颜色
	 * @param userID 程序员工号
	 * @param tag    此条日志前缀(程序员自定义解释性字符串)
	 * @param obj    一条探针数据的实际内容
	 */
	public synchronized void send(DataType type, String userID, String tag, Object obj) {
		DataType oldType = setDataType(type);
		send(userID, tag, obj);
		setDataType(oldType);
	}

	/**
	 * 将探针数据缓存和当前对象内容写入相应的htm文件
	 * 
	 * @param request HttpServletRequest对象,用来获取IP和session信息
	 * @param type    数据类型，用来设置文本颜色
	 * @param userID  程序员工号
	 * @param tag     此条日志前缀(程序员自定义解释性字符串)
	 * @param obj     一条探针数据的实际内容
	 */
	public synchronized void send(HttpServletRequest request, DataType type, String userID, String tag, Object obj) {
		DataType oldType = setDataType(type);
		append(request, userID, tag, obj);
		setDataType(oldType);
		flush();
	}

	/**
	 * 用于将totalString中的内容刷新到对应htm文件 如果totalString为空则无操作
	 */
	public synchronized void flush() {
		if (totalString.length() != 0) {
			// 写入文件和清空totalString缓存必须同步
			// 因为totalString是静态的，防止多对象竞争
			// 必须用LogDetect.clas做同步对象
			synchronized (JyLogDetect.class) {
				try {
					try (BufferedWriter writer = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(logFile, true), "UTF-8"))) {
						writer.write(totalString.toString());
						writer.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				totalString.setLength(0); // 写入成功则清空字符串缓存

				// 文件分割功能
				startDate = readDate(logFile);
				if (startDate == null) {
					startDate = new Date();
				}
				try {
					cutLogFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 探针文件切割：探针数据积累了两天后按体积分割一半到备份文件，备份文件以分割的日期作为后缀
	 * 
	 * @throws IOException
	 */
	private void cutLogFile() throws IOException {

		// 文件容量小于100K就不分割了
		if ((logFile.length() > 102400) && ((new Date().getTime() - startDate.getTime()) > new Long(172800000))) { // 2*24*60*60*1000
																													// 两天
			// TODO 分割完文件注意更新startDate
			Long size = logFile.length();
			Long backupSize = new Long(0);
			String strLine;
			// 准备写备份文件
			File fileBackup = new File(dirLogs, "log1_" + dfback.format(new Date()) + ".htm");
			if (!fileBackup.exists()) {
				fileBackup.createNewFile();
			}
			OutputStreamWriter oswBackup = new OutputStreamWriter(new FileOutputStream(fileBackup), "utf-8");
			// 准备写临时文件
			File fileTmp = new File(dirLogs, "tempfile$$$$");
			if (!fileTmp.exists()) {
				fileTmp.createNewFile();
			}
			OutputStreamWriter oswTemp = new OutputStreamWriter(new FileOutputStream(fileTmp), "utf-8");
			oswTemp.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n"); // 临时文件写入<meta>头

			// 开始读探针文件
			FileInputStream fis = new FileInputStream(logFile);
			InputStreamReader isr = new InputStreamReader(fis, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			while ((strLine = br.readLine()) != null) {
				// 如果备份文件已经写入一半的探针文件体积则开始写临时文件
				if (backupSize < size / 2) {
					oswBackup.write(strLine + "\r\n");
					backupSize += strLine.length();
				} else {
					oswTemp.write(strLine + "\r\n");
				}
			}
			// 文件关闭
			oswBackup.flush();
			oswBackup.close();
			oswTemp.flush();
			oswTemp.close();
			br.close();
			isr.close();
			fis.close();
			// 删除探针文件
			logFile.delete();
			// 重命名临时文件
			fileTmp.renameTo(new File(dirLogs, "log1.htm"));
			// 恢复发生变动的成员变量
			logFile = new File(dirLogs, "log1.htm");
		}
	}

	/**
	 * 获取探针文件的起始日期
	 * 
	 * @param file
	 * @return
	 */
	private Date readDate(File file) {
		try {
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(logFile), "utf-8"))) {
				Pattern pattern = Pattern.compile("^\\<.*\\>([0-9]{4})\\-([0-9]{2})\\-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})");
				String strLine = null;
				while ((strLine = reader.readLine()) != null) {
					Matcher matcher = pattern.matcher(strLine);
					if (matcher.find()) {
						Calendar calendar = Calendar.getInstance();
						calendar.set(Integer.parseInt(matcher.group(1)) - 1900, // 年
								Integer.parseInt(matcher.group(2)) - 1, // 月
								Integer.parseInt(matcher.group(3)), // 日
								Integer.parseInt(matcher.group(4)), // 时
								Integer.parseInt(matcher.group(5)), // 分
								Integer.parseInt(matcher.group(6)));
						return calendar.getTime(); // 秒
					}
				}
			}
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private String getIpAddress(HttpServletRequest request) throws IOException {
		// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

		String ip = request.getHeader("X-Forwarded-For");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}

	public DataType getDataType() {
		return enumDataType;
	}

	public DataType setDataType(DataType type) {
		DataType oldType = enumDataType;
		enumDataType = type;
		switch (type) {
		case noType: // 普通
			dataType = "black";
			break;
		case basicType: // 基本数据类型
			dataType = "blue";
			break;
		case nonbasicType: // 非基本数据类型
			dataType = "#bbbb00";
			break;
		case exceptionType: // 异常类型
			dataType = "red";
			break;
		case specialType: // 自定义特殊标志
			dataType = "green";
			break;
		}

		return oldType;
	}

	public static void addKeyWord(String word) {
		keyWords.add(word);
	}

	public static void removeKeyWord(String word) {
		keyWords.remove(word);
	}

	public enum DataType {
		noType, // 普通数据
		basicType, // 基本数据类型
		nonbasicType, // 非基本数据类型
		exceptionType, // 异常数据类型
		specialType // 特殊标志
	}
}