package com.mcams.service;

import java.util.ArrayList;

import com.mcams.bean.SongBean;

public interface IUserService {

	ArrayList<SongBean> searchArtistSong(String name);

	ArrayList<SongBean> searchComposerSong(String name);

}
