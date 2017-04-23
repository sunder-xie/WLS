package test;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.wheelys.model.report.ReportOrderDatePaymethod;
import com.wheelys.util.DateUtil;

public class Test1 {

	public static void main(String[] args) {
		/*
		 * String string="htt://good morning everyone"; String sub="http://";
		 * int a=string.indexOf(sub); if(a>=0){
		 * System.out.println("morning在字符串中的位置:"+a); String
		 * ss1=string.substring(0,a+sub.length()); String
		 * ss2=string.substring(a+sub.length(),string.length());
		 * System.out.println("你需要的结果是:"+ss1);
		 * System.out.println("删掉的字符是:"+ss2); }else{
		 * 
		 * 
		 * 
		 * System.out.println("不存在"); String s1="123,231,456"; String s2="123";
		 * List<String> list = Arrays.asList(s1.split(",")); boolean contains =
		 * list.contains(s2); System.out.println(contains);
		 */
		/*
		 * String s3="adsad13123"; int s4 = length(s3); if(s4>5){
		 * System.out.println(s4); System.out.println(123); }else{
		 * System.out.println(s4); System.out.println(345); }
		 */
/*

<<<<<<< .mine
||||||| .r791
		} else {
			System.out.println(2);
		}

=======
		} else {
			System.out.println(2);
		}*/

		/*
		 * List<String>a=new CopyOnWriteArrayList<>(); a.add("1"); a.add("2");
		 * a.add("6"); a.add("7"); a.add("3"); Set<String>b = new TreeSet<>();
		 * b.add("3"); b.add("4"); b.add("5"); b.add("1");
		 * 
		 * for(String s :a){ if(!b.contains(s)){ a.remove(s); }
		 * 
		 * } for(String c:a){ System.out.println(c); }
		 */
		
	
	/*	Timestamp closedate = DateUtil.addHour(DateUtil.parseTimestamp(date), -1);*/
		List<String>list = new ArrayList<>();
		System.out.println(list.get(0));
		ReportOrderDatePaymethod m1 = new ReportOrderDatePaymethod();
		m1.setDate(DateUtil.parseDate("2016-12-01"));
		m1.setPaymethod("wx");
		m1.setShopid(6L);
		m1.setQuantity(20);
		m1.setNetpaid(20000);
		ReportOrderDatePaymethod m2 = new ReportOrderDatePaymethod();
		m2.setDate(DateUtil.parseDate("2016-12-01"));
		m2.setPaymethod("ali");
		m2.setShopid(6L);
		m2.setQuantity(5);
		m2.setNetpaid(8000);
		

		ReportOrderDatePaymethod m3 = new ReportOrderDatePaymethod();
		m3.setDate(DateUtil.parseDate("2016-12-01"));
		m3.setPaymethod("wx");
		m3.setShopid(7L);
		m3.setQuantity(20);
		m3.setNetpaid(20000);
		ReportOrderDatePaymethod m4 = new ReportOrderDatePaymethod();
		m4.setDate(DateUtil.parseDate("2016-12-01"));
		m4.setPaymethod("ali");
		m4.setShopid(7L);
		m4.setQuantity(5);
		m4.setNetpaid(8000);
		List<ReportOrderDatePaymethod> list1 = new ArrayList<ReportOrderDatePaymethod>();
		list1.add(m1);
		list1.add(m2);
		list1.add(m3);
		list1.add(m4);
		Map<Long, Map<String, Vo>> data = new HashMap<Long, Map<String,Vo>>();
		for (ReportOrderDatePaymethod m : list1) {
			Map<String, Vo> voData = data.get(m.getShopid());
			if (voData == null) {
				voData = new HashMap<String, Vo>();
				data.put(m.getShopid(), voData);
			}
			Vo vo = voData.get(m.getShopid()+"_"+DateUtil.formatDate(m.getDate()));
			if (vo == null) {
				vo = new Vo();
				voData.put(m.getShopid()+"_"+DateUtil.formatDate(m.getDate()), vo);
			}
			if(StringUtils.equals(m.getPaymethod(), "wx")){
				vo.a1 = m.getNetpaid();
				vo.q1 = m.getQuantity();
			}else if(StringUtils.equals(m.getPaymethod(), "ali")){
				vo.a2 = m.getNetpaid();
				vo.q2 = m.getQuantity();
			}
			
		}
		for (Long shopid : data.keySet()) {
			System.out.println("---------------");
			System.out.println(shopid);
			Map<String, Vo> voData = data.get(shopid);
			for (String key : voData.keySet()) {
				Vo vo = voData.get(key);
				System.out.println(vo);
			}
			System.out.println("---------------");
		}
	}
	

	/*public static int length(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 
		for (int i = 0; i < value.length(); i++) {
			 获取一个字符 
			String temp = value.substring(i, i + 1);
			 判断是否为中文字符 
			if (temp.matches(chinese)) {
				 中文字符长度为2 
				valueLength += 2;
			} else {
				 其他字符长度为1 
				valueLength += 1;
			}
		}
		return valueLength;
	}*/

	/*public static boolean yan(String name) {
		// 允许数字和-
		//String engLish = "/\\d+(\\.\\d{0,1})?/";
		String e = "^[0-9\\.]{1,10}$";
		 String e2 = "^+$"; 
		Pattern pattern = Pattern.compile(e);
		Matcher matcher = pattern.matcher(name);
		return matcher.find();

	}*/
	
	

}
class Vo{
	int q1;
	int a1;
	int q2;
	int a2;
	@Override
	public String toString() {
		return "Vo [q1=" + q1 + ", a1=" + a1 + ", q2=" + q2 + ", a2=" + a2 + "]";
	}
}
