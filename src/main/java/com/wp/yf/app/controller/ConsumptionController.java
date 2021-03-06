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
import com.wp.yf.app.dao.UserPaymentDao;
import com.wp.yf.app.db.sql.SqlManagerFace;
import com.wp.yf.app.log.JyLogDetect;
import com.wp.yf.app.log.JyLogDetect.DataType;
import com.wp.yf.app.util.JsonUtil;

import net.coobird.thumbnailator.Thumbnails;

@Controller
@RequestMapping("/uiface")
public class ConsumptionController extends InOutManager implements InOutFace, ServletConfigAware {

	public ConsumptionController(String[] arg, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, SQLException {
		super(arg, request, response);
	}

	private ServletConfig servletConfig;

	protected ArrayList<Map<String, Object>> list;
	protected ArrayList<Map<String, Object>> list1;
	protected ArrayList<Map<String, Object>> list2;

	private SqlManagerFace sqlmface = new UserPaymentDao();

	@RequestMapping("/mA01201")
	protected void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] arg = null;
		JyLogDetect log = new JyLogDetect(request);

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		arg = JsonUtil.jsonReceive(request);
		log.send(DataType.specialType, "01201_____", "aipengServletInOut_01201_arg:", arg);

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
			log.send("01201", "Exception", e);
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
			// 爱碰添加举报信息记录
			case "report_record_add":// 180911创建
				report_record_add(arg);
				break;
			// 爱碰添加举报信息记录
			case "blacklist_add":// 180912创建
				blacklist_add(arg);
				break;
			// 爱碰视频一对一收支明细统计
			case "video_to_one_pay_add":// 180912创建
				video_to_one_pay_add(arg);
				break;
			case "man_price_add":
				man_price_add(arg);
				break;
		}
	}

	/*
	 * 男号价格添加---01201 18/09/16创建 arg[2] 男号id arg[3] 男号视频价格 arg[4] 男号语音价格 arg[5]
	 * 男号私信价格
	 */
	private void man_price_add(String[] arg) throws SQLException, IOException, ServletException {
		int count1 = 0, count2 = 0;
		String sql = sqlmface.addSqlface(-1, arg);
		log.send(DataType.basicType, "01201", "查询男号在角色表有无记录---sql:", sql);
		int count = sqlUtil.getInt(sql);
		log.send(DataType.basicType, "01201", " 查询男号在角色表有无记录---list1:", count);
		if (count != 0) {
			sql = sqlmface.addSqlface(0, arg);
			log.send(DataType.basicType, "01201", "修改记录---sql:", sql);
			count1 = sqlUtil.sqlExecute(sql);
			log.send(DataType.basicType, "01201", "修改记录---count1:", count1);
		} else {
			sql = sqlmface.addSqlface(1, arg);
			log.send(DataType.basicType, "01201", " 添加记录---sql:", sql);
			count2 = sqlUtil.sqlExecute(sql);
			log.send(DataType.basicType, "01201", " 添加记录---count2:", count2);

		}

		if (count1 != 0 || count2 != 0) {
			String jsonadd = "{\"success\":\"1\"}";
			inOutUtil.return_ajax(jsonadd);
		} else {
			String jsonadd = "{\"success\":\"0\"}";
			inOutUtil.return_ajax(jsonadd);
		}
	}

	/*
	 * 一对一视频结束时，统计通话记录及收支明细 爱碰视频一对一收支明细统计---01201 18/09/16创建 arg[2] 男号id arg[3] 女号id
	 * arg[4] 通话时间 arg[5] 通话整分钟时间 arg[6] 通话总费用 arg[7] 女号总收入
	 */
	private void video_to_one_pay_add(String[] arg) throws SQLException, IOException, ServletException {
		String sql = sqlmface.addSqlface(-1, arg);
		log.send(DataType.basicType, "01201", " 查询女号每分钟资费---sql:", sql);
		int videochat_price = sqlUtil.getInt(sql);
		log.send(DataType.basicType, "01201", " 查询女号每分钟资费---list1:", videochat_price);
		int time = Integer.parseInt(arg[5]);
		int sum = time * videochat_price;
		arg[6] = sum + "";

		sql = sqlmface.addSqlface(0, arg);
		log.send(DataType.basicType, "01201", "添加通话记录---sql:", sql);
		int a = sqlUtil.sqlExecute(sql);
		log.send(DataType.basicType, "01201", "添加通话记录---a:", a);

		sql = sqlmface.addSqlface(1, arg);
		log.send(DataType.basicType, "01201", "添加收入明细记录---sql:", sql);
		int b = sqlUtil.sqlExecute(sql);
		log.send(DataType.basicType, "01201", "添加收入明细记录---b:", b);

		sql = sqlmface.addSqlface(2, arg);
		log.send(DataType.basicType, "01201", "查询女号总收入---sql:", sql);
		int c = sqlUtil.getInt(sql);
		log.send(DataType.basicType, "01201", "查询女号总收入---c:", c);
		arg[7] = c + sum + "";
		sql = sqlmface.addSqlface(3, arg);
		log.send(DataType.basicType, "01201", "修改女号总收入---sql:", sql);
		int d = sqlUtil.sqlExecute(sql);
		log.send(DataType.basicType, "01201", "修改女号总收入---d:", d);

		sql = sqlmface.addSqlface(4, arg);
		log.send(DataType.basicType, "01201", "添加支出明细记录---sql:", sql);
		int e = sqlUtil.sqlExecute(sql);
		log.send(DataType.basicType, "01201", "添加支出明细记录---c:", e);

		if (a != 0 && b != 0 && c != 0 && d != 0 && e != 0) {
			String jsonadd = "{\"success\":\"1\"}";
			inOutUtil.return_ajax(jsonadd);
		} else {
			String jsonadd = "{\"success\":\"0\"}";
			inOutUtil.return_ajax(jsonadd);
		}
	}

	/*
	 * 黑名单添加---01201 180912创建 arg[2] user_id arg[3] black_id
	 */
	private void blacklist_add(String[] arg) throws SQLException, IOException, ServletException {
		String sql = sqlmface.addSqlface(0, arg);
		log.send(DataType.specialType, "01201", "爱碰黑名单记录或状态查询-sql", sql);
		String status = sqlUtil.getString(sql);
		log.send(DataType.specialType, "01201", "爱碰黑名单记录或状态查询-status", status);
		int success = 0;
		if ("".equals(status)) {
			sql = sqlmface.addSqlface(1, arg);
			log.send(DataType.specialType, "01201", "爱碰黑名单记录插入-sql", sql);
			success = sqlUtil.sqlExecute(sql);
			log.send(DataType.specialType, "01201", "爱碰黑名单记录插入-success", success);
		} else if ("0".equals(status)) {
			sql = sqlmface.addSqlface(2, arg);
			log.send(DataType.specialType, "01201", "爱碰黑名单记录状态修改1-sql", sql);
			success = sqlUtil.sqlExecute(sql);
			log.send(DataType.specialType, "01201", "爱碰黑名单记录状态修改1-success", success);
		} else if ("1".equals(status)) {
			sql = sqlmface.addSqlface(3, arg);
			log.send(DataType.specialType, "01201", "爱碰黑名单记录状态修改0-sql", sql);
			success = sqlUtil.sqlExecute(sql);
			log.send(DataType.specialType, "01201", "爱碰黑名单记录状态修改0-success", success);
		}

		if (success == 1) {
			// 修改成功
			String jsonadd = "{\"success\":\"1\"}";
			inOutUtil.return_ajax(jsonadd);
		} else {
			// 修改失败
			String jsonadd = "{\"success\":\"0\"}";
			inOutUtil.return_ajax(jsonadd);
		}
	}

	/*
	 * 爱碰添加举报信息记录---01201 180911创建 arg[2] 举报者id arg[3] 被举报者id arg[4] 举报原因 arg[5]
	 * 其他举报原因
	 */
	private void report_record_add(String[] arg) throws SQLException, IOException, ServletException {

		String sql = sqlmface.addSqlface(1, arg);
		log.send(DataType.specialType, "01201", "爱碰添加举报信息记录-sql", sql);
		int success = sqlUtil.sqlExecute(sql);
		log.send(DataType.specialType, "01201", "爱碰添加举报信息记录-success", success);

		// if (count == 0) {
		// String sql1 = sqlmface.addSqlface(2, arg);
		// log.send(DataType.specialType, "01201", "一键报名信息(添加)-sql1", sql1);
		// success1 = sqlUtil.sql_exec(sql1);
		// log.send(DataType.specialType, "01201", "一键报名信息-success1", success1);
		// } else {
		// String sql2 = sqlmface.addSqlface(3, arg);
		// log.send(DataType.specialType, "01201", "一键报名信息(修改)-sql2", sql2);
		// success2 = sqlUtil.sql_exec(sql2);
		// log.send(DataType.specialType, "01201", "一键报名信息-success2", success2);
		// }

		if (success == 1) {
			// 修改成功
			String jsonadd = "{\"success\":\"1\"}";
			inOutUtil.return_ajax(jsonadd);
		} else {
			// 修改失败
			String jsonadd = "{\"success\":\"0\"}";
			inOutUtil.return_ajax(jsonadd);
		}
	}

	@Override
	public void modFace() throws SQLException, ServletException, IOException {
		switch (arg[1]) {
		// 爱碰视频一对一按分钟扣费
		case "video_to_one_pay":// 180912创建
			video_to_one_pay(arg);
			break;

		}
	}

	/*
	 * 爱碰视频一对一按分钟扣费---01201 18/09/16创建 arg[2] 男号id arg[3] 主播id arg[4] 男号余额 arg[5]
	 * 女号视频价格/分
	 */
	private void video_to_one_pay(String[] arg) throws SQLException, IOException, ServletException {
		int sum;
		String sql = sqlmface.modSqlface(-1, arg);
		log.send(DataType.basicType, "01201", " 查询男号充值可用余额---sql:", sql);
		list1 = sqlUtil.getList(sql);
		log.send(DataType.basicType, "01201", " 查询男号充值可用余额---list1:", list1);
		int balance = Integer.parseInt(list1.get(0).get("balance").toString());
		int videochat_price = Integer.parseInt(arg[5]);

		if (balance >= videochat_price) {
			sum = balance - videochat_price;
			arg[4] = sum + "";
			sql = sqlmface.modSqlface(0, arg);
			log.send(DataType.basicType, "01201", " 修改男号可用余额---sql:", sql);
			int success = sqlUtil.sqlExecute(sql);
			log.send(DataType.basicType, "01201", " 修改男号可用余额---success:", success);

			if (sum >= videochat_price) {
				String jsonadd = "{\"success\":\"1\"}";// 下一分钟够扣费
				inOutUtil.return_ajax(jsonadd);
			} else {
				String jsonadd = "{\"success\":\"2\"}";// 下一分钟不够扣费
				inOutUtil.return_ajax(jsonadd);
			}

//				
//				if(success==1){
//					list.get(0).put("status", "1");
//					list.get(0).put("success", "1");
//					inOutUtil.return_ajax(JsonUtil.listToJson(list));
//				}else{
//					list.get(0).put("status", "1");
//					list.get(0).put("success", "0");
//					inOutUtil.return_ajax(JsonUtil.listToJson(list));
//				}
		} else {
			String jsonadd = "{\"success\":\"0\"}";
			inOutUtil.return_ajax(jsonadd);
//				list.get(0).put("status", "0");
//				list.get(0).put("success", "1");
//				inOutUtil.return_ajax(JsonUtil.listToJson(list));
		}

//			sql = sqlmface.searchSqlface(0, arg);
//			log.send(DataType.basicType, "01201", " 提现明细查询---sql:", sql);
//			list1 = sqlUtil.get_list(sql);
//			log.send(DataType.basicType, "01201", " 提现明细查询---list1:", list1);
		//
//			sql = sqlmface.searchSqlface(1, arg);
//			log.send(DataType.basicType, "01201", " 提现成功总数---sql:", sql);
//			int sum = sqlUtil.get_int(sql);
//			log.send(DataType.basicType, "01201", " 提现成功总数---sum:", sum);
//			for (int i = 0; i < list1.size(); i++) {
//				list1.get(i).put("sum", sum);
//			}
//			inOutUtil.return_ajax(JsonUtil.listToJson(list1));

	}

	@Override
	public void deleteFace() throws SQLException, ServletException, IOException {
		switch (arg[1]) {

		}
	}

	@Override
	public void searchFace() throws SQLException, ServletException, IOException {
		switch (arg[1]) {
		// 查看女号手机
		case "look_cost_search":// 180918创建
			look_cost_search(arg);
			break;
		}
	}

	/*
	 * 查看女号手机---01201 180918创建 arg[2] 男号id arg[3] 女号id arg[4] 查看某某 arg[5] 男号余额(扣除后的)
	 * arg[6] 女号总收入
	 */
	private void look_cost_search(String[] arg) throws SQLException, IOException, ServletException {
		list1 = new ArrayList<Map<String, Object>>();
		// 查询男号余额
		String sql = sqlmface.searchSqlface(0, arg);
		log.send(DataType.basicType, "01201", " 查询男号余额---sql:", sql);
		String yue = sqlUtil.getString(sql);
		log.send(DataType.basicType, "01201", " 查询男号余额---list1:", yue);

		int balance = Integer.parseInt(yue) - 250;

		if (balance < 0) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			rowData.put("result", "0");
			rowData.put("qq", "");
			rowData.put("wechat_name", "");
			rowData.put("wechat", "");
			rowData.put("phone", "");
			list.add(rowData);
			inOutUtil.return_ajax(JsonUtil.listToJson(list));
			return;
		}

