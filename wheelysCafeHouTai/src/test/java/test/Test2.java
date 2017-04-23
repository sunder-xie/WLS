package test;

import java.sql.Timestamp;
import java.util.Date;

import com.wheelys.util.DateUtil;

public class Test2 {
		public static void main(String[] args) {
			String formatDate = DateUtil.formatDate(new Date());
			Timestamp timestamp = DateUtil.parseTimestamp(formatDate,"yyyy-MM-dd");
			Timestamp endTimestamp = DateUtil.getEndTimestamp(timestamp);
			System.out.println(formatDate);
			System.out.println(timestamp);
			System.out.println(endTimestamp);
	}
}
