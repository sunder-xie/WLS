package com.wheelys.service.cafe.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.Config;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeShop;
import com.wheelys.model.ProductShop;
import com.wheelys.service.cafe.CafeProductService;
import com.wheelys.service.cafe.WpsService;
import com.wheelys.util.LatLngSortUtils;
import com.wheelys.web.action.openapi.mobile.vo.OpenApiCafeItemVo;
import com.wheelys.web.action.openapi.mobile.vo.OpenApiCafeProductVo;

@Service("wpsService")
public class WpsServiceImpl extends BaseServiceImpl implements WpsService {

	@Autowired@Qualifier("config")
	private Config config;
	@Autowired@Qualifier("cafeProductService")
	private CafeProductService cafeProductService;
	
	@Override
	public List<CafeShop> queryNearShopList(String citycode, String lat, String lng) {
		DetachedCriteria query = DetachedCriteria.forClass(CafeShop.class);
		query.add(Restrictions.eq("booking", "open"));
		List<CafeShop> shopList = baseDao.findByCriteria(query);
		if(StringUtils.isNotBlank(lng)){
			shopList = LatLngSortUtils.getShopListByNear(lat, lng, shopList);
		}
		return shopList;
	}

	@Override
	public Map<CafeItem, List<CafeProduct>> getShopCafeProductMap(Long shopid) {
		Map<CafeItem, List<CafeProduct>> cafeProductMap = new LinkedHashMap<CafeItem, List<CafeProduct>>();
		List<CafeItem> cafeItemList = getCafeItemList();
		List<CafeProduct> cafeProductList = getCafeProductListByShop(shopid);
		Map map = BeanUtil.groupBeanList(cafeProductList, "itemid");
		for (CafeItem cafeItem : cafeItemList) {
			List<CafeProduct> list = (List<CafeProduct>) map.get(cafeItem.getId());
			if(list == null || list.isEmpty()){
				continue;
			}
			cafeProductMap.put(cafeItem, list);
		}
		return cafeProductMap;
	}
	
	private List<CafeProduct> getCafeProductListByShop(Long shopid){
		DetachedCriteria query = DetachedCriteria.forClass(ProductShop.class);
		query.add(Restrictions.eq("shopid", shopid));
		query.add(Restrictions.eq("delstatus", "N"));
		query.add(Restrictions.eq("display", 1));
		query.addOrder(Order.asc("psorder"));
		query.setProjection(Projections.property("productid"));
		List<Long> idList = baseDao.findByCriteria(query);
		List<CafeProduct> productList = cafeProductService.getProductList(idList);
		return productList;
	}
	
	private List<CafeItem> getCafeItemList(){
		DetachedCriteria query = DetachedCriteria.forClass(CafeItem.class);
		query.add(Restrictions.eq("state", "Y"));
		query.addOrder(Order.asc("sortkey"));
		List<CafeItem> itemList = baseDao.findByCriteria(query);
		return itemList;
	}
	
	@Override
	public List<OpenApiCafeItemVo> getShopCafeItemVoList(Long shopid) {
		Map<CafeItem, List<CafeProduct>> itemMap = this.getShopCafeProductMap(shopid);
		List<OpenApiCafeItemVo> itemVoList = new ArrayList<OpenApiCafeItemVo>();
		for (CafeItem item : itemMap.keySet()) {
			List<OpenApiCafeProductVo> productVoList = new ArrayList<OpenApiCafeProductVo>();
			for (CafeProduct product : itemMap.get(item)) {
				OpenApiCafeProductVo productVo = new OpenApiCafeProductVo(product, config.getString("picPath"));
				productVoList.add(productVo);
			}
			OpenApiCafeItemVo itemVo = new OpenApiCafeItemVo(item, productVoList);
			itemVoList.add(itemVo);
		}
		return itemVoList;
	}

}
