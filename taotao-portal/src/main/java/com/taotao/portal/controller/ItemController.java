package com.taotao.portal.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.taotao.common.pojo.ItemInfo;
import com.taotao.portal.service.ItemService;

@Controller
public class ItemController {

	
	@Autowired
	private ItemService itemService;
	@RequestMapping("/item/{itemId}")
	public String getItemById(@PathVariable long itemId,Model model) {
		ItemInfo item = itemService.getItemById(itemId);
		model.addAttribute("item", item);
		return "item";
	}
	
	@RequestMapping(value="/item/desc/{itemId}",produces=MediaType.TEXT_HTML_VALUE + ";charset=utf-8" )
	@ResponseBody
	public String getItemDescById(@PathVariable long itemId) {
		String desc = itemService.getItemDesc(itemId);
		
	    return desc;
	}
	
	@RequestMapping(value="/item/param/{itemId}",produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
	@ResponseBody
	public String getItemParam(@PathVariable long itemId) {
		String param = itemService.getItemParam(itemId);
		return param;
	}
}
