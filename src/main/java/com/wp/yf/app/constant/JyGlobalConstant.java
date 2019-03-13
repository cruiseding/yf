package com.wp.yf.app.constant;

public class JyGlobalConstant {

	private static String ipAddr = "120.27.98.128";
	
	//private static String ipAddr = "121.42.136.119";
	private static String dbPort = "3306";
	//private static String dbUser = "wxjd";
	//private static String dbPwd = "wxjd123";
	
	private static String dbBaseName = "aipeng";
	private static String appid = "wx321142214f00fed6";
	private static String appsecret = "fb63cd9b600e652d42c5c59d116f6196";
//	private static String mchid = "1480000072";
//	private static String mchsecret = "QcgLhwvpEe0Z7x6f8fm9fjeQtVga5EvS";

//   private static String dbBaseName = "redpacket";
//	private static String appid = "wx15034b1300905282";
//	private static String appsecret = "82ed6a9e231232bb0c63fcbe645e95e6";
	private static String dbUser = "aipeng";
	private static String dbPwd = "aipeng123";
	
	public static String getAppid() {
		return appid;
	}

	public static void setAppid(String appid) {
		JyGlobalConstant.appid = appid;
	}

	public static String getAppsecret() {
		return appsecret;
	}

	public static void setAppsecret(String appsecret) {
		JyGlobalConstant.appsecret = appsecret;
	}

	//	private static String webPort = "8080";
//	private static String baseWebAddr = "http://"+ipAddr+":"+webPort+"/ssctrl_wx";
	private static String webPort = "3306";
	private static String baseWebAddr = "http://vvchat.vip:"+webPort;
	
//	private static String wxCallbackDomain = "3s.dkys.org";
//	private static String wxCallbackPort = "14364";
	private static String wxCallbackDomain = "121.42.136.119";
	private static String wxCallbackPort = "8087";
	
	
	
	public static String getDbBaseName() {
		return dbBaseName;
	}
	
	public static String getWXCallbackDomain() {
		return wxCallbackDomain;
	}
	
	public static String getWXCallbackPort() {
		return wxCallbackPort;
	}
	
	public static String getIp() {
		return ipAddr;
	}
	
	public static String getDbPort() {
		return dbPort;
	}
	
	public static String getWebPort() {
		return webPort;
	}
	
	public static String getDbBaseAddr() {
		return ipAddr + ":" + dbPort;
	}
	
	public static String getWebBaseAddr() {
		return baseWebAddr;
	}
	
	public static String getDbUser() {
		return dbUser;
	}
	
	public static String getDbPwd() {
		return dbPwd;
	}
}
