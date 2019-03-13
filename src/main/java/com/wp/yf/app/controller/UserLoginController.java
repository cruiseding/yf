package com.wp.yf.app.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wp.yf.app.controller.support.InOutFace;
import com.wp.yf.app.controller.support.InOutManager;
import com.wp.yf.app.dao.UserLoginDao;
import com.wp.yf.app.db.sql.SqlManagerFace;
import com.wp.yf.app.log.JyLogDetect;
import com.wp.yf.app.log.JyLogDetect.DataType;
import com.wp.yf.app.util.JsonUtil;



@Controller
@RequestMapping("/uiface")
public class UserLoginController extends InOutManager implements InOutFace {
	
	public UserLoginController(String[] arg, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SQLException {
		super(arg, request, response);
	}

	protected ArrayList<Map<String, Object>> list;
	protected ArrayList<Map<String, Object>> list1;
	protected ArrayList<Map<String, Object>> list2;
	protected String json = "";
	
	SqlManagerFace sqlmface = new UserLoginDao();
	
	@RequestMapping("/member01165")
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] arg = null;
		JyLogDetect log = new JyLogDetect(request);

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		arg = JsonUtil.jsonReceive(request);
		log.send(DataType.specialType, "01165_____", "vliaoServletInOut_01165_arg:", arg);

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
	
	@Override
	public void addFace() throws SQLException, ServletException, IOException {
		switch (arg[1]) {
		//用户注册
//		case "user_register"://180910创建
//			user_register(arg);
//			break;
		case "wx_login":
			wx_login(arg);
			break;
			
		case "qq_login":
			qq_login(arg);
			break;	
			
		case "bind_wx":
			String sql="select wx_id from user_data where id="+arg[2];
			list=sqlUtil.getList(sql);
			if(list.size()>0){
				String wxid="";
				if(list.get(0).get("wx_id")==null ){//|| list.get(0).get("wx_id").equals("null")
					wxid="";
				}else{
					wxid=list.get(0).get("wx_id").toString();
				}
				log.send(DataType.basicType, "01198", "微信 登陆-获取信息--phone_id:", wxid);
				if(wxid.equals(arg[3])){
					//本身绑定的，不需要更换
					String jsonadd = "{\"result\":\"0\"}";
					inOutUtil.return_ajax(jsonadd);
				}else{
					sql="select count(*) from user_data where wx_id='"+arg[3]+"'";
					int cont=sqlUtil.getCount(sql);
					if(cont>0){
						//别的账号绑定了
						String jsonadd = "{\"result\":\"2\"}";
						inOutUtil.return_ajax(jsonadd);
					}else{
						sql="update user_data set wx_id='"+arg[3]+"', wechat_name='"+arg[4]+"' where id="+arg[2];
						sqlUtil.sqlExecute(sql);
						//绑定成功
						String jsonadd = "{\"result\":\"1\"}";
						inOutUtil.return_ajax(jsonadd);
					}
				}
			}
			break;
		case "bind_phone":
			String sql1="select phone from user_data where id="+arg[2];
			list=sqlUtil.getList(sql1);
			if(list.size()>0){
				String phone="";
				if(list.get(0).get("phone")==null){
					phone="";
				}else{
					phone=list.get(0).get("phone").toString();
				}
				if(phone.equals(arg[3])){
					//本身绑定的，不需要更换
					String jsonadd = "{\"result\":\"0\"}";
					inOutUtil.return_ajax(jsonadd);
				}else{
					sql1="select count(*) from user_data where phone='"+arg[3]+"'";
					int cont=sqlUtil.getCount(sql1);
					if(cont>0){
						//别的账号绑定了
						String jsonadd = "{\"result\":\"2\"}";
						inOutUtil.return_ajax(jsonadd);
					}else{
						//绑定成功
						String jsonadd = "{\"result\":\"1\"}";
						inOutUtil.return_ajax(jsonadd);
					}
				}
			}
			break;
			
		}
	}
	
	/**
	 * arg[2] wx_id
	 * arg[3] nickname
	 * arg[4] photo
	 * 
	 * @param arg
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ServletException 
	 */

	private void wx_login(String[] arg) throws SQLException, IOException, ServletException {
		// TODO Auto-generated method stub
		String sql = sqlmface.addSqlface(3, arg);
		log.send(DataType.basicType, "01198", "微信 登陆-获取信息--sql:", sql);
		list = sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "微信 登陆-获取信息--phone_id:", list);
		
		String yqcode=arg[6];
		String channelcode=arg[7];
		String tmppath = request.getSession().getServletContext()
	            .getRealPath("/");
		
