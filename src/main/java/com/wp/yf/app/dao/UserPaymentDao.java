package com.wp.yf.app.dao;

import java.io.IOException;
import java.sql.SQLException;

import com.wp.yf.app.db.sql.SqlManager;
import com.wp.yf.app.db.sql.SqlManagerFace;

public class UserPaymentDao extends SqlManager implements SqlManagerFace {

	@Override
	public String addSqlface(int current, String[] arg) throws SQLException, IOException {
		
		switch (arg[1]) {
			case "report_record_add":
				if (current == 1) {
					// insert into <表名> [(<字段名1>[,..<字段名n > ])] values ( 值1 )[, ( 值n )];
					ressql = "insert into report_table (report_id,reported_id,reason,other_reason,report_time,result) "
							+ "values('" + arg[2] + "','" + arg[3] + "','" + arg[4] + "','" + arg[5] + "',now(),'待审核')";
				}
				break;
			// 查询是否添加黑名单
			case "blacklist_add":
				if (current == 0) {
					ressql = "select status from blacklist_table where user_id ='" + arg[2] + "' and black_id='" + arg[3]
							+ "'";
				} else if (current == 1) {
					ressql = "insert into blacklist_table (user_id,black_id,status) values('" + arg[2] + "','" + arg[3]
							+ "','1')";
				} else if (current == 2) {
					ressql = "update blacklist_table set status='1'  where user_id ='" + arg[2] + "' and black_id='"
							+ arg[3] + "'";
				} else if (current == 3) {
					ressql = "update blacklist_table set status='0'  where user_id ='" + arg[2] + "' and black_id='"
							+ arg[3] + "'";
				}
				break;
			// 爱碰视频一对一收支明细统计
			case "video_to_one_pay_add":
				if (current == -1) {// 查询女号每分钟资费
					ressql = "select videochat_price from roles_table where user_id='" + arg[3] + "'";
				} else if (current == 0) {// 添加视频通话记录
					ressql = "insert into call_detail_table (send_id,target_id,type,minutes,time,is_call) " + "values('"
							+ arg[2] + "','" + arg[3] + "','视频通话','" + arg[4] + "',now(),'1')";
				} else if (current == 1) {// 添加收入记录
					ressql = "insert into Income_details_table (user_id,pay_id,type,money,time,operation) " + "values('"
							+ arg[3] + "','" + arg[2] + "','视频通话','" + arg[6] + "',now(),'已到账')";
				} else if (current == 2) {// 查询女号总收入
					ressql = "select total_revenue from user_data where id='" + arg[3] + "'";
				} else if (current == 3) {// 修改女号总收入
					ressql = "update user_data set total_revenue='" + arg[7] + "' where id='" + arg[3] + "'";
				} else if (current == 4) {// 添加支出记录
					ressql = "insert into pay_table (user_id,target_id,type,num,time) " + "values('" + arg[2] + "','"
							+ arg[3] + "','视频通话','" + arg[6] + "',now())";
				}
				break;
			case "man_price_add":
				if (current == -1) {// 查询男号记录
					ressql = "select count(0) from roles_table where user_id='" + arg[2] + "'";
				} else if (current == 0) {// 修改记录
					ressql = "update roles_table set videochat_price='" + arg[3] + "',voicechat_price='" + arg[4]
							+ "',wordchat_price='" + arg[5] + "' " + "where user_id='" + arg[2] + "'";
				} else if (current == 1) {// 添加记录
					ressql = "insert into roles_table (user_id,videochat_price,voicechat_price,wordchat_price) "
							+ "values ('" + arg[3] + "','" + arg[3] + "','" + arg[4] + "','" + arg[5] + "')";
				}
				break;
		}
		return ressql;
	}

	@Override
	public String modSqlface(int current, String[] arg) throws SQLException, IOException {
		switch (arg[1]) {
			case "video_to_one_pay":
				if (current == -1) {// 查询男号充值可用余额
					ressql = "select balance from user_data where id ='" + arg[2] + "'";
				} else if (current == 0) {// 修改男号余额
					ressql = "update user_data set balance='" + arg[4] + "' where id ='" + arg[2] + "'";
				}
				break;
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

			case "look_cost_search":
				if (current == 0) {// 查询男号充值可用余额
					ressql = "select balance from user_data where id ='" + arg[2] + "'";
				} else if (current == 1) {// 修改男号余额
					ressql = "update user_data set balance='" + arg[5] + "' where id ='" + arg[2] + "'";
				} else if (current == 2) {// 查询女号总收入
					ressql = "select total_revenue from user_data where id='" + arg[3] + "'";
				} else if (current == 3) {// 增加女号总收入
					ressql = "update user_data set total_revenue='" + arg[6] + "' where id='" + arg[3] + "'";
				} else if (current == 4) {// 添加支出记录
					ressql = "insert into pay_table (user_id,target_id,type,num,time) " + "values('" + arg[2] + "','"
							+ arg[3] + "','" + arg[4] + "',250,now())";
				} else if (current == 5) {// 添加收入记录
					ressql = "insert into Income_details_table (user_id,pay_id,type,money,time,operation) " + "values('"
							+ arg[3] + "','" + arg[2] + "','" + arg[4] + "',250,now(),'已到账')";
				} else if (current == 6) {// 查询女号手机
					ressql = "select phone from user_data where id ='" + arg[3] + "'";
				} else if (current == 7) {// 查询女号微信
					ressql = "select wechat,wechat_name from user_data where id ='" + arg[3] + "'";
				} else if (current == 8) {// 查询女号qq
					ressql = "select qq from user_data where id ='" + arg[3] + "'";
				}
				break;
		}
		return ressql;
	}
}
