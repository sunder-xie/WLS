package test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SomeTest {

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		String yes = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		System.out.println(yes);
		long current=System.currentTimeMillis();//当前时间毫秒数
		long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
		Timestamp yesterday0 = new Timestamp(zero - 24*60*60*1000);
		long yesterday=System.currentTimeMillis()-24*60*60*1000;//昨天的这一时间的毫秒数
		System.out.println("zero:"+zero);
		System.out.println("当前时间:"+new Timestamp(current));//当前时间
		System.out.println("昨天这一时间点:"+new Timestamp(yesterday));//昨天这一时间点
		System.out.println("今天零点零分零秒:"+new Timestamp(zero));//今天零点零分零秒
		System.out.println("昨天零点零分零秒:"+yesterday0);//昨天零点零分零秒
		System.out.println("昨天零点零分零秒字符串:"+yesterday0);//昨天零点零分零秒字符串
		System.out.println("昨天23点59分59秒:"+new Timestamp(zero - 1));//昨天23点59分59秒
		System.out.println(new Timestamp(yesterday0.getTime() + 1000*3600*24));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date(yesterday0.getTime());
		String strDate = sdf.format(d);
		Date date = null;
		try {
			date = sdf.parse(strDate);
			System.out.println(sdf.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
