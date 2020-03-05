package com.taotao.portal.service;

import com.taotao.pojo.TbUser;

public interface UserService {
  
	public TbUser getUser(String token);
}
