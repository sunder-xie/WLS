package com.wheelys.service.admin;

import java.util.List;

import com.wheelys.model.booking.Booking;
import com.wheelys.web.action.admin.vo.BookingVo;

public interface BookingService {
	/**
	 * 店铺关停列表展示
	 * @return
	 */
	List<BookingVo> bookList();
	/**
	 * 改变状态
	 * @param id
	 * @param status
	 */
	void changestatus(Long id, String status);
	/**
	 * 根据id返回关停对象
	 * @param id
	 * @return
	 */
	Booking book(Long id);
	/**
	 * 根据店铺id拿到关停对象
	 * @param shopid
	 * @return
	 */
	Booking getCurBooking(Long shopid);

}
