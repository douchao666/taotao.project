package com.taotao.portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import com.taotao.commom.utils.HttpClientUtil;
import com.taotao.commom.utils.JsonUtils;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.pojo.Order;
import com.taotao.portal.service.OrderService;

public class OrderServiceImpl implements OrderService{

	@Value("${ORDER_BASE_URL}")
	private String ORDER_BASE_URL;
	@Value("${ORDER_CREATE_URL}")
	private String ORDER_CREATE_URL;
	
	@Override
	public String createOrder(Order order) {
		
		String json = HttpClientUtil.doPostJson(ORDER_BASE_URL + ORDER_CREATE_URL, JsonUtils.objectToJson(order));
		TaotaoResult result = TaotaoResult.formatToPojo(json, TaotaoResult.class);
		if (result.getStatus() == 200) {
			Object orderId = result.getData();
			return orderId.toString();
		}
		return "";
	}

}
