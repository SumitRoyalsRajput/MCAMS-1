package com.mcams.dao;

import java.util.ArrayList;

import com.mcams.bean.SongBean;

public interface IUserDao {

	ArrayList<SongBean> searchArtist(String name);
	
}
