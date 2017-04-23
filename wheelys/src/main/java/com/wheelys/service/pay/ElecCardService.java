package com.wheelys.service.pay;

import java.util.List;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.pay.ElecCard;
import com.wheelys.model.pay.ElecCardBatch;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.web.action.wap.vo.ElecCardVo;

public interface ElecCardService {

	ResultCode lockElecCard(Long memberid, Long cardid);
	
	ResultCode useElecCard(WheelysOrder order);
	
	ResultCode<ElecCard> bindElecCardByEbatchid(WheelysMember member, Long ebatchid);
	
	List<ElecCardVo> getElecCardVoList(Long memberid, String type, String status, int pageNo, int maxnum);

	ResultCode bindElecCardByEbatchid(WheelysMember member, String cardkey, String sign, Long ebatchid, int num, String curdate);

	Integer getBindElecCardCount(WheelysMember member, Long ebatchid, int num, String curdate);

	ElecCard getElecCard(Long memberid, Long cardid, String status);

	ElecCardBatch getElecCardBatch(Long ebatchid, String ukey);
	
	void unlockElecCard(Long memberid);

}
