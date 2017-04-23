package com.wheelys.service.cafe;

import java.util.List;

import com.wheelys.model.CafeProduct;

public interface CafeProductService {

	List<CafeProduct> getProductList(List<Long> idList);

	CafeProduct getProduct(Long productid);
	
	CafeProduct getCacheProduct(Long id);

}
