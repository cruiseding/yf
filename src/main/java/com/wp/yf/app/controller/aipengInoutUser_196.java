package com.wp.yf.app.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

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
import com.net.aipeng.classroot.interface4.JyHelpManager;
import com.net.aipeng.classroot.interface4.JyLogDetect.DataType;
import com.net.aipeng.redirect.resolverA.interface2.aipengSqlUser_196;
//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;




public class aipengInoutUser_196  extends aipengInOutManager implements
aipengInOutFace {
	protected ArrayList<Map<String, Object>> list;
	protected ArrayList<Map<String, Object>> list1;
	protected ArrayList<Map<String, Object>> list2;
	protected String json = "";
	aipengSqlMFace sqlmface = new UserChatDao();
	JyHelpManager helpmanager=new JyHelpManager();
	public aipengInoutUser_196(String[] arg, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			SQLException {
		super(arg, request, response);
	}

	@Override
	public void addface() throws SQLException, ServletException, IOException {
		switch (arg[1]) {
		//申请分享提现
				case "withdraw_cash":
					withdraw_cash(arg);
					break;
		case "my_visitor"://180903周一创建
			my_visitor(arg);
			break;
//		case "fangke":
//			fangke(arg);
//			break;
			//身份认证 identity_approve
		case "identity_approve":
			identity_approve(arg);
			break;
			
		case "give_gift":
			give_gift(arg);
			break;
			
		case "add_qiuliao":
			add_qiuliao(arg);
			break;
		}
	}


	@Override
	public void modface() throws SQLException, ServletException, IOException {
		switch (arg[1]) {
		case "is_rob_chat":
			is_rob_chat(arg);
			break;
		case "update_yiqiang":
			update_yiqiang	(arg);
			break;
		case "delete_qiuliao":
			delete_qiuliao	(arg);
			break;
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
		case "seek_chat"://180903周一创建
			seek_chat(arg);
			break;
		case "find_my_visitor"://180903周一创建
			find_my_visitor(arg);
			break;
		case "fangke":
			fangke(arg);
			break;
		case "add_guanzhu":
			add_guanzhu(arg);
			break;	
		case "quxiao_guanzhu":
			quxiao_guanzhu(arg);
			break;
		case "findall_guanzhu":
			findall_guanzhu(arg);
			break;
		case "findall_visitor":
			findall_visitor(arg);
			break;
		case "findall_beiguanzhu":
			findall_beiguanzhu(arg);
			break;
		case "findall_together_guanzhu":
			findall_together_guanzhu(arg);
			break;
		//================================
		case "wholookme":
			wholookme(arg);
			break;
		case "wholikesme":
			wholikesme(arg);
			break;
		case "whoilike":
			whoilike(arg);
			break;
		case "likeeachother":
			likeeachother(arg);
			break;
		case "isone_on_one":
			isone_on_one(arg);
			break;
		case "reward_gift":
			reward_gift(arg);
			break;
			
		case "gift_record":
			gift_record(arg);
			break;
			
		case "find_qiangliao":
			find_qiangliao(arg);
			break;
		}
	}
	private void withdraw_cash(String[] arg) throws SQLException, ServletException, IOException {
		arg[0]=helpmanager.order_create();
		
		String sql = sqlmface.addSqlface(0,arg);
		log.send(DataType.basicType, "196", "提现-sql:", sql);
		int success = sqlUtil.sqlExecute(sql);
		log.send(DataType.basicType, "196", "提现-success:", success);
		
		String sql1 = sqlmface.addSqlface(1,arg);
		log.send(DataType.basicType, "196", "用户表扣除提现金额-sql1:", sql1);
		int success1 = sqlUtil.sqlExecute(sql1);
		log.send(DataType.basicType, "196", "用户表扣除提现金额-success1:", success1);
		
		if(success ==1 && success1==1){
			String jsonadd = "{\"success\":\"1\"}";
			inOutUtil.return_ajax(jsonadd);
		}else{
			String jsonadd = "{\"success\":\"0\"}";
			inOutUtil.return_ajax(jsonadd);
		}
		
	}
	private void gift_record(String[] arg) throws SQLException, IOException,
	ServletException {
	    String sql=sqlmface.searchSqlface(1, arg);
	    log.send(DataType.basicType, "01162", "查看礼物记录-sql", sql);
	    list=sqlUtil.getList(sql);
	    sql=sqlmface.searchSqlface(0, arg);
	    log.send(DataType.basicType, "01162", "查看礼物记录-sql", sql);
	    list1=sqlUtil.getList(sql);
	    for(int i=0;i<list.size();i++){
	    	arg[2]=list.get(i).get("id")+"";
	    	sql=sqlmface.searchSqlface(2, arg);
	    	   log.send(DataType.basicType, "01162", "查看礼物记录-sql", sql);
	    	int b=sqlUtil.getCount(sql);
	    	list.get(i).put("gift_num", b+"");
	    }
	    
	    sql = sqlmface.searchSqlface(3, arg);
	    log.send(DataType.basicType, "01205", "查询主播礼物数量-sql ", sql);
	    int giftNum = sqlUtil.getInt(sql);	    
	    list.get(0).put("gift_amount", giftNum);
	    inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}
	//快撩页面查询抢聊表内容，显示未抢聊
	private void find_qiangliao(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "196", "快撩页面查询抢聊表内容，显示未抢聊-sql",sql);
		list1=sqlUtil.getList(sql);
		log.send(DataType.basicType, "196", "快撩页面查询抢聊表内容，显示未抢聊-list",list1);
		
		ArrayList<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		log.send(DataType.basicType, "196", "快撩页面查询抢聊表内容，显示未抢聊-list",list1.size());
		for(int i=0;i<list1.size();i++){
			log.send(DataType.basicType, "196", "快撩页面查询抢聊表内容，显示未抢聊-list",arg);
			arg[0]=list1.get(i).get("id1").toString();
			
            sql="select count(*) from blacklist_table where (user_id="+arg[2]+" and black_id="+arg[0]+" and status=1) or (user_id="+arg[0]+" and black_id="+arg[2]+" and status=1)";
            log.send(DataType.basicType, "196", "快撩页面查询抢聊表内容，显示未抢聊-sql",sql);
            int isblack=sqlUtil.getCount(sql);
			if(isblack>0){
				
			}else{
				list.add(list1.get(i));
			}
		}
		
		
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}
	//打赏礼物 (聊天界面)
	private void reward_gift(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "196", "打赏礼物（list查询）sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "196", "打赏礼物（list查询）-list",list);
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}
	
//	4。快撩页面,点击抢，进入一对1.将这条记录改为已抢
	private void update_yiqiang(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.modSqlface(0, arg);
		log.send(DataType.basicType, "196", "快撩页面,更改抢聊状态sql",sql);
		//int c=sqlUtil.get_int(sql);
		int success = sqlUtil.sqlExecute(sql);
		log.send(DataType.basicType, "196", "快撩页面,更改抢聊状态-success",success);
		if(success>0){
			String jsonadd = "{\"success\":\"1\"}";
			inOutUtil.return_ajax(jsonadd);
		}else{
			String jsonadd = "{\"success\":\"0\"}";
			inOutUtil.return_ajax(jsonadd);
		}
	}
	
	private void delete_qiuliao(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.modSqlface(0, arg);
		log.send(DataType.basicType, "196", "快撩页面,更改抢聊状态sql",sql);
		//int c=sqlUtil.get_int(sql);
		int success = sqlUtil.sqlExecute(sql);
		log.send(DataType.basicType, "196", "快撩页面,更改抢聊状态-success",success);
		if(success>0){
			String jsonadd = "{\"success\":\"1\"}";
			inOutUtil.return_ajax(jsonadd);
			
			jguang();
			
		}else{
			String jsonadd = "{\"success\":\"0\"}";
			inOutUtil.return_ajax(jsonadd);
		}
	}
	
	
	//更改抢聊状态
	//http://120.27.98.128:9810/uiface/member196?mode=A-user-mod&model=is_rob_chat&user_id=2&zhuangtai=1
	private void is_rob_chat(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.modSqlface(0, arg);
		log.send(DataType.basicType, "196", "更改抢聊状态sql",sql);
		//int c=sqlUtil.get_int(sql);
		int success = sqlUtil.sqlExecute(sql);
		log.send(DataType.basicType, "196", "更改抢聊状态-success",success);
		if(success>0){
			String jsonadd = "{\"success\":\"1\"}";
			inOutUtil.return_ajax(jsonadd);
		}else{
			String jsonadd = "{\"success\":\"0\"}";
			inOutUtil.return_ajax(jsonadd);
		}
	}
	//是否已经进入1对1聊天
	//
	private void isone_on_one(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "196", "是否进入1对一抢聊sql",sql);
		int c=sqlUtil.getInt(sql);
		log.send(DataType.basicType, "196", "是否进入1对一抢聊-list",c);
		if(c>0){
			String jsonadd = "{\"success\":\"1\"}";
			inOutUtil.return_ajax(jsonadd);
		}else{
			
			sql="select count(*) from Ask_Chat where  'status'=0 and  id="+arg[3];
			log.send(DataType.basicType, "196", "是否进入1对一抢聊sql",sql);
			int a=sqlUtil.getCount(sql);
			if(a>0){
				
				sql="update Ask_Chat set status=1 where id='"+arg[3]+"'";
				log.send(DataType.basicType, "196", "是否进入1对一抢聊sql",sql);
				sqlUtil.sqlExecute(sql);
				
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
				
				jguang();
				
			}else{
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			}
			
		}
	}
	//seek_chat 求聊 求聊  查在线女主播
	//
	private void seek_chat(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "196", "求聊  查在线女主播sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "196", "求聊  查在线女主播-list",list);
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}
	//身份认证
