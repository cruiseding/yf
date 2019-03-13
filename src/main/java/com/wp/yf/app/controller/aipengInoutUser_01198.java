package com.wp.yf.app.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.net.aipeng.classroot.interface1.mA.aipengInOutFace;
import com.net.aipeng.classroot.interface1.mA.aipengInOutManager;
import com.net.aipeng.classroot.interface2.mA.aipengSqlMFace;
import com.net.aipeng.classroot.interface4.JsonUtil;
import com.net.aipeng.classroot.interface4.JyLogDetect.DataType;
import com.net.aipeng.redirect.resolverA.interface2.aipengSqlUser_01198;




public class aipengInoutUser_01198  extends aipengInOutManager implements
aipengInOutFace {
	protected ArrayList<Map<String, Object>> list;
	protected ArrayList<Map<String, Object>> list1;
	protected ArrayList<Map<String, Object>> list2;
	protected String json = "";
	aipengSqlMFace sqlmface = new UserRegisterDao();
	
	public aipengInoutUser_01198(String[] arg, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SQLException {
		super(arg, request, response);
	}

	@Override
	public void addface() throws SQLException, ServletException, IOException {
		switch (arg[1]) {
		//用户注册
		case "user_register"://180910创建
			user_register(arg);
			break;
		}
	}

	/**
	 * 用户注册------01198
	 * arg[2]		phone
	 * arg[3]		user_photo
	 * arg[4]		nickname
	 * arg[5]		age
	 * arg[6]		address
	 * arg[7]		gender
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void user_register(String[] arg) throws SQLException, ServletException, IOException {
		log.send(DataType.basicType, "01198", "用户注册-arg:", arg);
		if("".equals(arg[10])){
			String sql = sqlmface.addSqlface(0,arg);
			log.send(DataType.basicType, "01198", "用户注册-sql:", sql);
			int success = sqlUtil.sqlExecute(sql);
			log.send(DataType.basicType, "01198", "用户注册-success:", success);
			String yqcode=arg[8];
			String channelcode=arg[9];
			String tmppath = request.getSession().getServletContext()
		            .getRealPath("/");
			if(success==1){
				String sql1 = sqlmface.addSqlface(1,arg);
				log.send(DataType.basicType, "01198", "用户id-sql:", sql);
				list = sqlUtil.getList(sql1);
				log.send(DataType.basicType, "01198", "用户id-success:", success);
				String user_id= list.get(0).get("id").toString();
				ShareAgentInfo.registerinfo(1,user_id,yqcode,channelcode,"0.5",tmppath,"app.quyuanapp.com");
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			}else{
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}
		}else{
			String sql = sqlmface.addSqlface(2,arg);
			log.send(DataType.basicType, "01198", "更改-sql:", sql);
			int success1 = sqlUtil.sqlExecute(sql);
			log.send(DataType.basicType, "01198", "更改-success1:", success1);
			if(success1 ==1){
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			}else{
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}
			
		}
	}

	@Override
	public void modface() throws SQLException, ServletException, IOException {
		switch (arg[1]) {
		
		}
	}
	
	

	@Override
	public void deleteface() throws SQLException, ServletException, IOException {
		switch (arg[1]) {
			
		}
	}



	@Override
	public void searchface() throws SQLException, ServletException, IOException {
		switch(arg[1]){
		//查询用户基本信息
		case "search_basic_personal_details"://180902创建
			search_basic_personal_details(arg);
			break;
			
		case "cashdraw_dou"://180902创建
			cashdraw_dou(arg);
			break;
			
		//查询是否关注了
		case "search_is_attention"://180907创建
			search_is_attention(arg);
			break;
		//查询他人详情页聊天价格信息
		case "search_chat_price_details"://180909创建
			search_chat_price_details(arg);
			break;
		//根据手机号查询用户信息
		case "search_info_by_phone"://180910创建
			search_info_by_phone(arg);
			break;	
		//获取短信验证码
		case "get_message_info"://180910创建
			get_message_info(arg);
			break;
		//查询昵称库
		case "select_name"://180920创建
			select_name(arg);
			break;	
		//查询拨打着余额与被拨打着状态
		case "search_balance_status"://180926创建
			search_balance_status(arg);
			break;
		//查询昵称是否存在
		case "nickname_is_exit"://180930创建
			nickname_is_exit(arg);
			break;
		}
	}
	/**
	 * 查询昵称是否存在------01198
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void nickname_is_exit(String[] arg) throws SQLException, ServletException, IOException{
		String sql = sqlmface.searchSqlface(0,arg);
		log.send(DataType.basicType, "01198", "查询昵称是否存在-sql:", sql);
		int success = sqlUtil.getInt(sql);
		log.send(DataType.basicType, "01198", "查询昵称是否存在-success:", success);
		if(success >0){
			String jsonadd = "{\"success\":\"0\"}";
			inOutUtil.return_ajax(jsonadd);
		}else{
			String jsonadd = "{\"success\":\"1\"}";
			inOutUtil.return_ajax(jsonadd);
		}
		
	}

	/**
	 * 查询拨打着余额与被拨打着状态
	 * arg[2]	id			拨打着余额
	 * arg[3]	user_id		被拨打着状态
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void search_balance_status(String[] arg) throws SQLException, ServletException, IOException{
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询拨打着余额sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "查询拨打着余额-list",list);
		
		String sql1 = sqlmface.searchSqlface(1, arg);
		log.send(DataType.basicType, "01198", "查询被拨打着状态sql1",sql1);
		list1=sqlUtil.getList(sql1);
		log.send(DataType.basicType, "01198", "查询被拨打着状态-list1",list1);
		
		sql = "select status from blacklist_table where user_id ='"+arg[3]+"' and black_id='"+arg[2]+"'";
		String st=sqlUtil.getString(sql);
		if(st==null || "null".equals(st)|| st.equals("0")){
			st="0";	
		}
		list.get(0).put("isblack",st);
		list.get(0).put("is_online",list1.get(0).get("is_online"));
		log.send(DataType.basicType, "01198", "查询昵称-list",JsonUtil.listToJson(list));
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
		
	}

	/**
	 * 查询昵称库
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void select_name(String[] arg) throws SQLException, ServletException, IOException{
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询昵称sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "查询昵称-list",list);

		inOutUtil.return_ajax(JsonUtil.listToJson(list));
		
	}

	private void get_message_info(String[] arg) throws SQLException, ServletException, IOException{
		//取随机数1000-9999
		int num = (int) ((Math.random()*9000)+1000);
		String jsonadd = "{\"success\":\""+num+"\"}";
		inOutUtil.return_ajax(jsonadd);
	}

	/**
	 * 根据手机号查询用户信息------01198
	 * arg[2]		phone	手机号
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void search_info_by_phone(String[] arg) throws SQLException, ServletException, IOException{
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "根据手机号查询用户信息sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "根据手机号查询用户信息-list",list);

		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}

	/**
	 * 查询他人详情页聊天价格信息------01198
	 * arg[2]		user_id		用户id
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void search_chat_price_details(String[] arg) throws SQLException, ServletException, IOException{
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询他人详情页聊天价格信息sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "查询他人详情页聊天价格信息-list",list);

		inOutUtil.return_ajax(JsonUtil.listToJson(list));
		
	}

	/**
	 * 查询是否关注了------01198
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void search_is_attention(String[] arg) throws SQLException, ServletException, IOException{
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询是否关注了---sql",sql);
		int count = sqlUtil.getInt(sql);
		log.send(DataType.basicType, "01198", "查询是否关注了---count",count);
		if(count==1){
			String sql1 = sqlmface.searchSqlface(1, arg);
			log.send(DataType.basicType, "01198", "查询是否关注了---sql1",sql1);
			list = sqlUtil.getList(sql1);
			log.send(DataType.basicType, "01198", "查询是否关注了---list",list);
			if("".equals(list.get(0).get("user_follow_time"))){
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}else{
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			}
		}else{
			String sql2 = sqlmface.searchSqlface(2, arg);
			log.send(DataType.basicType, "01198", "查询是否关注了---sq2",sql2);
			int count1 = sqlUtil.getInt(sql2);
			log.send(DataType.basicType, "01198", "查询是否关注了---count2",count1);
			if(count1==1){
				String sql3 = sqlmface.searchSqlface(3, arg);
				log.send(DataType.basicType, "01198", "查询是否关注了---sql3",sql3);
				list1 = sqlUtil.getList(sql3);
				log.send(DataType.basicType, "01198", "查询是否关注了---list1",list1);
				if("".equals(list1.get(0).get("target_follow_time"))){
					String jsonadd = "{\"success\":\"0\"}";
					inOutUtil.return_ajax(jsonadd);
				}else{
					String jsonadd = "{\"success\":\"1\"}";
					inOutUtil.return_ajax(jsonadd);
				}
			}else{
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}
		}
	
	}
	
	/**
	 * 查询用户基本信息------01198
	 * arg[2]		id		用户id
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void search_basic_personal_details(String[] arg) throws SQLException, ServletException, IOException {
	
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询用户基本信息sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "查询用户基本信息-list",list);

		if(list.size()>0){
			int a=Integer.parseInt(list.get(0).get("balance").toString());
			int b=Integer.parseInt(list.get(0).get("total_revenue").toString());
			int c=Integer.parseInt(list.get(0).get("total_withdrawals").toString());
			list.get(0).put("able_withdrawals", b-c);
			
			list.get(0).put("my_totlequdou", a+b-c);
		}
		
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
		
	}
	
	private void cashdraw_dou(String[] arg) throws SQLException, ServletException, IOException {
		
		
		int tot=Integer.parseInt(arg[3])*20;
		
		arg[1]="search_basic_personal_details";
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询用户基本信息sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "查询用户基本信息-list",list);
		int a=Integer.parseInt(list.get(0).get("total_revenue").toString());
		int b=Integer.parseInt(list.get(0).get("total_withdrawals").toString());
		if(tot<=(a-b)){
			
			sql="insert into cash_table (user_id,cash,out_biz_no,time) values ('"+arg[2]+"','"+arg[3]+"','"+hm.order_create()+hm.sb()+"',now())";
			sqlUtil.sqlExecute(sql);
			
			sql="update user_data set total_withdrawals=total_withdrawals+"+tot+" where id="+arg[2];
			sqlUtil.sqlExecute(sql);
		}
		
		sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询用户基本信息sql",sql);
		list=sqlUtil.getList(sql);

		if(list.size()>0){
			int a1=Integer.parseInt(list.get(0).get("balance").toString());
			int b1=Integer.parseInt(list.get(0).get("total_revenue").toString());
			int c1=Integer.parseInt(list.get(0).get("total_withdrawals").toString());
			list.get(0).put("able_withdrawals", b1-c1);
		}
		
		
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
		
	}
	
	
	
}
