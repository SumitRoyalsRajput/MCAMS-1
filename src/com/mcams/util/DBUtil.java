package com.mcams.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.mcams.exception.AppException;

public class DBUtil {
	public static Connection getConnection() {
		Properties p = new Properties();
		InputStream fin;
		try {
//			fin = new FileInputStream("database.properties");
//			p.load(fin);
			Class.forName("oracle.jdbc.driver.OracleDriver");
//			System.out.println("Connecting to database... please wait...");
//			Connection conn = DriverManager.getConnection(p.getProperty("url"),p.getProperty("username"),p.getProperty("password"));
//			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@10.51.103.201:1521:orcl11g","lab08trg27","lab08oracle");
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","system","system");
//			if(conn!=null) System.out.println("CONNECTED: "+conn);
//			else{
//				System.out.println("Connection Failed!");
//				System.out.println("Exiting program...");
//				System.exit(0);
//			}
			return conn;
		} catch (Exception e) {
			new AppException(e.getMessage());
			return null;
		}
	}
}