//	private void identity_approve(String[] arg) throws SQLException, IOException, ServletException {
//		// TODO Auto-generated method stub
//		String sql = sqlmface.addSqlface(0, arg);
//		log.send(DataType.basicType, "01196", "身份认证-sql1:", sql);
//		int success = sqlUtil.sql_exec(sql);
//		if(success==1){
//			String jsonadd = "{\"success\":\"1\"}";
//			inOutUtil.return_ajax(jsonadd);
//		}else{
//			String jsonadd = "{\"success\":\"0\"}";
//			inOutUtil.return_ajax(jsonadd);
//		}
//	}
	//添加求聊信息
	private void add_qiuliao(String[] arg) throws SQLException, IOException, ServletException {
		// TODO Auto-generated method stub
		
		String sql2="select balance from user_data where id="+arg[2];
		int balance=sqlUtil.getInt(sql2);
		if(balance>=Integer.parseInt(arg[3])){
			String sql = sqlmface.addSqlface(0, arg);
			log.send(DataType.basicType, "01196", "身份认证-sql1:", sql);
			int success = sqlUtil.sqlExecute(sql);
			if(success==1){
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
				jguang();
				
			}else{
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}
		}else{
			String jsonadd = "{\"success\":\"0\"}";
			inOutUtil.return_ajax(jsonadd);
		}
		
	
	}
	
	class MyThread implements Runnable{
		public JPushClient jpushClient;
		public String id;
		public MyThread(JPushClient jpushClient,String id){
			this.jpushClient=jpushClient;
			this.id=id;
		}
		
	    @Override
	    public void run() {
	        // TODO Auto-generated method stub
	    	PushPayload payload = buildPushObject_android_and_ios(id);
			log.send(DataType.basicType, "01196", "add_qiuliao-sql1:", id);
			try {
				jpushClient.sendPush(payload);
			} catch (APIConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.send(DataType.basicType, "01196", "add_qiuliao-sql1:", e);
			} catch (APIRequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.send(DataType.basicType, "01196", "add_qiuliao-sql1:", e);
			}
	    }   
	}
	
	private void jguang() throws SQLException{
		String sql="select id from user_data where is_online=1 and is_goddess=1";
		log.send(DataType.basicType, "01196", "add_qiuliao-sql1:", sql);
		list=sqlUtil.getList(sql);
		
		ClientConfig clientConfig = ClientConfig.getInstance();
		final JPushClient jpushClient = new JPushClient("1f649fe516da759c916538c5", "30d0a16dcb84cb5f2c3ff3ed", null, clientConfig);
		for(int i=0;i<list.size();i++){
			
			Thread t1 = new Thread(new MyThread(jpushClient,list.get(i).get("id").toString()));
	        t1.start(); 
			
		}
	}
	
	protected PushPayload buildPushObject_android_and_ios(String user) {
	

		Map<String, String> extras = new HashMap<String, String>();
        // extras.put("test", "https://community.jiguang.cn/push");
        extras.put("ask_for_chat", "ask_for_chat" + "");
		
		return PushPayload.newBuilder()
	              .setPlatform(Platform.android_ios())
	              .setAudience(Audience.alias(user))
	              //.setAudience(Audience.tag_and("285", "tag_all"))
	              .setNotification(Notification.newBuilder()
	              		.setAlert("")
	              		.addPlatformNotification(AndroidNotification.newBuilder()
	              				  //.setTitle("新的来电")
	                              .addExtras(extras)
	                              .build()) 
	              		.addPlatformNotification(IosNotification.newBuilder()
	              				.setBadge(0)
	              				//.setSound("music1.mp3")
	              				.addExtras(extras)
	              				//.setContentAvailable(false)
	              				.build())
	              		.build())
	              .setOptions(Options.newBuilder()
        		             .setApnsProduction(false)
        		             .build())
	              .build();
    }
	
	
	
	//身份认证
		private void identity_approve(String[] arg) throws SQLException, IOException, ServletException {
			// TODO Auto-generated method stub
			String sql = sqlmface.addSqlface(0, arg);
			log.send(DataType.basicType, "01196", "身份认证-sql1:", sql);
			int success = sqlUtil.sqlExecute(sql);
			
			String sql1 = sqlmface.addSqlface(1, arg);
			log.send(DataType.basicType, "01196", "用户表修改女神认证状态--sql:", sql);
			int success1 = sqlUtil.sqlExecute(sql1);
			log.send(DataType.basicType, "01196", "用户表修改女神认证状态--success:", success);
			
			if(success==1 && success1==1){
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			}else{
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}
		}
	
	/**
	 * arg[0] A-user-add
	 * arg[1] give_gift
	 * arg[2] user_id	----- 用户id
	 * arg[3] zhubo_id	----主播id
	 * arg[4] coin		----v币金额
	 * arg[5] id		----礼物ID
	 * @param arg
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ServletException 
	 */
	private void give_gift(String[] arg) throws SQLException, IOException, ServletException
	{
		String sql = sqlmface.addSqlface(4, arg);		// 1.查询余额
		log.send("01205", "give_gift() 查询余额 sql", sql);
		int yue = sqlUtil.getInt(sql);
		
		sql = sqlmface.addSqlface(8, arg);
		log.send("01205", "give_gift() 查询礼物数据 sql ", sql);
		List<Map<String, Object>> list = sqlUtil.getList(sql);
		if(list == null || list.isEmpty())// 没有查询到礼物数据
		{
			String jsonadd = "{\"success\":\"2\"}";
			inOutUtil.return_ajax(jsonadd);
		}else
		{
			Map<String, Object> giftData = list.get(0);
			arg[4] = giftData.get("price").toString();// 实际扣款金额以数据库查询到的为准
			int giftPrice = Integer.valueOf(giftData.get("price").toString()).intValue();
			if(yue < giftPrice)								// 2.判断余额
			{
				String jsonadd = "{\"success\":\"0\"}";		// 余额不足
				inOutUtil.return_ajax(jsonadd);
			}else
			{
				sql = sqlmface.addSqlface(2, arg);		// 3.修改用户余额
				log.send("01205", "give_gift() 修改用户余额 sql", sql);
				int success1 = sqlUtil.sqlExecute(sql);
				log.send("01205", "give_gift() 修改用户余额 结果 ", success1);
				
				sql = sqlmface.addSqlface(3, arg);		// 4.修改主播收入
				log.send("01205", "give_gift() 修改主播余额 sql", sql);
				int success2 = sqlUtil.sqlExecute(sql);
				log.send("01205", "give_gift() 修改用户余额 结果 ", success2);
							
				sql = sqlmface.addSqlface(-1, arg);		// 5.用户支出记录
				log.send("01205", "give_gift() 修改用户支出记录 sql", sql);
				int success3 = sqlUtil.sqlExecute(sql);
				log.send("01205", "give_gift() 修改用户支出记录 结果 ", success3);

				sql = sqlmface.addSqlface(0, arg);		// 6.修改主播收入记录
				log.send("01205", "give_gift() 修改主播收入记录 sql", sql);
				int success4 = sqlUtil.sqlExecute(sql);
				log.send("01205", "give_gift() 修改主播收入记录 结果 ", success4);

				sql = sqlmface.addSqlface(6, arg);		// 7.添加礼物赠送记录
				log.send("01205", "give_gift() 添加礼物赠送记录 sql", sql);
				int success5 = sqlUtil.sqlExecute(sql);
				log.send("01205", "give_gift() 添加礼物赠送记录 结果 ", success5);

				sql = sqlmface.addSqlface(7, arg);		// 8.更新主播收到礼物数目
				log.send("01205", "give_gift() 更新主播收到礼物数目 sql", sql);
				int success6 = sqlUtil.sqlExecute(sql);
				log.send("01205", "give_gift() 更新主播收到礼物数目 结果 ", success6);

				String responseStr = "";
				String jsonadd = "";
				// 上述步骤都成功
				if(success1 == 1 && success2 == 1 && success3 == 1 && success4 == 1 && success5 == 1 && success6 == 1)
				{
					sql = sqlmface.addSqlface(4, arg);		// 查询余额
					log.send("01205", "give_gift() 查询余额 sql", sql);
					yue = sqlUtil.getInt(sql);
					
					jsonadd = responseStr = "{\"success\":\"1\",\"yue\":\""+yue+"\"}";
				}else
				{
					jsonadd = responseStr = "{\"success\":\"2\"}";
				}
				inOutUtil.return_ajax(jsonadd);
				log.send(DataType.specialType, "01205", "打赏红包_反馈消息 ：", responseStr);
				
				// 10.查询数据，发送openfire信息。
				sql="select nickname,user_photo from user_data where id="+arg[2];
				log.send(DataType.specialType, "01205", "查询用户信息", sql);
				
				list1=sqlUtil.getList(sql);
				String name1="";String phote1="";String name2="";String phote2="";
				name1=list1.get(0).get("nickname").toString();   phote1=list1.get(0).get("user_photo").toString();
				
				sql="select nickname,user_photo from user_data where id="+arg[3];			
				log.send(DataType.specialType, "01205", "查询主播信息", sql);
				
				list2=sqlUtil.getList(sql);
				name2=list2.get(0).get("nickname").toString();   phote2=list2.get(0).get("user_photo").toString();
				
				sql="select img from play_tour where id="+arg[5];
				log.send(DataType.specialType, "01205", "查询礼物图片", sql);
				
				String gift_photo=sqlUtil.getString(sql);
				sql="select id from user_data where is_online=1 ";
				log.send(DataType.specialType, "01205", "查询服务期内所有在线用户", sql);
				
				list1=sqlUtil.getList(sql);
				String ids="";
				for(int i=0;i<list1.size();i++){
					if(i==0){
						ids=list1.get(i).get("id").toString();
					}else{
						ids=ids+","+list1.get(i).get("id").toString();
					}
				}
				log.send(DataType.specialType, "01165", "发送openfire消息内容 ", ids + "卍" + "send_liwu,"+name1+","+phote1+","+name2+","+phote2+","+gift_photo);
//				BizRenderTask send = new BizRenderTask(ids + "卍" + "send_liwu,"+name1+","+phote1+","+name2+","+phote2+","+gift_photo);
//				send.run();
			}
		}
	}
	
	//首页单个显示--谁看过我
	private void wholookme(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "196", "首页单个显示--谁看过我sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "196", "首页单个显示--谁看过我",list);
		
		String sql1=sqlmface.searchSqlface(1, arg);
		list1=sqlUtil.getList(sql1);
		list.addAll(list1);
		
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}
	//首页单个显示--谁喜欢我
	private void wholikesme(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "196", "首页单个显示--谁喜欢我sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "196", "首页单个显示--谁喜欢我",list);
		
		String sql1=sqlmface.searchSqlface(1, arg);
		list1=sqlUtil.getList(sql1);
		list.addAll(list1);
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}
	//首页单个显示--我喜欢谁
	private void whoilike(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "196", "首页单个显示--我喜欢谁",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "196", "首页单个显示--我喜欢谁",list);
		
		String sql1=sqlmface.searchSqlface(1, arg);
		list1=sqlUtil.getList(sql1);
		list.addAll(list1);
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}
	//首页单个显示--相互喜欢
	private void likeeachother(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "196", "首页单个显示--相互喜欢sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "196", "首页单个显示--相互喜欢-list",list);
		
		String sql1=sqlmface.searchSqlface(1, arg);
		list1=sqlUtil.getList(sql1);
		list.addAll(list1);
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}
	
	//findall_together_guanzhu （相互喜欢）
	private void findall_together_guanzhu(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "196", "查询（相互喜欢）列表sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "196", "查询（相互喜欢）列表-list",list);
		
		
		String sql1 = sqlmface.searchSqlface(1, arg);
		log.send(DataType.basicType, "196", "查询(相互喜欢)列表",sql1); 
		list1=sqlUtil.getList(sql1);
		log.send(DataType.basicType, "196", "查询(相互喜欢)列表1",list1);
		
		list.addAll(list1);
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}
	//findall_beiguanzhu(谁喜欢我)被关注
	private void findall_beiguanzhu(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "196", "查询(谁喜欢我)被关注列表sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "196", "查询(谁喜欢我)被关注列表-list",list);
		
		String sql1 = sqlmface.searchSqlface(1, arg);
		log.send(DataType.basicType, "196", "查询(谁喜欢我)被关注列表",sql1);
		list1=sqlUtil.getList(sql1);
		log.send(DataType.basicType, "196", "查询(谁喜欢我)被关注列表1",list1);
		
		list.addAll(list1);
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}
	//查询所有谁看过我列表findall_visitor
	 /**
	   * @ClassName: SortClass
	   * @Description: 按时间降序排列
	   * @date: 2018年9月28日 下午5:21:26
	   */
