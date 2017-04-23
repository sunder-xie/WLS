package com.wheelys.helper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.support.PropertyComparator;

import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.OuterSorter;
import com.wheelys.constant.ElecCardConstant;
import com.wheelys.model.CartProduct;
import com.wheelys.web.action.wap.vo.ElecCardVo;

public class ElecCardUtil {
	
	public static ElecCardVo getCardList(List<ElecCardVo> cardList,Long shopid, List<CartProduct> cartProductVoList){
		if(cardList.isEmpty()) return null;
		OuterSorter<ElecCardVo> sorter = new OuterSorter<ElecCardVo>(false);
		List<CartProduct> productVoList = new ArrayList<CartProduct>();
		productVoList.addAll(cartProductVoList);
		Collections.sort(productVoList, new PropertyComparator("price", false, false));
		for (ElecCardVo cardVo : cardList) {
			String status = "N"; 
			for (CartProduct cart : productVoList) {
				if(StringUtils.equals(cardVo.getCardtype(), ElecCardConstant.CARDTYPE_DEDUCTION)){
					cardVo.setUseamount(cardVo.getAmount());
				}else{
					cardVo.setUseamount(cart.getPrice());
				}
				Integer cardtotalprice = ElecCardUtil.validPrice(shopid, cardVo.getShopid(), cardVo.getProductid(), cartProductVoList);
				if(ElecCardUtil.validCard(shopid, cardVo.getShopid(), cardVo.getProductid(), cart.getProductid(),cardVo.getBegintime(),cardVo.getEndtime(), 
						cardVo.getCardtype(),cardtotalprice, cardVo.getMinprice())){
					status = "Y";
					break;
				}
			}
			if(StringUtils.equals(status, "Y")){
				if(StringUtils.isNotBlank(cardVo.getAnnexation())){
					List<Long> idList = BeanUtil.getIdList(cardVo.getAnnexation(), ",");
					if(!idList.isEmpty()){
						cardVo.setDesc("(另送鲜花)");
					}
				}
				sorter.addBean(cardVo.getUseamount(), cardVo);
			}
		}
		if(sorter.getDescResult().isEmpty())return null;
		return sorter.getDescResult().get(0);
	}
	
	public static boolean validCard(Long shopid, String validshopid, String validproductid, Long productid,
			Timestamp begintime, Timestamp endtime, String type, Integer totalfee,Integer mintotalfee){
		if(!validCardDate(begintime, endtime)){
			return false;
		}
		if(!containsProductid(shopid, validshopid, validproductid, productid)){
			return false;
		}
		if(StringUtils.equals(type, ElecCardConstant.CARDTYPE_DEDUCTION)){
			return totalfee >= mintotalfee*100;
		}
		return true;
	}
	private static boolean validCardDate(Timestamp begintime, Timestamp endtime){
		if(begintime.before(DateUtil.getMillTimestamp()) && endtime.after(DateUtil.getMillTimestamp())){
			return true;
		}
		return false;
	}

	private static boolean containsProductid(Long shopid, String validshopid, String validproductid, Long productid) {
		//判断店铺是否可以此优惠券
		List<Long> shopidList = BeanUtil.getIdList(validshopid, ",");
		if(!shopidList.contains(shopid))return false;
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

	public static Integer validPrice(Long shopid, String validshopid, String validproductid, List<CartProduct> cartProductVoList) {
		int cardtotalprice = 0;
		for (CartProduct cartProduct : cartProductVoList) {
			if(containsProductid(shopid, validshopid, validproductid, cartProduct.getProductid())){
				cardtotalprice += cartProduct.getTotalfee();
			}
		}
		return cardtotalprice * 100;
	}
	
}
