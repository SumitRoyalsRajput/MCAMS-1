package com.mcams.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mcams.bean.AuthenticationBean;
import com.mcams.exception.AppException;
import com.mcams.util.DBUtil;

public class AuthenticationDAO implements IAuthenticationDAO {
	public static Connection conn = DBUtil.getConnection();
	
	public int checkCredentials(AuthenticationBean bean) throws AppException {
		String sql = "SELECT User_Password FROM User_Master WHERE User_Id='"+bean.getUserId()+"'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			if(rs.next()) {
				if(bean.getPassword().equals(rs.getString(1))) return 0;
				else return 1;
			}
			else return -1;
			
		} catch (SQLException e) {
			throw new AppException(e.getMessage());
		}
	}

}
