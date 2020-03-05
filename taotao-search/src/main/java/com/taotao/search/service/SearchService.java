package com.taotao.search.service;

import com.taotao.common.pojo.TaotaoResult;

public interface SearchService {

	public TaotaoResult search(String queryString,int page,int rows) throws Exception;
}
