package com.mcams.service;

import com.mcams.bean.AuthenticationBean;
import com.mcams.dao.AuthenticationDAO;
import com.mcams.exception.AppException;

public class AuthenticationService implements IAuthenticationService {
	AuthenticationDAO dao = new AuthenticationDAO();
	
	@Override
	public int checkCredentials(AuthenticationBean bean) throws AppException {
		return dao.checkCredentials(bean);
	}

}
