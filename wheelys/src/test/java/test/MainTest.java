package test;

import java.util.Date;

import com.wheelys.util.DateUtil;

public class MainTest {

	public static void main(String[] args) {
		Date initdate = DateUtil.parseDate("2017-01-02");
		for (int i = 0; i < 60; i++) {
			int d = DateUtil.getDiffDay(initdate, DateUtil.addDay(initdate, i));
			System.out.println(DateUtil.formatDate(DateUtil.addDay(initdate, i))+":"+(d/7+1));
		}
	}
	
}
