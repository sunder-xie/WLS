package com.wheelys.service.admin;

import java.sql.Timestamp;
import java.util.List;

import com.wheelys.model.notice.NoticeManage;
import com.wheelys.web.action.admin.vo.NoticeVo;

public interface NoticeService {
	/**
	 * 修改和增加通知
	 * @param id
	 * @param noticename
	 * @param content
	 * @param shopid
	 * @param begintime
	 * @param endtime
	 */
	void addNotice(Long id, String noticename, String content,String shopid,Timestamp begintime, Timestamp endtime);
	/**
	 * 展示通知
	 * @return
	 */
	List<NoticeVo>showList(Integer pageNo, int maxnum);
	/**
	 * 删除通知
	 * @param id
	 */
	void delStatus(Long id);
	/**
	 * 修改前查询通知
	 * @param id
	 * @return
	 */
	NoticeManage notice(Long id);
	/**
	 * 接口用到的查询店铺通知名和内容
	 * @param shopid
	 * @return
	 */
	List<NoticeVo>voList(Long shopid);
	/**
	 * 查询店铺名
	 * @param id
	 * @return
	 */
	List<String>shoids(Long id);
	/**
	 * 聚合
	 * @return
	 */
	int findCount();
}
