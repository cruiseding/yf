package com.wp.yf.app.dao;

import java.io.IOException;
import java.sql.SQLException;

import com.wp.yf.app.db.sql.SqlManager;
import com.wp.yf.app.db.sql.SqlManagerFace;

public class UserChatDao extends SqlManager implements SqlManagerFace {

	@Override
	public String addSqlface(int current, String[] arg) throws SQLException, IOException {
		
		switch (arg[1]) {
			// withdraw cash
			case "withdraw_cash":
				if (current == 0) {
					ressql = "insert into cash_table (user_id,out_biz_no,cash,status,time,c_type) " + "values (" + arg[2]
							+ ",'" + arg[0] + "'," + arg[3] + ",'申请中',now(),'1')";
				} else if (current == 1) {
					ressql = "update user_data set ableinvite_price=ableinvite_price-'" + arg[3] + "' where id='" + arg[2]
							+ "'";
				}
				break;
	//		case "my_visitor"://我的访客
	//			if(current == 1){
	//				ressql = "INSERT INTO relational_table(user_id,visitor_id,visitor_time) "
	//						+ "VALUES ("+arg[2]+",'"+arg[3]+"',now())";
	//			}else if (current == 0){
	//				ressql="select visitor_id from relational_table where user_id=1";
	//			}
	//			break;
	//		case "add_fangke":
	//			if(current==0){
	//				ressql="insert into relation_table  (user_id,target_id,user_access_time) values("+arg[2]+",'"+arg[3]+"',NOW())";
	//			}
	//			break;
			// http://120.27.98.128:9810/uiface/member196?mode=A-user-add&model=add_qiuliao&qiu_id=3&price=22&qiu_type=1
			case "add_qiuliao":// 1.发求聊后，插入求聊表，记录未抢
				if (current == 0) {
					ressql = "insert into Ask_Chat (qiu_id,price,status,time,qiu_type) values(" + arg[2] + ",'" + arg[3]
							+ "',0,NOW(),'" + arg[4] + "')";
				}
				break;
			// 添加之前。先判断是否已经有此访客
			case "say_art_submit":
				if (current == 0) {
					ressql = "INSERT INTO say_art_table " + "(update_id,title,photo,isdel,update_time,status) " + "VALUES ("
							+ arg[2] + ",'" + arg[3] + "','" + arg[4] + "',0,NOW(),'待审核')";
				}
				break;
	//		case "identity_approve"://Identity_approve身份认证
	//			if(current==0){
	//				ressql="insert into card_table  (user_id,p_card_photo,card_photo,time) "
	//						+ "values("+arg[2]+",'"+arg[3]+"','"+arg[4]+"',NOW())";
	//			}
	//			break;
			case "identity_approve":// Identity_approve身份认证
				if (current == 0) {
					ressql = "insert into card_table  (user_id,p_card_photo,card_photo,time) " + "values(" + arg[2] + ",'"
							+ arg[3] + "','" + arg[4] + "',NOW())";
				} else if (current == 1) {
					ressql = "update user_data set is_realname='2' where id='" + arg[2] + "'";
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
		}
		return ressql;
	}

	@Override
	public String modSqlface(int current, String[] arg) throws SQLException, IOException {
		switch (arg[1]) {
			case "add_guanzhu":
				if (current == 0) {
					ressql = "update relation_table set target_id='" + arg[2] + "' where visitor_id='" + arg[3]
							+ "' and user_id='" + arg[4] + "'";
				}
				break;
			case "is_rob_chat":// 更改抢聊状态.
				if (arg[3].equals("3")) {
					ressql = "update user_data set is_online=3 where id='" + arg[2] + "'";
				} else {
					ressql = "update user_data set is_online=1 where id='" + arg[2] + "'";
				}
				break;
	
	//			4。快撩页面,点击抢，进入一对1.将这条记录改为已抢
	//			update Ask_Chat set status=1 where id=1
			// http://120.27.98.128:9810/uiface/member196?mode=A-user-mod&model=update_yiqiang&id=1
			case "update_yiqiang":
	
				if (arg.length > 3) {
					ressql = "update Ask_Chat set status=2 where qiu_id='" + arg[3] + "'";
				} else {
					ressql = "update Ask_Chat set status=1 where qiu_id='" + arg[2] + "'";
				}
	
				break;
			case "delete_qiuliao":
				if (arg.length > 3) {
					ressql = "update Ask_Chat set status=2 where qiu_id='" + arg[3] + "'";
				} else {
					ressql = "update Ask_Chat set status=1 where qiu_id='" + arg[2] + "'";
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
			// http://120.27.98.128:9810/uiface/member196?mode=A-user-search&model=find_qiangliao
			case "find_qiangliao":// 2.快撩页面查询抢聊表内容，且不是已抢的显示出来
				if (current == 0) {
					ressql = "select  a.*,b.id,b.nickname,b.user_photo,b.gender,b.age "
							+ "from Ask_Chat as a,user_data as b	"
							+ "where a.status=0 and a.qiu_id=b.id order by time desc ";
				}
				break;
			case "gift_record":
				if (current == 0) {
					ressql = "select * from user_data where id='" + arg[3] + "'";
				} else if (current == 1) {
					ressql = "select * from play_tour";
				} else if (current == 2) {
					ressql = "select count(*) from gift_record where gift_id='" + arg[2] + "' and target_id='" + arg[3]
							+ "'";
				} else if (current == 3) {
					ressql = "select gifts_sum from roles_table where user_id='" + arg[3] + "'";
				}
				break;
	
			case "reward_gift":// 打赏礼物（女主播页面）
				if (current == 0) {
					ressql = "select * from play_tour order by price asc";
				}
				break;
			case "seek_chat":// 求聊 查在线女主播
				if (current == 0) {
					ressql = "select id,nickname,user_photo from user_data where is_online=1 and gender='女' order by id desc ";
				}
				break;
			case "findall_guanzhu":// 查询所有关注（我关注的所有用户）
				if (current == 0) {
					ressql = "select a.user_photo,a.nickname,a.gender,a.age,a.signature,b.target_id as v_id,b.user_follow_time  as v_time "
							+ "from user_data as a,relation_table as b "
							+ "where a.id=b.target_id and  b.user_follow_time!='" + "" + "' and  b.user_id='" + arg[2]
							+ "' order by b.user_follow_time desc";
				} else if (current == 1) {
					ressql = "select a.user_photo,a.nickname,a.gender,a.age,a.signature,b.user_id as v_id,b.target_follow_time as v_time  "
							+ "from user_data as a,relation_table as b "
							+ "where a.id=b.user_id and  b.target_follow_time!='" + "" + "' and  b.target_id='" + arg[2]
							+ "' order by b.target_follow_time desc";
				}
				break;
			case "findall_beiguanzhu":// 查询所有关注（喜欢）我的用户（谁喜欢我）
				if (current == 0) {
					ressql = "select a.user_photo,date_format(b.target_follow_time,'%Y-%m-%d') target_follow_time,b.target_id   "
							+ "from user_data as a,relation_table as b " + "where a.id=b.target_id and  b.user_id='"
							+ arg[2] + "' and b.target_follow_time!='" + "" + "' " + "order by b.target_follow_time desc";
				} else if (current == 1) {
					ressql = "select a.user_photo,date_format(b.user_follow_time,'%Y-%m-%d') target_follow_time,b.user_id as  target_id "
							+ "from user_data as a,relation_table as b " + "where a.id=b.user_id and  b.target_id='"
							+ arg[2] + "' and b.user_follow_time!='" + "" + "' " + "order by b.user_follow_time desc";
				}
				break;
			case "findall_together_guanzhu":// 相互喜欢
				// http://120.27.98.128:9810/uiface/member196?mode=A-user-search&model=findall_together_guanzhu&user_id=21
				if (current == 0) {
					ressql = "select a.user_photo,a.nickname,a.gender,a.age,a.signature,date_format(b.target_follow_time,'%Y-%m-%d') "
							+ "target_follow_time,b.target_id  " + "from user_data as a,relation_table as b "
							+ "where a.id=b.target_id and  b.user_id='" + arg[2] + "' and b.target_follow_time!='" + ""
							+ "'" + " and user_follow_time!='" + "" + "' order by b.target_follow_time desc";
				} else if (current == 1) {
					ressql = "select a.user_photo,a.nickname,a.gender,a.age,a.signature,date_format(b.user_follow_time,'%Y-%m-%d') "
							+ "target_follow_time,b.user_id as target_id" + "from user_data as a,relation_table as b "
							+ "where a.id=b.user_id and  b.target_id='" + arg[2] + "' and b.target_follow_time!='" + ""
							+ "'" + " and user_follow_time!='" + "" + "' order by b.user_follow_time desc";
				}
				break;
			case "findall_visitor":// 查询所有谁看过我列表
				if (current == 0) {
					ressql = "select a.user_photo,date_format(b.target_access_time,'%Y-%m-%d') visitors_time,b.target_id  "
							+ "from user_data as a,relation_table as b " + "where a.id=b.target_id and  b.user_id='"
							+ arg[2] + "' and target_access_time!='" + "" + "' " + "order by b.target_access_time desc";
	//				ressql="select a.user_photo,b.target_access_time as visitors_time,b.target_id  "
	//						+ "from user_data as a,relation_table as b "
	//						+ "where a.id=b.target_id and  b.user_id='"+arg[2]+"' and target_access_time!='"+""+"' "
	//								+ "order by b.target_access_time desc";
	
				} else if (current == 1) {
					ressql = "select a.user_photo,date_format(b.user_access_time,'%Y-%m-%d') visitors_time,b.user_id as target_id "
							+ "from user_data as a,relation_table as b " + "where a.id=b.user_id and  b.target_id='"
							+ arg[2] + "' and user_access_time!='" + "" + "' " + "order by b.user_access_time desc";
	//				ressql="select a.user_photo,b.user_access_time as visitors_time,b.target_id  "
	//						+ "from user_data as a,relation_table as b "
	//						+ "where a.id=b.user_id and  b.target_id='"+arg[2]+"' and user_access_time!='"+""+"' "
	//								+ "order by b.user_access_time desc";
				}
				break;
			// 我的访客
			// http://120.27.98.128:9810/uiface/member196?mode=A-user-add&model=my_visitor&user_id=3&visitor_id=1
			case "fangke":// 访客，id参数在前面的是登录用户
				if (current == 0) {
					// 谁访问过我
					ressql = "select count(*) from relation_table where user_id='" + arg[2] + "' and target_id='" + arg[3]
							+ "'";
				} else if (current == 1) {
					ressql = "select count(*) from relation_table where target_id='" + arg[2] + "' and user_id='" + arg[3]
							+ "'";
				} else if (current == 2) {
					// 我访问过谁
					ressql = "insert into relation_table  (user_id,target_id,user_access_time) values(" + arg[2] + ",'"
							+ arg[3] + "',NOW())";
				} else if (current == 3) {
					ressql = "update relation_table set user_access_time=NOW() where user_id='" + arg[2]
							+ "' and target_id='" + arg[3] + "'";
				} else if (current == 4) {
					ressql = "update relation_table set target_access_time=NOW() where target_id='" + arg[2]
							+ "' and user_id='" + arg[3] + "'";
				}
				break;
	
			// 添加关注http://120.27.98.128:9810/uiface/member196?mode=A-user-search&model=add_guanzhu&user_id=1&target_id=2
			case "add_guanzhu":
				if (current == 0) {
					ressql = "select count(*) from relation_table where user_id='" + arg[2] + "' and target_id='" + arg[3]
							+ "'";
				} else if (current == 1) {
					ressql = "select count(*) from relation_table where target_id='" + arg[2] + "' and user_id='" + arg[3]
							+ "'";
				} else if (current == 3) {
					ressql = "update relation_table set user_follow_time=NOW() where user_id='" + arg[2]
							+ "' and target_id='" + arg[3] + "'";
				} else if (current == 5) {
					ressql = "update relation_table set target_follow_time=NOW() where target_id='" + arg[2]
							+ "' and  user_id='" + arg[3] + "'";
				} else if (current == 6) {
					ressql = "insert into relation_table  (user_id,target_id,user_follow_time) values (" + arg[2] + ",'"
							+ arg[3] + "',NOW())";
				}
				break;
			case "quxiao_guanzhu":// 取消关注http://120.27.98.128:9810/uiface/member196?mode=A-user-search&model=quxiao_guanzhu&user_id=1&target_id=2
				if (current == 0) {
					ressql = "select * from relation_table where user_id='" + arg[2] + "' and target_id='" + arg[3] + "'";
				} else if (current == 1) {
					ressql = "select * from relation_table where target_id='" + arg[2] + "' and user_id='" + arg[3] + "'";
				} else if (current == 3) {
					ressql = "update relation_table set user_follow_time='" + "" + "' where user_id='" + arg[2]
							+ "' and target_id='" + arg[3] + "'";
				} else if (current == 5) {
					ressql = "update relation_table set target_follow_time='" + "" + "' where target_id='" + arg[2]
							+ "' and user_id='" + arg[3] + "'";
				}
				break;
			// ======================================================================================================================
			/*
			 * 首页显示第一个数据
			 */
			case "wholookme":// 谁看过我
    //          http://120.27.98.128:9810/uiface/member196?mode=A-user-search&model=wholookme&id=4
	//			ressql="select a.user_photo "
	//					+ "from user_data as a,relation_table as b "
	//					+ "where a.id=b.target_id and  b.user_id='"+arg[2]+"' and target_access_time!='"+""+"' "
	//							+ "order by b.target_access_time desc LIMIT 1";
				if (current == 0) {
					ressql = "select a.user_photo,b.target_access_time as visitors_time,b.target_id  "
							+ "from user_data as a,relation_table as b " + "where a.id=b.target_id and  b.user_id='"
							+ arg[2] + "' and target_access_time!='" + "" + "' "
							+ "order by b.target_access_time desc LIMIT 1";
	
				} else if (current == 1) {
					ressql = "select a.user_photo,b.user_access_time as visitors_time,b.target_id  "
							+ "from user_data as a,relation_table as b " + "where a.id=b.user_id and  b.target_id='"
							+ arg[2] + "' and user_access_time!='" + "" + "' " + "order by b.user_access_time desc LIMIT 1";
				}
				break;
			case "wholikesme":// 谁喜欢我
	//          http://120.27.98.128:9810/uiface/member196?mode=A-user-search&model=wholikesme&id=4
	//			ressql="select a.user_photo   "
	//					+ "from user_data as a,relation_table as b "
	//					+ "where a.id=b.target_id and  b.user_id='"+arg[2]+"' and b.target_follow_time!='"+""+"' "
	//							+ "order by b.target_follow_time desc LIMIT 1";
				if (current == 0) {
					ressql = "select a.user_photo,date_format(b.target_follow_time,'%Y-%m-%d') target_follow_time,b.target_id   "
							+ "from user_data as a,relation_table as b " + "where a.id=b.target_id and  b.user_id='"
							+ arg[2] + "' and b.target_follow_time!='" + "" + "' "
							+ "order by b.target_follow_time desc  LIMIT 1";
				} else if (current == 1) {
					ressql = "select a.user_photo,date_format(b.target_follow_time,'%Y-%m-%d') target_follow_time,b.target_id   "
							+ "from user_data as a,relation_table as b " + "where a.id=b.user_id and  b.target_id='"
							+ arg[2] + "' and b.user_follow_time!='" + "" + "' "
							+ "order by b.user_follow_time desc LIMIT 1";
				}
				break;
			case "whoilike":// 我喜欢谁
	// 			http://120.27.98.128:9810/uiface/member196?mode=A-user-search&model=whoilike&id=4
	//			ressql="select a.user_photo  "
	//					+ "from user_data as a,relation_table as b "
	//					+ "where a.id=b.target_id and  b.user_id='"+arg[2]+"' order by b.user_follow_time desc LIMIT 1";
				if (current == 0) {
					ressql = "select a.user_photo,a.nickname,a.gender,a.age,a.signature,b.target_id as v_id,b.user_follow_time  as v_time "
							+ "from user_data as a,relation_table as b "
							+ "where a.id=b.target_id and  b.user_follow_time!='" + "" + "' and  b.user_id='" + arg[2]
							+ "' " + "order by b.user_follow_time desc LIMIT 1";
				} else if (current == 1) {
					ressql = "select a.user_photo,a.nickname,a.gender,a.age,a.signature,b.user_id as v_id,b.target_follow_time as v_time  "
							+ "from user_data as a,relation_table as b "
							+ "where a.id=b.user_id and  b.target_follow_time!='" + "" + "' and  b.target_id='" + arg[2]
							+ "' " + "order by b.target_follow_time desc LIMIT 1";
				}
				break;
			case "likeeachother":// 相互喜欢
	// http://120.27.98.128:9810/uiface/member196?mode=A-user-search&model=likeeachother&id=4
	//			ressql="select a.user_photo "
	//					+ "from user_data as a,relation_table as b "
	//					+ "where a.id=b.target_id and  b.user_id='"+arg[2]+"' and b.target_follow_time!='"+""+"' "
	//							+ "and user_follow_time!='"+""+"' "
	//									+ "order by b.target_access_time desc LIMIT 1";
				if (current == 0) {
					ressql = "select a.user_photo,a.nickname,a.gender,a.age,a.signature,date_format(b.target_follow_time,'%Y-%m-%d') "
							+ "target_follow_time,b.target_id  " + "from user_data as a,relation_table as b "
							+ "where a.id=b.target_id and  b.user_id='" + arg[2] + "' and b.target_follow_time!='" + ""
							+ "'" + " and user_follow_time!='" + "" + "' order by b.target_follow_time desc LIMIT 1";
				} else if (current == 1) {
					ressql = "select a.user_photo,a.nickname,a.gender,a.age,a.signature,date_format(b.user_follow_time,'%Y-%m-%d') "
							+ "target_follow_time,b.target_id  " + "from user_data as a,relation_table as b "
							+ "where a.id=b.user_id and  b.target_id='" + arg[2] + "' and b.target_follow_time!='" + ""
							+ "'" + " and user_follow_time!='" + "" + "' order by b.user_follow_time desc LIMIT 1";
				}
				break;
			// 查用户online字段是4的查询接口 ===是否进入1对1视频
			// 1个改，1个查两个接口
			case "isone_on_one":// 是否进入1对一抢聊
				ressql = "select count(*) from user_data where ( is_online=4 ) and id='" + arg[2] + "' ";
				break;
		}
		return ressql;
	}

}
