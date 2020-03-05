package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;

public interface ItemParamService {
	public EUDataGridResult getDataGridResult(int page,int rows);
	public TaotaoResult getItemParamByCid(long cid);
	public TaotaoResult InsertItemParam(TbItemParam item);
}
