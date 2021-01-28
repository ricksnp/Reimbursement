package com.reimb.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class EnvironmentConnectionUtil {
	
	// If security is needed, put these in your environment varaibles
	String url = "jdbc:postgresql://bankapp.cvu4hgznjaru.us-east-1.rds.amazonaws.com/postgres";
	String username = "master";
	String password = "Nathan13";
	
	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}

}

