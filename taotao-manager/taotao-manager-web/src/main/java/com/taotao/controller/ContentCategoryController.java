package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ContentCategoryService;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

	@Autowired
	private ContentCategoryService categoryService;
	
	@RequestMapping("/list")
	@ResponseBody
	public List<EUTreeNode> getContentCategory(@RequestParam(value="id",defaultValue = "0")long parentId){
		List<EUTreeNode> list = categoryService.getCategoryList(parentId);
		return list;
	}
	
	
	@RequestMapping("/create")
	@ResponseBody
	public TaotaoResult insertContentCategory(long parentId,String name) {
		
		TaotaoResult result = categoryService.insertContentCategory(parentId, name);
		return result;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public TaotaoResult deleteContentCategory(long id) {
		
		categoryService.deleteContentCategory(id);
		return TaotaoResult.ok();
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public TaotaoResult updateContentCategory(long id,String name) {
		
		
		TaotaoResult result = categoryService.updateContentCategory(id, name);
		return result;
	}
}
