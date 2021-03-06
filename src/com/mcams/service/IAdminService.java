package com.mcams.service;

import java.util.ArrayList;

import com.mcams.bean.ArtistBean;
import com.mcams.bean.ComposerBean;
import com.mcams.bean.SongBean;

public interface IAdminService {

	ArtistBean createArtist(ArtistBean artBean, boolean isUpdate);

	int createComposer(ComposerBean compBean, boolean isUpdate, String mSocietyName);

	ArrayList<SongBean> searchArtistSong(String name);

	ArrayList<SongBean> searchComposerSong(String name);

	ArtistBean searchArtist(String name);

	ComposerBean searchComposer(String name);

	ArtistBean updateArtist(ArtistBean artBean);

	ComposerBean updateComposer(ComposerBean compBean);

	SongBean assocArtist(SongBean songBean, ArtistBean artBean, int userId, boolean isUpdate);
	
	SongBean assocComposer(SongBean songBean, ComposerBean compBean, int userId, boolean isUpdate);

	int deleteArtist(int artistId, int userId);

	int deleteComposer(int composerId, int userId);

	int deleteSong(int songId, int userId);

}
