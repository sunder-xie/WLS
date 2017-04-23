package com.wheelys.service.admin;

import java.util.List;
import java.util.Map;
import com.wheelys.model.company.Company;
import com.wheelys.web.action.admin.vo.CompanyVo;

public interface CompanyService {
	/**
	 * 添加运营商
	 * @param id
	 * @param citycode
	 * @param name
	 * @param contants
	 * @param telephone
	 * @param password
	 */
	void addCompany(Long id,String citycode, String name, String contants, String telephone,String account,
			String password);
	/**
	 * 返回列表查询
	 * @param tradeno
	 * @param operatorname
	 * @param citycode
	 * @param pageNo
	 * @param maxnum
	 * @return
	 */
	List<CompanyVo> showList(String tradeno,String operatorname,String citycode,Integer pageNo, int maxnum);
	/**
	 * 统计
	 * @param tradeno
	 * @param operatorname
	 * @param citycode
	 * @return
	 */
	int findCompanyCount(String tradeno,String operatorname,String citycode);
	/**
	 * 返回运营商对象
	 * @param companyid
	 * @return
	 */
	Company findCompany(Long companyid);
	/**
	 * 删除
	 * @param operatorid
	 */
	void delstatus(Long operatorid);
	/**
	 * 返回运营商列表
	 * @return
	 */
	Map<Long, Company> mchProductItemMap() ;
	/**
	 * 
	 * @param operatorid
	 * @return
	 */
	int findCount(Long operatorid);
	/**
	 * 移除列表
	 * @param shopid
	 * @param operatorid
	 */
	void removeCompany(Long shopid, Long operatorid);
	/**
	 * 增加列表
	 * @param shopid
	 * @param operatorid
	 */
	void saveNumber(Long shopid,Long operatorid);
	/**
	 * 查询运营商是否重名
	 * @param name
	 * @param id
	 * @return
	 */
	boolean companyuser(String account, Long id);
}
