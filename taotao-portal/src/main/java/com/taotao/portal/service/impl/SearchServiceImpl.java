package com.taotao.portal.service.impl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.commom.utils.HttpClientUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.service.SearchService;


@Service
public class SearchServiceImpl implements SearchService{


	@Value("${SEARCH_BASE_URL}")
	private String SEARCH_BASE_URL;
	
	@Override
	public TaotaoResult search(String queryString, Integer page) {
		
		Map<String, String> param = new HashMap<>();
		param.put("q", queryString);
		param.put("page", page == null? "1" : page.toString());
		//调用taotao-search提供的搜索服务
		
		try {
			String json = HttpClientUtil.doGet(SEARCH_BASE_URL, param);
			TaotaoResult result = TaotaoResult.formatToPojo(json, TaotaoResult.class);
			if (result.getStatus() == 200) {
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
}