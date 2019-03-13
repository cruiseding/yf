package com.wp.yf.app.util;

public class JyLocation {

	private static final double EARTH_RADIUS = 6371;
	
	public static double[] getLLPoint(String longitude, String latitude, double distance) {

		double lng = Double.parseDouble(longitude);
		double lat = Double.parseDouble(latitude);
		
		// 计算经度弧度,从弧度转换为角度
		double dLongitude = 2 * (Math.asin(Math.sin(distance / (2 * EARTH_RADIUS)) / Math.cos(Math.toRadians(lat))));
		dLongitude = Math.toDegrees(dLongitude);

		// 计算纬度角度
		double dLatitude = distance / EARTH_RADIUS;
		dLatitude = Math.toDegrees(dLatitude);

		// 正方形 顺序:左右,上下(西东,北南)
		double[] setLL = { lng - dLongitude, lng + dLongitude, lat + dLatitude, lat - dLatitude };
		return setLL;
	}
}