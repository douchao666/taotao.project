package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.service.ItemParamItemService;

@Controller
public class ItemParamItemController {

	
	@Autowired 
	private ItemParamItemService itemParamItemService;
	
	
	@RequestMapping("/items/param/item/{itemId}")
	@ResponseBody
	public String itemParamItemShow(@PathVariable long itemId,Model model) {
		String string = itemParamItemService.ItemParamItemShow(itemId);
		model.addAttribute("itemParams", string);
		return "item-param-show";
	}
}
