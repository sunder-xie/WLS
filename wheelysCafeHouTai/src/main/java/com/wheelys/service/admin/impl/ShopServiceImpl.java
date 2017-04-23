package com.wheelys.service.admin.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.constant.BannerConstant;
import com.wheelys.constant.ShopStateConstant;
import com.wheelys.model.CafeItem;
import com.wheelys.model.CafeProduct;
import com.wheelys.model.CafeRecruit;
import com.wheelys.model.CafeShop;
import com.wheelys.model.CafeShopProfile;
import com.wheelys.model.ProductShop;
import com.wheelys.model.ShopExpressAddress;
import com.wheelys.model.banner.WheelysBanner;
import com.wheelys.model.booking.Booking;
import com.wheelys.model.company.Company;
import com.wheelys.service.admin.ProductService;
import com.wheelys.service.admin.ShopService;
import com.wheelys.service.cafe.CafeProductService;
import com.wheelys.util.VmUtils;
import com.wheelys.web.action.admin.vo.CafeShopVo;
import com.wheelys.web.action.admin.vo.ProductShopVo;

@Service("shopService")
public class ShopServiceImpl extends BaseServiceImpl implements ShopService {

	@Autowired
	@Qualifier("cafeProductService")
	private CafeProductService cafeProductService;
	@Autowired
	@Qualifier("productService")
	private ProductService productService;

	@Override
	public void saveCafeShop(CafeShop cafeShop) {
		this.baseDao.saveObject(cafeShop);
	}

