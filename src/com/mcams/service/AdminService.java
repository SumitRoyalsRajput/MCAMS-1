package com.mcams.service;

import java.util.ArrayList;

import com.mcams.bean.ArtistBean;
import com.mcams.bean.ComposerBean;
import com.mcams.bean.SongBean;
import com.mcams.dao.AdminDao;

public class AdminService implements IAdminService {

	static AdminDao adminDao = new AdminDao();
	
	@Override
	public ArtistBean createArtist(ArtistBean artBean, boolean isUpdate) {
		return adminDao.createArtist(artBean,isUpdate);
	}

	@Override
	public int createComposer(ComposerBean compBean, boolean isUpdate) {
		return adminDao.createComposer(compBean,isUpdate);
	}
	
	@Override
	public ArrayList<SongBean> searchArtist(String name) {
		return adminDao.searchArtist(name);
	}

	@Override
	public ArrayList<SongBean> searchComposer(String name) {
		return adminDao.searchComposer(name);
	}
	
}
