package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;

@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EUDataGridResult queryContentList(int page,int rows,long categoryId) {
		
		EUDataGridResult result = contentService.queryContentList(page, rows, categoryId);
		return result;
	}
	@RequestMapping(value="content/save", method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult insertContent(TbContent content) {
		
		TaotaoResult result = contentService.insertContentService(content);
		return result;
	}
}
