package com.wp.yf.app.util;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;



public class JyImageServlet {

	protected void doUpload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String fullPath = "img/goodsdes_images";
			
			String tmppath = request.getServletContext().getRealPath( "/" );
			File fullDir = new File(tmppath+fullPath);
			if (!fullDir.exists()) {
				fullDir.mkdirs();
			}
			
			MultipartResolver resolver = new CommonsMultipartResolver();
			if(resolver.isMultipart(request)) {
				long maxSize = 10 * 1024 * 1024;// 
				String allowFileExtList = "jar,exe,doc,docx,txt,html,xml,xls,pdf,jpg,png,PNG,gif,GIF,jpeg,JPEG,JPG,BMP,bmp";
				
				MultipartHttpServletRequest mpRequest = resolver.resolveMultipart(request);
				Map<String, MultipartFile> fileMap = mpRequest.getFileMap();
				String callback = request.getParameter("CKEditorFuncNum"); 
				Iterator<String> fns = fileMap.keySet().iterator(); 
				if(fns.hasNext()) {
					String fileName = fns.next();
					if(FilenameUtils.isExtension(fileName, StringUtils.split(allowFileExtList, ","))) {
						throw new IOException("上传文件不合法");
					}
					
					MultipartFile file = fileMap.get(fileName);
					if (file.getSize() > maxSize) {
						throw new IOException("上传文件大小已超上限");
					}
					response.setContentType("text/html;charset=gbk");// 
					fileName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") 
							+ RandomUtils.nextInt(0, 100) 
							+ FilenameUtils.getExtension(fileName);
					file.transferTo(new File(tmppath + fullPath + "/" + fileName));
					JyClusterSync.syncImages();
					PrintWriter out = response.getWriter(); 
				    out.println("<script type=\"text/javascript\">");  
			        out.println("window.parent.CKEDITOR.tools.callFunction(" + callback  
			                + ",'" +request.getContextPath()+"/"+fullPath + "/" +fileName + "','')");  
			        out.println("</script>");  
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
