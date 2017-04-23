package com.wheelys.service.admin;

import java.util.List;
import com.wheelys.model.merchant.MchOrder;
import com.wheelys.model.merchant.Mchrules;

public interface MchrulesService {
	/**
	 * 查询
	 * 
	 * @param supplierid
	 * @return
	 */
	List<Mchrules> ruleList(Long supplierid);

	/**
	 * 修改和新增
	 * 
	 * @param id
	 * @param supplierid
	 * @param morefreight
	 * @param morecost
	 * @param lessfreight
	 * @param lesscost
	 */
	void add(Long id, String citycode, Long supplierid, Float morefreight, Float morecost, Float lessfreight,
			Float lesscost);

	/**
	 * 返回修改前进入的规则对象
	 * 
	 * @param id
	 * @return
	 */
	Mchrules rules(Long id);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	void delsrules(Long id);

	/**
	 * 根据shopid存
	 * 
	 * @param shopid
	 * @return
	 */
	void ruleShop(MchOrder order, int weight);

	/**
	 * 判断该城市是否有规则
	 * 
	 * @param citycode
	 * @return
	 */
	boolean citycode(String citycode, Long id,Long supplierid);
	/**
	 * 返回上海地区对象
	 * @param citycode
	 * @param supplierid
	 * @return
	 */
	Mchrules findRule(Long supplierid);
}
