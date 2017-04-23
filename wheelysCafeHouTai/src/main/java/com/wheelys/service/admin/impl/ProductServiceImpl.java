package com.wheelys.service.admin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeShop;
import com.wheelys.model.ProductShop;
import com.wheelys.service.admin.ProductService;
import com.wheelys.service.cafe.CafeProductService;
import com.wheelys.web.action.admin.vo.ProductShopVo;

@Service("productService")
public class ProductServiceImpl extends BaseServiceImpl implements ProductService {

	@Autowired
	@Qualifier("cafeProductService")
	private CafeProductService cafeProductService;

	@Override
	public List<CafeProduct> showcafeList(String prodname, String itemid, Integer pageNo, int maxnum) {
		DetachedCriteria query = DetachedCriteria.forClass(CafeProduct.class);
		if (StringUtils.isNotBlank(prodname)) {
			query.add(Restrictions.like("name", "%" + prodname + "%"));
		}
		if (StringUtils.isNotBlank(itemid)) {
			query.add(Restrictions.eq("itemid", Long.parseLong(itemid)));
		}
		int from = pageNo * maxnum;
		query.add(Restrictions.eq("display", 1));
		query.addOrder(Order.desc("id"));
		List<CafeProduct> cafeList = baseDao.findByCriteria(query, from, maxnum);
		return cafeList;
	}

	@Override
	public int findListCount(String prodname, String itemid) {
		DetachedCriteria query = DetachedCriteria.forClass(CafeProduct.class);
		if (StringUtils.isNotBlank(prodname)) {
			query.add(Restrictions.like("name", "%" + prodname + "%"));
		}
		if (StringUtils.isNotBlank(itemid)) {
			query.add(Restrictions.eq("itemid", Long.parseLong(itemid)));
		}
		query.add(Restrictions.eq("display", 1));
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}

	@Override
	public Map<Long, CafeItem> getCafeMap() {
		DetachedCriteria query = DetachedCriteria.forClass(CafeItem.class);
		List<CafeItem> cafeItemList = baseDao.findByCriteria(query);
		Map<Long, CafeItem> map = new HashMap<Long, CafeItem>();
		for (CafeItem item : cafeItemList) {
			map.put(item.getId(), item);
		}
		return map;

	}

	@Override
	public CafeProduct cafeproduct(Long id) {
		CafeProduct cafeProduct = this.baseDao.getObject(CafeProduct.class, id);
		return cafeProduct;

	}

	@Override
	public boolean productName(String prodname, String prodenname, Long id) {
		DetachedCriteria query = DetachedCriteria.forClass(CafeProduct.class);
		query.add(Restrictions.eq("display", 1));
		if (StringUtils.isNotBlank(prodname)) {
			query.add(Restrictions.eq("name", prodname));
		}
		if (StringUtils.isNotBlank(prodenname)) {
			query.add(Restrictions.eq("enname", prodenname));
		}
		if (id != null) {
			query.add(Restrictions.ne("id", id));
		}
		query.setProjection(Projections.rowCount());
		List<Long> shopList = baseDao.findByCriteria(query);
		return shopList.get(0) > 0;
	}

	@Override
	public Map<CafeItem, List<ProductShopVo>> getShopCafeProductMap(Long shopid) {
		Map<CafeItem, List<ProductShopVo>> cafeProductMap = new HashMap<CafeItem, List<ProductShopVo>>();
		List<CafeItem> cafeItemList = getCafeItemList();
		List<ProductShopVo> cafeProductVoList = getCafeProductListByShop(shopid);
		Map map = BeanUtil.groupBeanList(cafeProductVoList, "itemid");
		for (CafeItem cafeItem : cafeItemList) {
			List<ProductShopVo> list = (List<ProductShopVo>) map.get(cafeItem.getId());
			cafeProductMap.put(cafeItem, list);
		}
		return cafeProductMap;
	}

