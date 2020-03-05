package com.taotao.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;
import com.taotao.service.ItemParamService;

@Controller
public class ItemParamController {

	@Autowired
	private ItemParamService itemParamService;
	
	@RequestMapping("/item/param/list")
	@ResponseBody
	public EUDataGridResult getDataGridResult(int page,int rows) {
		return itemParamService.getDataGridResult(page, rows);
	}
	@RequestMapping("/item/param/query/itemcatid/{itemcatid}")
	@ResponseBody
	public TaotaoResult getItemParamByCid(@PathVariable long itemcatid) {
		return itemParamService.getItemParamByCid(itemcatid);
	}
	
	@RequestMapping("/item/param/save/{cid}")
	@ResponseBody
	public TaotaoResult insertItemParam(@PathVariable long cid,String paramData) {
		
		TbItemParam tbItemParam = new TbItemParam();
		tbItemParam.setItemCatId(cid);
		tbItemParam.setParamData(paramData);
		TaotaoResult result = itemParamService.InsertItemParam(tbItemParam);
		return result;
	}
	
}
