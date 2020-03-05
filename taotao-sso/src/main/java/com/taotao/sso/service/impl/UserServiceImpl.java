package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import com.taotao.commom.utils.CookieUtils;
import com.taotao.commom.utils.JsonUtils;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;
	@Value("${SSO_SESSION_EXPIRE}")
	private Integer SSO_SESSION_EXPIRE;
	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Override
	public TaotaoResult checkData(String content, Integer type) {
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if (1 == type) {
			criteria.andUsernameEqualTo(content);
		}else if(2 == type){
			criteria.andEmailEqualTo(content);
		}else {
			criteria.andPhoneEqualTo(content);
		}
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return TaotaoResult.ok(true);
		}
		return TaotaoResult.ok(false);
	}
	@Override
	public TaotaoResult regisUser(TbUser user) {
		user.setCreated(new Date());
		user.setUpdated(new Date());
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		userMapper.insert(user);
		return TaotaoResult.ok();
	}
	@Override
	public TaotaoResult loginUse(String username, String password,HttpServletRequest request,HttpServletResponse response) {
		
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example );
		if (list == null || list.size() == 0) {
			return TaotaoResult.build(400, "用户名或者密码错误");
		}
		TbUser user = list.get(0);
		if (! DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return TaotaoResult.build(400, "用户名或者密码错误");
		}
		
		//生成token
		String token = UUID.randomUUID().toString();
		user.setPassword(null);
		jedisClient.set(REDIS_USER_SESSION_KEY + ":" + token, JsonUtils.objectToJson(user));
		//设置session的过期时间
		jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		
		//添加写cookie的逻辑 cookie的有效期是关闭浏览器就失效
		CookieUtils.setCookie(request, response, "TT_TOKEN", token);
		return TaotaoResult.ok(token);
	}
	@Override
	public TaotaoResult getUserByToken(String token) {
		
		String json = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		if (user == null) {
			return TaotaoResult.build(400, "此session过期了");
		}
		jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
		return TaotaoResult.ok(user);
	}
	
	@Override
	public TaotaoResult logoutUser(String token) {
	
		try {
			jedisClient.deleteKey(REDIS_USER_SESSION_KEY + ":" + token);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(400,"用户已经安全退出失败");
		}
		return TaotaoResult.ok();
	}
}
