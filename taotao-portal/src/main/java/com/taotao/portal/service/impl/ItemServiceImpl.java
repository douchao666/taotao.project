package com.taotao.portal.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.taotao.commom.utils.HttpClientUtil;
import com.taotao.commom.utils.JsonUtils;
import com.taotao.common.pojo.ItemInfo;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.portal.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService{

	@Value("${ITEM_BASE_URL}")
	private String ITEM_BASE_URL;
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${ITEM_DESC_URL}")
	private String ITEM_DESC_URL;
	@Value("${ITEM_PARAM_URL}")
	private String ITEM_PARAM_URL;

	@Override
	public ItemInfo getItemById(long itemId) {
		
		try {
			String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_BASE_URL + itemId);
			if (StringUtils.isNotBlank(json)) {
				TaotaoResult result = TaotaoResult.formatToPojo(json, ItemInfo.class);
				if (result.getStatus() == 200) {
					ItemInfo item = (ItemInfo) result.getData();
					return item;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getItemDesc(long itemId) {
		try {
			String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_DESC_URL + itemId);
			if (StringUtils.isNotBlank(json)) {
				TaotaoResult result = TaotaoResult.formatToPojo(json, TbItemDesc.class);
				if (result.getStatus() == 200) {
					TbItemDesc itemDesc = (TbItemDesc) result.getData();
					return itemDesc.getItemDesc();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getItemParam(long itemId) {
		
		String json;
		try {
			json = HttpClientUtil.doGet(ITEM_BASE_URL + ITEM_PARAM_URL + itemId);
			if (StringUtils.isNotBlank(json)) {
				TaotaoResult result = TaotaoResult.formatToPojo(json, TbItemParamItem.class);
				if (result.getStatus() == 200) {
					TbItemParamItem itemParam = (TbItemParamItem) result.getData();
					String paramData = itemParam.getParamData();
					List<Map> paramList = JsonUtils.jsonToList(paramData, Map.class);
					// 灏嗗弬鏁颁俊鎭浆鎹㈡垚html
					StringBuffer sb = new StringBuffer();
					// sb.append("<div>");
					sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\">\n");
					sb.append("    <tbody>\n");
					for (Map map : paramList) {
						sb.append("        <tr>\n");
						sb.append("            <th class=\"tdTitle\" colspan=\"2\">" + map.get("group") + "</th>\n");
						sb.append("        </tr>\n");
						List<Map> params = (List<Map>) map.get("params");
						for (Map map2 : params) {
							sb.append("        <tr>\n");
							sb.append("            <td class=\"tdTitle\">" + map2.get("k") + "</td>\n");
							sb.append("            <td>" + map2.get("v") + "</td>\n");
							sb.append("        </tr>\n");
						}
					}
					sb.append("    </tbody>\n");
					sb.append("</table>");
					return sb.toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