		if(list==null||"null".equals(list)||list.size()==0){
			sql = sqlmface.addSqlface(0, arg);
			log.send(DataType.basicType, "01198", "微信 注册-sql:", sql);
			int success = sqlUtil.sqlExecute(sql);
			log.send(DataType.basicType, "01198", "微信 注册-success:", success);	
			
			sql = sqlmface.addSqlface(1, arg);
			log.send(DataType.basicType, "01198", "微信 登陆-获取id--sql:", sql);
			String u_id = sqlUtil.getString(sql);
			log.send(DataType.basicType, "01198", "微信 登陆-获取id--phone_id:", u_id);
		
			ShareAgentInfo.registerinfo(1,u_id,yqcode,channelcode,"0.5",tmppath,"app.quyuanapp.com");
			
			//查询微信手机号sql = sqlmface.addSqlface(3, arg);
			
			sql = sqlmface.addSqlface(3, arg);
			log.send(DataType.basicType, "01198", "微信 登陆-获取信息--sql:", sql);
			list = sqlUtil.getList(sql);
			log.send(DataType.basicType, "01198", "微信 登陆-获取信息--phone_id:", list);
			list.get(0).put("is_new", "1");

			inOutUtil.return_ajax(JsonUtil.listToJson(list));
		}else{
			list.get(0).put("is_new", "0");
			inOutUtil.return_ajax(JsonUtil.listToJson(list));
		}
		
	}

	private void qq_login(String[] arg) throws SQLException, IOException, ServletException {
		// TODO Auto-generated method stub
		String sql = sqlmface.addSqlface(3, arg);
		log.send(DataType.basicType, "01198", "微信 登陆-获取信息--sql:", sql);
		list = sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "微信 登陆-获取信息--phone_id:", list);
		
		String yqcode=arg[6];
		String channelcode=arg[7];
		String tmppath = request.getSession().getServletContext()
	            .getRealPath("/");
		
		if(list==null||"null".equals(list)||list.size()==0){
			sql = sqlmface.addSqlface(0, arg);
			log.send(DataType.basicType, "01198", "微信 注册-sql:", sql);
			int success = sqlUtil.sqlExecute(sql);
			log.send(DataType.basicType, "01198", "微信 注册-success:", success);	
			
			sql = sqlmface.addSqlface(1, arg);
			log.send(DataType.basicType, "01198", "微信 登陆-获取id--sql:", sql);
			String u_id = sqlUtil.getString(sql);
			log.send(DataType.basicType, "01198", "微信 登陆-获取id--phone_id:", u_id);
		
			ShareAgentInfo.registerinfo(1,u_id,yqcode,channelcode,"0.5",tmppath,"app.quyuanapp.com");
			
			//查询微信手机号sql = sqlmface.addSqlface(3, arg);
			
			sql = sqlmface.addSqlface(3, arg);
			log.send(DataType.basicType, "01198", "微信 登陆-获取信息--sql:", sql);
			list = sqlUtil.getList(sql);
			log.send(DataType.basicType, "01198", "微信 登陆-获取信息--phone_id:", list);
			list.get(0).put("is_new", "1");

			inOutUtil.return_ajax(JsonUtil.listToJson(list));
		}else{
			list.get(0).put("is_new", "0");
			inOutUtil.return_ajax(JsonUtil.listToJson(list));
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
			ShareAgentInfo.registerinfo(1,user_id,yqcode,channelcode,"5",tmppath,"app.quyuanapp.com");
			String jsonadd = "{\"success\":\"1\"}";
			inOutUtil.return_ajax(jsonadd);
		}else{
			String jsonadd = "{\"success\":\"0\"}";
			inOutUtil.return_ajax(jsonadd);
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
		switch(arg[1]){
		//查询用户基本信息
		case "search_basic_personal_details"://180902创建
			search_basic_personal_details(arg);
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
		}
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

		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}
	
	public boolean JudgeIsMoblie(HttpServletRequest request) {  
        boolean isMoblie = false;  
        String[] mobileAgents = { "iphone", "android", "phone", "mobile",  
                "wap", "netfront", "java", "opera mobi", "opera mini", "ucweb",  
                "windows ce", "symbian", "series", "webos", "sony",  
                "blackberry", "dopod", "nokia", "samsung", "palmsource", "xda",  
                "pieplus", "meizu", "midp", "cldc", "motorola", "foma",  
                "docomo", "up.browser", "up.link", "blazer", "helio", "hosin",  
                "huawei", "novarra", "coolpad", "webos", "techfaith",  
                "palmsource", "alcatel", "amoi", "ktouch", "nexian",  
                "ericsson", "philips", "sagem", "wellcom", "bunjalloo", "maui",  
                "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",  
                "pantech", "gionee", "portalmmm", "jig browser", "hiptop",  
                "benq", "haier", "^lct", "320x320", "240x320", "176x220",  
                "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq",  
                "bird", "blac", "blaz", "brew", "cell", "cldc", "cmd-", "dang",  
                "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs", "kddi",  
                "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo",  
                "midp", "mits", "mmef", "mobi", "mot-", "moto", "mwbp", "nec-",  
                "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play",  
                "port", "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-",  
                "send", "seri", "sgh-", "shar", "sie-", "siem", "smal", "smar",  
                "sony", "sph-", "symb", "t-mo", "teli", "tim-", /*"tosh",*/ "tsm-",  
                "upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi", "wapp",  
                "wapr", "webc", "winw", "winw", "xda", "xda-",  
                "Googlebot-Mobile" };  
        if (request.getHeader("User-Agent") != null) {  
            for (String mobileAgent : mobileAgents) {  

                if (request.getHeader("User-Agent").toLowerCase()  
                        .indexOf(mobileAgent) >= 0) {  
                    isMoblie = true;  
                    break;  
                }  
            }  
        }  
        return isMoblie;  
    }
	
}
