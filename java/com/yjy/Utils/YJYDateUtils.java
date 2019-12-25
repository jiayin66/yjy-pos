package com.yjy.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.ibatis.type.DateOnlyTypeHandler;

public class YJYDateUtils {
	/**
	 * 通过账单日设置每期的实际账单时间
	 * @param now
	 * @param param
	 * @return
	 */
	public static Date getDateFormInteger(Date now, Integer param) {
		Calendar ca=Calendar.getInstance();
		ca.setTime(now);
		ca.set(Calendar.DAY_OF_MONTH,param);
		return ca.getTime();
	}

	/*
	 * 两个日期的间隔天数
	 */
	public static Integer getBetween(Date now, Date billDate) {
		Long count=now.getTime()-billDate.getTime();
		if(count==0) {
			return 0;
		}
		Long date=count/1000/60/60/24;
		return date.intValue();
	}

	/**
	 * 获取下一期的账单日
	 * @param now
	 * @param param
	 * @return
	 */
	public static Date getDateFormIntegerNext(Date now, Integer param) {
		Calendar ca=Calendar.getInstance();
		ca.setTime(now);
		ca.set(Calendar.DAY_OF_MONTH,param);
		//此处会溢出吗---亲测不溢出
		ca.set(Calendar.MONTH,ca.get(Calendar.MONTH)+1);
		return ca.getTime();
	}
	
	public static void main(String[] args) {
		Date date=new Date();
		Date dateFormInteger = getDateFormInteger(date,3);
		System.out.println(date);
		System.out.println(dateFormInteger);
		
		
		/**
		 * 溢出测试
		 */
		/*Calendar ca=Calendar.getInstance();
		ca.setTime(new Date());
		ca.set(Calendar.MONTH,ca.get(Calendar.MONTH)+3);
		System.out.println(ca.getTime());*/
	}

	/**
	 * 时间格式
	 * @param date
	 * @return
	 */
	public static String formartUseDate(Date date) {
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
		return sd.format(date);
	}

	/**
	 * 账单日设置拿到最后还款日
	 * @param billDate
	 * @param spaceDate
	 * @return
	 */
	public static Date getLastDate(Date billDate, Integer spaceDate) {
		Calendar ca=Calendar.getInstance();
		ca.setTime(billDate);
		ca.set(Calendar.DAY_OF_MONTH,ca.get(Calendar.DAY_OF_MONTH)+spaceDate);
		return ca.getTime();
	}

	/**
	 * 拿到前一天
	 * @param billDate
	 * @return
	 */
	public static Date getDatePreOne(Date billDate) {
		Calendar ca=Calendar.getInstance();
		ca.setTime(billDate);
		ca.set(Calendar.DAY_OF_MONTH,ca.get(Calendar.DAY_OF_MONTH)-1);
		return ca.getTime();
	}
}
