package com.wheelys.service.cafe.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.Gcache;
import com.wheelys.model.CafeProduct;
import com.wheelys.service.cafe.CafeProductService;

@Service("cafeProductService")
public class CafeProductServiceImpl extends BaseServiceImpl implements CafeProductService {

	public final Gcache<Long,CafeProduct> gcache = new Gcache<Long,CafeProduct>(500);
	
	@Override
	public List<CafeProduct> getProductList(List<Long> idList) {
		List<CafeProduct> productList = new ArrayList<CafeProduct>();
		for (Long id : idList) {
			CafeProduct product = getProduct(id);
			if(product != null){
				productList.add(product);
			}
		}
		return productList;
	}

	@Override
	public CafeProduct getProduct(Long id) {
		CafeProduct product = this.baseDao.getObject(CafeProduct.class, id);
		return product;
	}

	@Override
	public CafeProduct getCacheProduct(Long id) {
		try {
			CafeProduct product = gcache.getIfPresent(id);
			if(product == null){
				product = this.baseDao.getObject(CafeProduct.class, id);
				if(product == null)return null;
				gcache.put(id, product);
			}
			return product;
		} catch (Exception e) {
			dbLogger.warn("getProduct id :"+id);
			return null;
		}
	}
	
	@Override
	public CafeProduct getProductById(Long id) {
		// 根据id查询对应商品
		DetachedCriteria query = DetachedCriteria.forClass(CafeProduct.class);
		query.add(Restrictions.eq("display", 1));
		query.add(Restrictions.eq("id", id));
		List<CafeProduct> list = this.baseDao.findByCriteria(query);
		if (!list.isEmpty()) {
			CafeProduct product = list.get(0);
			return product;
		}else{
			return null;
		}
	}
	
}
