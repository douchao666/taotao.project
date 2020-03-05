package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.commom.utils.HttpClientUtil;
import com.taotao.commom.utils.SendJMail;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.service.ContentService;


@Service
public class ContentServiceImpl implements ContentService{

	
	@Autowired
	private TbContentMapper contentMapper;
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${REST_CONTENT_SYNC_URL}")
	private String REST_CONTENT_SYNC_URL;
	@Override
	public EUDataGridResult queryContentList(int page, int rows, long categoryId) {
		
		
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example );
		PageHelper.startPage(page, rows);
		EUDataGridResult euDataGridResult = new EUDataGridResult();
		euDataGridResult.setRows(list);
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
		euDataGridResult.setTotal(pageInfo.getTotal());
		return euDataGridResult;
	}
	@Override
	public TaotaoResult insertContentService(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		try {
			HttpClientUtil.doGet(REST_BASE_URL+REST_CONTENT_SYNC_URL+content.getCategoryId());
		} catch (Exception e) {
			//通知管理员 --发短信、发邮件
			e.printStackTrace();
			String email = "2623658502@qq.com";
			String emailMsg = "缓存失败" + "\t" +  new Date().toLocaleString() + content.getTitle();
			SendJMail.sendMail(email, emailMsg);
		}
		return TaotaoResult.ok();
	}

}
