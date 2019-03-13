package com.wp.yf.app.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wp.yf.app.controller.support.InOutFace;
import com.wp.yf.app.controller.support.InOutManager;
import com.wp.yf.app.dao.UserGiftDao;
import com.wp.yf.app.db.sql.SqlManagerFace;
import com.wp.yf.app.log.JyLogDetect;
import com.wp.yf.app.log.JyLogDetect.DataType;
import com.wp.yf.app.util.JsonUtil;


@Controller
@RequestMapping("/uiface")	// 礼物往来
public class UserGiftController extends InOutManager implements InOutFace {
	
	public UserGiftController(String[] arg, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SQLException {
		super(arg, request, response);
	}

	protected ArrayList<Map<String, Object>> list;
	protected ArrayList<Map<String, Object>> list1;
	protected ArrayList<Map<String, Object>> list2;
	
	SqlManagerFace sqlmface = new UserGiftDao();
	
	@RequestMapping("/gift_dealings")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] arg = null;
		JyLogDetect log = new JyLogDetect(request);

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		arg = JsonUtil.jsonReceive(request);
		log.send(DataType.specialType, "01165_____", "aipengServletInout_gift_01205_arg:", arg);

		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		
		
		try {
			switch (arg[0]) {
				case "A-user-add":
					addFace();
					break;
				case "A-user-mod":
					modFace();
					break;
				case "A-user-search":
					searchFace();
					break;
				case "A-user-delete":
					deleteFace();
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.send("01165", "Exception", e);
		}
	}
	
	
	public void addFace() throws SQLException, ServletException, IOException {
		switch (arg[1]) {
			case "give_gift":
				give_gift(arg);
				break;
				
			case "ask_gift":
				ask_gift(arg);
				break;
		}	
	}
	
