package com.taotao.portal.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.taotao.commom.utils.CookieUtils;
import com.taotao.commom.utils.HttpClientUtil;
import com.taotao.commom.utils.JsonUtils;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;

@Service
public class CartServiceImpl implements CartService{

	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${ITEM_BASE_URL}")
	private String ITEM_BASE_URL;
	@Override
	public TaotaoResult addCartItem(long itemId, int num,HttpServletRequest request,HttpServletResponse response) {
			
		// 取商品信息
		CartItem cartItem = null;
		// 去购物车商品列表
		List<CartItem> list = getItemList(request,response);
		for (CartItem cItem : list) {
			if (cItem.getId() == itemId) {
				cartItem = cItem;
				cartItem.setNum(cItem.getNum() + num);
				break;
			}
		}
		if (cartItem == null) {
			cartItem = new CartItem();
			String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_BASE_URL + itemId);
			// 将json转化为pojo
			TaotaoResult taotaoResult = TaotaoResult.formatToPojo(json, TbItem.class);
			if (taotaoResult.getStatus() == 200) {
				TbItem item = (TbItem) taotaoResult.getData();
				cartItem.setId(item.getId());
				cartItem.setImage(item.getImage() == null ? "" : item.getImage().split(",")[0]);
				cartItem.setPrice(item.getPrice());
				cartItem.setTitle(item.getTitle());
				cartItem.setNum(item.getNum());
			}
			list.add(cartItem);
		}
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list),true);
		return TaotaoResult.ok();
	}
	/**
	 * 从cookie中取商品列表
	 * @return
	 */
	private  List<CartItem> getItemList(HttpServletRequest request,HttpServletResponse response){
		
		String cartJson = CookieUtils.getCookieValue(request, "TT_CART",true);
		if (cartJson == null) {
			return new ArrayList();
		}
        List<CartItem> list = null;
		try {
			list = JsonUtils.jsonToList(cartJson, CartItem.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<CartItem>();
		}
		return list;  
	}
	/**
	 * 更新数量
	 */
	@Override
	public TaotaoResult updateCartItem(Long itemId,Integer num,HttpServletRequest request,
			HttpServletResponse response) {
		List<CartItem> list = getItemList(request, response);
		for (CartItem cartItem : list) {
			if (cartItem.getId() == itemId) {
				cartItem.setNum(num);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list),true);
		return TaotaoResult.ok();
	}
	
	/**
	 * 更新购物车
	 */
	@Override
	public TaotaoResult deleteCartItem(Long itemId, HttpServletRequest request, HttpServletResponse response) {
		
		List<CartItem> list = getItemList(request, response);
		for (CartItem cartItem : list) {
			if (cartItem.getId() == itemId ) {
				list.remove(cartItem);
				break;
			}
		}
		CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list),true);
		return TaotaoResult.ok();
	}
	
	@Override
	public List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) {
        List<CartItem> list = this.getCartItemList(request, response);
		return list;
	}
	
	
}
