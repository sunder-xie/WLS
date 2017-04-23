package com.wheelys.service.admin.impl;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.constant.CityContant;
import com.wheelys.model.CafeShop;
import com.wheelys.model.merchant.MchOrder;
import com.wheelys.model.merchant.Mchrules;
import com.wheelys.service.admin.MchrulesService;
import com.wheelys.util.VmUtils;

@Service("mchrulesService")
public class MchrulesServiceImpl extends BaseServiceImpl implements MchrulesService {

	@Override
	public List<Mchrules> ruleList(Long supplierid) {
		DetachedCriteria query = DetachedCriteria.forClass(Mchrules.class);
		query.add(Restrictions.eq("supplierid", supplierid));
		query.add(Restrictions.eq("delstatus", "N"));
		List<Mchrules> ruleList = baseDao.findByCriteria(query);
		return ruleList;
	}

	@Override
	public void add(Long id, String citycode, Long supplierid, Float morefreight, Float morecost, Float lessfreight,
			Float lesscost) {
		Mchrules rule = this.baseDao.getObject(Mchrules.class, id);
		if (rule == null) {
			rule = new Mchrules(citycode, supplierid, lessfreight, lesscost, morefreight, morecost);
		} else {
			rule.setCitycode(citycode);
			rule.setLesscost((int) (lesscost * 100));
			rule.setLessfreight((int) (lessfreight * 100));
			rule.setMorefreight((int) (morefreight * 100));
			rule.setMorecost((int) (morecost * 100));
			rule.setSupplierid(supplierid);
		}
		this.baseDao.saveObject(rule);
	}

	@Override
	public Mchrules rules(Long id) {
		Mchrules rules = this.baseDao.getObject(Mchrules.class, id);
		return rules;
	}

	@Override
	public void delsrules(Long id) {
		Mchrules rules = this.baseDao.getObject(Mchrules.class, id);
		if (StringUtils.equals(rules.getDelstatus(), "N")) {
			rules.setDelstatus("Y");
			this.baseDao.saveObject(rules);
		} else {
			this.baseDao.saveObject(rules);
		}
	}

	@Override
	public void ruleShop(MchOrder order, int weight) {
		CafeShop cafeShop = this.baseDao.getObject(CafeShop.class, order.getShopid());
		DetachedCriteria query = DetachedCriteria.forClass(Mchrules.class);
		query.add(Restrictions.eq("delstatus", "N"));
		query.add(Restrictions.eq("citycode", cafeShop.getCitycode()));
		query.add(Restrictions.eq("supplierid", cafeShop.getSupplierid()));
		List<Mchrules> mchList = baseDao.findByCriteria(query);
		if (mchList.isEmpty()) return;
		Mchrules mchrules = mchList.get(0);
		if (StringUtils.equals(cafeShop.getCitycode(), CityContant.CITYCODE_SH)&& cafeShop.getSupplierid() == 1) {
			// 当订单总额大于设置的订单总额时
			if (order.getTotalfee() >= mchrules.getLesscost()) {
				order.setExpressDes("此次订单满" + VmUtils.getDoubleAmount(mchrules.getLesscost()) + "元" + "免运费");
			} else {
				order.setExpressfee(mchrules.getLessfreight());
				order.setExpressDes("此次订单不满" + VmUtils.getDoubleAmount(mchrules.getLesscost()) + "元" + "需收取运费"
						+ VmUtils.getDoubleAmount(mchrules.getLessfreight()) + "元");
			}
		} else {
			// cafeShop.getSupplierid()==1代表圣唐
			if (order.getWeight() < 200 && cafeShop.getSupplierid() == 1) {
				// 总运费,每公斤*小于200时运费
				Integer alllessfreight = (int) (weight * mchrules.getLessfreight());
				// 总配送费,每公斤*小于200时配送费
				Integer alllesscost = (int) (weight * mchrules.getLesscost());
				// 总物流费
				order.setExpressfee(alllessfreight + alllesscost);
				order.setExpressDes("此次订单合计" + weight + "KG，运费每公斤" + VmUtils.getDoubleAmount(mchrules.getLessfreight())
						+ "元," + "配送费每公斤" + VmUtils.getDoubleAmount(mchrules.getLesscost()) + "元");
				order.setPayfee(order.getTotalfee() + order.getExpressfee());
			} else if (order.getWeight() >= 200 && cafeShop.getSupplierid() == 1 || cafeShop.getSupplierid() == 2) {
				// 总运费,每公斤*大于200时运费
				Integer allmorefreight = (int) (weight * mchrules.getMorefreight());
				// 总配送费,每公斤*每单150
				Integer allmorecost = 15000;
				// 总物流费
				order.setExpressfee(allmorefreight + allmorecost);
				order.setExpressDes("此次订单合计" + weight + "KG，运费每公斤"
						+ "" + VmUtils.getDoubleAmount(mchrules.getMorefreight())
						+ "元," + "配送费每单150元");
			}
		}
		this.baseDao.saveObject(order);
	}

	@Override
	public boolean citycode(String citycode, Long id,Long supplierid) {
		DetachedCriteria query = DetachedCriteria.forClass(Mchrules.class);
		query.add(Restrictions.eq("citycode", citycode));
		query.add(Restrictions.eq("delstatus", "N"));
		query.add(Restrictions.eq("supplierid", supplierid));
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		if(result.isEmpty())return false;
		return result.get(0) > 0;
	}

	@Override
	public Mchrules findRule(Long supplierid) {
		DetachedCriteria query = DetachedCriteria.forClass(Mchrules.class);
		query.add(Restrictions.eq("citycode",CityContant.CITYCODE_SH));
		query.add(Restrictions.eq("delstatus", "N"));
		query.add(Restrictions.eq("supplierid", supplierid));
		List<Mchrules> result = baseDao.findByCriteria(query);
		if(result.isEmpty())return null;
		return result.get(0);
	}
}
