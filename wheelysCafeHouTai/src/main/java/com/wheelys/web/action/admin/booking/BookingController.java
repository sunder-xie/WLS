package com.wheelys.web.action.admin.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wheelys.model.booking.Booking;
import com.wheelys.service.admin.BookingService;
import com.wheelys.web.action.AnnotationController;
import com.wheelys.web.action.admin.vo.BookingVo;

@Controller
public class BookingController extends AnnotationController {

	@Autowired
	@Qualifier("bookingService")
	private BookingService bookingService;

	@RequestMapping("/admin/shop/showbookingList.xhtml")
	public String showbookingList(ModelMap model) {
		List<BookingVo> bookList = bookingService.bookList();
		model.put("bookList", bookList);
		return "/admin/booking/bookList.vm";
	}

	@RequestMapping("/admin/shop/changestatus.xhtml")
	public String chanstatus(Long id, String status, ModelMap model) {
		bookingService.changestatus(id, status);
		return this.showJsonSuccess(model, null);
	}

	@RequestMapping("/admin/shop/bookDetail.xhtml")
	public String booking(Long id, ModelMap model) {
		Booking book = bookingService.book(id);
		model.put("book", book);
		return "/admin/booking/bookDetail.vm";
	}

	@RequestMapping("/admin/shop/applybooking.xhtml")
	public String bookingdetail(Long id, ModelMap model) {
		Booking book = bookingService.book(id);
		model.put("book", book);
		return "/admin/booking/applyDetail.vm";
	}

}
