package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taotao.common.pojo.EUTreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService{
	
	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public List<EUTreeNode> getItemCatSList(long parentId) {
		
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCatMapper.selectByExample(example );
		List<EUTreeNode> list1 = new ArrayList<EUTreeNode>();
		for (TbItemCat tbItemCat2 : list) {
			EUTreeNode node = new EUTreeNode();
			node.setId(tbItemCat2.getId());
			node.setText(tbItemCat2.getName());
			node.setState(tbItemCat2.getIsParent() ? "closed" : "open");
			list1.add(node);
		}
		return list1;
	}

}
