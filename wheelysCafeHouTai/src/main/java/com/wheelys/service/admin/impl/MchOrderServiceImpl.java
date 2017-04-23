package com.wheelys.service.admin.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.constant.OrderConstant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.merchant.MchOrder;
import com.wheelys.model.merchant.MchOrderDetail;
import com.wheelys.model.merchant.MchProduct;
import com.wheelys.service.admin.MchOrderService;
import com.wheelys.service.admin.MchProductService;
import com.wheelys.service.admin.MchrulesService;
import com.wheelys.service.cafe.CafeShopService;
import com.wheelys.web.action.openapi.vo.MchOrderDetailVo;
import com.wheelys.web.action.openapi.vo.MchOrderVo;

@Service("mchOrderService")
public class MchOrderServiceImpl extends BaseServiceImpl implements MchOrderService {

	@Autowired@Qualifier("cafeShopService")
	private CafeShopService cafeShopService;
	@Autowired@Qualifier("mchProductService")
	private MchProductService mchProductService;
	@Autowired@Qualifier("mchrulesService")
	private MchrulesService mchrulesService;
	private AtomicInteger counter = new AtomicInteger();
	
	@Override
	public ResultCode<MchOrderVo> addMchOrder(Long shopid, String mobile, String contacts, String isexpress, List<MchOrderDetailVo> vodetailList,
			String clientIp) {
		if(vodetailList == null || vodetailList.isEmpty()){
			return ResultCode.getFailure("购物车为空！");
		}
		int totalfee = 0;
		int payfee = 0;
		int quantity = 0;
		long weight = 0;
		List<MchOrderDetail> detailList = new ArrayList<MchOrderDetail>();
		for (MchOrderDetailVo vo : vodetailList) {
			MchProduct mchProduct = mchProductService.findmchproduct(vo.getProductid());
			if(StringUtils.equals(mchProduct.getStatus(), "N")){
				return ResultCode.getFailure("您所选择的"+mchProduct.getName()+"刚刚下架了，请返回修改订单后重新提交，谢谢。");
			}
			MchOrderDetail detail = new MchOrderDetail(mchProduct, vo.getQuantity());
			if(detail.getTotalfee() <= 0 || detail.getQuantity() <= 0){
				return ResultCode.getFailure("totalfee:"+detail.getTotalfee()+",quantity:"+detail.getQuantity());
			}
			detailList.add(detail);
			weight += mchProduct.getWeight()*detail.getQuantity();
			totalfee += detail.getTotalfee();
			payfee += detail.getPaidfee();
			quantity += detail.getQuantity();
		}
		CafeShop cafeShop = cafeShopService.getShop(shopid);
		MchOrder order = new MchOrder(cafeShop, isexpress);
		List<String> nameList = BeanUtil.getBeanPropertyList(detailList, "productname", true);
		String name = StringUtils.join(nameList,",");
		String title = StringUtils.length(name) > 15 ? StringUtils.substring(name, 0, 15) + "..." : name;
		order.setOrdertitle(title);
		order.setTradeno(getTicketTradeNo());
		order.setPayfee(payfee);
		order.setWeight(weight);
		order.setTotalfee(totalfee);
		order.setQuantity(quantity);
		if (StringUtils.equals(isexpress, "Y")) {
			order.setContacts(contacts);
			order.setMobile(mobile);
			Map otherinfoMap = new HashMap();
			otherinfoMap.put("isexpress", "Y");
			order.setOtherinfo(JsonUtils.writeMapToJson(otherinfoMap));
		}else {
			this.setExpress(order);
			Map otherinfoMap = new HashMap();
			otherinfoMap.put("isexpress", "N");
			order.setOtherinfo(JsonUtils.writeMapToJson(otherinfoMap));
		}
		this.baseDao.saveObject(order);
		this.saveOrderDetail(order,detailList);
		MchOrderVo orderVo = new MchOrderVo(order);
		List<MchOrderDetailVo> voDetailList = new ArrayList<MchOrderDetailVo>();
		for (MchOrderDetail detail : detailList) {
			MchProduct mchProduct = this.baseDao.getObject(MchProduct.class, detail.getProductid());
			MchOrderDetailVo vo = new MchOrderDetailVo(detail,mchProduct);
			voDetailList.add(vo);
		}
		orderVo.setDetailList(voDetailList);
		return ResultCode.getSuccessReturn(orderVo);
	}

	private void setExpress(MchOrder order) {
		int weight = (int) (order.getWeight()/1000);
		weight += order.getWeight()%1000 > 0 ? 1 : 0;
		order.setExpressfee(0);
		mchrulesService.ruleShop(order,weight);
		order.setDisreason("2000");
		if(order.getTotalfee() > 2000*100){
			order.setDiscount(order.getExpressfee());
		}else{
			order.setPayfee(order.getTotalfee() + order.getExpressfee());
		}
	}

