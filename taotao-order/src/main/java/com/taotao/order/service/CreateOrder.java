package com.taotao.order.service;

import java.util.List;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

public interface CreateOrder {

	public TaotaoResult creatOrder(TbOrder order,List<TbOrderItem> orderItem,TbOrderShipping orderShipping);
}