	@Override
	public List<ProductShopVo> getCafeProductListByShop(Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(ProductShop.class);
		query.add(Restrictions.eq("psshopid", shopid));
		query.add(Restrictions.eq("delstatus", "N"));
		query.addOrder(Order.asc("psorder"));
		List<ProductShop> productShopList = baseDao.findByCriteria(query);
		List<ProductShopVo> productShopVoList = new ArrayList<ProductShopVo>();
		for (ProductShop productShop : productShopList) {
			try {
				CafeProduct cafeProduct = cafeProductService.getProduct(productShop.getPsprodid());
				if (cafeProduct != null) {
					ProductShopVo vo = new ProductShopVo(productShop, cafeProduct);
					productShopVoList.add(vo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return productShopVoList;
	}

	@Override
	public List<Long> getProductIdList(Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(ProductShop.class);
		query.add(Restrictions.eq("psshopid", shopid));
		query.add(Restrictions.eq("delstatus", "N"));
		query.addOrder(Order.asc("psorder"));
		query.setProjection(Projections.property("psprodid"));
		List<Long> productidList = baseDao.findByCriteria(query);
		return productidList;
	}

	public ProductShop getProductShop(Long shopid, Long prodid) {
		DetachedCriteria query = DetachedCriteria.forClass(ProductShop.class);
		query.add(Restrictions.eq("psshopid", shopid));
		query.add(Restrictions.eq("psprodid", prodid));
		List<ProductShop> shopList = baseDao.findByCriteria(query);
		return shopList.isEmpty() ? null : shopList.get(0);
	}

	@Override
	public List<CafeItem> getCafeItemList() {
		DetachedCriteria query = DetachedCriteria.forClass(CafeItem.class);
		query.add(Restrictions.eq("state", "Y"));
		query.addOrder(Order.asc("sortkey"));
		List<CafeItem> itemList = baseDao.findByCriteria(query);
		return itemList;
	}

	@Override
	public void addProduct(CafeProduct cafeProduct) {
		this.baseDao.saveObject(cafeProduct);
	}

	@Override
	public void updateProcut(Long psid, Integer psorder, Integer display) {
		ProductShop productShop = this.baseDao.getObject(ProductShop.class, psid);
		if (productShop != null) {
			if (psorder != null) {
				productShop.setPsorder(psorder);
			}
			if (display != null) {
				productShop.setDisplay(display == 0 ? 0 : 1);
			}
			this.baseDao.saveObject(productShop);
		}
	}

	@Override
	public Map<Long, List<CafeProduct>> getCafeProductMap() {
		DetachedCriteria query = DetachedCriteria.forClass(CafeProduct.class);
		query.add(Restrictions.eq("display", 1));
		List<CafeProduct> cafeProductList = baseDao.findByCriteria(query);
		Map<Long, List<CafeProduct>> map = BeanUtil.groupBeanList(cafeProductList, "itemid");
		return map;
	}

	@Override
	public void addProductShop(Long shopid, String arr, String productidList) {
		List<Long> pList = BeanUtil.getIdList(productidList, ",");
		List<Long> rList = BeanUtil.getIdList(arr, ",");
		for (Long str : pList) {
			if (!rList.contains(str)) {
				DetachedCriteria query = DetachedCriteria.forClass(ProductShop.class);
				query.add(Restrictions.eq("psshopid", shopid));
				query.add(Restrictions.eq("psprodid", str));
				List<ProductShop> producShopList = baseDao.findByCriteria(query);
				ProductShop prodShop = producShopList.get(0);
				if (StringUtils.equals(prodShop.getDelstatus(), "N")) {
					prodShop.setDelstatus("Y");
				}
				this.baseDao.saveObject(prodShop);
			}
		}
		for (Long s : rList) {
			if (!pList.contains(s)) {
				DetachedCriteria query = DetachedCriteria.forClass(ProductShop.class);
				query.add(Restrictions.eq("psshopid", shopid));
				query.add(Restrictions.eq("psprodid", s));
				query.add(Restrictions.eq("delstatus", "Y"));
				List<ProductShop> producShopList = baseDao.findByCriteria(query);
				if (producShopList.size() == 0) {
					ProductShop product = new ProductShop(shopid, s);
					this.baseDao.saveObject(product);
				} else {
					ProductShop prodShop = producShopList.get(0);
					if (prodShop != null) {
						prodShop.setDelstatus("N");
						this.baseDao.saveObject(prodShop);
					}
				}
			}
		}
	}

	@Override
	public void upProductShop(Long psid, Long shopid, String delstatus, Long prodid) {
		ProductShop productShop = getProductShop(shopid, prodid);
		if (StringUtils.equals(delstatus, "Y")) {
			if (psid != null) {
				productShop = this.baseDao.getObject(ProductShop.class, psid);
			}
			if (productShop != null) {
				productShop.setDelstatus("Y");
			}
		} else {
			if (productShop == null) {
				productShop = new ProductShop(shopid, prodid);
				this.baseDao.saveObject(productShop);
			}
			if (StringUtils.equals(delstatus, "N")) {
				productShop.setDelstatus("N");
			}
		}
		this.baseDao.updateObject(productShop);
	}

	@Override
	public List<ProductShop> product(Long prodid) {
		DetachedCriteria query = DetachedCriteria.forClass(ProductShop.class);
		query.add(Restrictions.eq("psprodid", prodid));
		query.add(Restrictions.eq("display", 1));
		query.add(Restrictions.eq("delstatus", "N"));
		List<ProductShop> producShopList = baseDao.findByCriteria(query);
		return producShopList;
	}

	@Override

	public String ca(Long prodid) {
		List<ProductShop> li = this.product(prodid);
		List<CafeShop> licashopList = new ArrayList<>();
		String name = "";
		Set<String> nameset = new TreeSet<>();
		for (ProductShop p : li) {
			DetachedCriteria query = DetachedCriteria.forClass(CafeShop.class);
			query.add(Restrictions.eq("shopid", p.getPsshopid()));
			List<CafeShop> lica = baseDao.findByCriteria(query);
			if (lica != null) {
				for (CafeShop c : lica) {
					licashopList.add(c);
				}
			}
			for (CafeShop c : licashopList) {
				if (c.getShopname() != null) {
					nameset.add(c.getShopname());
				}
			}
			name = StringUtils.join(nameset, ",");
		}
		return name;
	}

	@Override
	public void delProduct(Long id, Integer display) {
		CafeProduct cafeProduct = this.baseDao.getObject(CafeProduct.class, id);
		DetachedCriteria query = DetachedCriteria.forClass(ProductShop.class);
		query.add(Restrictions.eq("psprodid", id));
		List<ProductShop> li = baseDao.findByCriteria(query);
		for (ProductShop p : li) {
			p.setDelstatus("Y");
			this.baseDao.saveObject(p);
		}
		if (display == 1) {
			cafeProduct.setDisplay(-1);
		}
		this.baseDao.saveObject(cafeProduct);

	}
}