	@Override
	public List<CafeShopVo> findlist(String name, Long operatorid, String cityCode, Integer pageNo, int maxnum) {
		List<CafeShop> shopList = getCafeShopList(name, operatorid, cityCode, pageNo, maxnum);
		List<CafeShopVo> cafeShopList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(shopList)) {
			for (CafeShop cafeShop : shopList) {
				CafeShopVo cafeShopVo = new CafeShopVo();
				cafeShopVo.setShopid(cafeShop.getShopid());
				cafeShopVo.setShopaccount(cafeShop.getShopaccount());
				cafeShopVo.setShopname(cafeShop.getShopname());
				cafeShopVo.setContacts(cafeShop.getContacts());
				cafeShopVo.setShoptelephone(cafeShop.getShoptelephone());
				cafeShopVo.setShoptime(cafeShop.getShoptime());
				cafeShopVo.setCitycode(cafeShop.getCitycode());
				cafeShopVo.setEsname(cafeShop.getEsname());
				cafeShopVo.setBooking(cafeShop.getBooking());
				Company company = baseDao.getObject(Company.class, cafeShop.getOperatorid());
				cafeShopVo.setOperatorName(company != null ? company.getName() : null);
				cafeShopList.add(cafeShopVo);
			}
		}
		return cafeShopList;
	}
	public List<CafeShopVo> findCloseList(String name,String cityCode, Long operatorid,Integer pageNo,int maxnum) {
		List<CafeShop> shopList = getCloseShopList(name,cityCode, operatorid,pageNo, maxnum);
		List<CafeShopVo> cafeShopList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(shopList)) {
			for (CafeShop cafeShop : shopList) {
				CafeShopVo cafeShopVo = new CafeShopVo();
				cafeShopVo.setShopid(cafeShop.getShopid());
				cafeShopVo.setShopaccount(cafeShop.getShopaccount());
				cafeShopVo.setShopname(cafeShop.getShopname());
				cafeShopVo.setContacts(cafeShop.getContacts());
				cafeShopVo.setShoptelephone(cafeShop.getShoptelephone());
				cafeShopVo.setShoptime(cafeShop.getShoptime());
				cafeShopVo.setCitycode(cafeShop.getCitycode());
				cafeShopVo.setEsname(cafeShop.getEsname());
				cafeShopVo.setBooking(cafeShop.getBooking());
				Company company = baseDao.getObject(Company.class, cafeShop.getOperatorid());
				cafeShopVo.setOperatorName(company != null ? company.getName() : null);
				cafeShopList.add(cafeShopVo);
			}
		}
		return cafeShopList;
	}

	private List<CafeShop> getCafeShopList(String name, Long operatorid, String cityCode, Integer pageNo, int maxnum) {
		DetachedCriteria query = this.getQuery(CafeShop.class, name, operatorid, cityCode);
		int from = pageNo * maxnum;
		List<CafeShop> shopList = baseDao.findByCriteria(query, from, maxnum);
		return shopList;
	}
	private List<CafeShop> getCloseShopList(String name,String cityCode, Long operatorid,Integer pageNo, int maxnum) {
		DetachedCriteria query = this.getCloseQuery(CafeShop.class,cityCode, name, operatorid);
		int from = pageNo * maxnum;
		List<CafeShop> shopList = baseDao.findByCriteria(query, from, maxnum);
		return shopList;
	}

	private DetachedCriteria getQuery(Class clazz, String name, Long operatorid, String cityCode) {
		DetachedCriteria query = DetachedCriteria.forClass(clazz);
		if (StringUtils.isNotBlank(name)) {
			query.add(Restrictions.like("shopname", "%" + name + "%"));
		}
		if (StringUtils.isNotBlank(cityCode)) {
			query.add(Restrictions.eq("citycode", cityCode));
		}
		if (operatorid != null) {
			query.add(Restrictions.eq("operatorid", operatorid));
		}
		query.add(Restrictions.eq("delstatus", "N"));
		query.add(Restrictions.in("booking",ShopStateConstant.SHOWSTATE));
		query.addOrder(Order.desc("shopid"));
		return query;
	}
	private DetachedCriteria getCloseQuery(Class clazz,String cityCode, String name, Long operatorid) {
		DetachedCriteria query = DetachedCriteria.forClass(clazz);
		if (StringUtils.isNotBlank(name)) {
			query.add(Restrictions.like("shopname", "%" + name + "%"));
		}
		if (operatorid != null) {
			query.add(Restrictions.eq("operatorid", operatorid));
		}
		if (StringUtils.isNotBlank(cityCode)) {
			query.add(Restrictions.eq("citycode", cityCode));
		}
		query.add(Restrictions.eq("booking", "FS"));
		query.add(Restrictions.eq("delstatus", "N"));
		query.addOrder(Order.desc("shopid"));
		return query;
	}

	@Override
	public int findListCount(String name, Long operatorid, String cityCode) {
		DetachedCriteria query = this.getQuery(CafeShop.class, name, operatorid, cityCode);
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}
	@Override
	public int findCloseListCount(String name, Long operatorid, String citycode) {
		DetachedCriteria query = this.getCloseQuery(CafeShop.class,citycode, name, operatorid);
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}
	
	public List<ProductShopVo> findlist2(Long psshopid, String name, Long itemid) {
		DetachedCriteria query = DetachedCriteria.forClass(ProductShop.class, "ps");
		query.add(Restrictions.eq("psshopid", psshopid));
		query.addOrder(Order.asc("psshopid"));
		DetachedCriteria subQuery = DetachedCriteria.forClass(CafeProduct.class, "p");
		subQuery.setProjection(Projections.property("p.id"));
		subQuery.add(Restrictions.eq("display", 1));
		subQuery.add(Restrictions.eqProperty("p.id", "ps.psprodid"));
		if (itemid != null) {
			query.add(Restrictions.eq("itemid", itemid));
		}
		if (StringUtils.isNotBlank(name)) {
			query.add(Restrictions.like("name", "%" + name + "%"));
		}
		query.add(Restrictions.eq("delstatus", "N"));
		query.add(Subqueries.exists(subQuery));
		List<ProductShop> productShopList = baseDao.findByCriteria(query);
		List<ProductShopVo> productShopVoList = new ArrayList<ProductShopVo>();
		for (ProductShop productShop : productShopList) {
			CafeProduct cafeProduct = this.baseDao.getObject(CafeProduct.class, productShop.getPsprodid());
			ProductShopVo vo = new ProductShopVo(productShop, cafeProduct);
			productShopVoList.add(vo);
		}
		return productShopVoList;
	}

	@Override
	public CafeShop findshop(Long shopid) {
		CafeShop cafeShop = this.baseDao.getObject(CafeShop.class, shopid);
		return cafeShop;
	}

	@Override
	public CafeShop findCafeShop(Long shopid) {
		CafeShop cafeShop = this.baseDao.getObject(CafeShop.class, shopid);
		return cafeShop;
	}

	@Override
	public boolean shopuser(String shopname, Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(CafeShop.class);
		query.add(Restrictions.eq("shopname", shopname));
		if (shopid != null) {
			query.add(Restrictions.ne("shopid", shopid));
		}
		query.setProjection(Projections.rowCount());
		List<Long> shopList = baseDao.findByCriteria(query);
		return shopList.get(0) > 0;
	}

	@Override
	public CafeShopProfile findShopProfileByShopid(Long shopid) {
		CafeShopProfile cafeShopProfile = baseDao.getObject(CafeShopProfile.class, shopid);
		if (VmUtils.isEmpObj(cafeShopProfile)) {
			return new CafeShopProfile();
		}
		return cafeShopProfile;
	}

	@Override
	public void saveOrUpdateShopProfile(Long shopid, String mobile, String takeawaystatus, String reservedstatus,
			String create) {
		CafeShopProfile shopProfile = baseDao.getObject(CafeShopProfile.class, shopid);
		if (VmUtils.isEmpObj(shopProfile)) {
			CafeShop shop = baseDao.getObject(CafeShop.class, shopid);
			shopProfile = new CafeShopProfile(shop);
		}
		shopProfile.setMobile(mobile);
		shopProfile.setReservedstatus(reservedstatus);
		shopProfile.setTakeawaystatus(takeawaystatus);
		shopProfile.setUpdatetime(DateUtil.getMillTimestamp());
		shopProfile.setTimeslot(create);
		baseDao.saveObject(shopProfile);
	}

	@Override
	public List<ShopExpressAddress> findExpressAddressByShopid(Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(ShopExpressAddress.class);
		query.add(Restrictions.eq("shopid", shopid));
		query.addOrder(Order.desc("updatetime"));
		List<ShopExpressAddress> addressList = baseDao.findByCriteria(query);
		if (VmUtils.isEmptyList(addressList)) {
			addressList = new ArrayList();
		}
		return addressList;
	}

	@Override
	public Long addExprAddr(Long shopid, String address) {
		List<ShopExpressAddress> addressList = this.findExpressAddressByShopid(shopid);
		for (ShopExpressAddress addr : addressList) {
			if (addr.getAddress().equals(address)) {
				return null;
			}
		}
		CafeShop shop = this.findshop(shopid);
		if (VmUtils.isEmpObj(shop)) {
			return null;
		}
		ShopExpressAddress expressAddress = new ShopExpressAddress(shop, address);
		baseDao.saveObject(expressAddress);
		return expressAddress.getId();
	}

	@Override
	public Boolean deleteExprAddrByAddr(Long id) {
		ShopExpressAddress expressAddress = this.baseDao.getObject(ShopExpressAddress.class, id);
		baseDao.removeObject(expressAddress);
		return true;
	}

	@Override
	public List<CafeRecruit> recruit(String name, Integer pageNo, int maxnum) {
		DetachedCriteria query = DetachedCriteria.forClass(CafeRecruit.class);
		if (StringUtils.isNotBlank(name)) {
			query.add(Restrictions.like("name", "%" + name + "%"));
		}
		query.addOrder(Order.desc("id"));
		query.add(Restrictions.eq("state", 1));
		int from = pageNo * maxnum;
		List<CafeRecruit> recruitList = baseDao.findByCriteria(query, from, maxnum);
		return recruitList;
	}

	@Override
	public int findCafeRecruitCount(String name) {
		DetachedCriteria query = DetachedCriteria.forClass(CafeRecruit.class);
		if (StringUtils.isNotBlank(name)) {
			query.add(Restrictions.like("name", "%" + name + "%"));
		}
		query.add(Restrictions.eq("state", 1));
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}

	@Override
	public CafeRecruit caferecruit(Long id) {
		CafeRecruit caferecruit = this.baseDao.getObject(CafeRecruit.class, id);
		return caferecruit;
	}

	@Override
	public List<WheelysBanner> showBannerImgList(Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(WheelysBanner.class);
		query.add(Restrictions.eq("shopid", shopid));
		query.add(Restrictions.eq("delstatus", BannerConstant.DELSTATUS_NO));
		query.add(Restrictions.eq("showstatus", "Y"));
		query.addOrder(Order.asc("sn"));
		List<WheelysBanner> bannerList = baseDao.findByCriteria(query);
		return bannerList;
	}

	private List<ProductShopVo> getCafeProductListByShop(Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(ProductShop.class);
		query.add(Restrictions.eq("psshopid", shopid));
		query.addOrder(Order.asc("psorder"));
		query.add(Restrictions.eq("delstatus", "N"));
		query.add(Restrictions.eq("display", 1));
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
	public Map<CafeItem, List<ProductShopVo>> getShopCafeProductMap(Long shopid) {
		Map<CafeItem, List<ProductShopVo>> cafeProductMap = new HashMap<CafeItem, List<ProductShopVo>>();
		List<CafeItem> cafeItemList = productService.getCafeItemList();
		List<ProductShopVo> cafeProductVoList = getCafeProductListByShop(shopid);
		Map map = BeanUtil.groupBeanList(cafeProductVoList, "itemid");
		for (CafeItem cafeItem : cafeItemList) {
			List<ProductShopVo> list = (List<ProductShopVo>) map.get(cafeItem.getId());
			cafeProductMap.put(cafeItem, list);
		}
		return cafeProductMap;
	}

	@Override
	public ResultCode changeShopStatus(Long shopid, String status, Timestamp closedowntime, String reason) {
		CafeShop cafeShop = this.baseDao.getObject(CafeShop.class, shopid);
		if (StringUtils.equals("open", status) && !StringUtils.equals(cafeShop.getBooking(), "closedown")) {
			cafeShop.setBooking(status);
			this.baseDao.saveObject(cafeShop);
		} else if (StringUtils.equals("close", status) && !StringUtils.equals(cafeShop.getBooking(), "closedown")) {
			cafeShop.setBooking(status);
			this.baseDao.saveObject(cafeShop);
		} else if (StringUtils.equals("closedown", status)) {
			DetachedCriteria query = DetachedCriteria.forClass(Booking.class);
			query.add(Restrictions.eq("shopid", shopid));
			query.add(Restrictions.eq("status", "closedown"));
			query.setProjection(Projections.rowCount());
			List<Long> list = baseDao.findByCriteria(query);
			if (list.get(0) == 0) {
				Booking booking = new Booking(shopid, reason, closedowntime, status);
				CafeShop shop = this.baseDao.getObject(CafeShop.class, shopid);
				booking.setContant(shop.getContacts());
				booking.setTelephone(shop.getShoptelephone());
				booking.setCompanyid(shop.getOperatorid());
				booking.setType("applyclose");
				this.baseDao.saveObject(booking);
			}
		} else if (StringUtils.equals("cancel", status)) {
			DetachedCriteria query = DetachedCriteria.forClass(Booking.class);
			LogicalExpression r1 = Restrictions.and(Restrictions.eq("shopid", shopid),
					Restrictions.eq("status", "closedown"));
			LogicalExpression r2 = Restrictions.and(Restrictions.eq("shopid", shopid),
					Restrictions.eq("status", "startbusiness"));
			query.add(Restrictions.or(r1, r2));
			List<Booking> list = baseDao.findByCriteria(query);
			if (!list.isEmpty()) {
				list.get(0).setStatus(status);
				list.get(0).setUpdatetime(DateUtil.getMillTimestamp());
				this.baseDao.saveObject(list.get(0));
			} else {
				return ResultCode.getFailure("已经审核通过，撤销失败！");
			}
		} else if (StringUtils.equals("startbusiness", status)
				&& StringUtils.equals(cafeShop.getBooking(), "closedown")) {
			DetachedCriteria query = DetachedCriteria.forClass(Booking.class);
			query.add(Restrictions.eq("shopid", shopid));
			query.add(Restrictions.eq("status", "startbusiness"));
			query.setProjection(Projections.rowCount());
			List<Long> list = baseDao.findByCriteria(query);
			if (list.get(0) == 0) {
				Booking booking = new Booking(shopid, reason, closedowntime, status);
				CafeShop shop = this.baseDao.getObject(CafeShop.class, shopid);
				booking.setContant(shop.getContacts());
				booking.setTelephone(shop.getShoptelephone());
				booking.setCompanyid(shop.getOperatorid());
				booking.setType("applystart");
				this.baseDao.saveObject(booking);
			}
		}
		return ResultCode.SUCCESS;
	}

	@Override
	public List<CafeShop> cafeShopList() {
		DetachedCriteria query = DetachedCriteria.forClass(CafeShop.class);
		query.add(Restrictions.eq("delstatus", "N"));
		List<CafeShop> cafeShopList = baseDao.findByCriteria(query);
		return cafeShopList;
	}

	@Override
	public boolean shopesname(String esname, Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(CafeShop.class);
		query.add(Restrictions.eq("esname", esname));
		if (shopid != null) {
			query.add(Restrictions.ne("shopid", shopid));
		}
		query.setProjection(Projections.rowCount());
		List<Long> shopList = baseDao.findByCriteria(query);
		return shopList.get(0) > 0;
	}

	@Override
	public void delshop(Long shopid) {
		CafeShop upCafeShop = this.findCafeShop(shopid);
		if (StringUtils.equals(upCafeShop.getDelstatus(), "N")) {
			upCafeShop.setDelstatus("Y");
		}
		this.baseDao.saveObject(upCafeShop);
	}

	@Override
	public void shopclose(Long shopid) {
		// 永久关闭店铺
		CafeShop cafeShop = this.findCafeShop(shopid);
		String allstop = cafeShop.getBooking();
		if (StringUtils.isNotBlank(allstop)) {
				cafeShop.setBooking("FS");
		}
		this.baseDao.saveObject(cafeShop);
	}


}


