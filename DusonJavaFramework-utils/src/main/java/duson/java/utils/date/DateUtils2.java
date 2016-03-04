package duson.java.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 */
public class DateUtils2 extends org.apache.commons.lang3.time.DateUtils {
	/**
	 * 获取当前时间 如:20080708231656859
	 * 
	 * @return 20080708231656859
	 */
	public static String getNowDayTimesMilliSecond() {
		StringBuffer getDayTimes = new StringBuffer();
		Calendar now = Calendar.getInstance();
		getDayTimes.append(now.get(Calendar.YEAR));
		int month = now.get(Calendar.MONTH) + 1;
		if (month < 10)
			getDayTimes.append("0" + month);
		else
			getDayTimes.append(month);
		int day = now.get(Calendar.DATE);
		if (day < 10)
			getDayTimes.append("0" + day);
		else
			getDayTimes.append(day);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		if (hour < 10)
			getDayTimes.append("0" + hour);
		else
			getDayTimes.append(hour);
		int minute = now.get(Calendar.MINUTE);
		if (minute < 10)
			getDayTimes.append("0" + minute);
		else
			getDayTimes.append(minute);
		int second = now.get(Calendar.SECOND);
		if (second < 10)
			getDayTimes.append("0" + second);
		else
			getDayTimes.append(second);
		int millisecond = now.get(Calendar.MILLISECOND);
		if (millisecond < 10)
			getDayTimes.append("00" + millisecond);
		else if (millisecond >= 10 && millisecond < 100)
			getDayTimes.append("0" + millisecond);
		else
			getDayTimes.append(millisecond);
		return getDayTimes.toString();
	}

	/**
	 * 获取当前时间 如:2008-06-25 00:00:00
	 * 
	 * @return 2008-06-25 00:00:00
	 */
	public static String getDayTimes() {
		StringBuffer getDayTimes = new StringBuffer();
		Calendar now = Calendar.getInstance();
		getDayTimes.append(now.get(Calendar.YEAR) + "-");
		int month = now.get(Calendar.MONTH) + 1;
		if (month < 10)
			getDayTimes.append("0" + month + "-");
		else
			getDayTimes.append(month + "-");
		int day = now.get(Calendar.DATE);
		if (day < 10)
			getDayTimes.append("0" + day + " ");
		else
			getDayTimes.append(day + " ");
		int hour = now.get(Calendar.HOUR_OF_DAY);
		if (hour < 10)
			getDayTimes.append("0" + hour + ":");
		else
			getDayTimes.append(hour + ":");
		int minute = now.get(Calendar.MINUTE);
		if (minute < 10)
			getDayTimes.append("0" + minute + ":");
		else
			getDayTimes.append(minute + ":");
		int second = now.get(Calendar.SECOND);
		if (second < 10)
			getDayTimes.append("0" + second);
		else
			getDayTimes.append(second);
		return getDayTimes.toString();
	}

	/**
	 * 获取当前时间 如:2008-06-25 00:00:00,000
	 * 
	 * @return 2008-06-25 00:00:00,000
	 */
	public static String getDayAllTimes() {
		StringBuffer getDayTimes = new StringBuffer();
		Calendar now = Calendar.getInstance();
		getDayTimes.append(now.get(Calendar.YEAR) + "-");
		int month = now.get(Calendar.MONTH) + 1;
		if (month < 10)
			getDayTimes.append("0" + month + "-");
		else
			getDayTimes.append(month + "-");
		int day = now.get(Calendar.DATE);
		if (day < 10)
			getDayTimes.append("0" + day + " ");
		else
			getDayTimes.append(day + " ");
		int hour = now.get(Calendar.HOUR_OF_DAY);
		if (hour < 10)
			getDayTimes.append("0" + hour + ":");
		else
			getDayTimes.append(hour + ":");
		int minute = now.get(Calendar.MINUTE);
		if (minute < 10)
			getDayTimes.append("0" + minute + ":");
		else
			getDayTimes.append(minute + ":");
		int second = now.get(Calendar.SECOND);
		if (second < 10)
			getDayTimes.append("0" + second);
		else
			getDayTimes.append(second);
		if (second < 10)
			getDayTimes.append(",00" + second);
		else if (second < 100)
			getDayTimes.append(",0" + second);
		else
			getDayTimes.append("," + second);
		return getDayTimes.toString();
	}

