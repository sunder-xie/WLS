package com.wheelys.service.pay.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.StringUtil;
import com.wheelys.constant.ElecCardConstant;
import com.wheelys.model.order.WheelysOrder;
import com.wheelys.model.pay.ElecCard;
import com.wheelys.model.pay.ElecCardBatch;
import com.wheelys.model.user.WheelysMember;
import com.wheelys.service.pay.ElecCardService;
import com.wheelys.web.action.wap.vo.ElecCardVo;

@Service("elecCardService")
public class ElecCardServiceImpl extends BaseServiceImpl implements ElecCardService {

	@Override
	public ResultCode lockElecCard(Long memberid, Long cardid) {
		DetachedCriteria query = DetachedCriteria.forClass(ElecCard.class);
		query.add(Restrictions.eq("id", cardid));
		query.add(Restrictions.eq("memberid", memberid));
		query.add(Restrictions.gt("endtime", DateUtil.getMillTimestamp()));
		query.addOrder(Order.asc("bindtime"));
		List<ElecCard> elecCardList = baseDao.findByCriteria(query, 0 ,1);
		if(elecCardList.size() == 0){
			return ResultCode.getFailure("优惠券不存在");
		}
		ElecCard elecCard = elecCardList.get(0);
		if(StringUtils.equals(elecCard.getStatus(), ElecCardConstant.STATUS_SOLD)){
			elecCard.setStatus(ElecCardConstant.STATUS_LOCK);
			this.baseDao.saveObject(elecCard);
			return ResultCode.SUCCESS;
		}
		return ResultCode.getFailure("优惠券状态异常"+elecCard.getStatus());
	}
	
	@Override
	public void unlockElecCard(Long memberid) {
		DetachedCriteria query = DetachedCriteria.forClass(ElecCard.class);
		query.add(Restrictions.eq("memberid", memberid));
		query.add(Restrictions.gt("endtime", DateUtil.getMillTimestamp()));
		query.add(Restrictions.eq("status", ElecCardConstant.STATUS_LOCK));
		query.addOrder(Order.asc("bindtime"));
		List<ElecCard> elecCardList = baseDao.findByCriteria(query, 0 ,10);
		for (ElecCard elecCard : elecCardList) {
			elecCard.setStatus(ElecCardConstant.STATUS_SOLD);
			this.baseDao.saveObject(elecCard);
		}
	}

	@Override
	public ResultCode useElecCard(WheelysOrder order) {
		ResultCode<ElecCard> reuslt = getEleccard(order.getMemberid(), order.getCardid());
		if(reuslt.isSuccess()){
			ElecCard elecCard = reuslt.getRetval();
			if(StringUtils.equals(elecCard.getStatus(), ElecCardConstant.STATUS_LOCK)){
				elecCard.setStatus(ElecCardConstant.STATUS_USED);
				elecCard.setOrderid(order.getId());
				elecCard.setPaidamount(order.getCardpaid());
				this.baseDao.saveObject(elecCard);
				dbLogger.warn("优惠券使用成功，卡号："+elecCard.getId()+",memberid："+order.getMemberid());
				return ResultCode.SUCCESS;
			}
			return ResultCode.getFailure("优惠券状态异常"+elecCard.getStatus());
		}
		return ResultCode.getFailure(reuslt.getMsg());
	}
	
	private ResultCode<ElecCard> getEleccard(Long memberid, Long cardid){
		DetachedCriteria query = DetachedCriteria.forClass(ElecCard.class);
		query.add(Restrictions.eq("id", cardid));
		query.add(Restrictions.eq("memberid", memberid));
		query.add(Restrictions.gt("endtime", DateUtil.getMillTimestamp()));
		query.addOrder(Order.asc("bindtime"));
		List<ElecCard> elecCardList = baseDao.findByCriteria(query, 0 ,1);
		if(elecCardList.size() == 0){
			return ResultCode.getFailure("优惠券不存在");
		}
		ElecCard elecCard = elecCardList.get(0);
		return ResultCode.getSuccessReturn(elecCard);
	}

	@Override
	public ResultCode<ElecCard> bindElecCardByEbatchid(WheelysMember member, Long ebatchid) {
		ElecCardBatch elecCardBatch = this.baseDao.getObject(ElecCardBatch.class, ebatchid);
		ElecCard elecCard = createElecCard(member, elecCardBatch, 0, null);
		return ResultCode.getSuccessReturn(elecCard);
	}
	
	private ElecCard createElecCard(WheelysMember member, ElecCardBatch elecCardBatch, int seq, String key){
		ElecCard elecCard = new ElecCard(member, elecCardBatch, seq, key);
		this.baseDao.saveObject(elecCard);
		dbLogger.warn("createElecCard"+seq+":"+JsonUtils.writeObjectToJson(elecCard));
		return elecCard;
	}

