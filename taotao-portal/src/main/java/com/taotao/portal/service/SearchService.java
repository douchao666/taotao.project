package com.taotao.portal.service;

import com.taotao.common.pojo.TaotaoResult;

public interface SearchService {

	public TaotaoResult search(String queryString,Integer page);
}
