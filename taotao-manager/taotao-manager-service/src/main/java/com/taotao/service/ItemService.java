package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface ItemService {

	public TbItem getItemById(long itemId);
	public EUDataGridResult getItemList(int page,int rows);
	public TaotaoResult CreatItemService(TbItem item ,String desc,String itemParams)throws Exception;
}