	/**
	 * @return 2008-06-25 00:00:00
	 */
	public static String getDayTimesForHour(int intervalHour) {
		StringBuffer getDayTimes = new StringBuffer();
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, intervalHour);
		getDayTimes.append(now.get(Calendar.YEAR) + "-");
		int month = now.get(Calendar.MONTH) + 1;
		if (month < 10)
			getDayTimes.append("0" + month + "-");
		else
			getDayTimes.append(month + "-");
		int day = now.get(Calendar.DATE);
		if (day < 10)
			getDayTimes.append("0" + day + " ");
		else
			getDayTimes.append(day + " ");
		int hour = now.get(Calendar.HOUR_OF_DAY);
		if (hour < 10)
			getDayTimes.append("0" + hour + ":");
		else
			getDayTimes.append(hour + ":");
		int minute = now.get(Calendar.MINUTE);
		if (minute < 10)
			getDayTimes.append("0" + minute + ":");
		else
			getDayTimes.append(minute + ":");
		int second = now.get(Calendar.SECOND);
		if (second < 10)
			getDayTimes.append("0" + second);
		else
			getDayTimes.append(second);
		return getDayTimes.toString();
	}

	public static String getDateTimeAddOrSubtraction(String datetime, int type, int value) {
		StringBuffer getDayTimes = new StringBuffer();
		int yearInt = Integer.valueOf(datetime.substring(0, 4)).intValue();
		int mouthInt = Integer.valueOf(datetime.substring(5, 7)).intValue();
		int dayInt = Integer.valueOf(datetime.substring(8, 10)).intValue();
		int hourInt = Integer.valueOf(datetime.substring(11, 13)).intValue();
		int minuteInt = Integer.valueOf(datetime.substring(14, 16)).intValue();
		int secondInt = Integer.valueOf(datetime.substring(17)).intValue();
		Calendar now = Calendar.getInstance();
		now.set(yearInt, mouthInt - 1, dayInt, hourInt, minuteInt, secondInt);
		if (type == Calendar.YEAR)
			now.add(Calendar.YEAR, value);
		if (type == Calendar.MONTH)
			now.add(Calendar.MONTH, value);
		if (type == Calendar.DATE)
			now.add(Calendar.DATE, value);
		if (type == Calendar.HOUR_OF_DAY)
			now.add(Calendar.HOUR_OF_DAY, value);
		if (type == Calendar.MINUTE)
			now.add(Calendar.MINUTE, value);
		if (type == Calendar.SECOND)
			now.add(Calendar.SECOND, value);
		getDayTimes.append(now.get(Calendar.YEAR) + "-");
		int month = now.get(Calendar.MONTH) + 1;
		if (month < 10)
			getDayTimes.append("0" + month + "-");
		else
			getDayTimes.append(month + "-");
		int day = now.get(Calendar.DATE);
		if (day < 10)
			getDayTimes.append("0" + day + " ");
		else
			getDayTimes.append(day + " ");
		int hour = now.get(Calendar.HOUR_OF_DAY);
		if (hour < 10)
			getDayTimes.append("0" + hour + ":");
		else
			getDayTimes.append(hour + ":");
		int minute = now.get(Calendar.MINUTE);
		if (minute < 10)
			getDayTimes.append("0" + minute + ":");
		else
			getDayTimes.append(minute + ":");
		int second = now.get(Calendar.SECOND);
		if (second < 10)
			getDayTimes.append("0" + second);
		else
			getDayTimes.append(second);
		return getDayTimes.toString();
	}

	/**
	 * 根据date取date的前一天日期
	 * 
	 * @author wds
	 * @param date
	 *            如果为空，默认取当前日期的前一天
	 * @return
	 * @date 2012-11-28
	 */
	public static String getYesterday(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		} else {
			calendar.setTime(new Date());
		}
		calendar.add(Calendar.DATE, -1);
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	/**
	 * 获取传入日期的下一天日期 yyyy-MM-dd
	 * 
	 * @author 彭彩云
	 * @param date
	 * @return [参数说明]
	 * @return String [返回类型说明]
	 */
	public static String getTomorrow(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		} else {
			calendar.setTime(new Date());
		}
		calendar.add(Calendar.DATE, 1);
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	/**
	 * 获取明天日期 yyyy-MM-dd
	 * 
	 * @author 彭彩云
	 * @return [参数说明]
	 * @return String [返回类型说明]
	 */
	public static String getTomorrow() {
		return getTomorrow(null);
	}

	/**
	 * 获取昨天日期 yyyy-MM-dd
	 * 
	 * @author 彭彩云
	 * @return [参数说明]
	 * @return String [返回类型说明]
	 */
	public static String getYesterday() {
		return getYesterday(null);
	}

	public static String getToday() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

	/**
	 * @return 20080625000000
	 */
	public static String getDayTime() {
		return getDayTimes().replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
	}

	/**
	 * @return 2008
	 */
	public static String getYear() {
		return getDays().substring(0, 4);
	}

	/**
	 * @return 2008-06
	 */
	public static String getMonth() {
		return getDays().substring(0, 7);
	}

	/**
	 * @return 2008-06-25
	 */
	public static String getDays() {
		return getDayTimes().substring(0, 10);
	}

	/**
	 * @return 20080625
	 */
	public static String getDay() {
		return getDays().replaceAll("-", "");
	}

	/**
	 * @return 00:00:00
	 */
	public static String getNowTime() {
		return getDayTimes().substring(11);
	}

	/**
	 * @param thisTime
	 *            09:00:00
	 * @param minture
	 *            60
	 * @return 10:00:00
	 */
	public static String getTimeJS(String thisTime, int minture) {
		int hour = Integer.valueOf(thisTime.substring(0, 2)).intValue();
		int min = Integer.valueOf(thisTime.substring(3, 5)).intValue() + minture;
		int sec = Integer.valueOf(thisTime.substring(6)).intValue();
		Calendar now = Calendar.getInstance();
		now.set(2010, 2, 22, hour, min, sec);
		String returnStr = "";
		if (now.get(Calendar.HOUR_OF_DAY) < 10) {
			returnStr = "0" + String.valueOf((now.get(Calendar.HOUR_OF_DAY)));
		} else {
			returnStr = String.valueOf(now.get(Calendar.HOUR_OF_DAY));
		}

		if (now.get(Calendar.MINUTE) < 10) {
			returnStr = returnStr + ":0" + String.valueOf(now.get(Calendar.MINUTE));
		} else {
			returnStr = returnStr + ":" + String.valueOf(now.get(Calendar.MINUTE));
		}
		if (now.get(Calendar.SECOND) < 10) {
			returnStr = returnStr + ":0" + String.valueOf(now.get(Calendar.SECOND));
		} else {
			returnStr = returnStr + ":" + String.valueOf(now.get(Calendar.SECOND));
		}
		return returnStr;
	}

	public static Calendar getCalendar(String date) {
		String[] strdate = date.split("-");
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(strdate[0]), Integer.parseInt(strdate[1]) - 1, Integer.parseInt(strdate[2]), 0, 0, 0);
		return cal;
	}

	/**
	 * @param date
	 *            2008-06-25
	 * @return 1 周日 ||2 周一 ||3 周二 ||4 周三 ||5 周四 ||6 周五 ||7 周六
	 */
	public static int getDayOfWeek(String date) {
		return getCalendar(date).get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * @param date
	 *            2008-06-25
	 * @return 1 周一 ||2 周二 ||3 周三 ||4 周四 ||5 周五 ||6 周六 ||7 周日
	 */
	public static int getDayOfWeekForChina(String date) {
		if (getDayOfWeek(date) - 1 == 0) {
			return 7;
		} else {
			return getDayOfWeek(date) - 1;
		}
	}

	/**
	 * @param date
	 * @return 1~31
	 */
	public static int getDayOfMonth(String date) {
		return getCalendar(date).get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @param date
	 * @return 1~366
	 */
	public static int getDayOfYear(String date) {
		return getCalendar(date).get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * @param date
	 * @return 1~5
	 */
	public static int getDayOfWeekInMonth(String date) {
		return getCalendar(date).get(Calendar.DAY_OF_WEEK_IN_MONTH);
	}

	/**
	 * @param beginDate
	 *            2008-06-25
	 * @param endDate
	 *            2008-06-26
	 * @return 1
	 */
	public static int getDayNumbCha(String beginDate, String endDate) {
		long second = getCalendar(endDate).getTime().getTime() - getCalendar(beginDate).getTime().getTime();
		Long oneDaySecond = new Long(1000 * 60 * 60 * 24);
		double dayNumbCha = second / oneDaySecond.doubleValue() + 0.001;
		return (int) dayNumbCha;
	}

	/**
	 * @param beginDate
	 *            2008-06-25
	 * @param endDate
	 *            2008-06-26
	 * @return 2
	 */
	public static int getDayNumb(String beginDate, String endDate) {
		return getDayNumbCha(beginDate, endDate) + 1;
	}

	public static String getDate(int year, int month, int day) {
		Calendar now = Calendar.getInstance();
		now.set(year, month - 1, day);
		String stryear = String.valueOf(now.get(Calendar.YEAR));
		String strmonth = String.valueOf((now.get(Calendar.MONTH) + 1));
		String strday = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
		if (now.get(Calendar.MONTH) + 1 < 10) {
			strmonth = "0" + strmonth;
		}
		if (now.get(Calendar.DAY_OF_MONTH) < 10) {
			strday = "0" + strday;
		}
		return stryear + "-" + strmonth + "-" + strday;
	}

	/**
	 * @param days
	 *            2009-04-07
	 * @param numb
	 *            2
	 * @return String 2009-04-09
	 */
	public static String getDate(String days, int numb) {
		int year = Integer.valueOf(days.substring(0, 4)).intValue();
		int mouth = Integer.valueOf(days.substring(5, 7)).intValue();
		int day = Integer.valueOf(days.substring(8)).intValue() + numb;
		return getDate(year, mouth, day);
	}

	/**
	 * @param eday
	 *            2010-06-25
	 * @param etime
	 *            20:11
	 * @param bday
	 *            2010-06-25
	 * @param btime
	 *            19:10
	 * @return 01:10
	 */
	public static String getHourMinute(String eday, String etime, String bday, String btime) {
		String[] edays = eday.split("-");
		String[] etimes = etime.split(":");
		String[] bdays = bday.split("-");
		String[] btimes = btime.split(":");
		Calendar edate = Calendar.getInstance();
		edate.set(Integer.parseInt(edays[0]), Integer.parseInt(edays[1]) - 1, Integer.parseInt(edays[2]), Integer.parseInt(etimes[0]),
				Integer.parseInt(etimes[1]), 0);
		Calendar bdate = Calendar.getInstance();
		bdate.set(Integer.parseInt(bdays[0]), Integer.parseInt(bdays[1]) - 1, Integer.parseInt(bdays[2]), Integer.parseInt(btimes[0]),
				Integer.parseInt(btimes[1]), 0);
		long second = edate.getTime().getTime() - bdate.getTime().getTime();
		long oneHourSecond = 1000 * 60 * 60;
		long oneMinuteSecond = 1000 * 60;
		long houre = second / oneHourSecond;
		long minute = second % oneHourSecond / oneMinuteSecond;
		String returnString = "";
		if (houre < 10) {
			returnString = "0" + houre + ":";
		} else {
			returnString = houre + ":";
		}
		if (minute < 10) {
			returnString += "0" + minute;
		} else {
			returnString += minute;
		}
		return returnString;
	}

	public static XMLGregorianCalendar converDateToXMLGregorianCalendar(Date date) {
		if (null == date) {
			return null;
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		try {
			DatatypeFactory factory = DatatypeFactory.newInstance();
			XMLGregorianCalendar xc = factory.newXMLGregorianCalendar(gc);
			return xc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将字符串转换为日期（包括时分秒) yyyy-MM-dd HH:mm:ss
	 * 
	 * @author 彭彩云
	 * @param dateTime
	 * @return
	 * @throws Exception
	 *             [参数说明]
	 * @exception throws [违例类型] [违例说明]
	 */
	public static Date stringConvertToDate(String dateTime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return df.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字符串转换为日期
	 * 
	 * @author 彭彩云
	 * @param dateTime
	 * @return
	 * @throws Exception
	 *             [参数说明]
	 * @exception throws [违例类型] [违例说明]
	 */
	public static Date stringConvertToDate(String dateTime, String formatter) throws Exception {
		String date = dateTime;
		if (dateTime.indexOf("/") > 0) {
			date = dateTime.replace("/", "-");
		}
		DateFormat df = new SimpleDateFormat(formatter);
		return df.parse(date);
	}

	/**
	 * 将日期类型转换为String
	 * 
	 * @author 彭彩云
	 * @param dateTime
	 * @param formatter
	 * @return
	 * @throws Exception
	 *             [参数说明]
	 * @exception throws [违例类型] [违例说明]
	 */
	public static String dateConvertToString(Date date, String formatter) {
		DateFormat df = new SimpleDateFormat(formatter);
		return df.format(date);
	}

	/**
	 * 将日期类型转换为String 格式为:yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return [参数说明]
	 * @return String
	 * @exception throws [违例类型] [违例说明]
	 */
	public static String date2String(Date date) {
		if (null == date)
			return "";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(date);
	}

	/**
	 * 获取当前年月 格式:yyyyMM
	 * 
	 * @author 彭彩云
	 * @return [参数说明]
	 * @exception throws [违例类型] [违例说明]
	 */
	public static String getCurYearAndMonth() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
		return simpleDateFormat.format(new Date());
	}

	/**
	 * 将‘2012-05-03’格式的字符串转换为java.util.Date
	 * 
	 * @param dateStr
	 * @return [参数说明]
	 * @return java.util.Date
	 * @exception throws [违例类型] [违例说明]
	 */
	public static java.util.Date convertStrToDate(String dateStr) {
		try {
			java.sql.Date date = java.sql.Date.valueOf(dateStr);
			java.util.Date result = new java.util.Date(date.getTime());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取月初时间 例如：2014-12-01 , 2014-05-01
	 * 
	 * @param year
	 * @param month
	 * @return [参数说明]
	 * @return String
	 * @exception throws [违例类型] [违例说明]
	 */
	public static String getMonthBegin(int year, int month) {
		if (month < 10) {
			return year + "-" + "0" + month + "-01";
		} else {
			return year + "-" + month + "-01";
		}
	}

	/**
	 * 根据date获取月末时间
	 * 
	 * @param date
	 * @return [参数说明]
	 * @return String
	 * @exception throws [违例类型] [违例说明]
	 */
	public static String getMonthBegin(java.util.Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String str = dateFormat.format(date);
		Integer year = Integer.parseInt(str.split("-")[0]);
		Integer month = Integer.parseInt(str.split("-")[1]);
		return DateUtils2.getMonthBegin(year, month);
	}

	/**
	 * 获取月末时间
	 * 
	 * @param year
	 * @param month
	 * @return [参数说明]
	 * @return String
	 * @exception throws [违例类型] [违例说明]
	 */
	public static String getMonthEnd(int year, int month) {
		String strY = null;
		String strZ = null;
		boolean leap = false;

		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			strZ = "31";
		}

		if (month == 4 || month == 6 || month == 9 || month == 11) {
			strZ = "30";
		}

		if (month == 2) {
			leap = leapYear(year);

			if (leap) {
				strZ = "29";
			} else {
				strZ = "28";
			}
		}

		strY = month >= 10 ? String.valueOf(month) : ("0" + month);

		return year + "-" + strY + "-" + strZ;

	}

	/**
	 * 根据date获取月末时间
	 * 
	 * @param date
	 * @return [参数说明]
	 * @return String
	 * @exception throws [违例类型] [违例说明]
	 */
	public static String getMonthEnd(java.util.Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String str = dateFormat.format(date);
		Integer year = Integer.parseInt(str.split("-")[0]);
		Integer month = Integer.parseInt(str.split("-")[1]);

		return DateUtils2.getMonthEnd(year, month);
	}

	/**
	 * 功能：判断输入年份是否为闰年<br>
	 * 
	 * @param year
	 * @return 是：true 否：false
	 * @author pure
	 */

	public static boolean leapYear(int year) {
		boolean leap;
		if (year % 4 == 0) {
			if (year % 100 == 0) {
				if (year % 400 == 0)
					leap = true;
				else
					leap = false;
			} else
				leap = true;
		} else
			leap = false;
		return leap;
	}

	/**
	 * 格式化成中文日期格式 例如：2014年12月25日
	 * 
	 * @param date
	 * @return [参数说明]
	 * @return String
	 * @exception throws [违例类型] [违例说明]
	 */
	public static String formatChinaDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String[] tep = dateFormat.format(date).split("-");
		return tep[0] + "年" + tep[1] + "月" + tep[2] + "日";
	}

	/**
	 * 只返回月和日
	 * 
	 * @param date
	 * @return [参数说明]
	 * @return String
	 * @exception throws [违例类型] [违例说明]
	 */
	public static String formatChinaDateMonthAndDay(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String[] tep = dateFormat.format(date).split("-");
		return tep[1] + "月" + tep[2] + "日";
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(DateUtil.formatChinaDate(new Date()));
		// System.out.println(getHourMinute("2015-07-07", "14:15", "2015-07-07", "12:01"));
//		Date d1 = stringConvertToDate("2015-07-07" + " 18:10", "yyyy-MM-dd HH:mm");
//		Date d2 = stringConvertToDate("2015-07-07" + " 14:01:00", "yyyy-MM-dd HH:mm");
//		long l = d1.getTime() - d2.getTime();
//		long day = l / (24 * 60 * 60 * 1000);
//		long hour = (l / (60 * 60 * 1000) - day * 24);
//		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		//System.out.println(l / (60 * 1000));
		//System.out.println(getDate("2015-08-05",-0));
		System.out.println(getDayOfWeekForChina("2015-10-11"));
	}
}
