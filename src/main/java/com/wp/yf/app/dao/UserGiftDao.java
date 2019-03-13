package com.wp.yf.app.dao;

import java.io.IOException;
import java.sql.SQLException;

import com.wp.yf.app.db.sql.SqlManager;
import com.wp.yf.app.db.sql.SqlManagerFace;

public class UserGiftDao extends SqlManager implements SqlManagerFace {

	public String addSqlface(int current, String[] arg) throws SQLException, IOException {
		switch (arg[1]) {
			case "ask_gift":
				if (current == 0) {// 查询礼物数据
					ressql = "select * from play_tour where id='" + arg[4] + "'";
				} else if (current == 1) {// 查询发起求赏的用户的信息
					ressql = "select * from user_data where id='" + arg[2] + "'";
				}
				break;
	
			case "give_gift":
				if (current == -1) {// 支出明细表 Red Envelope Rewards---红包打赏，，，，Has arrived--已到账
					// ressql = "insert into pay_details values
					// (id,"+arg[2]+",'礼物打赏',"+arg[4]+",now(),"+arg[3]+", "+arg[4]+","+arg[3]+")";
					ressql = "insert into pay_table (user_id, type, num, time, target_id) values('" + arg[2] + "','礼物打赏','"
							+ arg[4] + "',now(),'" + arg[3] + "')";
				} else if (current == 0) {// 主播收入明细表
	
					ressql = "INSERT INTO Income_details_table " + "(user_id,time,type,money,pay_id,operation) " + "VALUES("
							+ arg[3] + ",now(),'礼物打赏'," + arg[4] + "," + arg[2] + ",'已到账') ";
	
				} else if (current == 2) {// 修改用户V币余额
					ressql = "update user_data set balance = balance-'" + arg[4] + "' WHERE id = " + arg[2];
				} else if (current == 3) {// 主播修改V币余额
					ressql = "update user_data set total_revenue = total_revenue+'" + arg[4] + "' WHERE id = " + arg[3];
				} else if (current == 4) {// 查询余额
					ressql = "SELECT balance from user_data WHERE id = " + arg[2];
				} else if (current == 6) {// 插入礼物记录
					ressql = "insert into gift_record (gift_id,send_id,target_id,send_time) values('" + arg[5] + "','"
							+ arg[2] + "','" + arg[3] + "',now())";
				} else if (current == 7) {// 修改主播收到礼物数目
					ressql = "update roles_table set gifts_sum=gifts_sum+1 where user_id='" + arg[3] + "'";
				} else if (current == 8) {// 查询礼物信息
					ressql = "select * from play_tour where id='" + arg[5] + "'";
				}
				break;
	
			default:
				ressql = "";
				break;
		}

		return ressql;
	}

	@Override
	public String modSqlface(int current, String[] arg) throws SQLException, IOException {
		switch (arg[1]) {
			default:
				ressql = "";
		}
		return ressql;
	}

	@Override
	public String deleteSqlface(String[] arg) throws SQLException {
		switch (arg[1]) {

		default:
			ressql = "";
		}
		return ressql;
	}

	@Override
	public String searchSqlface(int current, String[] arg) throws SQLException, IOException {
		switch (arg[1]) {
			case "gift_count":
				if (current == 0) {
					if ("1".equals(arg[4])) // 查询用户收到的礼物数量
						ressql = "select gifts_sum from roles_table where user_id='" + arg[3] + "'";
					else if ("0".equals(arg[4])) // 查询用户送出的礼物数量
						ressql = "select count(*) from gift_record where send_id='" + arg[3] + "'";
				}
				break;
	
			case "gift_record":
				if (current == 0) {
					ressql = "select * from user_data where id='" + arg[3] + "'";
				} else if (current == 1) {
					ressql = "select * from play_tour";
				} else if (current == 2) {
					if ("1".equals(arg[4])) // 查询用户收到的礼物数量
						ressql = "select count(*) from gift_record where gift_id='" + arg[2] + "' and target_id='" + arg[3]
								+ "'";
					else if ("0".equals(arg[4])) // 查询用户送出的礼物数量
						ressql = "select count(*) from gift_record where gift_id='" + arg[2] + "' and send_id='" + arg[3] + "'";
				} else if (current == 3) {
					if ("1".equals(arg[4])) // 查询用户收到的礼物数量
						ressql = "select gifts_sum from roles_table where user_id='" + arg[3] + "'";
					else if ("0".equals(arg[4])) // 查询用户送出的礼物数量
						ressql = "select count(*) from gift_record where send_id='" + arg[3] + "'";
				}
				break;
	
			case "reward_gift":// 打赏礼物（女主播页面）
				if (current == 0) {
					ressql = "select * from play_tour order by price asc";
				}
				break;
			default:
				ressql = "";
		}
		return ressql;
	}

}