	private String getTicketTradeNo() {
		int index = new Random().nextInt(1000*5)+1;
		String s = "P" + DateUtil.format(new Date(), "yyMMddHHmm");
		long num = counter.incrementAndGet()+index%100000;
		s += StringUtils.leftPad("" + num, 5, '0'); // 订单号
		return s;
	}

	private void saveOrderDetail(MchOrder order, List<MchOrderDetail> detailList) {
		for (MchOrderDetail detail : detailList) {
			detail.setOrderid(order.getId());
			this.baseDao.saveObject(detail);
		}
	}

	@Override
	public List<MchOrder> getMchOrderList(Long shopid, String status, Timestamp fromtime, Timestamp totime,
			String tradeno, Integer pageno) {
		DetachedCriteria query = DetachedCriteria.forClass(MchOrder.class);
		query.add(Restrictions.eq("shopid", shopid));
		if(StringUtils.isNotBlank(status)){
			query.add(Restrictions.eq("status", status));
		}else{
			query.add(Restrictions.ne("status", OrderConstant.STATUS_NEW));
		}
		if(fromtime != null){
			query.add(Restrictions.ge("createtime", fromtime));
		}
		if(totime != null){
			query.add(Restrictions.lt("createtime", totime));
		}
		if (StringUtils.isNotBlank(tradeno)) {
			query.add(Restrictions.like("tradeno", "%" + tradeno));
		}
		List<MchOrder> resultList = null;
		query.addOrder(Order.desc("updatetime"));
		if(pageno != null){
			int from = pageno * 20;
			int maxnum = 20;
			resultList = baseDao.findByCriteria(query, from, maxnum);
		}else{
			resultList = baseDao.findByCriteria(query, 0, 120);
		}
		return resultList;
	}

	@Override
	public ResultCode<MchOrderVo> confirmMchOrder(Long shopid, String tradeno, String status, String contacts, 
			String mobile, String isexpress, String remark, String clientIp) {
		DetachedCriteria query = DetachedCriteria.forClass(MchOrder.class);
		query.add(Restrictions.eq("shopid", shopid));
		query.add(Restrictions.eq("tradeno", tradeno));
		List<MchOrder> resultList = baseDao.findByCriteria(query, 0, 1);
		if(resultList.isEmpty()) return ResultCode.getFailure("订单不存在！");
		MchOrder order = resultList.get(0);
		order.setStatus(status);
		if(StringUtils.equals(isexpress, "Y")){
			order.setContacts(contacts);
			order.setMobile(mobile);
			this.setExpress(order);
			Map otherinfoMap = new HashMap();
			otherinfoMap.put("isexpress", "Y");
			order.setOtherinfo(JsonUtils.writeMapToJson(otherinfoMap));
		}
		order.setUpdatetime(DateUtil.getMillTimestamp());
		if(StringUtils.isNotBlank(remark)){
			order.setRemark(remark);
		}
		this.baseDao.saveObject(order);
		MchOrderVo orderVo = new MchOrderVo(order);
		return ResultCode.getSuccessReturn(orderVo);
	}
	
	private List<MchOrderDetail> getOrderDetail(Long orderid){
		DetachedCriteria query = DetachedCriteria.forClass(MchOrderDetail.class);
		query.add(Restrictions.eq("orderid", orderid));
		List<MchOrderDetail> resultList = baseDao.findByCriteria(query);
		return resultList;
	}

	@Override
	public MchOrderVo getMchOrderVoDetail(Long shopid, String tradeno) {
		DetachedCriteria query = DetachedCriteria.forClass(MchOrder.class);
		query.add(Restrictions.eq("tradeno", tradeno));
		query.add(Restrictions.eq("shopid", shopid));
		List<MchOrder> resultList = baseDao.findByCriteria(query);
		if(resultList.isEmpty()){
			return null;
		}
		MchOrder mchOrder = resultList.get(0);
		List<MchOrderDetail> detailList = getOrderDetail(mchOrder.getId());
		MchOrderVo orderVo = new MchOrderVo(mchOrder);
		List<MchOrderDetailVo> voDetailList = new ArrayList<MchOrderDetailVo>();
		for (MchOrderDetail detail : detailList) {
			MchProduct mchProduct = this.baseDao.getObject(MchProduct.class, detail.getProductid());
			MchOrderDetailVo vo = new MchOrderDetailVo(detail,mchProduct);
			voDetailList.add(vo);
		}
		orderVo.setDetailList(voDetailList);
		return orderVo;
	}

	@Override
	public MchOrder getMchOrderDetail(String tradeno) {
		MchOrder order = this.baseDao.getObjectByUkey(MchOrder.class, "tradeno", tradeno);
		return order;
	}

}
