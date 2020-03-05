package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {

	public EUDataGridResult queryContentList(int page,int rows,long categoryId);
	public TaotaoResult insertContentService(TbContent content);
}
