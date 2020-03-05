package com.taotao.portal.service;

import com.taotao.common.pojo.ItemInfo;

public interface ItemService {

	public ItemInfo getItemById(long itemId);
	public String getItemDesc(long itemId);
	public String getItemParam(long itemId);
}
