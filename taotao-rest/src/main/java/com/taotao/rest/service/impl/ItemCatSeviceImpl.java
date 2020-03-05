package com.taotao.rest.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.taotao.commom.utils.JsonUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;


@Service
public class ItemCatSeviceImpl implements ItemCatService{

	
	@Autowired 
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${INDEX_ITEMCAT_REDIS_KEY}")
	private String INDEX_ITEMCAT_REDIS_KEY;

	
	@Override
	public CatResult getItemCatList() {
		
		//加入查询缓存
		try {
			String result = jedisClient.get(INDEX_ITEMCAT_REDIS_KEY);
		    if (StringUtils.isNotBlank(result)) {
		    	CatResult catResult = JsonUtils.jsonToPojo(result, CatResult.class);
			   	return catResult;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		CatResult result = new CatResult();
		result.setData(getCatList(0));

		//缓存
		try {
			
			String result1 = JsonUtils.objectToJson(result);
			jedisClient.set(INDEX_ITEMCAT_REDIS_KEY, result1);
			
 		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private List<?> getCatList(long parentId){
		

		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		List dataList = new ArrayList();
		int count = 0;
		for (TbItemCat tbItemCat : list) {
			//判断是否为父节点
			if (tbItemCat.getIsParent()) {
				CatNode cateNode = new CatNode();
				cateNode.setUrl("/category/" + tbItemCat.getId() + ".html");
				cateNode.setName(tbItemCat.getName());
				//递归调用
				cateNode.setItem(getCatList(tbItemCat.getId()));
				//添加到列表
				dataList.add(cateNode);
				count++;
				if (parentId == 0 && count >= 14) {
					break;
				}
			} else {
				String catItem = "/item/" + tbItemCat.getId() + ".html|" + tbItemCat.getName();
				dataList.add(catItem);
			}
		}
		return dataList;
	}
}
