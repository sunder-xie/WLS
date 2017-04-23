package com.wheelys.service.cafe.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.JsonUtils;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CartProduct;
import com.wheelys.service.cafe.CafeProductService;
import com.wheelys.service.cafe.ShoppingCartService;

@Service("shoppingCartService")
public class ShoppingCartServiceImpl extends BaseServiceImpl implements ShoppingCartService {

	@Autowired@Qualifier("cafeProductService")
	private CafeProductService cafeProductService;
	
	@Override
	public List<CartProduct> getMemberCart(String memberKey){
		List<CartProduct> list = getCartProduct(memberKey);
		for (CartProduct cartProduct : list) {
			CafeProduct product = cafeProductService.getProduct(cartProduct.getProductid());
			cartProduct.setProduct(product);
			String otherinfo = cartProduct.getOtherinfo();
			if(StringUtils.isNotBlank(otherinfo)){
				cartProduct.setMap(JsonUtils.readJsonToMap(otherinfo));
			}else{
				cartProduct.setMap(new HashMap());
			}
		}
		return list;
	}

	@Override
	public void addProductToCart(String memberKey, CartProduct productVo){
		CartProduct cartProduct = getCartProduct(memberKey, productVo.getPkey());
		if(cartProduct != null){
			cartProduct.setQuantity(cartProduct.getQuantity()+productVo.getQuantity());
		}else{
			CafeProduct cafeProduct = cafeProductService.getProduct(productVo.getProductid());
			if(cafeProduct == null) return;
			cartProduct = new CartProduct(memberKey,productVo.getProductid(), productVo.getQuantity(), productVo.getOtherinfo());
			cartProduct.setPkey(productVo.getPkey());
			cartProduct.setPrice(cafeProduct.getPrice());
			if(StringUtils.equals(cartProduct.getMap().get("bean")+"", "y")){
				cartProduct.setPrice(cafeProduct.getPrice()+3);
			}
		}
		cartProduct.setTotalfee(cartProduct.getPrice()*cartProduct.getQuantity());
		this.baseDao.saveObject(cartProduct);
	}

	@Override
	public void removeProductToCart(String memberKey, CartProduct productVo,int quantity){
		CartProduct cartProduct = getCartProduct(memberKey, productVo.getPkey());
		if(cartProduct != null){
			int curquantiy = cartProduct.getQuantity() + quantity;
			cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
			if(curquantiy < 1){
				this.baseDao.removeObject(cartProduct);
				return;
			}
			cartProduct.setTotalfee(cartProduct.getPrice()*cartProduct.getQuantity());
			this.baseDao.saveObject(cartProduct);
		}
	}

	@Override
	public void reset(String memberKey) {
		List<CartProduct> list = getCartProduct(memberKey);
		this.baseDao.removeObjectList(list);
	}

	@Override
	public void remove(String memberKey, CartProduct productVo) {
		CartProduct cartProduct = getCartProduct(memberKey, productVo.getPkey());
		if(cartProduct != null){
			this.baseDao.removeObject(cartProduct);
		}
	}

	@Override
	public Map getMemberCartMap(String memberKey, String cartkey) {
		List<CartProduct> list = getCartProduct(memberKey);
		Map map = new HashMap();
		if(list != null){
			int quantity = 0;
			int totalfee = 0;
			CartProduct vo = null;
			for (CartProduct cartProduct : list) {
				quantity += cartProduct.getQuantity();
				totalfee += cartProduct.getQuantity()*cartProduct.getPrice();
				if(StringUtils.equals(cartProduct.getPkey(), cartkey)){
					vo = cartProduct;
				}
			}
			map.put("quantity", quantity);
			map.put("totalfee", totalfee);
			if(vo != null){
				map.put("productnum", vo.getQuantity());
				map.put("productfee", vo.getQuantity()*vo.getPrice());
			}
		}else{
			map.put("quantity", 0);
			map.put("totalfee", 0);
		}
		return map;
	}
	
	private List<CartProduct> getCartProduct(String memberKey) {
		DetachedCriteria query = DetachedCriteria.forClass(CartProduct.class);
		query.add(Restrictions.eq("ukey", memberKey));
		List<CartProduct> shopList = baseDao.findByCriteria(query);
		return shopList;
	}
	
	private CartProduct getCartProduct(String memberKey, String cartkey) {
		DetachedCriteria query = DetachedCriteria.forClass(CartProduct.class);
		query.add(Restrictions.eq("ukey", memberKey));
		query.add(Restrictions.eq("pkey", cartkey));
		List<CartProduct> list = baseDao.findByCriteria(query);
		return list.isEmpty() ? null : list.get(0);
	}
	

}
