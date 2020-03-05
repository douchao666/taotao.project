package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.service.SearchService;


@Service
public class SearchServicecImpl implements SearchService{

	
	@Autowired
	private SearchDao searchDao;
	
	@Override
	public TaotaoResult search(String queryString, int page, int rows) throws Exception {
		
		SolrQuery query = new SolrQuery();
	    query.setQuery(queryString);
	    query.setStart((page - 1) * rows);
	    query.setRows(rows);
	    //设置默认搜索阈
	    query.set("df","item_keywords");
	    //设置高亮显示
	    query.setHighlight(true);
	    query.addHighlightField("item_title");
	    query.setHighlightSimplePre("<em style=\"color:red\">");
	    query.setHighlightSimplePost("</em>");
	    
	    SearchResult result = searchDao.search(query);
	    long record = result.getTotalRecord();
	    long pageCount = record / rows;
	    if (pageCount % rows > 0) {
			pageCount += 1;
		}
	    
	    result.setPageCount(pageCount);
	    result.setCurrentPage(page);
		return TaotaoResult.ok(result);
	}

}
