package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.service.ContentCategoryService;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{

	
	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	@Override
	public List<EUTreeNode> getCategoryList(long parentId) {

		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> result = tbContentCategoryMapper.selectByExample(example);
		List<EUTreeNode> list = new ArrayList<EUTreeNode>();
		for (TbContentCategory category : result) {
			EUTreeNode node = new EUTreeNode();
			node.setId(category.getId());
			node.setText(category.getName());
			node.setState(category.getIsParent() ? "closed" : "open");
			list.add(node);
		}
		return list;
	}
	@Override
	public TaotaoResult insertContentCategory(long parentId, String name) {
		TbContentCategory category = new TbContentCategory();
		category.setParentId(parentId);
		category.setName(name);
		category.setIsParent(false);
		category.setSortOrder(1);
		category.setCreated(new Date());
		category.setUpdated(new Date());
		category.setStatus(1);
		tbContentCategoryMapper.insert(category);
		//查看父节点的isParent列是否为true，如果不是true改成true
		TbContentCategory category2 = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(! category2.getIsParent()) {
			category2.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKey(category2);
		}
		return TaotaoResult.ok(category);
	}
	@Override
	public TaotaoResult deleteContentCategory(long id) {
		
		TbContentCategory category2 = tbContentCategoryMapper.selectByPrimaryKey(id);
		Long parentId = category2.getParentId();
		tbContentCategoryMapper.deleteByPrimaryKey(id);
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		if(list.size() == 0) {
			TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(parentId);
			category.setIsParent(false);
			tbContentCategoryMapper.updateByPrimaryKey(category);
		}
		return TaotaoResult.ok();
	}
	@Override
	public TaotaoResult updateContentCategory(long id, String name) {
		
		TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);
		if (!name.equals(category.getName())) {
			category.setName(name);
			tbContentCategoryMapper.updateByPrimaryKey(category);
		}
		return TaotaoResult.ok(category);
	}
}