//	  static class SortClass implements Comparator {
//	    @Override
//	    public int compare(Object obj0, Object obj1) {
//	      Map<String, String> map0 = (Map) obj0;
//	      Map<String, String> map1 = (Map) obj1;
//	      int flag = map0.get("createTime").toString().compareTo(map1.get("createTime").toString());
//	      return -flag; // 不取反，则按正序排列
//	    }
//	  }
	private void findall_visitor(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "196", "查询所有‘谁看过我’列表sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "196", "查询所有‘谁看过我’列表-list0",list);
//		int visitornum=list.size();
//		 DecimalFormat df = new DecimalFormat(".00");
//	     list.get(0).put("avg_offprice", df.format(list.get(0).get("avg_offprice")));
//		list.get(0).put("visitornum", visitornum);
		String sql1 = sqlmface.searchSqlface(1, arg);
		log.send(DataType.basicType, "196", "查询所有‘谁看过我’列表sql",sql1);
		list1=sqlUtil.getList(sql1);
		log.send(DataType.basicType, "196", "查询所有‘谁看过我’列表",list1);
		
		list.addAll(list1);

		
//		 for(int i=0;i<list.size();i++){
//			 if(!list.get(i).get("visitors_time").toString().equals("")){
//						
//				 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//24小时制//	
//				 
//				 String date =list.get(i).get("visitors_time").toString() ;
//				 
//				 try {
////					 Date f = new Date(date);
//					long time2 = simpleDateFormat.parse(date).getTime();
//					list.get(i).put("times", time2);	
//				} catch (ParseException e) {
//					log.send(DataType.basicType, "196", "查询所有‘谁看过我’列表",e);
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				 
//			 }
//		 }
//		 log.send(DataType.basicType, "196", "查询所有‘谁看过我’列表",list);
//		 Collections.sort(list, new Comparator<Map<String, Object>>(){
//
//				@Override
//				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
//					Integer time1 = Integer.valueOf(o1.get("times").toString());
//					//long time1=Long.valueOf(o1.get("times").toString());
//					Integer time2 = Integer.valueOf(o2.get("times").toString());
//					//long time2=Long.valueOf(o2.get("times").toString());
//
//					return time1.compareTo(time2);
//					
//				}
//			 });
		
		
		
		//log.send(DataType.basicType, "196", "查询所有‘谁看过我’列表-list0.1时间排序",list);
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
		
	}
	
	

	//查询我的所有关注
	private void findall_guanzhu(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "196", "查询所有我的关注sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "196", "查询所有我的关注-list",list);
		
		String sql1 = sqlmface.searchSqlface(1, arg);
		log.send(DataType.basicType, "196", "查询所有我的关注sql1",sql1);
		list1=sqlUtil.getList(sql1);
		log.send(DataType.basicType, "196", "查询所有我的关注-list1",list1);
		
		list.addAll(list1);
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
		
	}
	/**
	 添加关注 target_id
1 添加关注之前，先查询是否已经有关联关系（有没有访问记录，为下一步做添加还是修改做判断）
select * from relation_table where user_id=3(arg[2]) and target_id=4(arg[3])
-----select * from relation_table where user_id=3(arg[3]) and target_id=4(arg[2])
3 查询--关注时间 如果有如果有关联关系了，但是关注时间为空,做修改操作
select user_follow_time from relation_table where user_id=3 and target_id=4
	3.1 修改语句中添加关注时间的操作
	update relation_table set user_follow_time=NOW() where user_id=3 and target_id=4

select target_follow_time from relation_table where user_id=4 and target_id=3
	3.2 修改语句中添加关注时间的操作
	update relation_table set target_follow_time=NOW() where user_id=3 and target_id=4
else --------if 
2 如果有关系值，说明已经有关联关系过，返回查询到的值，如果没有，添加一条关注记录
insert into relation_table  (user_id,target_id,user_follow_time) values (5,6,NOW())
	 * **/
	
	
