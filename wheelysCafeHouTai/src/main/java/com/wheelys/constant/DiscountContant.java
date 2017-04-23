package com.wheelys.constant;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.map.UnmodifiableMap;

public class DiscountContant {
	//控制开关
	public static final String CONTROL_1 = "N";//中止
	public static final String CONTROL_2 = "Y";//开启

	//活动状态
	public static final String STATUSTYPE_1 = "I";//优惠中
	public static final String STATUSTYPE_2 = "W";//未到期
	public static final String STATUSTYPE_3 = "G";//已过期
	public static final String STATUSTYPE_4 = "Z";//已中止
	//优先级
	public static final String PRIORITY_1 = "1";//低
	public static final String PRIORITY_2 = "2";//中
	public static final String PRIORITY_3 = "3";//高
	//活动类型
	public static final String ONEBUYONE = "ONEBUYONE";	//买一送一
	public static final String FIXAMOUNT = "FIXAMOUNT";	//固定价格
	public static final String ZHEKOU = "ZHEKOU";		//折扣
	public static final String FIRSTCUP = "FIRSTCUP";	//首杯x元
	public static final String SECONDCUP = "SECONDCUP";	//第二杯折扣
	public static final String MANJIAN = "MANJIAN";	//满减
	//周期集合
	public static final String WEEK_1 = "1";//星期一
	public static final String WEEK_2 = "2";//星期二
	public static final String WEEK_3 = "3";//星期三
	public static final String WEEK_4 = "4";//星期四
	public static final String WEEK_5 = "5";//星期五
	public static final String WEEK_6 = "6";//星期六
	public static final String WEEK_7 = "7";//星期日
	
	//活动附加选项
	public static final String append_1="A";//特浓加价3元
	public static Map<String, String> APPENDMAP=new HashMap<>();
	static{
		Map<String, String> map=new HashMap<>();
		map.put(append_1, "特浓加价3元");
		APPENDMAP=UnmodifiableMap.decorate(map);
	}
	
	//活动状态集合
	public static Map<String, String> STATEMAP=new HashMap<>();
	static{
		Map<String, String> map=new HashMap<>();
		map.put(STATUSTYPE_1, "优惠中");
		map.put(STATUSTYPE_2, "未到期");
		map.put(STATUSTYPE_3, "已过期");
		map.put(STATUSTYPE_4, "已中止");
		STATEMAP=UnmodifiableMap.decorate(map);
	}
	
	//优先级集合
	public static Map<String, String> PRIORITYMAP=new HashMap<>();
	static{
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		map.put(PRIORITY_1, "低");
		map.put(PRIORITY_2, "中");
		map.put(PRIORITY_3, "高");
		PRIORITYMAP=UnmodifiableMap.decorate(map);
	}
	
	//活动类型集合
	public static Map<String,String> ACTIVITYMAP = new HashMap<String, String>();
	static{
		Map<String, String> map=new HashMap<>();
		map.put(ZHEKOU, "打折");
		map.put(FIRSTCUP, "首杯x元");
		map.put(FIXAMOUNT, "特定商品特价");
		map.put(ONEBUYONE, "买一赠一");
		map.put(SECONDCUP, "第二件x折");
		map.put(MANJIAN, "满x件减x元");
		ACTIVITYMAP=UnmodifiableMap.decorate(map);
	}
	
	//周期集合
	public static Map<String,String> WEEKMAP = new HashMap<String, String>();
	static{
		Map<String, String> map=new HashMap<>();
		map.put(WEEK_1, "星期一");
		map.put(WEEK_2, "星期二");
		map.put(WEEK_3, "星期三");
		map.put(WEEK_4, "星期四");
		map.put(WEEK_5, "星期五");
		map.put(WEEK_6, "星期六");
		map.put(WEEK_7, "星期日");
		WEEKMAP=UnmodifiableMap.decorate(map);
	}
	
}
