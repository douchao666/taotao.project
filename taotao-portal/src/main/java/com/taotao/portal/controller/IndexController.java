package com.taotao.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.portal.service.ContentService;


@Controller
public class IndexController {

	@Autowired
	private ContentService service;
	@RequestMapping("/index")
	public String showIndex(Model model) {
		String list = service.getContentList();
		model.addAttribute("ad1",list);
		return "index";
	}
	
	@RequestMapping(value="/httpclient/post",method = RequestMethod.POST,produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
	@ResponseBody
	public String doPost(String username,String password) {
		return "username" + username + "\t" + "password" + password;
	}
}
