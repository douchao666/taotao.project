package com.taotao.portal.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.service.SearchService;

@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;
	@RequestMapping("/search")
	@ResponseBody
	public String search(@RequestParam(defaultValue = "q")String queryString,@RequestParam(defaultValue = "1")Integer page,Model model) throws UnsupportedEncodingException {
		
		queryString = new String(queryString.getBytes("iso8859-1"),"utf-8");
		TaotaoResult result = searchService.search(queryString, page);
		SearchResult searchResult = (SearchResult) result.getData();
		model.addAttribute("itemList",searchResult.getItemList() );
		model.addAttribute("query", queryString);
		model.addAttribute("totalPages", searchResult.getPageCount());
		model.addAttribute("page", searchResult.getCurrentPage());
		model.addAttribute("pages", searchResult.getPageCount());	
		return "search";		
	}
}

