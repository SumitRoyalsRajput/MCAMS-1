package com.mcams.dao;

import java.util.ArrayList;

import com.mcams.bean.ArtistBean;
import com.mcams.bean.ComposerBean;
import com.mcams.bean.SongBean;

public interface IAdminDao {

	ArtistBean createArtist(ArtistBean artBean, boolean isUpdate);

	int createComposer(ComposerBean compBean, boolean isUpdate);

	ArrayList<SongBean> searchArtist(String name);

	ArrayList<SongBean> searchComposer(String name);

}
