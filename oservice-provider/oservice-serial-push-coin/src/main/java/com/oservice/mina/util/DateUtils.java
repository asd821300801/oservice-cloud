package com.oservice.mina.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * DateFormat不是线程安全的，因此通过此工具类并每个线程准备一个副本。
 * @author 
 * @mail 
 *
 */
public class DateUtils {
	private static Logger logger = LoggerFactory.getLogger(DateUtils.class);
	public final static String FULLHOURTIME = " 23:59:59";
	private final static String dateformat = "yyyy-MM-dd";
	private final static String dateNOLineformat = "yyyyMMdd";
	private final static String dateformatYyyyMMddHH = "yyyyMMddHH";
	private final static String dateHourMinuteformat = "yyyy-MM-dd HH:mm";
	private final static String dateHourMinuteSecondformat = "yyyy-MM-dd HH:mm:ss";
	private final static String dateHourMinuteSecondNOLineformat = "yyyyMMddHHmmss";

	private static final Map<String, ThreadLocal<SimpleDateFormat>> pool = new HashMap<String, ThreadLocal<SimpleDateFormat>>();
	private static final Object lock = new Object();

	public static SimpleDateFormat getDateFormat(String pattern) {
		ThreadLocal<SimpleDateFormat> tl = pool.get(pattern);
		if (tl == null) {
			synchronized (lock) {
				tl = pool.get(pattern);
				if (tl == null) {
					final String p = pattern;
					tl = new ThreadLocal<SimpleDateFormat>() {
						protected synchronized SimpleDateFormat initialValue() {
							return new SimpleDateFormat(p);
						}
					};
					pool.put(p, tl);
				}
			}
		}
		return tl.get();
	}


	/**
	 * 获得现在时间long类型
	 * @return
	 */
	public static long getNowTimeLong(){
		return System.currentTimeMillis();
	}

	/**
	 * 获得当前日期Date型
	 * @return
	 */
	public static Date getNowDate(){
		Date date = new Date();
		String dateString = getDateFormat(dateformat).format(date);
		try {
			date = getDateFormat(dateformat).parse(dateString);
		} catch (ParseException e) {
			logger.error("ParseException",e);
		}
		return date;
	}

	/**
	 * 获得当前日期long类型
	 * @return
	 */
	public static long getNowDateLong(){
		return getNowDate().getTime();
	}

	/**
	 * 获得当前日期long类型
	 * @return
	 */
	public static long getDateLong(String dateString){
		Date date = null;
		try {
			date = getDateFormat(dateformat).parse(dateString);
		} catch (ParseException e) {
			logger.error("ParseException",e);
		}
		return date.getTime();
	}

	/**
	 * 获得date and week
	 * @param beginTime
	 * @return
	 */
	public static DateWeekBean getDateWeekBean(Date beginTime){
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(beginTime);
		//添加第一天
		DateWeekBean bean = new DateWeekBean();
		bean.setDate(DateUtils.getDateString(beginTime));
		bean.setWeek(getWeekDay(calBegin.get(Calendar.DAY_OF_WEEK)));
		bean.setIspast(ispast(getDateString(beginTime)));
		return bean;
	}

	/**
	 * 获取当前日期，不带横线
	 * @return
	 */
	public static String getNowDateStringNOLine(){
		return getDateFormat(dateNOLineformat).format(new Date());
	}

