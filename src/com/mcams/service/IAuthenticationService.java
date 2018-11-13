package com.mcams.service;

import com.mcams.bean.AuthenticationBean;
import com.mcams.exception.AppException;

public interface IAuthenticationService {
	public int checkCredentials(AuthenticationBean bean) throws AppException;
}
