package com.mcams.service;

import java.util.ArrayList;

import com.mcams.bean.SongBean;

public interface IUserService {

	ArrayList<SongBean> searchArtist(String name);

	ArrayList<SongBean> searchComposer(String name);

}
