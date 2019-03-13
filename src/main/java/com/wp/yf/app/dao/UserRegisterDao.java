package com.wp.yf.app.dao;

import java.io.IOException;
import java.sql.SQLException;

import com.wp.yf.app.db.sql.SqlManager;
import com.wp.yf.app.db.sql.SqlManagerFace;

public class UserRegisterDao extends SqlManager implements SqlManagerFace {

	@Override
	public String addSqlface(int current, String[] arg) throws SQLException, IOException {
		switch (arg[1]) {
			// 用户注册
			case "user_register":
				if (current == 0) {
					ressql = "insert into user_data (phone,user_photo,nickname,age,address,gender,registered_time) "
							+ "values(" + arg[2] + ",'" + arg[3] + "','" + arg[4] + "','" + arg[5] + "','" + arg[6] + "','"
							+ arg[7] + "',NOW())";
				} else if (current == 1) {
					ressql = "select * from user_data where phone='" + arg[2] + "'";
				} else if (current == 2) {
					ressql = "update user_data set user_photo='" + arg[3] + "',nickname='" + arg[4] + "',age='" + arg[5]
							+ "'," + "address='" + arg[6] + "',gender='" + arg[7] + "' where id='" + arg[10] + "'";
				}
				break;
		}
		return ressql;
	}

	@Override
	public String modSqlface(int current, String[] arg) throws SQLException, IOException {
		switch (arg[1]) {

		}
		return ressql;
	}

	@Override
	public String deleteSqlface(String[] arg) throws SQLException {
		switch (arg[1]) {

		}
		return ressql;
	}

	@Override
	public String searchSqlface(int current, String[] arg) throws SQLException, IOException {
		switch (arg[1]) {
			// 查询用户基本信息
			case "search_basic_personal_details":
				if (current == 0) {
					ressql = "select * from user_data where id='" + arg[2] + "'";
				}
				break;
			// 查询是否关注了
			case "search_is_attention":
				if (current == 0) {
					ressql = "select count(*) from relation_table where user_id='" + arg[2] + "' and target_id='" + arg[3]
							+ "'";
				} else if (current == 1) {
					ressql = "select user_follow_time from relation_table where user_id='" + arg[2] + "' and target_id='"
							+ arg[3] + "'";
				} else if (current == 2) {
					ressql = "select count(*) from relation_table where user_id='" + arg[3] + "' and target_id='" + arg[2]
							+ "'";
				} else if (current == 3) {
					ressql = "select target_follow_time from relation_table where user_id='" + arg[3] + "' and target_id='"
							+ arg[2] + "'";
				}
				break;
			// 查询他人详情页聊天价格信息
			case "search_chat_price_details":
				if (current == 0) {
					ressql = "select * from roles_table where user_id='" + arg[2] + "'";
				}
				break;
			// 根据手机号查询用户信息
			case "search_info_by_phone":
				if (current == 0) {
					ressql = "select * from user_data where phone='" + arg[2] + "'";
				}
				break;
			// 查询昵称库
			case "select_name":
				if (current == 0) {
					ressql = "select * from select_name_table";
				}
				break;
			// 查询拨打着余额与被拨打着状态
			case "search_balance_status":
				if (current == 0) {
					ressql = "select balance from user_data where id='" + arg[2] + "'";
				} else if (current == 1) {
					ressql = "select is_online from user_data where id='" + arg[3] + "'";
				}
				break;
			// 查询昵称是否存在
			case "nickname_is_exit":
				if (current == 0) {
					if (arg.length > 3) {
						ressql = "select count(*) from user_data where nickname='" + arg[2] + "' and id!=" + arg[3];
					} else {
						ressql = "select count(*) from user_data where nickname='" + arg[2] + "'";
					}
	
				}
				break;
		}
		return ressql;
	}

}
