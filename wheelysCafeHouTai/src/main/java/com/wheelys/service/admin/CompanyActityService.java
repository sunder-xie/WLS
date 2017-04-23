package com.wheelys.service.admin;

import java.util.List;
import com.wheelys.api.vo.ResultCode;
import com.wheelys.model.company.CompanyActivity;

public interface CompanyActityService {
	/**
	 * 展示运营商活动列表
	 * @param pageNo
	 * @param rowsPerPage
	 * @return
	 */
	List<CompanyActivity> showList(Integer pageNo, Integer rowsPerPage);
	/**
	 * 根据id返回详细活动
	 * @param id
	 * @param operatorstatus
	 * @return
	 */
	CompanyActivity operator(Long id, String operatorstatus);
	/**
	 * 添加活动内容
	 * @param actity
	 */
	void actity(String actity);
	/**
	 * 接口添加活动
	 * @param shoid
	 * @param operatorinfo
	 * @return
	 */
	ResultCode<CompanyActivity> addoperator(Long shoid, String operatorinfo);
	/**
	 * 返回分页总条数
	 * @return
	 */
	int findListCount();

}
