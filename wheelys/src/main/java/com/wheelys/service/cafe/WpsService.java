package com.wheelys.service.cafe;

import java.util.List;
import java.util.Map;

import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeShop;
import com.wheelys.web.action.openapi.mobile.vo.OpenApiCafeItemVo;

public interface WpsService {

	List<CafeShop> queryNearShopList(String citycode, String lat, String lng);

	Map<CafeItem, List<CafeProduct>> getShopCafeProductMap(Long shopid);
	
	List<OpenApiCafeItemVo> getShopCafeItemVoList(Long shopid);

}
