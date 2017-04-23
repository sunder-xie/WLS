package com.wheelys.service.cafe;

import java.sql.Timestamp;
import java.util.List;

import com.wheelys.model.discount.DiscountActivity;
import com.wheelys.web.action.admin.vo.DiscountActivityVo;

public interface DiscountService {

	void updateDiscount1(Long discountid, String status, String shopids, Timestamp begintime, Timestamp endtime);

	void savediscount(DiscountActivity discountActivity);

	/**
	 * 获取所有优惠活动列表
	 * @param pageNo
	 * @param name
	 * @param activitytype
	 * @param rowsPerPage
	 * @return
	 */
	List<DiscountActivityVo> findAllDiscountList(Integer pageNo, String name, String activitytype, int rowsPerPage);

	/**
	 * 获取优惠活动的count
	 * @param name
	 * @param activitytype
	 * @return
	 */
	int findAllCount(String name, String activitytype);

	DiscountActivity findActiveById(Long id);

	List<DiscountActivity> showactivityList(String actname, Integer pageNo, int rowsPerPage);

	int findListCount(String actname);
	/**
	 * 查找活动商品分类
	 * @param id
	 * @return
	 */
	List<Long>itemids(Long id);
	/**
	 * 查找商品id
	 * @param id
	 * @return
	 */
	List<Long>productids(Long id);
	
}
