package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.commom.utils.JsonUtils;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService{

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;
	@Value("${REDIS_ITEM_EXPIRE}")
	private Integer REDIS_ITEM_EXPIRE;
	@Autowired
	private TbItemDescMapper descMapper;
	@Autowired
	private TbItemParamItemMapper paramItemMapper;
	
	@Override
	public TaotaoResult getItemBaseInfo(long itemId) {
		
		try {
			String result = jedisClient.get("REDIS_ITEM_KEY" + ":" + itemId + ":base");
			if (StringUtils.isNotBlank(result)) {
				return TaotaoResult.ok(JsonUtils.jsonToPojo(result, TbItem.class));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
			
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		
		try {
			//把商品信息写入缓存
			//设置KEY的有效期
			jedisClient.set("REDIS_ITEM_KEY" + ":" + itemId + ":base", JsonUtils.objectToJson(item));
			jedisClient.expire("REDIS_ITEM_KEY" + ":" + itemId + ":base",REDIS_ITEM_EXPIRE);
		} catch (Exception e) {
			//缓存失败发消息给管理员
			e.printStackTrace();
		}
		return TaotaoResult.ok(item);
	}
	
	@Override
	public TaotaoResult getItemDesc(long itemId) {
		//添加缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":desc");
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return TaotaoResult.ok(itemDesc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TbItemDesc itemDesc = descMapper.selectByPrimaryKey(itemId);
		
		try {
			jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":desc", JsonUtils.objectToJson(itemDesc));
			jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":desc", REDIS_ITEM_EXPIRE);
		} catch (Exception e) {
			//通知管理员
			e.printStackTrace();
		}

		return TaotaoResult.ok(itemDesc);
		
	}

	@Override
	public TaotaoResult getItemParam(long itemId) {
		
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":param");
			if (StringUtils.isNotBlank(json)) {
				TbItemParamItem paramItem = JsonUtils.jsonToPojo(json, TbItemParamItem.class);
				return TaotaoResult.ok(paramItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = paramItemMapper.selectByExampleWithBLOBs(example);
		TbItemParamItem paramItem = null;
		if (list != null && list.size() > 0) {
			paramItem = list.get(0);
			//添加缓存
			try {
				jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":param", JsonUtils.objectToJson(paramItem));
				jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":param", REDIS_ITEM_EXPIRE);
			} catch (Exception e) {
				//通知管理员
				e.printStackTrace();
			}
			return TaotaoResult.ok(paramItem);
		}
		return TaotaoResult.build(400, "无此商品规格");
	}

}
