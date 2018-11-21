package com.mcams.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBUtil {
	public static Connection getConnection() {
		Properties properties = new Properties();
		InputStream fin;
		String resource;
		try {
			resource = "database.properties";
//			resource = "db.properties";
    		fin = DBUtil.class.getClassLoader().getResourceAsStream(resource);
			properties.load(fin);
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			System.out.println("Connecting to database...\nPlease Wait...");
			Connection conn = DriverManager.getConnection(properties.getProperty("url"),properties.getProperty("username"),properties.getProperty("password"));
			if(conn!=null) System.out.println("CONNECTED!");
			else {
				System.out.println("Connection Failed!");
				System.out.println("<-PROGRAM TERMINATED->");
				System.exit(0);
			}
			return conn;
		} catch (Exception e) {
			System.out.println("Connection Failed!");
			System.out.println("<-PROGRAM TERMINATED->");
			System.exit(0);
			return null;
		}
	}
}
