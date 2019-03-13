package com.wp.yf.app.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JyInOutUtil {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected PrintWriter out = null;

	public JyInOutUtil(String[] arg, HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.request = request;
		this.response = response;

		this.response.setCharacterEncoding("UTF-8");
		this.response.setContentType("text/html;charset=UTF-8");
		out = this.response.getWriter();
	}

	// 返回列表1
	public void return_list(ArrayList<Map<String, Object>> list, String jsp) throws ServletException, IOException {

		request.setAttribute("reList", list);
		request.getRequestDispatcher(jsp).forward(request, response);
	}

	public void return_list5(ArrayList<Map<String, Object>> list0, ArrayList<Map<String, Object>> list1,
			ArrayList<Map<String, Object>> list2, ArrayList<Map<String, Object>> list3,
			ArrayList<Map<String, Object>> list4, ArrayList<Map<String, Object>> list5, String jsp)
			throws ServletException, IOException {

		request.setAttribute("list0", list0);
		request.setAttribute("list1", list1);
		request.setAttribute("list2", list2);
		request.setAttribute("list3", list3);
		request.setAttribute("list4", list4);
		request.setAttribute("list5", list5);
		request.getRequestDispatcher(jsp).forward(request, response);
	}

	public void return_list(ArrayList<Map<String, Object>> list, String json, String jsp)
			throws ServletException, IOException {

		request.setAttribute("reList", list);
		request.setAttribute("json", json);
		request.getRequestDispatcher(jsp).forward(request, response);
	}

	// 返回列表1
	public void return_listnum(ArrayList<Map<String, Object>> list, String num, String jsp)
			throws ServletException, IOException {

		request.setAttribute("reList", list);
		request.setAttribute("num", num);

		request.getRequestDispatcher(jsp).forward(request, response);
	}

	// 返回列表1,列表2
	public void return_list(ArrayList<Map<String, Object>> list, ArrayList<Map<String, Object>> list1, String jsp)
			throws ServletException, IOException {
		request.setAttribute("reList", list);
		request.setAttribute("reList1", list1);

		request.getRequestDispatcher(jsp).forward(request, response);
	}

	// 返回列表1,列表2
	public void return_list(ArrayList<Map<String, Object>> list, String a, String b, String c, String jsp)
			throws ServletException, IOException {
		request.setAttribute("reList", list);
		request.setAttribute("sort2id", a);
		request.setAttribute("sort12name", b);
		request.setAttribute("sort3name", c);
		request.getRequestDispatcher(jsp).forward(request, response);
	}

	public void return_list(ArrayList<Map<String, Object>> list, String a, String b, String jsp)
			throws ServletException, IOException {
		request.setAttribute("reList", list);
		request.setAttribute("sort2id", a);
		request.setAttribute("sort2name", b);
		request.getRequestDispatcher(jsp).forward(request, response);
	}

	public void toWX(String url) throws ServletException, IOException {

		// response.sendRedirect("http://chaochaoa.mingweishipin.com:80/uiface/"+jsp);
		response.sendRedirect(url);
		return;

	}

	public void return_list(ArrayList<Map<String, Object>> list, ArrayList<Map<String, Object>> list1, String json,
			String jsp) throws ServletException, IOException {
		request.setAttribute("reList", list);
		request.setAttribute("reList1", list1);
		request.setAttribute("json", json);
		request.getRequestDispatcher(jsp).forward(request, response);
	}

	public void return_list(ArrayList<Map<String, Object>> list, ArrayList<Map<String, Object>> list1,
			ArrayList<Map<String, Object>> list2, String jsp) throws ServletException, IOException {
		request.setAttribute("reList", list);
		request.setAttribute("reList1", list1);
		request.setAttribute("reList2", list2);
		request.getRequestDispatcher(jsp).forward(request, response);
	}

	// 返回列表1,列表2,列表3
	public void return_list(ArrayList<Map<String, Object>> list, ArrayList<Map<String, Object>> list1,
			ArrayList<Map<String, Object>> list2, ArrayList<Map<String, Object>> list3, String json, String jsp)
			throws ServletException, IOException {
		request.setAttribute("reList", list);
		request.setAttribute("reList1", list1);
		request.setAttribute("reList2", list2);
		request.setAttribute("reList3", list3);
		request.setAttribute("json", json);
		request.getRequestDispatcher(jsp).forward(request, response);
	}

	// 返回列表1,页码信息
	public void return_listpage(ArrayList<Map<String, Object>> list, int[] no, String jsp)
			throws ServletException, IOException {
		request.setAttribute("reList", list);
		request.setAttribute("pageNo", no);
		request.getRequestDispatcher(jsp).forward(request, response);
	}

	// 返回列表1,页码信息,数字
	public void return_listpage(ArrayList<Map<String, Object>> list, int[] no, String num, String jsp)
			throws ServletException, IOException {
		request.setAttribute("reList", list);
		request.setAttribute("pageNo", no);
		request.setAttribute("num", num);

		request.getRequestDispatcher(jsp).forward(request, response);
	}

	// 返回列表1,页码信息,数字
	public void return_listpage(ArrayList<Map<String, Object>> list, int[] no, String num, String json, String jsp)
			throws ServletException, IOException {
		request.setAttribute("reList", list);
		request.setAttribute("pageNo", no);
		request.setAttribute("num", num);
		request.setAttribute("json", json);

		request.getRequestDispatcher(jsp).forward(request, response);
	}

	// 返回列表1,页码信息,数字
	public void return_listpage(ArrayList<Map<String, Object>> list, int[] no, String num, String json, String num1,
			String json1, String num2, String json2, String jsp) throws ServletException, IOException {
		request.setAttribute("reList", list);
		request.setAttribute("pageNo", no);
		request.setAttribute("num", num);
		request.setAttribute("json", json);
		request.setAttribute("num1", num1);
		request.setAttribute("json1", json1);
		request.setAttribute("num2", num2);
		request.setAttribute("json2", json2);
		request.getRequestDispatcher(jsp).forward(request, response);
	}

	public void return_listpage(ArrayList<Map<String, Object>> list, int[] no, String num, String json, String num1,
			String json1, String jsp) throws ServletException, IOException {
		request.setAttribute("reList", list);
		request.setAttribute("pageNo", no);
		request.setAttribute("num", num);
		request.setAttribute("json", json);
		request.setAttribute("num1", num1);
		request.setAttribute("json1", json1);
		request.getRequestDispatcher(jsp).forward(request, response);
	}

	// 返回列表1,列表2,页码信息
	public void return_listpage(ArrayList<Map<String, Object>> list, ArrayList<Map<String, Object>> sort1, int[] no,
			String jsp) throws ServletException, IOException {
		request.setAttribute("reList", list);
		request.setAttribute("resort1", sort1);
		request.setAttribute("pageNo", no);

		request.getRequestDispatcher(jsp).forward(request, response);
	}

	// 返回列表1,列表2,列表3,页码信息
	public void return_listpage(ArrayList<Map<String, Object>> list, ArrayList<Map<String, Object>> sort1,
			ArrayList<Map<String, Object>> sort2, int[] no, String json, String jsp)
			throws ServletException, IOException {

		request.setAttribute("reList", list);
		request.setAttribute("resort1", sort1);
		request.setAttribute("resort2", sort2);
		request.setAttribute("pageNo", no);
		request.setAttribute("json", json);

		request.getRequestDispatcher(jsp).forward(request, response);
	}

	// 返回列表json,页码信息
	public void return_ajax(String json) throws ServletException {
		out.write(json);
	}

	// 跳转jsp
	public void return_only(String jsp) throws ServletException, IOException {

		response.sendRedirect("http://xinliao.mingweishipin.cn/uiface1/" + jsp);
		// response.sendRedirect("http://localhost:8080/GrapLottery/uiface/" + jsp);
		// response.sendRedirect("http://121.42.136.119:8082/uiface/"+jsp);
		// response.sendRedirect("http://chou.mingweishipin.com/uiface/" + jsp);
		return;
	}

	// 112传递页面传入参数
	public void return_arg(String[] arg, String jsp) throws ServletException, IOException {
		request.setAttribute("arg", arg);
		request.getRequestDispatcher(jsp).forward(request, response);

	}

	public void return_str(String arg, String jsp) throws ServletException, IOException {
		request.setAttribute("str", arg);
		request.getRequestDispatcher(jsp).forward(request, response);

	}

	public HttpServletRequest getRequest() {
		// TODO Auto-generated method stub
		return request;
	}

	public HttpServletResponse getResponse() {
		// TODO Auto-generated method stub
		return response;
	}

}