	@Override
	public List<ElecCardVo> getElecCardVoList(Long memberid, String cardtype, String status, int pageNo, int maxnum) {
		
		DetachedCriteria query = DetachedCriteria.forClass(ElecCard.class);
		if(StringUtils.equals(status, ElecCardConstant.STATUS_USED)){
			LogicalExpression rhs1 = Restrictions.and(Restrictions.eq("memberid", memberid), Restrictions.eq("status", status));
			LogicalExpression rhs2 = Restrictions.and(Restrictions.eq("memberid", memberid), Restrictions.lt("endtime", DateUtil.getMillTimestamp()));
			query.add(Restrictions.or(rhs1, rhs2));
		}else{
			query.add(Restrictions.eq("status", status));
			query.add(Restrictions.eq("memberid", memberid));
			query.add(Restrictions.gt("endtime", DateUtil.getMillTimestamp()));
		}
		if(StringUtils.isNotBlank(cardtype)){
			query.add(Restrictions.eq("cardtype", cardtype));
		}
		query.addOrder(Order.desc("bindtime"));
		List<ElecCard> elecCardList = baseDao.findByCriteria(query, pageNo * maxnum, maxnum);
		List<ElecCardVo> elecCardVoList = new ArrayList<ElecCardVo>();
		for (ElecCard card : elecCardList) {
			ElecCardBatch elecCardBatch = this.baseDao.getObject(ElecCardBatch.class, card.getEbatchid());
			try {
				ElecCardVo vo = new ElecCardVo(card,elecCardBatch);
				elecCardVoList.add(vo);
			} catch (Exception e) {
				dbLogger.warn("ElecCard："+card.getEbatchid()+":"+elecCardBatch);
			}
		}
		return elecCardVoList;
	}

	@Override
	public ResultCode bindElecCardByEbatchid(WheelysMember member, String cardkey, String sign, Long ebatchid, int num, String key) {
		ElecCardBatch elecCardBatch = this.baseDao.getObject(ElecCardBatch.class, ebatchid);
		if(elecCardBatch.getCardcount()%num > 0 || elecCardBatch.getCardcount() == 0){
			return ResultCode.getFailure("优惠券数量配置有误，"+elecCardBatch.getCardname());
		}
		if(StringUtils.isNotBlank(sign) && !StringUtils.equals(StringUtil.md5(elecCardBatch.getCommand()), sign)){
			return ResultCode.getFailure("口令错误！");
		}else if(StringUtils.isNotBlank(elecCardBatch.getCommand()) && !StringUtils.equals(elecCardBatch.getCommand(), cardkey)){
			return ResultCode.getFailure("口令错误！");
		}
		if(elecCardBatch.getCardcount() >= elecCardBatch.getLqcount()+num){
			for (int i = 0; i < num; i++) {
				createElecCard(member, elecCardBatch, i, key);
			}
			elecCardBatch.setLqcount(elecCardBatch.getLqcount()+num);
			this.baseDao.saveObject(elecCardBatch);
			return ResultCode.SUCCESS;
		}
		return ResultCode.getFailure("优惠券已领完");
	}

	@Override
	public Integer getBindElecCardCount(WheelysMember member, Long ebatchid, int seqno, String key) {
		DetachedCriteria query = DetachedCriteria.forClass(ElecCard.class);
		query.add(Restrictions.eq("ebatchid", ebatchid));
		query.add(Restrictions.eq("memberid", member.getId()));
		String ukey = ebatchid + "_" + member.getId() + "_" + seqno + "_" + key;
		query.add(Restrictions.eq("ukey", ukey));
		query.setProjection(Projections.rowCount());
		List<Long> elecCardList = baseDao.findByCriteria(query);
		return elecCardList.get(0).intValue();
	}

	@Override
	public ElecCard getElecCard(Long memberid, Long cardid, String status) {
		DetachedCriteria query = DetachedCriteria.forClass(ElecCard.class);
		query.add(Restrictions.eq("id", cardid));
		query.add(Restrictions.eq("memberid", memberid));
		query.add(Restrictions.gt("endtime", DateUtil.getMillTimestamp()));
		query.add(Restrictions.eq("status", status));
		List<ElecCard> elecCardList = baseDao.findByCriteria(query, 0, 1);
		return elecCardList.size() > 0 ? elecCardList.get(0) : null;
	}

	@Override
	public ElecCardBatch getElecCardBatch(Long ebatchid, String ukey) {
		if(StringUtils.isNotBlank(ukey)){
			DetachedCriteria query = DetachedCriteria.forClass(ElecCardBatch.class);
			query.add(Restrictions.eq("ukey", ukey));
			List<ElecCardBatch> batchList = baseDao.findByCriteria(query, 0, 1);
			return batchList.isEmpty() ? null : batchList.get(0);
		}
		ElecCardBatch elecCardBatch = this.baseDao.getObject(ElecCardBatch.class, ebatchid);
		return elecCardBatch;
	}

}
