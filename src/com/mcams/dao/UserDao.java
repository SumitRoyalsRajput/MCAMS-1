package com.mcams.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mcams.bean.SongBean;
import com.mcams.util.DBUtil;

public class UserDao implements IUserDao {
	Connection conn = AuthenticationDAO.conn;
	
	@Override
	public ArrayList<SongBean> searchArtist(String name) {
		String sql;
		Statement st;
		ResultSet rs;
		ArrayList<SongBean> songList = new ArrayList<SongBean>();
		
		sql="SELECT Artist_Id FROM Artist_Master WHERE LOWER(Artist_Name) LIKE LOWER('%"+name+"%') AND Artist_DeletedFlag=0";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(!rs.next()) return null;
			else {
				sql =  "SELECT Song_Master.Song_Name, Song_Master.Song_Duration FROM Song_Master INNER JOIN Artist_Song_Assoc ON Artist_Song_Assoc.Song_Id=Song_Master.Song_Id WHERE "
						+ "Artist_Song_Assoc.Artist_Id IN (SELECT Artist_Id FROM Artist_Master WHERE LOWER(Artist_Name) LIKE LOWER('%"+name+"%')) AND Song_Master.Song_DeletedFlag=0";
				rs = st.executeQuery(sql);
				while(rs.next()){
					SongBean sb = new SongBean();
					sb.setName(rs.getString(1));
					sb.setDuration(rs.getTime(2).toLocalTime());
					songList.add(sb);
				}
				return songList;
			}
		} catch (SQLException e) {
			
			System.out.println(e.getMessage());
			return null;
		}
	}

	public ArrayList<SongBean> serachComposer(String name) {
		String sql;
		Statement st;
		ResultSet rs;
		ArrayList<SongBean> songList = new ArrayList<SongBean>();
		
		sql="SELECT Composer_Id FROM Composer_Master WHERE LOWER(Composer_Name) LIKE LOWER('%"+name+"%') AND Composer_DeletedFlag=0";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(!rs.next()) return null;
			else {
				sql =  "SELECT Song_Master.Song_Name, Song_Master.Song_Duration FROM Song_Master INNER JOIN Composer_Song_Assoc ON Composer_Song_Assoc.Song_Id=Song_Master.Song_Id WHERE "
						+ "Composer_Song_Assoc.Composer_Id IN (SELECT Composer_Id FROM Composer_Master WHERE LOWER(Composer_Name) LIKE LOWER('%"+name+"%')) AND Song_Master.Song_DeletedFlag=0";
				rs = st.executeQuery(sql);
				while(rs.next()){
					SongBean sb = new SongBean();
					sb.setName(rs.getString(1));
					sb.setDuration(rs.getTime(2).toLocalTime());
					songList.add(sb);
				}
				return songList;
			}
		} catch (SQLException e) {
			
			System.out.println(e.getMessage());
			return null;
		}
	}

}
