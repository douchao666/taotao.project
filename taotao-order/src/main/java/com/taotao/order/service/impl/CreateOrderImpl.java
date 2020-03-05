package com.taotao.order.service.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.dao.JedisClient;
import com.taotao.order.service.CreateOrder;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

@Service
public class CreateOrderImpl implements CreateOrder{

	@Value("${ORDER_GEN_KEY}")
	private String ORDER_GEN_KEY;
	@Value("${ORDER_INIT_ID}")
	private String ORDER_INIT_ID;
	@Value("${ORDER_DETAIL_GEN_KEY}")
    private String ORDER_DETAIL_GEN_KEY;
	
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private JedisClient jedisClient;
	@Override
	public TaotaoResult creatOrder(TbOrder order, List<TbOrderItem> orderItem, 
			TbOrderShipping orderShipping) {
		//向表单中插入记录
		//获得订单号
		String string = jedisClient.get(ORDER_GEN_KEY);
		if (StringUtils.isBlank(string)) {
			jedisClient.set(ORDER_GEN_KEY, ORDER_INIT_ID);
		}
		long orderId = jedisClient.incr(ORDER_GEN_KEY);
		
		//补全pojo的属性
		Date date = new Date();
		order.setCreateTime(date);
		order.setUpdateTime(date);
		order.setOrderId(orderId + "");
		//1 表示未付款 2 已付款 3 未发货 4 已发货 5 交易成功 6 交易关闭
		order.setStatus(1);
		//0 是未评价 1 已评价
		order.setBuyerRate(0);
		//插入订单数据
		orderMapper.insert(order);
		//插入订单明细
		for (TbOrderItem tbOrderItem : orderItem) {
			long detai = jedisClient.incr(ORDER_DETAIL_GEN_KEY);
			tbOrderItem.setId(detai + "");
			orderItemMapper.insert(tbOrderItem);
		}
		//插入物流表
		//补全物流表的属性
		orderShipping.setOrderId(orderId + "");
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		return TaotaoResult.ok(orderId);
	}

}
