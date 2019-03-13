package com.wp.yf.app.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	public static String[] jsonReceive(HttpServletRequest request) throws UnsupportedEncodingException {
		ArrayList<String> jsonMap = new ArrayList<String>();

		String jstr = request.getParameter("Jhidden");
		String pName = "";
		if ("".equals(jstr) || jstr == null) {
			Enumeration<String> name = request.getParameterNames();
			while (name.hasMoreElements()) {
				pName = (String) name.nextElement();
				jsonMap.add(request.getParameter(pName));
			}
		} else {
			Iterator<String> it;
			String key = "";
			String value = "";
			JSONArray jArray;
			JSONObject jObject;
			try {
				jArray = new JSONArray(jstr);
				for (int i = 0; i < jArray.length(); i++) {
					jObject = (JSONObject) jArray.get(i);
					it = jObject.keys();
					key = it.next().toString();
					value = jObject.getString(key);
					jsonMap.add(value);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return jsonMap.toArray(new String[jsonMap.size()]);
	}

	public static String mapToJson(Map<String, ArrayList<String>> map) {
		StringBuffer json = new StringBuffer("{");
		for (String key : map.keySet()) {
			json.append("\"" + key + "\":");
			json.append("{");
			int i = 0;
			for (String value : map.get(key)) {

				json.append("\"" + i++ + "\":\"" + value + "\",");

			}
			json.deleteCharAt(json.length() - 1);
			json.append("},");
		}

		json.deleteCharAt(json.length() - 1);
		json.append("}");
		return json.toString();
	}

	public static String mapToJson2(Map<String, Object> map) {
		StringBuffer json = new StringBuffer("{");
		for (String key : map.keySet()) {
			json.append("\"" + key + "\":\"" + map.get(key) + "\",");
		}

		json.deleteCharAt(json.length() - 1);
		json.append("}");
		return json.toString();
	}

	public static String listToJson(ArrayList<Map<String, Object>> list) {
		StringBuffer json = new StringBuffer("[");
		for (Map<String, Object> map : list) {
			json.append("{");
			for (Entry<String, Object> entry : map.entrySet()) {
				if (!"good_image_text".equals(entry.getKey())) {
					json.append("\"" + entry.getKey() + "\":\"" + entry.getValue() + "\",");
				}
			}
			json.deleteCharAt(json.length() - 1);
			json.append("},");
		}

		json.deleteCharAt(json.length() - 1);

		if (json.length() > 0) {
			json.append("]");
		}
		return json.toString();
	}

	public static String listPageToJson(ArrayList<Map<String, Object>> list, int[] pages) {

		StringBuffer json = new StringBuffer(listToJson(list));
		if (json.length() > 0) {
			json.deleteCharAt(json.length() - 1);
		}
		if (json.length() == 0) {
			json.append("[{\"totlePage\":\"" + pages[0] + "\",\"totle\":\"" + pages[1] + "\",\"current\":\"" + pages[2]
					+ "\",\"lastcount\":\"" + pages[3] + "\",\"pagenum\":\"" + pages[4] + "\"}");

		} else {
			json.append(",{\"totlePage\":\"" + pages[0] + "\",\"totle\":\"" + pages[1] + "\",\"current\":\"" + pages[2]
					+ "\",\"lastcount\":\"" + pages[3] + "\",\"pagenum\":\"" + pages[4] + "\"}");
		}

		if (json.length() > 0) {
			json.append("]");
		}
		return json.toString();

	}

}
