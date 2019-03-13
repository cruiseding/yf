package com.wp.yf.app.controller.support;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

public interface InOutFace {
	
	public void addFace() throws SQLException, ServletException, IOException;

	public void modFace() throws SQLException, ServletException, IOException;

	public void deleteFace() throws SQLException, ServletException, IOException;

	public void searchFace() throws SQLException, ServletException, IOException;
	
}
