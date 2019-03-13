package com.wp.yf.app.controller;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.lang3.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.wp.yf.app.constant.JyGlobalConstant;
import com.wp.yf.app.log.JyLogDetect;
import com.wp.yf.app.log.JyLogDetect.DataType;
import com.wp.yf.app.util.SqlUtil;

public class ShareAgentInfo {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	private static JyLogDetect log = new JyLogDetect();

	// tt2 0 .一对一视频 1. 赠送礼物 2.付费短视频 3 私信收入 用户id 主播id 虚拟币数量 类型
	public static String anchor_ticheng(String uId, String userId, double realMoney, int tt2) {
		SqlUtil sqlUtil = new SqlUtil(JyGlobalConstant.getDbBaseName());
		ArrayList<Map<String, Object>> listcast = null;
		ArrayList<Map<String, Object>> list1 = null;
		// sql="select * from user_data where id="+u_id;
		// log.send("01158", "sql", sql);
		// list1=sqlUtil.get_list(sql);

		// if(tt2==0){
		// BizRenderTask send = new BizRenderTask(userid + "卍" +
		// "与 "+list1.get(0).get("nickname").toString()+" 一对一视频收入+"+realmoney);
		// send.run();
		// }else if(tt2==1){
		// BizRenderTask send = new BizRenderTask(userid + "卍" +
		// ""+list1.get(0).get("nickname").toString()+" 赠送礼物收入+"+realmoney);
		// send.run();
		// }else{
		// BizRenderTask send = new BizRenderTask(userid + "卍" +
		// ""+list1.get(0).get("nickname").toString()+" 付费短视频收入+"+realmoney);
		// send.run();
		// }
		//
		// PushDetectionMessage(userid);
		int cc = 0;
		try {
			String sql = "select * from cash_set ";
			log.send("01158", "sql", sql);
			listcast = sqlUtil.getList(sql);

			DecimalFormat df = new DecimalFormat("#.00");

			// 一级分销
			sql = "select * from user_data where id=" + userId;
			log.send("01158", "sql", sql);
			list1 = sqlUtil.getList(sql);
			if (list1.size() == 0) {

			} else {
				String type = StringUtils.EMPTY;
				if (tt2 == 0) {
					type = "一对一视频收入提成";
				} else if (tt2 == 1) {
					type = "礼物收入提成";
				} else if (tt2 == 2) {
					type = "付费短视频收入提成";
				} else {
					type = "私信收入提成";
				}

				String oneid = list1.get(0).get("promoter_id").toString();
				if (!oneid.equals("0")) {
					String isv = "1";
					String sacleone = listcast.get(0).get("dvcash_onefee").toString();
					Double oneable_money = Double.parseDouble(sacleone) * realMoney;
					oneable_money = Double.parseDouble(df.format(oneable_money));
					sql = "insert into tuiguang_detail (upuser_id,downuser_id,is_dv,levle,money_type,money_num,scale_num,able_money,uptime) values ('"
							+ oneid + "','" + userId + "','" + isv + "',1,'" + type + "','" + realMoney + "','"
							+ sacleone + "','" + oneable_money + "',now())";
					log.send("01158", "sql", sql);
					sqlUtil.sqlExecute(sql);

					// 添加收入明细
					sql = "insert into Income_details_table (user_id,time,type,money,operation,pay_id)values(" + oneid
							+ ",now(),'" + type + "','" + oneable_money + "','已到账','" + userId + "')";
					log.send("01158", "sql", sql);
					sqlUtil.sqlExecute(sql);

					sql = "update user_data set invite_price = invite_price+" + oneable_money
							+ ",ableinvite_price=ableinvite_price+" + oneable_money + " where id=" + oneid + " ";
					log.send("01158", "sql", sql);
					sqlUtil.sqlExecute(sql);
				}

				String twoid = list1.get(0).get("twopromoter_id").toString();
				if (!twoid.equals("0") && !listcast.get(0).get("dvcash_twofee").toString().equals("0")) {
					String isv = "1";
					String sacleone = listcast.get(0).get("dvcash_twofee").toString();
					Double oneable_money = Double.parseDouble(sacleone) * realMoney;
					oneable_money = Double.parseDouble(df.format(oneable_money));
					sql = "insert into tuiguang_detail (upuser_id,downuser_id,is_dv,levle,money_type,money_num,scale_num,able_money,uptime) values ('"
							+ twoid + "','" + userId + "','" + isv + "',2,'" + type + "','" + realMoney + "','"
							+ sacleone + "','" + oneable_money + "',now())";
					log.send("01158", "sql", sql);
					sqlUtil.sqlExecute(sql);

					// 添加收入明细
					sql = "insert into Income_details_table (user_id,time,type,money,operation,pay_id)values(" + twoid
							+ ",now(),'" + type + "','" + oneable_money + "','已到账','" + userId + "')";
					log.send("01158", "sql", sql);
					sqlUtil.sqlExecute(sql);

					sql = "update user_data set invite_price = invite_price+" + oneable_money
							+ ",ableinvite_price=ableinvite_price+" + oneable_money + " where id=" + twoid + " ";
					log.send("01158", "sql", sql);
					sqlUtil.sqlExecute(sql);
				}
				String up_agentid = list1.get(0).get("up_agentid").toString();
				log.send("01158", "sql", up_agentid);
				if (!up_agentid.equals("0")) {
					sql = "select fencheng_v from agentlist   where agent_channelcode='" + up_agentid + "'";
					log.send("01158", "sql", sql);
					String agent_fee = sqlUtil.getString(sql);
					log.send("01158", "sql", agent_fee);
					String isv = "1";
					Double agentable_money = Double.parseDouble(agent_fee) * realMoney;
					agentable_money = Double.parseDouble(df.format(agentable_money));
					sql = "insert into agenttuiguang_detail (upuser_id,downuser_id,is_dv,levle,money_type,money_num,scale_num,able_money,uptime) values ('"
							+ up_agentid + "','" + userId + "','" + isv + "',1,'" + type + "','" + realMoney + "','"
							+ agent_fee + "','" + agentable_money + "',now())";
					log.send("01158", "sql", sql);
					sqlUtil.sqlExecute(sql);
					sql = "update agentlist set totle_money = totle_money+" + agentable_money
							+ ",ablew_money=ablew_money+" + agentable_money + " where agent_channelcode='" + up_agentid
							+ "' ";
					log.send("01158", "sql", sql);
					sqlUtil.sqlExecute(sql);
				}

				/*
				 * if(tt2==0){
				 * sql="insert into income_details (user_id,time,type,money,operation,pay_id)values("
				 * +oneid+",now(),'视频收入提成','"+oneable_money+"','已到账','"+userid+"')";
				 * //BizRenderTask send = new BizRenderTask(oneid + "卍" +
				 * "邀请者："+list1.get(0).get("nickname").toString()+" 一对一视频收入提成+"+oneable_money);
				 * //send.run(); }else if(tt2==1){
				 * sql="insert into income_details (user_id,time,type,money,operation,pay_id)values("
				 * +oneid+",now(),'礼物收入提成','"+oneable_money+"','已到账','"+userid+"')";
				 * //BizRenderTask send = new BizRenderTask(oneid + "卍" +
				 * "邀请者："+list1.get(0).get("nickname").toString()+" 礼物收入提成+"+oneable_money);
				 * //send.run(); }else if(tt2==2){
				 * sql="insert into income_details (user_id,time,type,money,operation,pay_id)values("
				 * +oneid+",now(),'付费短视频收入提成','"+oneable_money+"','已到账','"+userid+"')";
				 * //BizRenderTask send = new BizRenderTask(oneid + "卍" +
				 * "邀请者："+list1.get(0).get("nickname").toString()+" 付费短视频收入提成+"+oneable_money);
				 * //send.run(); }
				 */

				// PushDetectionMessage(oneid);
				// log.send("01158", "提成sql ", sql);
				// cc=sqlUtil.sql_exec(sql);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cc + "";
	}

	// 充值人id 充值虚拟币数量 充值爱豆或充值会员
	public static void pay_info(String userid, double realmoney, String type) throws SQLException {
		SqlUtil sqlUtil = new SqlUtil(JyGlobalConstant.getDbBaseName());
		ArrayList<Map<String, Object>> listcast = null;
		String sql = "select * from cash_set ";
		log.send("01158", "sql", sql);
		listcast = sqlUtil.getList(sql);

		ArrayList<Map<String, Object>> list1 = null;
		// 一级分销
		sql = "select promoter_id,up_agentid,twopromoter_id from user_data where id=" + userid;
		log.send("01158", "sql", sql);
		list1 = sqlUtil.getList(sql);
		if (list1.size() == 0) {

		} else {
			String oneid = list1.get(0).get("promoter_id").toString();
			if (!oneid.equals("0")) {
				String isv = "0";
				String sacleone = listcast.get(0).get("cash_onefee").toString();
				Double oneable_money = Double.parseDouble(sacleone) * realmoney;
				sql = "insert into tuiguang_detail (upuser_id,downuser_id,is_dv,levle,money_type,money_num,scale_num,able_money,uptime) values ('"
						+ oneid + "','" + userid + "','" + isv + "',1,'" + type + "提成','" + realmoney + "','" + sacleone
						+ "','" + oneable_money + "',now())";
				log.send("01158", "sql", sql);
				sqlUtil.sqlExecute(sql);

				// 添加收入明细
				sql = "insert into Income_details_table (user_id,time,type,money,operation,pay_id)values(" + oneid
						+ ",now(),'" + type + "提成','" + oneable_money + "','已到账','" + userid + "')";
				log.send("01158", "sql", sql);
				sqlUtil.sqlExecute(sql);

				sql = "update user_data set invite_price = invite_price+" + oneable_money
						+ ",ableinvite_price=ableinvite_price+" + oneable_money + " where id=" + oneid + " ";
				log.send("01158", "sql", sql);
				sqlUtil.sqlExecute(sql);
			}

			String twoid = list1.get(0).get("twopromoter_id").toString();
			if (!twoid.equals("0") && !listcast.get(0).get("cash_twofee").toString().equals("0")) {
				String isv = "0";
				String sacleone = listcast.get(0).get("cash_twofee").toString();
				Double oneable_money = Double.parseDouble(sacleone) * realmoney;
				sql = "insert into tuiguang_detail (upuser_id,downuser_id,is_dv,levle,money_type,money_num,scale_num,able_money,uptime) values ('"
						+ twoid + "','" + userid + "','" + isv + "',1,'" + type + "提成','" + realmoney + "','" + sacleone
						+ "','" + oneable_money + "',now())";
				log.send("01158", "sql", sql);
				sqlUtil.sqlExecute(sql);

				// 添加收入明细
				sql = "insert into Income_details_table (user_id,time,type,money,operation,pay_id)values(" + oneid
						+ ",now(),'" + type + "提成','" + oneable_money + "','已到账','" + userid + "')";
				log.send("01158", "sql", sql);
				sqlUtil.sqlExecute(sql);

				sql = "update user_data set invite_price = invite_price+" + oneable_money
						+ ",ableinvite_price=ableinvite_price+" + oneable_money + " where id=" + twoid + " ";
				log.send("01158", "sql", sql);
				sqlUtil.sqlExecute(sql);
			}
			String up_agentid = list1.get(0).get("up_agentid").toString();
			log.send("01158", "sql", up_agentid);
			if (!up_agentid.equals("0")) {
				// sql="select b.agent_percent from agentlist as a ,agent_set as b where
				// a.agent_channelcode="+up_agentid+" and a.agent_level=b.agent_level";
				sql = "select fencheng_u from agentlist   where agent_channelcode='" + up_agentid + "'";
				log.send("01158", "sql", sql);
				String agent_fee = sqlUtil.getString(sql);
				log.send("01158", "sql", agent_fee);
				// String agent_fee=listcast.get(0).get("agent_fee").toString();
				String isv = "0";
				Double agentable_money = Double.parseDouble(agent_fee) * realmoney;
				sql = "insert into agenttuiguang_detail (upuser_id,downuser_id,is_dv,levle,money_type,money_num,scale_num,able_money,uptime) values ('"
						+ up_agentid + "','" + userid + "','" + isv + "',1,'" + type + "提成','" + realmoney + "','"
						+ agent_fee + "','" + agentable_money + "',now())";
				log.send("01158", "sql", sql);
				sqlUtil.sqlExecute(sql);
				sql = "update agentlist set totle_money = totle_money+" + agentable_money + ",ablew_money=ablew_money+"
						+ agentable_money + " where agent_channelcode='" + up_agentid + "' ";
				log.send("01158", "sql", sql);
				sqlUtil.sqlExecute(sql);
			}
		}
	}

	// 分销 1或2级 用户id 邀请码 渠道编号 服务器跟目录
	public static boolean registerinfo(int level, String id, String upcode, String upchannel, String inviteprice,
			String tmppath, String yuming) throws SQLException {

		int len = 7 - id.length();
		String userid = "";
		switch (len) {
		case 7:
			userid = "1000000";
			break;
		case 6:
			userid = "100000";
			break;
		case 5:
			userid = "10000";
			break;
		case 4:
			userid = "1000";
			break;
		case 3:
			userid = "100";
			break;
		case 2:
			userid = "10";
			break;
		case 1:
			userid = "1";
			break;
		}
		userid = userid + id;
		int mycode = (int) ((Math.random() * 9 + 1) * 100000);
		String qrcode = "";
		try {
			// 生成二维码
			String qrcodeUrl = "http://" + yuming + "/share/xiazai.html?code=" + mycode + "&fa="
					+ (Math.random() * 9 + 1) * 100000;
			log.send(DataType.basicType, "01156", "qrcodeUrl: ", qrcodeUrl);
			BufferedImage bi = createCodeStream(qrcodeUrl);
			String fullPath = "img/qrcodeimg";
			String filedir = tmppath + fullPath;
			File fullDir = new File(tmppath + fullPath);
			if (!fullDir.exists()) {
				fullDir.mkdirs();
			}
			String fileName = userid + ".jpg";
			String filepath = filedir + File.separator + fileName;
			log.send(DataType.basicType, "01150", "filepath========", filepath);
			File file = new File(filepath);
			ImageIO.write(bi, "JPEG", file);
			qrcode = "http://" + yuming + "/img/qrcodeimg/" + userid + ".jpg";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.send(DataType.exceptionType, "01156", "getqrcode: ", "exception: " + e);
		}
		SqlUtil sqlUtil = new SqlUtil(JyGlobalConstant.getDbBaseName());
		String oneid = "0";
		String twoid = "0";
		int a = 0;
		String sql = "";
		if (!upcode.equals("")) {
			sql = "select id,promoter_id,up_agentlevel,up_agentid from user_data where invite_num='" + upcode + "'";
			ArrayList<Map<String, Object>> list = sqlUtil.getList(sql);
			if (list.size() > 0) {
				oneid = list.get(0).get("id").toString();
				if (level == 2) {
					twoid = list.get(0).get("promoter_id").toString();
				}
			}
			String upid = "0";
			String uplevel = "0";
			uplevel = list.get(0).get("up_agentlevel").toString();
			upid = list.get(0).get("up_agentid").toString();

			if (uplevel.equals("1")) {
				sql = "update user_data set user_id='" + userid + "',promoter_id='" + oneid + "',twopromoter_id='"
						+ twoid + "',up_agentid='" + upid + "',qrcode='" + qrcode + "',up_agentlevel='2',invite_num='"
						+ mycode + "' where id=" + id;
				a = sqlUtil.sqlExecute(sql);
			} else if (uplevel.equals("2")) {
				sql = "update user_data set user_id='" + userid + "',promoter_id='" + oneid + "',twopromoter_id='"
						+ twoid + "',up_agentid='0',qrcode='" + qrcode + "',up_agentlevel='0',invite_num='" + mycode
						+ "' where id=" + id;
				a = sqlUtil.sqlExecute(sql);
			}
		} else if (!upchannel.equals("")) {
			sql = "update user_data set user_id='" + userid + "',promoter_id='" + oneid + "',twopromoter_id='" + twoid
					+ "',up_agentid='" + upchannel + "',qrcode='" + qrcode + "',up_agentlevel='1',invite_num='" + mycode
					+ "' where id=" + id;
			a = sqlUtil.sqlExecute(sql);
		} else {
			sql = "update user_data set user_id='" + userid
					+ "',promoter_id='0',twopromoter_id='0',up_agentid='0',qrcode='" + qrcode
					+ "',up_agentlevel='0',invite_num='" + mycode + "' where id=" + id;
			a = sqlUtil.sqlExecute(sql);
		}

		// String inviteprice="5";
		if (!inviteprice.equals("0")) {
			sql = "insert into tuiguang_detail (upuser_id,downuser_id,is_dv,levle,money_type,money_num,scale_num,able_money,uptime) values ('"
					+ oneid + "','" + id + "','" + "0" + "',1,'新注册','" + inviteprice + "','" + "1" + "','" + inviteprice
					+ "',now())";
			log.send("01158", "sql", sql);
			sqlUtil.sqlExecute(sql);

			// 添加收入明细
//				sql="insert into Income_details_table (user_id,time,type,money,operation,pay_id)values("+oneid+",now(),'新注册提成','"+inviteprice+"','已到账','"+id+"')";
//				log.send("01158", "sql", sql);
//				sqlUtil.sql_exec(sql);

			sql = "update user_data set invite_price = invite_price+" + inviteprice
					+ ",ableinvite_price=ableinvite_price+" + inviteprice + " where id=" + oneid + " ";
			log.send("01158", "sql", sql);
			sqlUtil.sqlExecute(sql);
		}

		if (a == 0) {
			return false;
		} else {
			return true;
		}
	}

	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public static BufferedImage writeToStream(BitMatrix matrix, String format/*
																				 * , OutputStream stream
																				 */) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		return image;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static BufferedImage createCodeStream(String text) throws Exception {

		// response.setContentType("image/jpeg");
		// ServletOutputStream sos = response.getOutputStream();

		int width = 898;
		int height = 898;
		// 二维码的图片格式
		String format = "jpg";
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);
		// 内容所使用编码
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

		// 生成二维码

		BufferedImage image = writeToStream(bitMatrix, format/* ,sos */);

		// sos.close();

		return image;
	}

	public static BufferedImage toBufferedImage(Image image) {

		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see e661 Determining If an Image Has Transparent
		// Pixels
		// boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}
		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			// int type = BufferedImage.TYPE_3BYTE_BGR;//by wang
			/*
			 * if (hasAlpha) { type = BufferedImage.TYPE_INT_ARGB; }
			 */
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}
		// Copy image to buffered image
		Graphics g = bimage.createGraphics();
		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bimage;
	}
}
