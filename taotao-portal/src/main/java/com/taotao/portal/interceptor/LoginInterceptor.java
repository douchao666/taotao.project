package com.taotao.portal.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.commom.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.impl.UserServiceImpl;

public class LoginInterceptor implements HandlerInterceptor{
	
	@Autowired
	private UserServiceImpl userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		
		// 在处理器之前
		//判断用户是否登陆
		//1.从cookie中回去token
		//根据token获取用户信息，调用sso接口
		//取不到用户信息
		//跳转到登陆界面，将用户请求的url作为参数传递给登陆界面
		//返回false
		//取到用户信息，放行
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		TbUser user = userService.getUser(token);
		if (user == null) {
			//跳转到登陆界面，把用户请求的url作为参数传递给
			response.sendRedirect(userService.SSO_BASE_URL + userService.SSO_PAGE_LOGIN + 
					"?redirect=" + request.getRequestURL());
			return false;
		}
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
        //在处理器之后，在返回modelanView之前		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 返回modelandView之后
		
	}

}
