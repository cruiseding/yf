package com.wp.yf.app.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JyResultSetToListConverter {

	public static ArrayList<Map<String, Object>> ResultSetToList(ResultSet resSet) throws SQLException {
		String name = "";
		String value = "";
		ArrayList<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
		ResultSetMetaData resMetaData = resSet.getMetaData();
		int columnCount = resMetaData.getColumnCount();
		while (resSet.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();// 对应于每行的映射
			for (int i = 1; i <= columnCount; i++) {
				name = resMetaData.getColumnLabel(i);
				if (rowData.get(name) != null) {
					int z = 1;
					name = resMetaData.getColumnLabel(i) + z++;
				}

				if (resSet.getObject(i) != null && resMetaData.getColumnTypeName(i).equals("DATETIME")
						|| resMetaData.getColumnTypeName(i).equals("TIMESTAMP")) {
					value = resSet.getObject(i).toString();
					rowData.put(name, value.substring(0, value.length() - 2));
				} else {
					rowData.put(name, resSet.getObject(i));
				}

			}
			returnList.add(rowData);
		}
		return returnList;
	}
}
