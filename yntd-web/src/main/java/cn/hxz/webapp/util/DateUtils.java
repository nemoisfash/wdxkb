package cn.hxz.webapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	/**
	 * 获取当前是星期几（返回数字，前台标签用）
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return w;
	}
	
	/**
	 * 获取当前日期（年月日 星期）
	 * @param date
	 * @return
	 */
	public static String getDate() {
		int y,m,d;
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五","星期六" };
		Calendar cal=Calendar.getInstance();
		y=cal.get(Calendar.YEAR);
		m=cal.get(Calendar.MONTH)+1;
		d=cal.get(Calendar.DATE);
		Date date = new Date();
		cal.setTime(date);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1;
		if(dayOfWeek <0){
			dayOfWeek=0;
		}
		return y+"年"+m+"月"+d+"日 "+dayNames[dayOfWeek];
	}
	
	/**
	 * 获取当前日期（年月日）
	 * @param date
	 * @return
	 */
	public static String getDt() {
		int y,m,d;
		Calendar cal=Calendar.getInstance();
		y=cal.get(Calendar.YEAR);
		m=cal.get(Calendar.MONTH);
		d=cal.get(Calendar.DATE);
		return y+"年"+m+"月"+d+"日";
	}
	
	/**
	* 根据传入日期得到本月月初
	* @param calendar 传入日期
	* @return date 月末日期
	*/
	public static Date getFirstDateOfMonth(Date date) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
	
	/**
	* 根据传入日期得到本月月末，如果传入日期为月末则返回传入日期
	* @param date 传入日期
	* @return boolean true为是
	*/
	public static Date getLastDateOfMonth(Date date) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
	
	/**
	 * 
	 * @Description: 时间转字符串
	 * @Title: DatetoString
	 * @author Mzm
	 * @date 2018年9月11日
	 * @param date 时间
	 * @param str 转换格式
	 * 			<li>yyyy-MM-dd</li>
	 * 			<li>yyyy-MM-dd HH:mm:ss</li>
	 * 			<li>yyyy/MM/dd </li>
	 * @return
	 */
	public static String DateToString(Date date, String str) {
	 	SimpleDateFormat sdf = new SimpleDateFormat(str);
		return sdf.format(date);
		
	}
	
	/**
	 * 
	 * @Description: 字符串转日期
	 * @Title: StringToDate
	 * @author Mzm
	 * @date 2018年9月11日
	 * @param time
	 * @param str 格式  根据字符串日期格式
	 * @return
	 */
	public static Date StringToDate(String time, String str) {
			
	    SimpleDateFormat format = new SimpleDateFormat(str);
	    Date date = null;
	    try {
	        date = format.parse(time);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return date;
		
	}
	
	/**
	 * 	计算时差
	 * @param endDate
	 * @param nowDate
	 * @return
	 */
	public static Long getDatePoor(Date startTime, Date endTime,String type) {
	    long nd = 1000 * 24 * 60 * 60;
	    long nh = 1000 * 60 * 60;
	    long nm = 1000 * 60;
	    long diff = startTime.getTime() - endTime.getTime();
	    long num=0;
	    if(type.equalsIgnoreCase("day")){
	    	num = diff/ nd;
	    }else if(type.equalsIgnoreCase("hour")){
	    	num = diff/ nh;
	    }else if(type.equalsIgnoreCase("min")){
	    	num = diff/ nm;
	    }
	    return Math.abs(num);
	}
	
}
