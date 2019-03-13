package com.wp.yf.app.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wp.yf.app.constant.JyGlobalConstant;
import com.wp.yf.app.log.JyLogDetect;

/**
 * 文件上传的Controller
 * 
 * Servlet implementation class FileImageUploadServlet
 * 
 * 此处的文件上传比较简单没有处理各种验证，文件处理的错误等�? 如果�?要处理，请修改源代码即可�?
 * 
 * @Title:
 * @Description: 实现TODO
 * @Copyright:Copyright (c) 2011
 * @Company:易程科技股份有限公司
 * @Date:2012-7-22
 * @author longgangbai
 * @version 1.0
 */

@Controller
@RequestMapping("/uiface")
public class ImageController {
	
	private ServletFileUpload upload;
	private final long MAXSize = 4194304 * 2L;// 4*2MB
	private String filedir = null;
	
	String sqlUpdate1;

	// private String dizhi =
	// "http://120.27.98.128:9111/img/imgheadpic/";注意：可以看动态表是不是全名，是就要不加这个；不是全名，就要加上全地址
	private String dizhi = "http://" + JyGlobalConstant.getIp() + ":8090/img/imgheadpic/";
	// http://120.27.98.128:9112/img/imgheadpic/07.jpg
	// http://139.129.38.194:9000/logs/log1.htm

	@RequestMapping("/ImageServlet_01192")
	public void doUpload(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		JyLogDetect log = new JyLogDetect(request);
		log.send("01150", "arg:", 111);

		PrintWriter out = response.getWriter();
		FileItemFactory factory = new DiskFileItemFactory();// Create a factory
		this.upload = new ServletFileUpload(factory);// Create a new file upload
		this.upload.setSizeMax(this.MAXSize);// Set overall request size

		int j = -1;
		String fullPath = "img/imgheadpic";
		String tmppath = request.getSession().getServletContext().getRealPath("/");
		filedir = tmppath + fullPath;
		File fullDir = new File(tmppath + fullPath);
		if (!fullDir.exists()) {
			fullDir.mkdirs();
		}
		String fileName = StringUtils.EMPTY;
		try {
			List<FileItem> items = this.upload.parseRequest(new ServletRequestContext(request));
			if (items != null && !items.isEmpty()) {
				for (FileItem fileItem : items) {

					if (fileItem.isFormField()) {
						String user_id = fileItem.getString("UTF-8");
					} else {
						fileName = fileItem.getName();
						String type2 = fileName.substring(fileName.lastIndexOf("."));
						Random rnd = new Random();
						int r = rnd.nextInt(100);
						Date date2 = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
						String strDate2 = formatter.format(date2);
						fileName = strDate2 + r + type2;

						String filepath = filedir + File.separator + fileName;
						File file = new File(filepath);
						InputStream inputSteam = fileItem.getInputStream();
						BufferedInputStream fis = new BufferedInputStream(inputSteam);
						FileOutputStream fos = new FileOutputStream(file);
						int f;
						while ((f = fis.read()) != -1) {
							fos.write(f);
						}
						fos.flush();
						fos.close();
						fis.close();
						inputSteam.close();
					}
				}
			}
			log.send("01165", "arg:", fileName);
			String img = StringUtils.EMPTY;
			if (j == -1) { // 新图片
				log.send("01178", "arg:", sqlUpdate1);
				img = dizhi + fileName;
				log.send("01178", "img:", img);
			} else {
				// 否则在将我的相册后加上逗号
				img = dizhi + fileName;
				log.send("01165---", "img:", img);
			}

			JSONObject jsonObj0 = new JSONObject();
			jsonObj0.put("imgurl", img);
			out.write(jsonObj0.toString());
			log.send("01165---", "jsonObj0:", jsonObj0);

		} catch (FileUploadException e) {
			e.printStackTrace();
			JSONObject jsonObj0 = new JSONObject();
			jsonObj0.put("imgurl", "0");
			out.write(jsonObj0.toString());
		}
	}

}