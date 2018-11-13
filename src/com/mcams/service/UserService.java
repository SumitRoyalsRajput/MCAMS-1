package com.mcams.service;

import java.util.ArrayList;

import com.mcams.bean.SongBean;
import com.mcams.dao.UserDao;

public class UserService implements IUserService {
	static UserDao userDao = new UserDao();
	
	@Override
	public ArrayList<SongBean> searchArtist(String name) {
		return userDao.searchArtist(name);
	}

	@Override
	public ArrayList<SongBean> searchComposer(String name) {
		return userDao.serachComposer(name);
	}

}