	/**
	 * 获取指定日期，不带横线
	 * @return
	 */
	public static String getDateStringNOLine(Date date){
		return getDateFormat(dateNOLineformat).format(date);
	}
	public static String getDateStringYyyyMMddHH(Date date){
		return getDateFormat(dateformatYyyyMMddHH).format(date);
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getNowString(){
		return getDateFormat(dateHourMinuteSecondformat).format(new Date());
	}

	/**
	 * 获取当前时间，不带横线
	 * @return
	 */
	public static String getNowStringNOLine(){
		return getDateFormat(dateHourMinuteSecondNOLineformat).format(new Date());
	}
	/**
	 * 获取date，不带横线
	 * @return
	 */
	public static String getNowStringNOLine(Date date){
		return getDateFormat(dateHourMinuteSecondNOLineformat).format(date);
	}


	/**
	 * 获取当前日期
	 * @return
	 */
	public static String getNowDateString(){
		return getDateFormat(dateformat).format(new Date());
	}

	/**
	 * 获取时间字符串
	 * @param time
	 * @return
	 */
	public static String getTimeString(Date time){
		return getDateFormat(dateHourMinuteSecondformat).format(time);
	}

	/**
	 * 获取日期字符串
	 * @param time
	 * @return
	 */
	public static String getDateString(Date time){
		return getDateFormat(dateformat).format(time);
	}

	/**
	 * 获取日期格式yyyy-MM-dd
	 * @param time
	 * @return
	 */
	public static Date getDateDate(String time){
		Date d = null;
		try{
			d = getDateFormat(dateformat).parse(time);
		}catch (ParseException e){
			logger.error("时间格式转换错误", e);
		}
		return d;
	}

	/**
	 * 获取日期格式 yyyy-MM-dd hh:mm:ss
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static Date getTimeDate(String time){
		Date d = null;
		try{
			d = getDateFormat(dateHourMinuteSecondformat).parse(time);
		}catch (ParseException e){
			logger.error("格式转换错误",e);
		}
		return d;
	}

	/**
	 * 获取日期格式 yyyy-MM-dd hh:mm
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static Date getTimeDateNoSecond(String time){
		Date d = null;
		try{
			d = getDateFormat(dateHourMinuteformat).parse(time);
		}catch (ParseException e){
			logger.error("格式转换错误",e);
		}
		return d;
	}

	/**
	 * 获取一个月之前的日期
	 * @param time
	 * @return
	 */
	public static Date getLastMonthTime(Date time){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.add(Calendar.MONTH, -1);
		return  calendar.getTime();
	}
	/**
	 * 获取一指定天数之前的时间
	 * @param
	 * @return
	 * @throws ParseException
	 */
	public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 获取指定天数之前的时间字符串
     * @param d
     * @param day
     * @return
     */
    public static String getDateStringBefore(Date d,int day){
        Date date = getDateBefore(d,day);
        return getDateFormat(dateHourMinuteSecondformat).format(date);
    }
    
	public static Date getYesterdayTime(){
		return new Date(System.currentTimeMillis() - 24*60*60*1000);
	}

	/**
	 * 获取某天全时间，date+" 23:59:59"
	 * @param date
	 * @return
	 */
	public static String getFullDayString(String date){
		return date+FULLHOURTIME;
	}

	/**
	 * 查询一段时间内所有日期
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Date> findDates(Date beginTime, Date endTime) {
		List lDate = new ArrayList();
		lDate.add(beginTime);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(beginTime);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(endTime);
		// 测试此日期是否在指定日期之后
		while (endTime.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}

	/**
	 * 查询一段时间内所有日期和星期
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<DateWeekBean> findDatesAndWeeks(Date beginTime, Date endTime) {
		List lDate = new ArrayList();
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(beginTime);
		//添加第一天
		DateWeekBean bean = new DateWeekBean();
		bean.setDate(DateUtils.getDateString(beginTime));
		bean.setWeek(getWeekDay(calBegin.get(Calendar.DAY_OF_WEEK)));
		bean.setIspast(DateUtils.ispast(DateUtils.getDateString(beginTime)));
		lDate.add(bean);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(endTime);
		// 测试此日期是否在指定日期之后
		while (endTime.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			DateWeekBean tempBean = new DateWeekBean();
			tempBean.setDate(DateUtils.getDateString(calBegin.getTime()));
			tempBean.setWeek(getWeekDay(calBegin.get(Calendar.DAY_OF_WEEK)));
			tempBean.setIspast(DateUtils.ispast(DateUtils.getDateString(calBegin.getTime())));
			lDate.add(tempBean);
		}
		return lDate;
	}

	/**
	 * 获取传入时间的展示格式如：2015年2月第2周
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String getNextWeekDateName(){

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7);
		int year = cal.get(cal.YEAR);
		int month =cal.get(cal.MONTH)+1;
		int week =  cal.get(cal.DAY_OF_WEEK_IN_MONTH);
		return year+"年"+month+"月第"+week+"周";
	}

	/**
	 * 获得下周周一
	 * @return
	 */
	public static Date getNextWeekMonday(){
		Calendar cal = Calendar.getInstance();
		int n = 1;
		cal.add(Calendar.DATE, n*7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		return cal.getTime();
	}
	/**
	 * 获得下周周日
	 * @return
	 */
	public static Date getNextWeekSunday(){
		Calendar cal = Calendar.getInstance();
		int n = 2;
		cal.add(Calendar.DATE, n*7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		return cal.getTime();
	}
	/**
	 * 获得下周时间段
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<DateWeekBean> getNextWeekDates(){

		List<DateWeekBean> dates = new ArrayList();
		Calendar cal = Calendar.getInstance();
		int n = 1;
		cal.add(Calendar.DATE, n*7);
		for(int i=Calendar.MONDAY ;i<=Calendar.SATURDAY;i++){
			DateWeekBean bean = new DateWeekBean();
			cal.set(Calendar.DAY_OF_WEEK,i);
			bean.setDate(getDateFormat(dateformat).format(cal.getTime()));
			bean.setWeek(getWeekDay(i));
			bean.setIspast(DateUtils.ispast(getDateFormat(dateformat).format(cal.getTime())));
			dates.add(bean);
		}
		cal.add(Calendar.DATE, n*7);
		cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		DateWeekBean bean = new DateWeekBean();
		bean.setDate(getDateFormat(dateformat).format(cal.getTime()));
		bean.setWeek(getWeekDay(Calendar.SUNDAY));
		bean.setIspast(DateUtils.ispast(getDateFormat(dateformat).format(cal.getTime())));
		dates.add(bean);
		return dates;
	}

	/**
	 * 获得week day
	 * @param i
	 * @return
	 */
	public static String getWeekDay(int i){
		String day;
		switch (i){
			case 1: day = "星期日";break;
			case 2: day = "星期一";break;
			case 3: day = "星期二";break;
			case 4: day = "星期三";break;
			case 5: day = "星期四";break;
			case 6: day = "星期五";break;
			case 7: day = "星期六";break;
			default:
				day = "星期日";break;
		}
		return day;
	}

	public static boolean ispast(String date){
		long nowdate = getNowDateLong();
		long datelong = getDateLong(date);
		if(nowdate>datelong)
			return true;
		else
			return false;
	}

	public static boolean ispast(Date date,int day){
		long nowdate = System.currentTimeMillis();
		long datelong = date.getTime();
		long pastTime = day*24*60*60*1000L;
		if(nowdate>(datelong+pastTime))
			return true;
		else
			return false;
	}

	/**
	 * 通过毫秒数获取时间
	 * @return
	 */
	public static Date getDateByMills(Long mills){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(mills);
		return calendar.getTime();
	}

	public static Date getMonthBefore(Date d, int month) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) - 1*month);
        return now.getTime();
    }
	
	public static Date getWeekBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - 7*day);
        return now.getTime();
    }
	
	public static String format(String pattern, Date date) {
		return getDateFormat(pattern).format(date);
	}

	public static Date parse(String pattern, String date) throws ParseException {
		return getDateFormat(pattern).parse(date);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void dd(List l){
		List l2 = new ArrayList();
		l2 .addAll(l.subList(1,2)) ;
		l.clear();
		l.addAll(l2);
	}
	
	//add
	public static Date getHourBefore(Date d, int hour) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.HOUR, now.get(Calendar.HOUR) - 1*hour);
        return now.getTime();
    }

	/**
	 * 获取指定时间和现在相差分钟数
	 *
	 * @param beforeDate
	 * @return
	 */
	public static int minuteBetween(Date beforeDate) {
		Calendar cal = Calendar.getInstance();
		long time1 = cal.getTimeInMillis();
		cal.setTime(beforeDate);
		long time2 = cal.getTimeInMillis();
		long between_hours = (time1 - time2) / (1000 * 60);
		return Integer.parseInt(String.valueOf(between_hours));
	}
}
