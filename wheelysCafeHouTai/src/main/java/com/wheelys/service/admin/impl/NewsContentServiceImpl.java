package com.wheelys.service.admin.impl;

import org.springframework.stereotype.Service;

import com.wheelys.service.impl.BaseServiceImpl;
import com.wheelys.model.news.WheelysNewsContent;
import com.wheelys.service.admin.NewsContentService;

@Service("newsContentService")
public class NewsContentServiceImpl extends BaseServiceImpl implements NewsContentService {

	@Override
	public WheelysNewsContent getNewsContentByNewsid(Long id) {
		WheelysNewsContent newsContent = baseDao.getObject(WheelysNewsContent.class, id);
		return newsContent;
	}

}
