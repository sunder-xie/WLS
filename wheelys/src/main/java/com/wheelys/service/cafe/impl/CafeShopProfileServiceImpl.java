package com.wheelys.service.cafe.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.DateUtil;
import com.wheelys.util.JsonUtils;
import com.wheelys.model.CafeShop;
import com.wheelys.model.CafeShopProfile;
import com.wheelys.model.ShopExpressAddress;
import com.wheelys.service.cafe.CafeShopProfileService;
import com.wheelys.util.VmUtils;

@Service("cafeShopProfileService")
public class CafeShopProfileServiceImpl extends BaseServiceImpl implements CafeShopProfileService {

	@Override
	public List<String> getExpressAddressByShopid(Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(ShopExpressAddress.class);
		query.setProjection(Projections.property("address"));
		query.add(Restrictions.eq("shopid", shopid));
		query.addOrder(Order.desc("updatetime"));
		List<String> exprAddrList = baseDao.findByCriteria(query);
		return exprAddrList;
	}
	
	@Override
	public Boolean getTakeawaystatus(Long shopid) {
		CafeShopProfile shopProfile = baseDao.getObject(CafeShopProfile.class, shopid);
		
		if(VmUtils.isEmpObj(shopProfile) || shopProfile.getTakeawaystatus().equalsIgnoreCase("N")){
			return false;
		}else{
			Map<String, String> date = new HashMap<String, String>();
			String s = shopProfile.getTimeslot();
			date = JsonUtils.readJsonToMap(s);
			String time1 = date.get("time1");
			String time2 = date.get("time2");
			String time3 = date.get("time3");
			String time4 = date.get("time4");
			String time5 = date.get("time5");
			String time6 = date.get("time6");
			Timestamp currtime = DateUtil.getMillTimestamp();
			if(StringUtils.isNotBlank(time1)&&StringUtils.isNotBlank(time2)){
				String date1= DateUtil.getCurDateStr()+" "+time1+":00";
				Timestamp bengindate = DateUtil.parseTimestamp(date1);
				String date2 = DateUtil.getCurDateStr()+" "+time2+":00";
				Timestamp enddate = DateUtil.parseTimestamp(date2);
				if(currtime.before(enddate)&&currtime.after(bengindate)){
					return true;
				}
			}
			if(StringUtils.isNotBlank(time3)&&StringUtils.isNotBlank(time4)){
				String date3= DateUtil.getCurDateStr()+" "+time3+":00";
				Timestamp bengindate = DateUtil.parseTimestamp(date3);
				String date4 = DateUtil.getCurDateStr()+" "+time4+":00";
				Timestamp enddate = DateUtil.parseTimestamp(date4);
				if(currtime.before(enddate)&&currtime.after(bengindate)){
					return true;
				}
			}
			if(StringUtils.isNotBlank(time5)&&StringUtils.isNotBlank(time6)){
				String date5= DateUtil.getCurDateStr()+" "+time5+":00";
				Timestamp bengindate = DateUtil.parseTimestamp(date5);
				String date6 = DateUtil.getCurDateStr()+" "+time6+":00";
				Timestamp enddate = DateUtil.parseTimestamp(date6);
				if(currtime.before(enddate)&&currtime.after(bengindate)){
					return true;
				}
			}
			if(StringUtils.isBlank(time1)&&StringUtils.isBlank(time2)
					&&StringUtils.isBlank(time3)&&StringUtils.isBlank(time4)
					&&StringUtils.isBlank(time5)&&StringUtils.isBlank(time6)){
				return true;
			}
			return false;
		}
	}
	
	@Override
	public Boolean getReservedstatus(Long shopid) {
		CafeShopProfile shopProfile = baseDao.getObject(CafeShopProfile.class, shopid);
		if(VmUtils.isEmpObj(shopProfile) || StringUtils.equalsIgnoreCase(shopProfile.getReservedstatus(), "N")){
			return false;
		}else{
			try {
				CafeShop cafeShop = baseDao.getObject(CafeShop.class, shopid);
				if(StringUtils.contains(cafeShop.getShoptime(), "-")){
					String[] arr = StringUtils.split(cafeShop.getShoptime(), "-");
					String closetime = arr[1];
					String date = DateUtil.getCurDateStr()+" "+closetime+":00";
					Timestamp closedate = DateUtil.addHour(DateUtil.parseTimestamp(date), -1);
					if(closedate.after(DateUtil.getMillTimestamp())){
						return true;
					}
					return false;
				}
			} catch (Exception e) {
				dbLogger.warn("getReservedstatus shopid:"+shopid);
			}
			return true;
		}
	}

	@Override
	public CafeShopProfile getShopProfile(Long shopid) {
		CafeShopProfile shopProfile = baseDao.getObject(CafeShopProfile.class, shopid);
		return shopProfile;
	}

}
