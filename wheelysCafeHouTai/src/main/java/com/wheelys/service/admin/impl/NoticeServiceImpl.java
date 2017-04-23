package com.wheelys.service.admin.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.util.BeanUtil;
import com.wheelys.util.DateUtil;
import com.wheelys.model.CafeShop;
import com.wheelys.model.notice.NoticeDetail;
import com.wheelys.model.notice.NoticeManage;
import com.wheelys.service.admin.NoticeService;
import com.wheelys.web.action.admin.vo.NoticeVo;

@Service("noticeService")
public class NoticeServiceImpl extends BaseServiceImpl implements NoticeService {

	@Override
	public void addNotice(Long id, String noticename, String content, String validshopid, Timestamp begintime,
			Timestamp endtime) {
		NoticeManage notice = this.baseDao.getObject(NoticeManage.class, id);
		if (notice == null) {
			notice = new NoticeManage(noticename, content, validshopid, begintime, endtime);
			this.baseDao.saveObject(notice);
			List<String> rList = Arrays.asList(validshopid.split(","));
			for (String sid : rList) {
				Long lid = Long.parseLong(sid);
				NoticeDetail detail = new NoticeDetail(notice.getId(), lid, begintime, endtime);
				this.baseDao.saveObject(detail);
			}
		} else {
			List<String> rList = Arrays.asList(notice.getShopids().split(","));
			List<String> alist = new CopyOnWriteArrayList<>(rList);
			List<String> dList = Arrays.asList(validshopid.split(","));
			List<String> slist = new CopyOnWriteArrayList<>(dList);
			if (StringUtils.isBlank(alist.get(0))) {
				notice.setNoticename(noticename);
				notice.setContent(content);
				notice.setShopids(validshopid);
				notice.setBegintime(begintime);
				notice.setEndtime(endtime);
				this.baseDao.saveObject(notice);
				List<String> rList2 = Arrays.asList(validshopid.split(","));
				for (String sid : rList2) {
					Long lid = Long.valueOf(sid);
					NoticeDetail detail = new NoticeDetail(notice.getId(), lid, begintime, endtime);
					this.baseDao.saveObject(detail);
				}
			} else {
				for (String s : alist) {
					if (!slist.contains(s)) {
						DetachedCriteria query = DetachedCriteria.forClass(NoticeDetail.class);
						Long sid = Long.valueOf(s);
						query.add(Restrictions.eq("shopid", sid));
						List<NoticeDetail> detailList = baseDao.findByCriteria(query);
						for (NoticeDetail n : detailList) {
							n.setDelstatus("Y");
							this.baseDao.saveObject(n);
						}
						alist.remove(s);
					}
				}

				for (String a : slist) {
					if (!alist.contains(a)) {
						DetachedCriteria query = DetachedCriteria.forClass(NoticeDetail.class);
						Long sid = Long.valueOf(a);
						query.add(Restrictions.eq("shopid", sid));
						List<NoticeDetail> detailList = baseDao.findByCriteria(query);
						if (detailList.isEmpty()) {
							NoticeDetail detail = new NoticeDetail(id, sid, begintime, endtime);
							this.baseDao.saveObject(detail);
						} else {
							for (NoticeDetail n : detailList) {
								n.setDelstatus("N");
								this.baseDao.saveObject(n);
							}
						}
						alist.add(a);
					}
				}
				String ids = StringUtils.join(alist, ",");
				notice.setNoticename(noticename);
				notice.setContent(content);
				notice.setShopids(ids);
				notice.setBegintime(begintime);
				notice.setEndtime(endtime);
				this.baseDao.saveObject(notice);
			}
		}
	}

	@Override
	public List<NoticeVo> showList(Integer pageNo, int maxnum) {
		DetachedCriteria query = DetachedCriteria.forClass(NoticeManage.class);
		query.add(Restrictions.eq("delstatus", "N"));
		query.addOrder(Order.asc("begintime"));
		int from = pageNo * maxnum;
		List<NoticeManage> showList = this.baseDao.findByCriteria(query, from, maxnum);
		List<NoticeVo> voList = new ArrayList<>();
		List<String> nameList = null;
		for (NoticeManage manage : showList) {
			List<Long> shopIdList = BeanUtil.getIdList(manage.getShopids(), ",");
			List<CafeShop> cafeShopList = baseDao.getObjectList(CafeShop.class, shopIdList);
			nameList = BeanUtil.getBeanPropertyList(cafeShopList, String.class, "esname", true);
			String names = StringUtils.join(nameList, ",");
			NoticeVo vo = new NoticeVo();
			vo.setNoticeManage(manage);
			vo.setShopname(names);
			voList.add(vo);
		}
		return voList;
	}

	@Override
	public void delStatus(Long id) {
		NoticeManage notice = this.baseDao.getObject(NoticeManage.class, id);
		if (StringUtils.equals(notice.getDelstatus(), "N")) {
			notice.setDelstatus("Y");
			this.baseDao.saveObject(notice);
		} else {
			this.baseDao.saveObject(notice);
		}
	}

	@Override
	public NoticeManage notice(Long id) {
		NoticeManage notice = this.baseDao.getObject(NoticeManage.class, id);
		return notice;
	}

	@Override
	public List<NoticeVo> voList(Long shopid) {
		Timestamp c = DateUtil.getMillTimestamp();
		Timestamp t = DateUtil.getBeginTimestamp(c);
		List<NoticeVo> voList = new ArrayList<>();
		DetachedCriteria query = DetachedCriteria.forClass(NoticeDetail.class);
		query.add(Restrictions.eq("shopid", shopid));
		query.add(Restrictions.eq("delstatus", "N"));
		query.add(Restrictions.le("begintime", c));
		query.add(Restrictions.ge("endtime", c));
		query.add(Restrictions.le("updatetime", t));
		List<NoticeDetail> detailList = baseDao.findByCriteria(query);
		List<Long> idList = new ArrayList<>();
		if (!detailList.isEmpty()) {
			for (NoticeDetail d : detailList) {
				d.setUpdatetime(DateUtil.getMillTimestamp());
				idList.add(d.getNoticeid());
			}
			for (Long id : idList) {
				NoticeManage manage = this.baseDao.getObject(NoticeManage.class, id);
				NoticeVo vo = new NoticeVo(manage.getNoticename(), manage.getContent());
				voList.add(vo);
			}
		}
		return voList;
	}

	@Override
	public List<String> shoids(Long id) {
		NoticeManage manage = this.baseDao.getObject(NoticeManage.class, id);
		if (StringUtils.isNotBlank(manage.getShopids())) {
			List<String> nameList = null;
			List<Long> shopIdList = BeanUtil.getIdList(manage.getShopids(), ",");
			List<CafeShop> cafeShopList = baseDao.getObjectList(CafeShop.class, shopIdList);
			nameList = BeanUtil.getBeanPropertyList(cafeShopList, String.class, "esname", true);
			return nameList;
		} else {
			return null;
		}
	}

	@Override
	public int findCount() {
		DetachedCriteria query = DetachedCriteria.forClass(NoticeManage.class);
		query.add(Restrictions.eq("delstatus", "N"));
		query.setProjection(Projections.rowCount());
		List<Long> result = baseDao.findByCriteria(query);
		return result.get(0).intValue();
	}
}