//		    double d = Double.valueOf(yue);
//		    double dd = Math.round(d*100);
//		    double balance =(dd-25000)/100;
		arg[5] = balance + "";
		// 扣除男号余额
		sql = sqlmface.searchSqlface(1, arg);
		log.send(DataType.basicType, "01201", " 扣除男号余额---sql:", sql);
		int success = sqlUtil.sqlExecute(sql);
		log.send(DataType.basicType, "01201", " 扣除男号余额---success:", success);

		// 查询女号总收入
		sql = sqlmface.searchSqlface(2, arg);
		log.send(DataType.basicType, "01201", "查询女号总收入---sql:", sql);
		String c = sqlUtil.getString(sql);
		log.send(DataType.basicType, "01201", "查询女号总收入---c:", c);
		int ba = Integer.parseInt(c) + 250;
//		    double dc = Double.valueOf(c);
//		    double ddc = Math.round(dc*100);
//		    double balancec =(ddc+25000)/100;
		arg[6] = ba + "";

		sql = sqlmface.searchSqlface(3, arg);
		log.send(DataType.basicType, "01201", "增加女号收入---sql:", sql);
		int suc = sqlUtil.sqlExecute(sql);
		log.send(DataType.basicType, "01201", "增加女号收入---c:", c);

		String weeksql = "update user_data set week_inout=week_inout+250 where id ='" + arg[2] + "' or id='" + arg[3]
				+ "'";
		sqlUtil.sqlExecute(weeksql);

		if (success == 1 && suc == 1) {

			sql = sqlmface.searchSqlface(4, arg);
			log.send(DataType.basicType, "01201", "添加支出记录---sql:", sql);
			int a = sqlUtil.sqlExecute(sql);
			log.send(DataType.basicType, "01201", "添加支出记录--ab:", a);

			sql = sqlmface.searchSqlface(5, arg);
			log.send(DataType.basicType, "01201", "添加收益记录---sql:", sql);
			int b = sqlUtil.sqlExecute(sql);
			log.send(DataType.basicType, "01201", "添加收益记录---b:", b);

			if ("查看手机".equals(arg[4])) {
				sql = sqlmface.searchSqlface(6, arg);
				log.send(DataType.basicType, "01201", " 查询女号手机---sql:", sql);
				list = sqlUtil.getList(sql);
				log.send(DataType.basicType, "01201", " 查询女号手机---list:", list);
				if (a == 1 && b == 1 && list.size() != 0) {
					if (a == 1 && b == 1 && list.size() != 0) {
						list.get(0).put("qq", "");
						list.get(0).put("wechat", "");
						list.get(0).put("wechat_name", "");
						list.get(0).put("result", "1");
						inOutUtil.return_ajax(JsonUtil.listToJson(list));
					}
				}
			} else if ("查看微信".equals(arg[4])) {
				sql = sqlmface.searchSqlface(7, arg);
				log.send(DataType.basicType, "01201", " 查询女号微信---sql:", sql);
				list = sqlUtil.getList(sql);
				log.send(DataType.basicType, "01201", " 查询女号微信---list:", list);
				if (a == 1 && b == 1 && list.size() != 0) {
					list.get(0).put("qq", "");
					list.get(0).put("phone", "");
					list.get(0).put("result", "1");
					inOutUtil.return_ajax(JsonUtil.listToJson(list));
				}
			} else if ("查看qq".equals(arg[4])) {
				sql = sqlmface.searchSqlface(8, arg);
				log.send(DataType.basicType, "01201", " 查询女号qq---sql:", sql);
				list = sqlUtil.getList(sql);
				log.send(DataType.basicType, "01201", " 查询女号qq---list:", list);
				if (a == 1 && b == 1 && list.size() != 0) {
					list.get(0).put("phone", "");
					list.get(0).put("wechat", "");
					list.get(0).put("wechat_name", "");
					list.get(0).put("result", "1");
					inOutUtil.return_ajax(JsonUtil.listToJson(list));
				}
			}

		}
	}

	@Override
	public void setServletConfig(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}

}
