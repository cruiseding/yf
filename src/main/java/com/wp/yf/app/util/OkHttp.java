package com.wp.yf.app.util;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.wp.yf.app.constant.JyGlobalConstant;
import com.wp.yf.app.log.JyLogDetect;
import com.wp.yf.app.log.JyLogDetect.DataType;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp {

	public OkHttpClient mOkHttpClient;

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	JyLogDetect log = new JyLogDetect();

	public OkHttp() {
		if (mOkHttpClient == null) {
			mOkHttpClient = new OkHttpClient();
		}
	}

	public void postAsynHttp(String url, String json) {
		RequestBody formBody = new FormBody.Builder().add("json", json).build();
		Request request = addHeaders().url("http://" + JyGlobalConstant.getIp() + ":9000/uiface").post(formBody)
				.build();
		Call call = mOkHttpClient.newCall(request);
		call.enqueue(new Callback() {

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				// TODO Auto-generated method stub
				/*
				 * String str = arg1.body().string(); if(!str.equals("")){ String[]
				 * ids=str.split("@"); MainActivity.sqlserverDao.updateSqlserver(ids); }
				 */
			}
		});
	}

	public String requestPostBySyn(String actionUrl) {
		String json = StringUtils.EMPTY;

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(actionUrl).build();

		Call call = client.newCall(request);
		// 执行请求
		Response response;
		try {
			response = call.execute();
			log.send(DataType.basicType, "01150", "response: ", response);
			if (response.isSuccessful()) {
				json = response.body().string();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.send(DataType.basicType, "01150", "e==========: ", e);
			e.printStackTrace();
		}
		/*
		 * call.enqueue(new Callback() {
		 * 
		 * @Override public void onFailure(Call call, IOException arg1) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void onResponse(Call call, Response response) throws
		 * IOException { // TODO Auto-generated method stub
		 * 
		 * int code = response.code(); String json1 = response.body().string();
		 * 
		 * System.out.println("code: "+code);
		 * System.out.println("body: "+response.body().string()); }
		 * 
		 * });
		 */

		return json;

		/*
		 * RequestBody formBody = new FormBody.Builder().build(); Request request = new
		 * Request.Builder().url(actionUrl).post(formBody).build();
		 * log.send(DataType.basicType, "01150", "request: ", request); OkHttpClient
		 * client = new OkHttpClient(); //OkHttpClient.Builder builder = new
		 * OkHttpClient.Builder(); //builder.sslSocketFactory(createSSLSocketFactory());
		 * Call call = client.newCall(request); //执行请求 Response response; try { response
		 * = call.execute(); log.send(DataType.basicType, "01150", "response: ",
		 * response); if (response.isSuccessful()) { json = response.body().string(); }
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		/*
		 * try { // //创建一个FormBody.Builder FormBody.Builder builder = new
		 * FormBody.Builder(); // for(int i=0;i<paramsMap.length;i++){ //
		 * builder.add("param"+i, paramsMap[i]); // } //生成表单实体对象 RequestBody formBody =
		 * builder.build(); // //补全请求地址 String requestUrl = String.format("%s%s", "",
		 * actionUrl); log.send(DataType.basicType, "01150",
		 * "requestUrl=========================", requestUrl); //创建一个请求 final Request
		 * request = addHeaders().url(requestUrl).post(formBody).build();
		 * log.send(DataType.basicType, "01150", "request=========================",
		 * request); //final Request request = new
		 * Request.Builder().url(requestUrl).build(); //创建一个Call final Call call =
		 * mOkHttpClient.newCall(request); log.send(DataType.basicType, "01150",
		 * "call=========================", call); //执行请求 Response response =
		 * call.execute(); log.send(DataType.basicType, "01150",
		 * "response=========================", response.body().string()); if
		 * (response.isSuccessful()) {
		 * 
		 * json = response.body().string();
		 * 
		 * }else { log.send(DataType.basicType, "01150",
		 * "else=========================", response); } } catch (Exception e) {
		 * log.send(DataType.basicType, "01150", "e=========================", e); }
		 */
	}

	private Request.Builder addHeaders() {
		Request.Builder builder = new Request.Builder().addHeader("Connection", "close").addHeader("userid", "");
		// .addHeader("userid","22");
		return builder;
	}

}
