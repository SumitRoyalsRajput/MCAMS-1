package com.mcams.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mcams.bean.ArtistBean;
import com.mcams.bean.ComposerBean;
import com.mcams.bean.SongBean;
import com.mcams.util.DBUtil;

public class AdminDao implements IAdminDao {
	
	private static String sql;
	private static Statement st;
	private static Connection conn = DBUtil.getConnection();

	@Override
	public ArtistBean createArtist(ArtistBean artBean, boolean isUpdate) {
		if(isUpdate) {
			String bDate, dDate;
			
			if(artBean.getBornDate()==null) bDate=null;
			else bDate="TO_DATE('"+artBean.getBornDate()+"','yyyy/mm/dd')";
			
			if(artBean.getDiedDate()==null) dDate=null;
			else dDate="TO_DATE('"+artBean.getDiedDate()+"','yyyy/mm/dd')";
			
			sql = "UPDATE Artist_Master SET Artist_BornDate="+bDate+",Artist_DiedDate="+dDate+",Updated_by="+artBean.getUpdatedBy()+",Updated_On=SYSDATE,Artist_DeletedFlag=0 WHERE Artist_Id="+artBean.getId();
			try {
				st.executeUpdate(sql);
				return artBean;
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				return artBean;
			}
			
		}
		
		else {
			sql = "SELECT * FROM Artist_Master WHERE Artist_Name='"+artBean.getName()+"'";
			try {
				st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql);
				
				if(rs.next()) {
					artBean.setBornDate(rs.getDate(3).toLocalDate());
					artBean.setDiedDate(rs.getDate(4).toLocalDate());
					artBean.setCreatedBy(rs.getInt(5));
					artBean.setCreatedOn(rs.getDate(6).toLocalDate());
					artBean.setUpdatedBy(rs.getInt(7));
					artBean.setUpdatedOn(rs.getDate(8).toLocalDate());
					artBean.setDeletedFlag(rs.getInt(9));
					
					if(artBean.getDeletedFlag()==0) {
						artBean.setId(-(rs.getInt(1)));
						return artBean;
					}
					else {
						artBean.setId(-((rs.getInt(1))+100000));
						return artBean;
					}
				}
				
				sql = "INSERT INTO Artist_Master (Artist_Id,Artist_Name,Artist_BornDate,Artist_DiedDate,Created_By,Created_On,Updated_By,Updated_On,Artist_DeletedFlag) "
						+ "VALUES(artistSeq.NEXTVAL,'"+artBean.getName()+"',TO_DATE('"+artBean.getBornDate()+"','yyyy/mm/dd'),TO_DATE('"+artBean.getDiedDate()+"','yyyy/mm/dd'),"+artBean.getCreatedBy()+",SYSDATE,"+artBean.getUpdatedBy()+",SYSDATE,0)";
				st.executeUpdate(sql);
				rs = st.executeQuery("SELECT artistSeq.CURRVAL FROM DUAL");
				rs.next();
				artBean.setId(rs.getInt(1));
				return artBean;
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				artBean.setId(0);
				return artBean;
			}
			
		}
	}

	@Override
	public int createComposer(ComposerBean compBean, boolean isUpdate) {
		if(isUpdate) {
			sql = "INSERT INTO Composer_Master (Composer_Id,Composer_Name,Composer_CaeipiNumber,Composer_MusicSocietyID,Updated_By,Updated_On) "
					+ "VALUES("+compBean.getId()+",'"+compBean.getName()+"','"+compBean.getCaeipiNumber()+"','"+compBean.getMusicSocietyId().toString()+"',"+compBean.getUpdatedBy()+",SYSDATE)";
			return compBean.getId();
		}
		
		else {
			sql = "SELECT Composer_Id, Composer_DeletedFlag FROM Composer_Master WHERE Composer_Name='"+compBean.getName()+"'";
			try {
				st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql);
				if(rs.next()) {
					if(rs.getInt(2)==1)
						return -(rs.getInt(1));
					else
						return -((rs.getInt(1))+100000);
				}
				
				sql = "INSERT INTO Composer_Master (Composer_Id,Composer_Name,Composer_CaeipiNumber,Composer_MusicSocietyID,Updated_By,Updated_On) "
						+ "VALUES(composerSeq.NEXTVAL,'"+compBean.getName()+"','"+compBean.getCaeipiNumber()+"','"+compBean.getMusicSocietyId().toString()+"',"+compBean.getUpdatedBy()+",SYSDATE)";
				st.executeUpdate(sql);
				rs = st.executeQuery("SELECT composerSeq.CURRVAL FROM DUAL");
				return rs.getInt(1);
				
			} catch (SQLException e) {
				return 0;
			}
			
		}
		
		
	}
	
	@Override
	public ArrayList<SongBean> searchArtist(String name) {
		ResultSet rs;
		SongBean sb = new SongBean();
		ArrayList<SongBean> songList = new ArrayList<SongBean>();
		
		sql="SELECT Artist_Id from Artist_Master WHERE Artist_Name="+name+" AND Artist_DeletedFlag=0";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(!rs.next()) return null;
			else {
				sql =  "SELECT * from Song_Master INNER JOIN Artist_Song_Assoc ON Artist_Song_Assoc.Song_Id=Song_Master.Song_Id WHERE "
						+ "Artist_Song_Assoc.Artist_Id IN (select Artist_Id FROM Artist_Master WHERE Artist_Name="+name+") AND Song_Master.Song_DeletedFlag=0";
				rs = st.executeQuery(sql);
				while(rs.next()){
					sb.setId(rs.getInt(1));
					sb.setName(rs.getString(2));
					sb.setDuration(rs.getTime(3).toLocalTime());
					sb.setCreatedBy(rs.getInt(4));
					sb.setCreatedOn(rs.getDate(5).toLocalDate());
					sb.setUpdatedBy(rs.getInt(6));
					sb.setUpdatedOn(rs.getDate(7).toLocalDate());
					songList.add(sb);
				}
				return songList;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	@Override
	public ArrayList<SongBean> searchComposer(String name) {
		ResultSet rs;
		SongBean sb = new SongBean();
		ArrayList<SongBean> songList = new ArrayList<SongBean>();
		
		sql="SELECT Composer_Id from Composer_Master WHERE Composer_Name="+name+" AND Composer_DeletedFlag=0";
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(!rs.next()) return null;
			else {
				sql =  "SELECT * from Song_Master INNER JOIN Composer_Song_Assoc ON Composer_Song_Assoc.Song_Id=Song_Master.Song_Id WHERE "
						+ "Composer_Song_Assoc.Composer_Id IN (select Composer_Id FROM Composer_Master WHERE Composer_Name="+name+") AND Song_Master.Song_DeletedFlag=0";
				rs = st.executeQuery(sql);
				while(rs.next()){
					sb.setId(rs.getInt(1));
					sb.setName(rs.getString(2));
					sb.setDuration(rs.getTime(3).toLocalTime());
					sb.setCreatedBy(rs.getInt(4));
					sb.setCreatedOn(rs.getDate(5).toLocalDate());
					sb.setUpdatedBy(rs.getInt(6));
					sb.setUpdatedOn(rs.getDate(7).toLocalDate());
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
