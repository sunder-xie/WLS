package com.wheelys.service.ticket.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeShop;
import com.wheelys.model.pay.ElecCardBatch;
import com.wheelys.service.cafe.CafeProductService;
import com.wheelys.service.ticket.TicketService;

@Service("ticketService")
public class TicketServiceImpl extends BaseServiceImpl implements TicketService {

	@Autowired
	@Qualifier("cafeProductService")
	protected CafeProductService cafeProductService;

	public List<ElecCardBatch> findAllTicketList(Integer pageNo, String name, int maxnum) {
		DetachedCriteria query = getTicketQuery(name);
		query.addOrder(Order.desc("id"));
		int from = pageNo * maxnum;
		List<ElecCardBatch> ticketList = baseDao.findByCriteria(query, from, maxnum);
		return ticketList;
	}

	@Override
	public int findAllCount(String name) {
		DetachedCriteria query = getTicketQuery(name);
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		int count = result.get(0).intValue();
		return count;
	}

	private DetachedCriteria getTicketQuery(String name) {
		DetachedCriteria query = DetachedCriteria.forClass(ElecCardBatch.class);
		query.add(Restrictions.eq("delstatus", "N"));
		if (StringUtils.isNotBlank(name)) {
			query.add(Restrictions.eq("dhname", name));
		}
		return query;
	}

	@Override
	public void updateTicket(Long id) {
		ElecCardBatch elecCardBatch = getTicketDetail(id);
		if (!elecCardBatch.getDelstatus().equals("Y")) {
			elecCardBatch.setDelstatus("Y");
			this.baseDao.saveObject(elecCardBatch);
		}
	}

	@Override
	public ElecCardBatch getTicketDetail(Long id) {
		ElecCardBatch batch = this.baseDao.getObject(ElecCardBatch.class, id);
		return batch;
	}

	@Override
	public CafeItem getcafeItemListById(Long long1) {
		CafeItem cafeItem = this.baseDao.getObject(CafeItem.class, long1);
		return cafeItem;
	}

	@Override
	public void updateTicket(ElecCardBatch elecCardBatch) {
		this.baseDao.saveObject(elecCardBatch);
	}

	@Override
	public String jsonTomapTokey(String json) {
		// 将json串转为map集合并获取其中的key值
		Map<String, String> jsonToMap = JsonUtils.readJsonToMap(json);
		Set<String> keySet = jsonToMap.keySet();
		String key1 = keySet.toString();
		String keys = key1.substring(1, key1.length() - 1);
		return keys;
	}

	@Override
	public Map<CafeItem, List<CafeProduct>> itemproductMap(String producrids) {
		// 将所有的查询的item和product放在linklistmap中
		Map<CafeItem, List<CafeProduct>> productMap = new LinkedHashMap<CafeItem, List<CafeProduct>>();
		// 查询所有的item集合
		String itemids1 = this.jsonTomapTokey(producrids);
		List<Long> itemids = BeanUtil.getIdList(itemids1, ",");
		List<CafeItem> cafeItemList = new ArrayList<CafeItem>();
		for (Long itemid : itemids) {
			CafeItem cafeItem = this.findCafeItemById(itemid);
			cafeItemList.add(cafeItem);
		}
		Map<String, String> itemproMap = JsonUtils.readJsonToMap(producrids);
		for (CafeItem cafeItem : cafeItemList) {
			if (cafeItem.getId() != null) {
				String proids = itemproMap.get(cafeItem.getId().toString());
				List<Long> proidList = BeanUtil.getIdList(proids, ",");
				List<CafeProduct> productList = cafeProductService.getProductList(proidList);
				productMap.put(cafeItem, productList);
			}
		}
		return productMap;
	}

	private CafeItem findCafeItemById(Long id) {
		CafeItem cafeItem = this.baseDao.getObject(CafeItem.class, id);
		return cafeItem;
	}

	@Override
	public List<String> shopnames(Long id) {
		ElecCardBatch batch = this.getTicketDetail(id);
		if (StringUtils.isNotBlank(batch.getValidshopid())) {
			List<String> rList = null;
			List<Long> idList = BeanUtil.getIdList(batch.getValidshopid(), ",");
			List<CafeShop> cafeShopList = baseDao.getObjectList(CafeShop.class, idList);
			rList = BeanUtil.getBeanPropertyList(cafeShopList, String.class, "esname", true);
			return rList;
		} else {
			return null;
		}
	}

	@Override
	public List<Long> productids(Long id) {
		ElecCardBatch batch = this.getTicketDetail(id);
		List<Long> idList = new ArrayList<>();
		String productname = null;
		if (StringUtils.isNotBlank(batch.getValidproductid())) {
			Map<String, String> temp = JsonUtils.readJsonToMap(batch.getValidproductid());
			for (String string : temp.values()) {
				productname += "," + string + ",";
			}
		}
		idList = BeanUtil.getIdList(productname, ",");
		return idList;
	}

	@Override
	public List<Long> caitmsids(Long id) {
		ElecCardBatch batch = this.getTicketDetail(id);
		List<Long> idList = BeanUtil.getIdList(batch.getValiditemid(), ",");
		return idList;
	}
}
