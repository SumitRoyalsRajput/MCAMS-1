package com.mcams.dao;

import java.util.ArrayList;

import com.mcams.bean.ArtistBean;
import com.mcams.bean.ComposerBean;
import com.mcams.bean.SongBean;

public interface IAdminDao {

	ArtistBean createArtist(ArtistBean artBean, boolean isUpdate);

	int createComposer(ComposerBean compBean, boolean isUpdate);

	ArrayList<SongBean> searchArtistSong(String name);

	ArrayList<SongBean> searchComposerSong(String name);

	ArtistBean searchArtist(String name);

	ComposerBean searchComposer(String name);

	ArtistBean updateArtist(ArtistBean artBean);

	ComposerBean updateComposer(ComposerBean compBean);

	SongBean assocArtist(SongBean songBean, ArtistBean artBean);
	
	SongBean assocComposer(SongBean songBean, ComposerBean compBean);

}
