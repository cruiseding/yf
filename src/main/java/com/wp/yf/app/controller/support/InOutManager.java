package com.wp.yf.app.controller.support;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InOutManager extends JyInOut {
	
	public InOutManager(String[] arg, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, SQLException {
		super(arg,request,response);
	}
}
