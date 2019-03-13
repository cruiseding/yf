package com.wp.yf.app.controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;















import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
/*import com.net.feimiaoquan.classroot.interface1.mAfmqInOutFace;*/
import com.net.aipeng.classroot.interface1.mA.aipengInOutFace;
import com.net.aipeng.classroot.interface4.JsonUtil;
import com.net.aipeng.classroot.interface4.JyLogDetect;
import com.net.aipeng.classroot.interface4.ThumbImage;
import com.net.aipeng.classroot.interface4.JyLogDetect.DataType;




@WebServlet("/uiface/member01198")
public class aipengServletInOut_01198  extends HttpServlet {

	
	private static final long serialVersionUID = -1204545408335287565L;


	public aipengServletInOut_01198() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] arg = null;
		aipengInOutFace inOutFace;
		JyLogDetect log = new JyLogDetect(request);

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		arg = JsonUtil.jsonReceive(request);
		log.send(DataType.specialType, "01165_____", "vliaoServletInOut_01165_arg:", arg);

		Map<String, String> map = new HashMap<String, String>();
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		
		
		
//		arg = JsonUtil.jsonReceive(request);
	/*	if(arg[0].contains("user")){
			if(map.containsKey("userid")) {
				arg[2]=map.get("userid");
			}
		}*/
		
		
		try {
			switch (arg[0]) {
			
			case "A-user-add":
				inOutFace = new aipengInoutUser_01198(arg, request, response);
				inOutFace.addface();
				break;
			case "A-user-mod":
				inOutFace = new aipengInoutUser_01198(arg, request, response);
				inOutFace.modface();
				break;
			case "A-user-search":
				inOutFace =new aipengInoutUser_01198(arg, request, response);
				inOutFace.searchface();
				break;
			case "A-user-delete":
				inOutFace=new aipengInoutUser_01198(arg, request, response);
			    inOutFace.deleteface();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.send("01165", "Exception", e);
		}
	}

	// 含有图片内容提交
		public String[] upfile(String[] arg,HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException, SmartUploadException {
			JyLogDetect log = new JyLogDetect(request);
			int argSize = 0;
			String path = "img/article/";
			String loadpath = this.getServletConfig().getServletContext().getRealPath("/");
			String fullPath = loadpath + path;
			File fullDir = new File(fullPath);
			if (!fullDir.exists()) {
				fullDir.mkdirs();
			}
			SmartUpload smartUpload = new SmartUpload();
			long maxSize = 10 * 1024 * 1024;//
			String allowFileExtList = "jar,exe,doc,docx,txt,html,xml,xls,pdf,jpg,png,PNG,gif,GIF,jpeg,JPEG,JPG,BMP,bmp";
			smartUpload.initialize(this.getServletConfig(), request, response);
			smartUpload.setMaxFileSize(maxSize);
			smartUpload.setAllowedFilesList(allowFileExtList);
			smartUpload.upload();
			switch (arg[1]) {
			case "seller_modify_phone":case "seller_modify": {
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
					com.jspsmart.upload.File smartFile = smartUpload.getFiles()
							.getFile(i);
					if (!smartFile.isMissing()) {
						String fileName = smartFile.getFileName();
						String type2 = fileName
								.substring(fileName.lastIndexOf("."));
						Random rnd = new Random();
						int r = rnd.nextInt(100);
						Date date2 = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyyMMddHHmmss");
						String strDate2 = formatter.format(date2);
						fileName = strDate2 + r + type2;
						smartFile.saveAs(fullPath + "//" + fileName,
								com.jspsmart.upload.File.SAVEAS_PHYSICAL);
	
						// names[i] = path + fileName;
						// thumbimage.thumbnailImage(fullPath + fileName, 300, 240);
	
						File imgFile = new File(fullPath + "/" + fileName);
						Image img = ImageIO.read(imgFile);
						int width = img.getWidth(null);
						int height = img.getHeight(null);
						Thumbnails
								.of(fullPath + "/" + fileName)
								.size(width, height)
								.outputQuality(0.9f)
								.outputFormat("jpg")
								.toFile(fullPath
										+ "/thumb_"
										+ fileName.substring(0,
												fileName.lastIndexOf(".")));
	
						goodinfo[7 + i] = path + "thumb_"
								+ fileName.substring(0, fileName.lastIndexOf("."))
								+ ".jpg";
					}
	
				}
				return goodinfo;
			}
			
			default :{
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
				String[] names = new String[4];
				ThumbImage thumbimage = new ThumbImage();
	
				for (int i = 0; i < smartUpload.getFiles().getCount(); i++) {
					com.jspsmart.upload.File smartFile = smartUpload.getFiles()
							.getFile(i);
					if (!smartFile.isMissing()) {
						String fileName = smartFile.getFileName();
						String type2 = fileName
								.substring(fileName.lastIndexOf("."));
						Random rnd = new Random();
						int r = rnd.nextInt(100);
						Date date2 = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
						String strDate2 = formatter.format(date2);
						fileName = strDate2 + r + type2;
						smartFile.saveAs(fullPath + "//" + fileName,com.jspsmart.upload.File.SAVEAS_PHYSICAL);
	
						// names[i] = path + fileName;
						// thumbimage.thumbnailImage(fullPath + fileName, 300, 240);
	
						File imgFile = new File(fullPath + "/" + fileName);
						Image img = ImageIO.read(imgFile);
						int width = img.getWidth(null);
						int height = img.getHeight(null);
						Thumbnails
								.of(fullPath + "/" + fileName)
								.size(width, height)
								.outputQuality(0.9f)
								.outputFormat("jpg")
								.toFile(fullPath
										+ "/thumb_"
										+ fileName.substring(0,
												fileName.lastIndexOf(".")));
	
						goodinfo[6 + i] = path + "thumb_"
								+ fileName.substring(0, fileName.lastIndexOf("."))
								+ ".jpg";
					}
	
				}
				return goodinfo;
			}
			}
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