	/**
	 * arg[0] A-user-add
	 * arg[1] ask_gift
	 * arg[2] 发起ID
	 * arg[3] 目标ID
	 * arg[4] 礼物ID
	 * @param arg
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ServletException 
	 */
	private void ask_gift(String[] arg) throws SQLException, IOException, ServletException
	{
		// 查询礼物信息
		String sql = sqlmface.addSqlface(0, arg);
		log.send("01205", "求赏 查询礼物信息： sql ", sql);
		Map<String, Object> giftInfo = sqlUtil.getList(sql).get(0);
		
		// 查询发起用户信息
		sql = sqlmface.addSqlface(1, arg);
		log.send("01205", "求赏 查询用户信息： sql ", sql);
		Map<String, Object> userInfo = sqlUtil.getList(sql).get(0);
		String pc = giftInfo.get("price").toString();
		if(pc == null || pc.isEmpty())pc="0";
		// 构建消息体，通过openfire发送到目标用户
		StringBuilder sb = new StringBuilder();
		sb.append(arg[3])
			.append("卍ask_gift,")
			.append(userInfo.get("nickname"))				// 昵称
			.append(",")
			.append(userInfo.get("id"))						// 用户ID
			.append(",")
			.append(userInfo.get("user_photo"))				// 头像
			.append(",")
			.append(giftInfo.get("id"))						// 礼物ID
			.append(",")
			.append(giftInfo.get("img"))					// 礼物图标
			.append(",")
			.append(giftInfo.get("name"))					// 礼物名
			.append(",")
			.append(pc)										// 礼物的价格
			;
		
		// 发送openfire消息
		String openfireMSG = sb.toString();
		log.send("01205", "发送求赏openfire消息：", openfireMSG);
		BizRenderTask send = new BizRenderTask(openfireMSG);
		send.run();

		// 反馈消息
		String jsonadd = "{\"success\":\"OK\"}";
		inOutUtil.return_ajax(jsonadd);
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
			
				String weeksql="update user_data set week_inout=week_inout+"+arg[4]+" where id ='"+arg[2]+"' or id='"+arg[3]+"'";
				sqlUtil.sqlExecute(weeksql);
				
				
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
				sql="select id from user_data where is_online!=0 ";
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
				log.send(DataType.specialType, "01165", "发送openfire消息内容 ", ids + "卍" + "give_gift,"+name1+","+phote1+","+name2+","+phote2+","+gift_photo);
				BizRenderTask send = new BizRenderTask(ids + "卍" + "give_gift,"+name1+","+phote1+","+name2+","+phote2+","+gift_photo);
				send.run();
			}
		}
	}

	@Override
	public void modFace() throws SQLException, ServletException, IOException {
		switch (arg[1]) {
		
		}	
		
	}

	@Override
	public void deleteFace() throws SQLException, ServletException, IOException {
		switch (arg[1]) {
		
		}	
	}

	@Override
	public void searchFace() throws SQLException, ServletException, IOException {
		switch (arg[1]) {

		case "reward_gift":
			reward_gift(arg);
			break;
			
		case "gift_record":
			gift_record(arg);
			break;
			
		case "gift_count":
			gift_count(arg);
			break;
		}
	}
	
	/*
	 * arg[0] A-user-search
	 * arg[1] reward_gift
	 * arg[2] 未使用
	 */
	//打赏礼物 (聊天界面)
	private void reward_gift(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01205", "打赏礼物（list查询）sql",sql);
		list=sqlUtil.getList(sql);
		log.send(DataType.basicType, "01205", "打赏礼物（list查询）-list",list);
		
		sql="select balance from user_data where id='"+arg[2]+"'";
		String balance=sqlUtil.getString(sql);
		list.get(0).put("balance", balance);
		
		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}

	/*
	 * arg[0] A-user-search
	 * arg[1] gift_record
	 * arg[2] 未使用
	 * arg[3] 主播ID			// 将修改为用户ID
	 * arg[4]				// 将增加，0表示查询送出礼物，1表示查询收到礼物
	 */
	private void gift_record(String[] arg) throws SQLException, IOException,
	ServletException {
		if(arg.length < 5)	// 测试时兼容老版本，防止客户激动
		{
			arg = Arrays.copyOf(arg, 5);
			arg[4] = "1";
		}
	    String sql=sqlmface.searchSqlface(1, arg);
	    log.send(DataType.basicType, "01162", "查看礼物记录-sql", sql);
	    list=sqlUtil.getList(sql);
//	    sql=sqlmface.searchSqlface(0, arg);
//	    log.send(DataType.basicType, "01162", "查看礼物记录-sql", sql);
//	    list1=sqlUtil.get_list(sql);
	    for(int i=0;i<list.size();i++){
	    	arg[2]=list.get(i).get("id")+"";
	    	sql=sqlmface.searchSqlface(2, arg);
	    	   log.send(DataType.basicType, "01162", "查看礼物记录-sql", sql);
	    	int b=sqlUtil.getCount(sql);
	    	list.get(i).put("gift_num", b+"");
	    }
	    
	    sql = sqlmface.searchSqlface(3, arg);
	    log.send(DataType.basicType, "01205", "查询礼物数量-sql ", sql);
	    int giftNum = sqlUtil.getInt(sql);	    
	    list.get(0).put("gift_amount", giftNum);
	    inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}

	/**
	 * arg[0] A-user-search
	 * arg[1] gift_count
	 * arg[2] 未使用
	 * arg[3] 用户ID
	 * arg[4] 0表示查询送出礼物，1表示查询收到礼物
	 * @param arg
	 * @throws SQLException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void gift_count(String[] arg) throws SQLException, IOException, ServletException{
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01205", "查询礼物数量 sql ",sql);
		int count = sqlUtil.getInt(sql);
		HashMap<String, Object> p = new HashMap<>();
		p.put("count", count);
		inOutUtil.return_ajax(JsonUtil.mapToJson2(p));
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

				client = new Socket("47.99.110.97", 9200);
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
