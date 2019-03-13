package com.wp.yf.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wp.yf.app.constant.JyGlobalConstant;
import com.wp.yf.app.log.JyLogDetect;
import com.wp.yf.app.log.JyLogDetect.DataType;
import com.wp.yf.app.util.JsonUtil;
import com.wp.yf.app.util.JyInOutUtil;
import com.wp.yf.app.util.SqlUtil;

@Controller
@RequestMapping("/uiface")
public class MemberController {


	@RequestMapping("/member107A")
	protected void handle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		String[] arg = JsonUtil.jsonReceive(request);
		
		SqlUtil sqlUtil = new SqlUtil(JyGlobalConstant.getDbBaseName());
		JyInOutUtil inOutUtil = new JyInOutUtil(arg, request, response);
		JyLogDetect log = new JyLogDetect(request);
		
		try {
			switch(arg[1]) {
				case "check_update": {
					String sql = "select * from updown";	//sqlmface.searchSqlface(0, arg);
					log.send(DataType.specialType, "01160", "check_update()-sql: ", sql);
					ArrayList<Map<String,Object>> list=sqlUtil.getList(sql);
					 log.send(DataType.specialType, "01160", "check_update()-list: ", list);
					inOutUtil.return_ajax(JsonUtil.listToJson(list));
					break;
				}
			}
		} catch(Exception e) {
			log.send(DataType.exceptionType, "01107", "memberAServletInOut_107-Exception: ", e);
		}
	}

}
