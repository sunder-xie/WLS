
package com.wheelys.service.admin.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.DateUtil;
import com.wheelys.model.CafeShop;
import com.wheelys.model.booking.Booking;
import com.wheelys.service.admin.BookingService;
import com.wheelys.web.action.admin.vo.BookingVo;

@Service("bookingService")
public class BookingServiceImpl extends BaseServiceImpl implements BookingService {

	@Override
	public List<BookingVo> bookList() {
		DetachedCriteria query = DetachedCriteria.forClass(Booking.class);
		query.addOrder(Order.desc("id"));
		List<Booking> showbookList = baseDao.findByCriteria(query);
		List<BookingVo> voList = new ArrayList<BookingVo>();
		for (Booking booking : showbookList) {
			CafeShop cafeshop = this.baseDao.getObject(CafeShop.class, booking.getShopid());
			if (cafeshop != null) {
				BookingVo bookingvo = new BookingVo(booking.getId(), cafeshop.getEsname(), null, cafeshop.getContacts(),
						cafeshop.getShoptelephone(), booking.getType(), booking.getApplytime(), booking.getStatus());
				voList.add(bookingvo);
			}
		}
		return voList;
	}

	@Override
	public void changestatus(Long id, String status) {
		Booking booking = this.baseDao.getObject(Booking.class, id);
		if (StringUtils.equals(booking.getStatus(), "closedown")
				|| StringUtils.equals(booking.getStatus(), "startbusiness")) {
			CafeShop shop = this.baseDao.getObject(CafeShop.class, booking.getShopid());
			if (StringUtils.equals(booking.getType(), "applyclose")) {
				shop.setBooking("closedown");
			} else {
				shop.setBooking("close");
			}
			this.baseDao.saveObject(shop);
			booking.setStatus(status);
			booking.setPasstime(DateUtil.getMillTimestamp());
			this.baseDao.saveObject(booking);
		}
	}

	@Override
	public Booking book(Long id) {
		Booking booking = this.baseDao.getObject(Booking.class, id);
		return booking;
	}

	@Override
	public Booking getCurBooking(Long shopid) {
		DetachedCriteria query = DetachedCriteria.forClass(Booking.class);
		query.add(Restrictions.eq("shopid", shopid));
		// query.add(Restrictions.in("status", "closedown","startbusiness"));
		query.addOrder(Order.desc("id"));
		List<Booking> showbookList = baseDao.findByCriteria(query);
		return showbookList.isEmpty() ? null : showbookList.get(0);
	}

}
