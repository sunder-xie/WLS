package com.wheelys.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.model.CartProduct;
import com.wheelys.model.discount.DiscountActivity;

public class DiscountUtil {
	
	public static boolean validDiscount(DiscountActivity discountActivity, List<CartProduct> cartProductVoList) {
		int cardtotalprice = 0;
		int cardtotalcup = 0;
		for (CartProduct cartProduct : cartProductVoList) {
			if(containsProductid(discountActivity.getValidproductid(), cartProduct.getProductid())){
				cardtotalprice += cartProduct.getTotalfee();
				cardtotalcup += cartProduct.getQuantity();
			}
		}
		if(StringUtils.isNotBlank(discountActivity.getWeek())){
			int week = DateUtil.getWeek(DateUtil.getCurDate());
			if(!StringUtils.contains(discountActivity.getWeek(), week+"")){
				return false;
			}
		}
		if((discountActivity.getLimitamount() == null && discountActivity.getLimitcup() == null) ||
			(discountActivity.getLimitamount() != null && discountActivity.getLimitcup() != null &&
			discountActivity.getLimitamount() == 0 && discountActivity.getLimitcup() == 0)){
			return true;
		}
		if(discountActivity.getLimitamount() != null && discountActivity.getLimitamount() > 0){
			if(cardtotalprice >= discountActivity.getLimitamount()){
				return true;
			}
		}
		if(discountActivity.getLimitcup() != null && discountActivity.getLimitcup() > 0){
			if(cardtotalcup >= discountActivity.getLimitcup()){
				return true;
			}
		}
		return false;
	}

	private static boolean containsProductid(String validproductid, Long productid) {
		//判断此品项是否可以使用此优惠券
		List<Long> productidList = getProductidList(validproductid);
		return productidList.contains(productid);
	}
	
	public static List<Long> getProductidList(String validproductid){
		Map<String, String> map = JsonUtils.readJsonToMap(validproductid);
		List<Long> productidList = new ArrayList<Long>();
		for (String key : map.keySet()) {
			productidList.addAll(BeanUtil.getIdList(map.get(key), ","));
		}
		return productidList;
	}
	
}
