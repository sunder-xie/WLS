package com.wheelys.service.pos;

import java.sql.Timestamp;
import java.util.List;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.helper.OrderContainer;
import com.wheelys.model.ShopSeller;
import com.wheelys.web.action.openapi.vo.PosOrderDetailVo;
import com.wheelys.web.action.openapi.vo.PosOrderVo;

public interface PosService {
	
	ResultCode<ShopSeller> shopUserLogin(Long shopid, String loginname, String pass);

	ResultCode<OrderContainer> addPosOrder(Long shopid, String citycode, List<PosOrderDetailVo> detailList, String clientIp);
	
	ResultCode changeOrderStatus(Long shopid, String status, String tradenolist);

	ResultCode posOrderPay(Long shopid, String tradeno, String payseqno);

	ResultCode changeShopStatus(Long shopid, String status);

	List<PosOrderVo> getPosOrderList(Long shopid, String status, Timestamp fromtime);

	List<PosOrderVo> getHistortyOrderList(Long shopid, Timestamp fromtime, Timestamp totime, String tradeno,
			Integer pageno);
	
}
