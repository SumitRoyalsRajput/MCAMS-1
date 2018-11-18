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
	public ArrayList<SongBean> searchArtistSong(String name) {
		return adminDao.searchArtistSong(name);
	}

	@Override
	public ArrayList<SongBean> searchComposerSong(String name) {
		return adminDao.searchComposerSong(name);
	}
	
	@Override
	public ArtistBean searchArtist(String name) {
		return adminDao.searchArtist(name);
	}
	
	@Override
	public ComposerBean searchComposer(String name) {
		return adminDao.searchComposer(name);
	}

	@Override
	public ArtistBean updateArtist(ArtistBean artBean) {
		return adminDao.updateArtist(artBean);
	}

	@Override
	public ComposerBean updateComposer(ComposerBean compBean) {
		return adminDao.updateComposer(compBean);
	}

	@Override
	public SongBean assocArtist(SongBean songBean, ArtistBean artBean, int userId, boolean isUpdate) {
		return adminDao.assocArtist(songBean, artBean, userId, isUpdate);
	}

	@Override
	public SongBean assocComposer(SongBean songBean, ComposerBean compBean, int userId, boolean isUpdate) {
		return adminDao.assocComposer(songBean, compBean, userId, isUpdate);
	}

	@Override
	public int deleteArtist(int artistId, int userId) {
		return adminDao.deleteArtist(artistId, userId);
	}

	@Override
	public int deleteComposer(int composerId, int userId) {
		
		return adminDao.deleteComposer(composerId, userId);
	}

	@Override
	public int deleteSong(int songId, int userId) {
		return adminDao.deleteSong(songId,userId);
	}

	public SongBean searchSong(String name) {
		return adminDao.searchSong(name);
	}
}