//	if(current==0){
//		ressql = "select * from relation_table where user_id='"+arg[2]+"' and target_id='"+arg[3]+"'";
//	}else if(current==1){
//		ressql = "select * from relation_table where user_id='"+arg[3]+"' and target_id='"+arg[2]+"'";
//	}
//
//	else if(current==3){
//		ressql = "update relation_table set user_follow_time=NOW() where user_id='"+arg[2]+"' and target_id='"+arg[3]+"'";
//	}
//	else if(current==5){
//		ressql = "update relation_table set target_follow_time=NOW() where user_id='"+arg[3]+"' and target_id='"+arg[2]+"'";
//	}else if(current==6){
//		ressql = "insert into relation_table  (user_id,target_id,user_follow_time) values ("+arg[2]+",'"+arg[3]+"',NOW())";
//	}
	//添加关注
	private void add_guanzhu(String[] arg) throws SQLException, IOException, ServletException {
		String findsql=sqlmface.searchSqlface(0, arg);
		int a =sqlUtil.getInt(findsql);
		log.send(DataType.basicType, "196", "查询关注：是否已有关联联系 --sql00000:", findsql);
		log.send(DataType.basicType, "196", "查询关注：是否已有关联联系 --count(*) a:", a);
		
		String findsql1=sqlmface.searchSqlface(1, arg);
		int a1 =sqlUtil.getInt(findsql1);
		log.send(DataType.basicType, "196", "查询关注：是否已有关联联系 --sql11111:", findsql);
		log.send(DataType.basicType, "196", "查询关注：是否已有关联联系 --count(*) a1", a1);
		if(a==0&&a1==0){
			//添加
				String addsql2=sqlmface.searchSqlface(6, arg);
				log.send(DataType.basicType, "196", "修改访问时间---如果互相都无访问记录，添加access-user-list：：：", addsql2);
				int success = sqlUtil.sqlExecute(addsql2);
				if(success==1){
					String jsonadd = "{\"success\":\"1\"}";
					inOutUtil.return_ajax(jsonadd);
				}else{
					String jsonadd = "{\"success\":\"0\"}";
					inOutUtil.return_ajax(jsonadd);
				}
		} else if(a>0){
			//修改(删除--取消关注)
			String cancelsql3=sqlmface.searchSqlface(3, arg);
			int success = sqlUtil.sqlExecute(cancelsql3);
			if(success==1){
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			}else{
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}
		} else if(a1>0){
			String cancelsql5=sqlmface.searchSqlface(5, arg);
			int success = sqlUtil.sqlExecute(cancelsql5);
			if(success==1){
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			}else{
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}
		} 
	}
	private void quxiao_guanzhu(String[] arg) throws SQLException, IOException, ServletException {
		String findsql=sqlmface.searchSqlface(0, arg);
		//list=sqlUtil.get_list(findsql);
		int a =sqlUtil.getInt(findsql);
		log.send(DataType.basicType, "196", "取消关注：是否已有关联联系 --sql00000:", findsql);
		log.send(DataType.basicType, "196", "取消关注：是否已有关联联系 --count(*) a:", a);
		
		String findsql1=sqlmface.searchSqlface(1, arg);
		int a1 =sqlUtil.getInt(findsql1);
		log.send(DataType.basicType, "196", "取消关注：是否已有关联联系 --sql11111:", findsql);
		log.send(DataType.basicType, "196", "取消关注：是否已有关联联系 --count(*) a1", a1);
		
//		if(a==0&&a1==0){
//				String addsql2=sqlmface.searchSqlface(6, arg);
//				log.send(DataType.basicType, "196", "修改访问时间---如果互相都无访问记录，添加access-user-list：：：", addsql2);
//				int success = sqlUtil.sql_exec(addsql2);
//				if(success==1){
//					String jsonadd = "{\"success\":\"1\"}";
//					inOutUtil.return_ajax(jsonadd);
//				}else{
//					String jsonadd = "{\"success\":\"0\"}";
//					inOutUtil.return_ajax(jsonadd);
//				}
//		} else 
		if(a>0){
			String cancelsql3=sqlmface.searchSqlface(3, arg);
			int success = sqlUtil.sqlExecute(cancelsql3);
			if(success==1){
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			}else{
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}
		} else {
			String cancelsql5=sqlmface.searchSqlface(5, arg);
			int success = sqlUtil.sqlExecute(cancelsql5);
			if(success==1){
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			}else{
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}
		} 
	}
	//fangke 访客
	private void fangke(String[] arg) throws SQLException, IOException, ServletException {
		String findsql=sqlmface.searchSqlface(0, arg);
		int a=sqlUtil.getInt(findsql);
		log.send(DataType.basicType, "196", "查询访客是否已经存在 user-access-findsql:", findsql);
		log.send(DataType.basicType, "196", "查询访客是否已经存在 user-access-list:", a);	
		
		String findsql1=sqlmface.searchSqlface(1, arg);
		int b=sqlUtil.getInt(findsql1);
		log.send(DataType.basicType, "196", "访客不存在access-user-findsql1:", findsql1);
		log.send(DataType.basicType, "196", "访客不存在access-user-list:", list);
		if(a==0&&b==0){	
				//如果互相都无访问记录，添加
				String addsql2=sqlmface.searchSqlface(2, arg);
				log.send(DataType.basicType, "196", "修改访问时间---如果互相都无访问记录，添加access-user-list：：：", addsql2);
				
				int success = sqlUtil.sqlExecute(addsql2);
				if(success==1){
					String jsonadd = "{\"success\":\"1\"}";
					inOutUtil.return_ajax(jsonadd);
				}else{
					String jsonadd = "{\"success\":\"0\"}";
					inOutUtil.return_ajax(jsonadd);
				}
		}else if(a>0){
				//不为空 修改 user=1
				String updatesql3=sqlmface.searchSqlface(3, arg);
				int a2 =sqlUtil.sqlExecute(updatesql3);
				if(a2==1){
					String jsonadd = "{\"success\":\"1\"}";
					inOutUtil.return_ajax(jsonadd);
				}else{
					String jsonadd = "{\"success\":\"0\"}";
					inOutUtil.return_ajax(jsonadd);
				}
		}else if(b>0){
			/*String sql1=sqlmface.searchSqlface(1, arg);
			list1=sqlUtil.get_list(sql1);*/
			//if(!"".equals(list1.get(0))){
			String sql4=sqlmface.searchSqlface(4, arg);
			int a1=sqlUtil.sqlExecute(sql4);
			//int success = sqlUtil.sql_exec(addsql2);
			if(a1==1){
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			}else{
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}
			//}
		}
		
		
//		if(a==0){
//			String findsql1=sqlmface.searchSqlface(1, arg);
//			int b=sqlUtil.get_int(findsql1);
//			log.send(DataType.basicType, "196", "访客不存在access-user-findsql1:", findsql1);
//			log.send(DataType.basicType, "196", "访客不存在access-user-list:", list);
//			
//			if(b==0){
//				//如果互相都无访问记录，添加
//				String addsql2=sqlmface.searchSqlface(2, arg);
//				log.send(DataType.basicType, "196", "修改访问时间---如果互相都无访问记录，添加access-user-list：：：", addsql2);
//				
//				int success = sqlUtil.sql_exec(addsql2);
//				if(success==1){
//					String jsonadd = "{\"success\":\"1\"}";
//					inOutUtil.return_ajax(jsonadd);
//				}else{
//					String jsonadd = "{\"success\":\"0\"}";
//					inOutUtil.return_ajax(jsonadd);
//				}
//			}else {
//				//不为空 修改 user=1
//				String updatesql3=sqlmface.searchSqlface(3, arg);
//				int a2 =sqlUtil.sql_exec(updatesql3);
//				if(a2==1){
//					String jsonadd = "{\"success\":\"1\"}";
//					inOutUtil.return_ajax(jsonadd);
//				}else{
//					String jsonadd = "{\"success\":\"0\"}";
//					inOutUtil.return_ajax(jsonadd);
//				}
//			}
//		}else {
//			/*String sql1=sqlmface.searchSqlface(1, arg);
//			list1=sqlUtil.get_list(sql1);*/
//			//if(!"".equals(list1.get(0))){
//			String sql4=sqlmface.searchSqlface(4, arg);
//			int a1=sqlUtil.sql_exec(sql4);
//			//int success = sqlUtil.sql_exec(addsql2);
//			if(a1==1){
//				String jsonadd = "{\"success\":\"1\"}";
//				inOutUtil.return_ajax(jsonadd);
//			}else{
//				String jsonadd = "{\"success\":\"0\"}";
//				inOutUtil.return_ajax(jsonadd);
//			}
//			//}
//		}
	}
	//判断是否增加访客
	private void my_visitor(String[] arg) throws SQLException, IOException, ServletException {
		// TODO Auto-generated method stub
		String sql = sqlmface.addSqlface(0, arg);
		log.send(DataType.basicType, "196", "查询访客是否已经存在-sql1:", sql);
		
		list1=sqlUtil.getList(sql);
		log.send(DataType.basicType, "01196", "查询到的集合长度：-数据库语句",list1.size());
		if(list1.get(0).equals("")){
			int success = sqlUtil.sqlExecute(sql);
			if(success==1){
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			}else{
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}
		}	
	}
	//查询访客
	private void find_my_visitor(String[] arg) throws SQLException, ServletException, IOException {
	String sql = sqlmface.searchSqlface(0, arg);
	log.send(DataType.basicType, "196", "查询我的访客sql",sql);
	list=sqlUtil.getList(sql);
	int count_list=list.size();
	log.send(DataType.basicType, "196", "我的访客人数-count_list",count_list);
	
	list.get(0).put("visitor_num",count_list);
	
	log.send(DataType.basicType, "196", "查询我的访客-list",list);

	inOutUtil.return_ajax(JsonUtil.listToJson(list));
	
}
	

	public class BizRenderTask implements Runnable {
		private String title;
		private String content;
		private String group;

		BizRenderTask(String content) {
			// this.group=group;
			// this.title=title;
			this.content = content;
		}

		public void run() {
			Socket client;
			try {
				// log.send(DataType.specialType, "01066", "BizRenderTask",
				// content);

				client = new Socket("120.27.98.128", 9200);
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(
						client.getOutputStream(), "UTF-8"), true);
				writer.println(content);
				writer.close();
				client.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.send(DataType.basicType, "01160", "流错误", e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.send(DataType.basicType, "01160", "IO错 ", e);
			}
		}

	}
	
}
