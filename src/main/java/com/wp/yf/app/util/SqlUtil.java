package com.wp.yf.app.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import com.wp.yf.app.db.JyResultSetToListConverter;
import com.wp.yf.app.db.pool.TomcatJdbcPool;

public class SqlUtil {

	TomcatJdbcPool sqlPool;

	Connection conn;

	public SqlUtil(String dbName) {
		sqlPool = TomcatJdbcPool.getInstance(dbName);
		conn = sqlPool.getConnection();
	}

	@Override
	public void finalize() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Map<String, Object>> getList(String sql) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list = JyResultSetToListConverter.ResultSetToList(rs);
		return list;
	}

	public int getInt(String sql) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return rs.getInt(1);
		} else {
			return 0;
		}
	}

	public int getCount(String sql) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return rs.getInt(1);
		} else {
			return -1;
		}
	}

	public String getString(String sql) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return rs.getString(1);
		} else {
			return "";
		}
	}

	public double getDouble(String sql) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return rs.getDouble(1);
		} else {
			return 0.0;
		}
	}

	public int sqlExecute(String sql) throws SQLException {
		Statement stmt = conn.createStatement();
		return stmt.executeUpdate(sql);
	}

	public ResultSet get_rs(String sql) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		return pstmt.executeQuery();
	}

	/**
	 * 2018-8-22 01202 更新批处理
	 * 
	 * @param sql
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	public int[] sqlBatchExecute(String sql, ArrayList<ArrayList<String>> list) {
		int[] set = new int[list.size()];
		try {
			conn.setAutoCommit(false);
			int count = 0;
			PreparedStatement pst = conn.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				count++;
				ArrayList<String> list1 = list.get(i);
				for (int j = 0; j < list1.size(); j++) {
					pst.setString(j + 1, list1.get(j));
				}
				pst.addBatch();// 加入批处理，进行打包
				if (count % 1000 == 0) {// 设置1000条提交一次，可以设置不同的大小；如50，100，500，1000等等
					set = pst.executeBatch();
					conn.commit();
					pst.clearBatch();

				}
			}
			set = pst.executeBatch();
			conn.commit();
			pst.close();

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				;
			}

		} finally {
			// 改回自动提交
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				;
			}
		}
		return set;
	}

}
