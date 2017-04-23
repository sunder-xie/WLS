package com.wheelys.untrans.eleme.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wheelys.model.pay.WheelysOrder;
import com.wheelys.untrans.eleme.ElemeService;

@Service("elemeService")
public class ElemeServiceImpl implements ElemeService {

	@Value("${ELEME.FENGNIAO.APPID}")
	private String APPID;
	@Value("${ELEME.FENGNIAO.CECRETKEY}")
	private String CECRETKEY;
	public static final String CHARSET = "utf-8";
	
	public void notifyEleOrder(WheelysOrder order){
		//1.获取AccessToken
	
	//2.Token签名
	Map<String, Object> tokenParams = new HashMap<String, Object>();
	//随机生成四位数字
	int salt=(int)(Math.random()*(9999-1000+1)+1000);
	tokenParams.put("app_id", APPID);
	tokenParams.put("secret_key", CECRETKEY);
	tokenParams.put("salt",salt);
	String tokensignature="";
	List<String> keys = new ArrayList(tokenParams.keySet());
	for (String key : keys) {
		tokensignature += key+"="+tokenParams.get(key)+"&";
	}
	try {
		URLEncoder.encode(tokensignature,CHARSET);
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
	//3.组装业务参数
	
	//4.调用蜂鸟配送推单接口
	
	}
}
