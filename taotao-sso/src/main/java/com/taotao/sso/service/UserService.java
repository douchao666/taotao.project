package com.taotao.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {
  public TaotaoResult checkData(String content,Integer type);
  public TaotaoResult regisUser(TbUser user);
  public TaotaoResult getUserByToken(String token);
  public TaotaoResult logoutUser(String token);
  public TaotaoResult loginUse(String username, String password, HttpServletRequest request,
		HttpServletResponse response);
}
