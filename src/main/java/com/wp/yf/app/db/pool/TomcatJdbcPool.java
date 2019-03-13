package com.wp.yf.app.db.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatJdbcPool {

	// 连接池相关
	private static volatile Map<String, TomcatJdbcPool> pool = new HashMap<String, TomcatJdbcPool>();
	private DataSource ds;
	private PoolProperties poolProps;
	
	@Value("#{spring.datasource.driver}")
	private String driver;
	
	@Value("#{spring.datasource.url}")
	private String url;
	
	@Value("#{spring.datasource.username}")
	private String username;
	
	@Value("#{spring.datasource.password}")
	private String password;

	public TomcatJdbcPool() {
		init();
	}

	private void init() {
		poolProps = new PoolProperties();
		poolProps.setUrl(url);
		poolProps.setDriverClassName(driver);
		poolProps.setUsername(username);
		poolProps.setPassword(password);

		poolProps.setInitialSize(10); // 初始化10个连接
		poolProps.setMaxActive(300); // 最大200个连接
		poolProps.setMaxWait(10000); // 最长10秒钟等待
		poolProps.setRemoveAbandonedTimeout(20); // 60秒移除废弃的连接
		poolProps.setRemoveAbandoned(true); // 可以移除废弃的连接
		
		poolProps.setValidationQuery("SELECT 1");
		poolProps.setTestOnBorrow(true);
		poolProps.setTestOnConnect(true);
		poolProps.setTestWhileIdle(true);
		poolProps.setTestOnReturn(true);

		// 创建连接池, 使用了 tomcat 提供的的实现，它实现了 javax.sql.DataSource 接口
		ds = new DataSource();
		// 为连接池设置属性
		ds.setPoolProperties(poolProps);
	}

	private static boolean isNull(Connection conn) {
		try {
			if (conn == null || conn.isClosed()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static TomcatJdbcPool getInstance(String dbName) {
		if (pool.get(dbName) == null) {
			synchronized (TomcatJdbcPool.class) {
				if (pool.get(dbName) == null) {
					pool.put(dbName, new TomcatJdbcPool());
				}
			}
		} else {
			if(isNull(pool.get(dbName).getConnection())){
				pool.put(dbName, new TomcatJdbcPool());
			}
		}
		return pool.get(dbName);
	}

	public synchronized Connection getConnection() {
		Connection connect = null;
		try {
			connect = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connect;
	}
}
