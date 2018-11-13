package com.mcams.dao;

import com.mcams.bean.AuthenticationBean;
import com.mcams.exception.AppException;

public interface IAuthenticationDAO {
	public int checkCredentials(AuthenticationBean bean) throws AppException;
}
