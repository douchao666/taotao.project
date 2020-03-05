package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.taotao.commom.utils.ExceptionUtil;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	/**
	 * 用户数据检查
	 * @param user
	 * 支持jsonp跨域调用
	 * @return
	 */
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public Object checkData(@PathVariable String param,@PathVariable Integer type,String callback) {
		
		TaotaoResult result = null;
		if (StringUtils.isBlank(param)) {
			result = TaotaoResult.build(400, "校验内容不能为空");
		}
		if (type == null) {
			result = TaotaoResult.build(400, "校验内容类型不能为空");
		}
		if (type != 1 && type != 2 && type != 3) {
			result = TaotaoResult.build(400, "校验类型错误");
		}
		
		if (result != null) {
			if (callback != null) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
				mappingJacksonValue.setValue(callback);
				return mappingJacksonValue;
			}else {
				return result;
			}
		}
		
		try {
			 result = userService.checkData(param, type);
		} catch (Exception e) {
			result =  TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		
		if (callback != null) {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setValue(callback);
			return mappingJacksonValue;
		}else {
			return result;
		}
	}
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/register",method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult registUser(TbUser user) {
		
		TaotaoResult result = null;
		try {
			  result = userService.regisUser(user);
			  //此处可以进行数据校验
			  
		} catch (Exception e) {
			e.printStackTrace();
			  result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return result;
	}
	/**
	 * 用户登陆
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/login",method = RequestMethod.POST)
	@ResponseBody
	public TaotaoResult loginUser(String username,String password,HttpServletRequest request,HttpServletResponse response) {
		try {
			TaotaoResult result = userService.loginUse(username, password,request,response);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}
	/**
	 * 用户查询
	 * @param user
	 * @GET方法 支持jsonP调用
	 * @return
	 */
	@RequestMapping("/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback) {
		TaotaoResult taotaoResult = null;
		try {
			taotaoResult = userService.getUserByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
			taotaoResult = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		if (StringUtils.isBlank(callback)) {
			return taotaoResult;
		}else {
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(taotaoResult);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
	}
	/**
	 * 用户安全退出
	 * @param user
	 * @GET方法
	 * @return
	 */
	@RequestMapping("/logout/{token}")
	@ResponseBody
	public TaotaoResult logoutUser(@PathVariable String token) {
		
		TaotaoResult result = null;
		try {
			result = userService.logoutUser(token);
		} catch (Exception e) {
			e.printStackTrace();
			result = TaotaoResult.build(400, ExceptionUtil.getStackTrace(e));
		}
		
		return result;
	}
	

}
