package com.wp.yf.app.controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ServletConfigAware;

import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import com.wp.yf.app.controller.support.InOutFace;
import com.wp.yf.app.controller.support.InOutManager;
import com.wp.yf.app.controller.support.JyHelpManager;
import com.wp.yf.app.dao.UserRegisterDao;
import com.wp.yf.app.db.sql.SqlManagerFace;
import com.wp.yf.app.log.JyLogDetect;
import com.wp.yf.app.log.JyLogDetect.DataType;
import com.wp.yf.app.util.JsonUtil;

import net.coobird.thumbnailator.Thumbnails;

@Controller
@RequestMapping("/uiface")
public class WalletController extends InOutManager implements InOutFace, ServletConfigAware {

	public WalletController(String[] arg, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SQLException {
		super(arg, request, response);
	}

	private ServletConfig servletConfig;

	protected ArrayList<Map<String, Object>> list;
	protected ArrayList<Map<String, Object>> list1;
	protected ArrayList<Map<String, Object>> list2;

	SqlManagerFace sqlmface = new UserRegisterDao();

	@Override
	public void setServletConfig(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}

	@RequestMapping("/member01198")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
					this.addFace();
					break;
				case "A-user-mod":
					this.modFace();
					break;
				case "A-user-search":
					this.searchFace();
					break;
				case "A-user-delete":
					this.deleteFace();
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.send("01165", "Exception", e);
		}
	}

	// 含有图片内容提交
	public String[] upfile(String[] arg, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SmartUploadException {
		JyLogDetect log = new JyLogDetect(request);
		String path = "img/article/";
		String loadpath = request.getServletContext().getRealPath("/");
		String fullPath = loadpath + path;
		File fullDir = new File(fullPath);
		if (!fullDir.exists()) {
			fullDir.mkdirs();
		}
		SmartUpload smartUpload = new SmartUpload();
		long maxSize = 10 * 1024 * 1024;//
		String allowFileExtList = "jar,exe,doc,docx,txt,html,xml,xls,pdf,jpg,png,PNG,gif,GIF,jpeg,JPEG,JPG,BMP,bmp";
		smartUpload.initialize(servletConfig, request, response);
		smartUpload.setMaxFileSize(maxSize);
		smartUpload.setAllowedFilesList(allowFileExtList);
		smartUpload.upload();
		switch (arg[1]) {
			case "seller_modify_phone":
			case "seller_modify": {
				String[] goodinfo = new String[10];
	
				goodinfo[0] = arg[0];
				goodinfo[1] = arg[1];
				goodinfo[2] = arg[2];
				goodinfo[3] = smartUpload.getRequest().getParameter("username");
				goodinfo[4] = smartUpload.getRequest().getParameter("shopname");
				goodinfo[5] = smartUpload.getRequest().getParameter("address");
				goodinfo[6] = smartUpload.getRequest().getParameter("tele");
				goodinfo[8] = smartUpload.getRequest().getParameter("pc_phone");
				goodinfo[9] = smartUpload.getRequest().getParameter("passw");
				goodinfo[7] = "";
				for (int i = 0; i < smartUpload.getFiles().getCount(); i++) {
					com.jspsmart.upload.File smartFile = smartUpload.getFiles().getFile(i);
					if (!smartFile.isMissing()) {
						String fileName = smartFile.getFileName();
						String type2 = fileName.substring(fileName.lastIndexOf("."));
						Random rnd = new Random();
						int r = rnd.nextInt(100);
						Date date2 = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
						String strDate2 = formatter.format(date2);
						fileName = strDate2 + r + type2;
						smartFile.saveAs(fullPath + "//" + fileName, com.jspsmart.upload.File.SAVEAS_PHYSICAL);
	
						// names[i] = path + fileName;
						// thumbimage.thumbnailImage(fullPath + fileName, 300, 240);
	
						File imgFile = new File(fullPath + "/" + fileName);
						Image img = ImageIO.read(imgFile);
						int width = img.getWidth(null);
						int height = img.getHeight(null);
						Thumbnails.of(fullPath + "/" + fileName).size(width, height).outputQuality(0.9f).outputFormat("jpg")
								.toFile(fullPath + "/thumb_" + fileName.substring(0, fileName.lastIndexOf(".")));
	
						goodinfo[7 + i] = path + "thumb_" + fileName.substring(0, fileName.lastIndexOf(".")) + ".jpg";
					}
	
				}
				return goodinfo;
			}
	
			default: {
				String[] goodinfo = new String[9];
				goodinfo[8] = "";
				if (smartUpload.getRequest().getParameter("good_id") != null) {
					goodinfo[8] = smartUpload.getRequest().getParameter("good_id");
					goodinfo[6] = smartUpload.getRequest().getParameter("pict1");
				}
	
				goodinfo[0] = arg[0];
				goodinfo[1] = arg[1];
				goodinfo[2] = arg[2];
				goodinfo[3] = smartUpload.getRequest().getParameter("good_sort1");
				goodinfo[4] = smartUpload.getRequest().getParameter("good_name");
				goodinfo[5] = smartUpload.getRequest().getParameter("good_note");
	
				String a = smartUpload.getRequest().getParameter("good_image_text");
				log.send(DataType.specialType, "01115", "头信息:   ", a);
				a = a.replaceAll("'", "\\\\'");
				log.send(DataType.specialType, "01115", "头信息:   ", a);
				goodinfo[7] = a;
	
				for (int i = 0; i < smartUpload.getFiles().getCount(); i++) {
					com.jspsmart.upload.File smartFile = smartUpload.getFiles().getFile(i);
					if (!smartFile.isMissing()) {
						String fileName = smartFile.getFileName();
						String type2 = fileName.substring(fileName.lastIndexOf("."));
						Random rnd = new Random();
						int r = rnd.nextInt(100);
						Date date2 = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
						String strDate2 = formatter.format(date2);
						fileName = strDate2 + r + type2;
						smartFile.saveAs(fullPath + "//" + fileName, com.jspsmart.upload.File.SAVEAS_PHYSICAL);
	
						File imgFile = new File(fullPath + "/" + fileName);
						Image img = ImageIO.read(imgFile);
						int width = img.getWidth(null);
						int height = img.getHeight(null);
						Thumbnails.of(fullPath + "/" + fileName).size(width, height).outputQuality(0.9f).outputFormat("jpg")
								.toFile(fullPath + "/thumb_" + fileName.substring(0, fileName.lastIndexOf(".")));
	
						goodinfo[6 + i] = path + "thumb_" + fileName.substring(0, fileName.lastIndexOf(".")) + ".jpg";
					}
	
				}
				return goodinfo;
			}
		}
	}

	public boolean JudgeIsMoblie(HttpServletRequest request) {
		boolean isMoblie = false;
		String[] mobileAgents = { "iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
				"opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
				"nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
				"docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
				"techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
				"wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos", "pantech",
				"gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320", "240x320",
				"176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac", "blaz",
				"brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs", "kddi",
				"keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi", "mot-",
				"moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port", "prox",
				"qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem", "smal",
				"smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", /* "tosh", */ "tsm-", "upg1", "upsi", "vk-v",
				"voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };
		if (request.getHeader("User-Agent") != null) {
			for (String mobileAgent : mobileAgents) {

				if (request.getHeader("User-Agent").toLowerCase().indexOf(mobileAgent) >= 0) {
					isMoblie = true;
					break;
				}
			}
		}
		return isMoblie;
	}

	@Override
	public void addFace() throws SQLException, ServletException, IOException {
		switch (arg[1]) {
			// 用户注册
			case "user_register":// 180910创建
				user_register(arg);
				break;
		}
	}

	/**
	 * 用户注册------01198 arg[2] phone arg[3] user_photo arg[4] nickname arg[5] age
	 * arg[6] address arg[7] gender
	 * 
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void user_register(String[] arg) throws SQLException, ServletException, IOException {
		log.send(DataType.basicType, "01198", "用户注册-arg:", arg);
		if ("".equals(arg[10])) {
			String sql = sqlmface.addSqlface(0, arg);
			log.send(DataType.basicType, "01198", "用户注册-sql:", sql);
			int success = sqlUtil.sqlExecute(sql);
			log.send(DataType.basicType, "01198", "用户注册-success:", success);
			String yqcode = arg[8];
			String channelcode = arg[9];
			String tmppath = request.getSession().getServletContext().getRealPath("/");
			if (success == 1) {
				String sql1 = sqlmface.addSqlface(1, arg);
				log.send(DataType.basicType, "01198", "用户id-sql:", sql);
				list = sqlUtil.getList(sql1);
				log.send(DataType.basicType, "01198", "用户id-success:", success);
				String user_id = list.get(0).get("id").toString();
				ShareAgentInfo.registerinfo(1, user_id, yqcode, channelcode, "0.5", tmppath, "app.quyuanapp.com");
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			} else {
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}
		} else {
			String sql = sqlmface.addSqlface(2, arg);
			log.send(DataType.basicType, "01198", "更改-sql:", sql);
			int success1 = sqlUtil.sqlExecute(sql);
			log.send(DataType.basicType, "01198", "更改-success1:", success1);
			if (success1 == 1) {
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			} else {
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
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
		// 查询用户基本信息
		case "search_basic_personal_details":// 180902创建
			search_basic_personal_details(arg);
			break;

		case "cashdraw_dou":// 180902创建
			cashdraw_dou(arg);
			break;

		// 查询是否关注了
		case "search_is_attention":// 180907创建
			search_is_attention(arg);
			break;
		// 查询他人详情页聊天价格信息
		case "search_chat_price_details":// 180909创建
			search_chat_price_details(arg);
			break;
		// 根据手机号查询用户信息
		case "search_info_by_phone":// 180910创建
			search_info_by_phone(arg);
			break;
		// 获取短信验证码
		case "get_message_info":// 180910创建
			get_message_info(arg);
			break;
		// 查询昵称库
		case "select_name":// 180920创建
			select_name(arg);
			break;
		// 查询拨打着余额与被拨打着状态
		case "search_balance_status":// 180926创建
			search_balance_status(arg);
			break;
		// 查询昵称是否存在
		case "nickname_is_exit":// 180930创建
			nickname_is_exit(arg);
			break;
		}
	}

	/**
	 * 查询昵称是否存在------01198
	 * 
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void nickname_is_exit(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询昵称是否存在-sql:", sql);
		int success = sqlUtil.getInt(sql);
		log.send(DataType.basicType, "01198", "查询昵称是否存在-success:", success);
		if (success > 0) {
			String jsonadd = "{\"success\":\"0\"}";
			inOutUtil.return_ajax(jsonadd);
		} else {
			String jsonadd = "{\"success\":\"1\"}";
			inOutUtil.return_ajax(jsonadd);
		}

	}

	/**
	 * 查询拨打着余额与被拨打着状态 arg[2] id 拨打着余额 arg[3] user_id 被拨打着状态
	 * 
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void search_balance_status(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询拨打着余额sql", sql);
		list = sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "查询拨打着余额-list", list);

		String sql1 = sqlmface.searchSqlface(1, arg);
		log.send(DataType.basicType, "01198", "查询被拨打着状态sql1", sql1);
		list1 = sqlUtil.getList(sql1);
		log.send(DataType.basicType, "01198", "查询被拨打着状态-list1", list1);

		sql = "select status from blacklist_table where user_id ='" + arg[3] + "' and black_id='" + arg[2] + "'";
		String st = sqlUtil.getString(sql);
		if (st == null || "null".equals(st) || st.equals("0")) {
			st = "0";
		}
		list.get(0).put("isblack", st);
		list.get(0).put("is_online", list1.get(0).get("is_online"));
		log.send(DataType.basicType, "01198", "查询昵称-list", JsonUtil.listToJson(list));
		inOutUtil.return_ajax(JsonUtil.listToJson(list));

	}

	/**
	 * 查询昵称库
	 * 
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void select_name(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询昵称sql", sql);
		list = sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "查询昵称-list", list);

		inOutUtil.return_ajax(JsonUtil.listToJson(list));

	}

	private void get_message_info(String[] arg) throws SQLException, ServletException, IOException {
		// 取随机数1000-9999
		int num = (int) ((Math.random() * 9000) + 1000);
		String jsonadd = "{\"success\":\"" + num + "\"}";
		inOutUtil.return_ajax(jsonadd);
	}

	/**
	 * 根据手机号查询用户信息------01198 arg[2] phone 手机号
	 * 
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void search_info_by_phone(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "根据手机号查询用户信息sql", sql);
		list = sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "根据手机号查询用户信息-list", list);

		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}

	/**
	 * 查询他人详情页聊天价格信息------01198 arg[2] user_id 用户id
	 * 
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void search_chat_price_details(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询他人详情页聊天价格信息sql", sql);
		list = sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "查询他人详情页聊天价格信息-list", list);

		inOutUtil.return_ajax(JsonUtil.listToJson(list));

	}

	/**
	 * 查询是否关注了------01198
	 * 
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void search_is_attention(String[] arg) throws SQLException, ServletException, IOException {
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询是否关注了---sql", sql);
		int count = sqlUtil.getInt(sql);
		log.send(DataType.basicType, "01198", "查询是否关注了---count", count);
		if (count == 1) {
			String sql1 = sqlmface.searchSqlface(1, arg);
			log.send(DataType.basicType, "01198", "查询是否关注了---sql1", sql1);
			list = sqlUtil.getList(sql1);
			log.send(DataType.basicType, "01198", "查询是否关注了---list", list);
			if ("".equals(list.get(0).get("user_follow_time"))) {
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			} else {
				String jsonadd = "{\"success\":\"1\"}";
				inOutUtil.return_ajax(jsonadd);
			}
		} else {
			String sql2 = sqlmface.searchSqlface(2, arg);
			log.send(DataType.basicType, "01198", "查询是否关注了---sq2", sql2);
			int count1 = sqlUtil.getInt(sql2);
			log.send(DataType.basicType, "01198", "查询是否关注了---count2", count1);
			if (count1 == 1) {
				String sql3 = sqlmface.searchSqlface(3, arg);
				log.send(DataType.basicType, "01198", "查询是否关注了---sql3", sql3);
				list1 = sqlUtil.getList(sql3);
				log.send(DataType.basicType, "01198", "查询是否关注了---list1", list1);
				if ("".equals(list1.get(0).get("target_follow_time"))) {
					String jsonadd = "{\"success\":\"0\"}";
					inOutUtil.return_ajax(jsonadd);
				} else {
					String jsonadd = "{\"success\":\"1\"}";
					inOutUtil.return_ajax(jsonadd);
				}
			} else {
				String jsonadd = "{\"success\":\"0\"}";
				inOutUtil.return_ajax(jsonadd);
			}
		}

	}

	/**
	 * 查询用户基本信息------01198 arg[2] id 用户id
	 * 
	 * @param arg
	 * @throws SQLException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void search_basic_personal_details(String[] arg) throws SQLException, ServletException, IOException {

		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询用户基本信息sql", sql);
		list = sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "查询用户基本信息-list", list);

		if (list.size() > 0) {
			int a = Integer.parseInt(list.get(0).get("balance").toString());
			int b = Integer.parseInt(list.get(0).get("total_revenue").toString());
			int c = Integer.parseInt(list.get(0).get("total_withdrawals").toString());
			list.get(0).put("able_withdrawals", b - c);

			list.get(0).put("my_totlequdou", a + b - c);
		}

		inOutUtil.return_ajax(JsonUtil.listToJson(list));

	}

	private void cashdraw_dou(String[] arg) throws SQLException, ServletException, IOException {
		int tot = Integer.parseInt(arg[3]) * 20;

		arg[1] = "search_basic_personal_details";
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询用户基本信息sql", sql);
		list = sqlUtil.getList(sql);
		log.send(DataType.basicType, "01198", "查询用户基本信息-list", list);
		int a = Integer.parseInt(list.get(0).get("total_revenue").toString());
		int b = Integer.parseInt(list.get(0).get("total_withdrawals").toString());
		if (tot <= (a - b)) {

			sql = "insert into cash_table (user_id,cash,out_biz_no,time) values ('" + arg[2] + "','" + arg[3] + "','"
					+ hm.order_create() + JyHelpManager.sb() + "',now())";
			sqlUtil.sqlExecute(sql);

			sql = "update user_data set total_withdrawals=total_withdrawals+" + tot + " where id=" + arg[2];
			sqlUtil.sqlExecute(sql);
		}

		sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01198", "查询用户基本信息sql", sql);
		list = sqlUtil.getList(sql);

		if (list.size() > 0) {
			int a1 = Integer.parseInt(list.get(0).get("balance").toString());
			int b1 = Integer.parseInt(list.get(0).get("total_revenue").toString());
			int c1 = Integer.parseInt(list.get(0).get("total_withdrawals").toString());
			list.get(0).put("able_withdrawals", b1 - c1);
		}

		inOutUtil.return_ajax(JsonUtil.listToJson(list));
	}
}
