package com.none.core.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.none.core.exception.ValidateException;

/**
 * Title: 日期操作工具类
 */
public class DateUtil {
	// 一天的毫秒数
	public static final long DAY_MILLI = 24 * 60 * 60 * 1000;

	/**
	 * 要用到的DateFormat的定义
	 */
	public static String DATE_FORMAT_EN_DATEONLY = "yyyy-MM-dd";
	public static String DATE_FORMAT_CN_DATEONLY = "yyyy年MM月dd日";
	public static String DATE_FORMAT_EN_DATETIME = "yyyy-MM-dd HH:mm";
	public static String DATE_FORMAT_CN_DATETIME = "yyyy年MM月dd日 HH:mm";
	public static String DATE_FORMAT_EN_DATESECS = "yyyy-MM-dd HH:mm:ss";
	public static String DATE_FORMAT_CN_DATESECS = "yyyy年MM月dd日 HH:mm:ss";
	public static String DATE_FORMAT_EN_DATEMSEL = "yyyy-MM-dd HH:mm:ss:SS";
	public static String DATE_FORMAT_CN_DATEMSEL = "yyyy年MM月dd日 HH:mm:ss:SS";

	/**
	 * 格式：yyyy-MM-dd
	 */
	private static SimpleDateFormat sdfEnDateOnly = new SimpleDateFormat(DateUtil.DATE_FORMAT_EN_DATEONLY);
	private static SimpleDateFormat sdfCnDateOnly = new SimpleDateFormat(DateUtil.DATE_FORMAT_CN_DATEONLY);
	private static SimpleDateFormat sdfEnDateTime = new SimpleDateFormat(DateUtil.DATE_FORMAT_EN_DATETIME);
	private static SimpleDateFormat sdfCnDateTime = new SimpleDateFormat(DateUtil.DATE_FORMAT_CN_DATETIME);
	private static SimpleDateFormat sdfEnDateSecs = new SimpleDateFormat(DateUtil.DATE_FORMAT_EN_DATESECS);
	private static SimpleDateFormat sdfCnDateSecs = new SimpleDateFormat(DateUtil.DATE_FORMAT_CN_DATESECS);
	private static SimpleDateFormat sdfEnDateMsel = new SimpleDateFormat(DateUtil.DATE_FORMAT_EN_DATEMSEL);
	private static SimpleDateFormat sdfCnDateMsel = new SimpleDateFormat(DateUtil.DATE_FORMAT_CN_DATEMSEL);

	public static String formatCurrentDateTime(String formatStr) {
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		return formatter.format(currentDate);
	}

	public static String formatDateTime(Date date, String formatStr) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		return formatter.format(date);
	}

	public static String getQuarterStar() {
		String strMonth = "";
		String strYear = "";
		int month = 0;
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		strYear = formatter.format(currentDate);
		formatter = new SimpleDateFormat("MM");
		strMonth = formatter.format(currentDate);
		month = StringUtil.toInteger(strMonth).intValue();
		if (month < 4) {
			return strYear + "-01";
		} else if (month < 7) {
			return strYear + "-04";
		} else if (month < 9) {
			return strYear + "-07";
		} else {
			return strYear + "-10";
		}
	}

	public static String getQuarterEnd() {
		String strMonth = "";
		String strYear = "";
		int month = 0;
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		strYear = formatter.format(currentDate);
		formatter = new SimpleDateFormat("MM");
		strMonth = formatter.format(currentDate);
		month = StringUtil.toInteger(strMonth).intValue();
		if (month < 4) {
			return strYear + "-03";
		} else if (month < 7) {
			return strYear + "-06";
		} else if (month < 9) {
			return strYear + "-09";
		} else {
			return strYear + "-12";
		}
	}

	/**
	 * 获取系统当前年的数据字符串
	 * 
	 * @return String 类型，格式 yyyy
	 */
	public static String getYear() {
		String strYear = "";
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		strYear = formatter.format(currentDate);
		return strYear;
	}

	public static String getQuarter() {
		String strQuarter = "";
		String strMonth = getMonth();
		int month = StringUtil.toInteger(strMonth).intValue();
		if (month < 4) {
			strQuarter = "1";
		} else if (month < 7) {
			strQuarter = "2";
		} else if (month < 10) {
			strQuarter = "3";
		} else {
			strQuarter = "4";
		}
		return strQuarter;
	}

	public static String getMonth() {
		String strMonty = "";
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		strMonty = formatter.format(currentDate);
		return strMonty;
	}

	/**
	 * 获取系统当前年月的数据字符串
	 * 
	 * @return String 类型，格式 yyyy-MM
	 */
	public static String getYearMonth() {
		String strYearMonth = "";
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		strYearMonth = formatter.format(currentDate);
		return strYearMonth;
	}

	/**
	 * 获取系统当前日期的前一个年月的数据字符串
	 * 
	 * @return String 类型，格式 yyyyMM
	 */
	public static String getPreYearMonth() {
		String strYearMonth = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		// 取得现在时间
		calendar.setTime(new java.util.Date());
		// 取得上一个时间
		calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
		strYearMonth = formatter.format(calendar.getTime());
		return strYearMonth;
	}

	/**
	 * 获取系统当前年月日的数据字符串
	 * 
	 * @return String 类型，格式 yyyy-MM-dd
	 */
	public static String getDate() {
		return DateUtil.getYearMonthDay();
	}

	/**
	 * 获取系统当前时分秒的数据字符串
	 * 
	 * @return String 类型，格式 HH:mm:ss
	 */
	public static String getTime() {
		String strCurrentTime = "";
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		strCurrentTime = formatter.format(currentTime);
		return strCurrentTime;
	}

	/**
	 * 获取系统当前时分秒毫秒的数据字符串
	 * 
	 * @return String 类型，格式 HH:mm:ss:SS
	 */
	public static String getTimes() {
		String strCurrentTime = "";
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SS");
		strCurrentTime = formatter.format(currentTime);
		return strCurrentTime;
	}

	/**
	 * 获取系统当前年月日时分秒的数据字符串
	 * 
	 * @return String 类型，格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getDateTime() {
		return DateUtil.getYearMonthDayHMS();
	}

	/**
	 * 获取系统当前年月日的数据字符串
	 * 
	 * @return String 类型，格式 yyyy-MM-dd
	 */
	public static String getYearMonthDay() {
		String strCurrentDate = "";
		Date currentDate = new Date();
		strCurrentDate = sdfEnDateOnly.format(currentDate);
		return strCurrentDate;
	}

	/**
	 * 获取系统当前中文年月日的数据字符串
	 * 
	 * @return String 类型，格式 yyyy年MM月dd日
	 */
	public static String getYearMonthDayCn() {
		String strYearMonth = "";
		Date currentDate = new Date();
		strYearMonth = sdfCnDateOnly.format(currentDate);
		return strYearMonth;
	}

	/**
	 * 获取系统当前年月日时分的数据字符串
	 * 
	 * @return String 类型，格式 yyyy-MM-dd HH:mm
	 */
	public static String getYearMonthDayHM() {
		String strYearMonth = "";
		Date currentDate = new Date();
		strYearMonth = sdfEnDateTime.format(currentDate);
		return strYearMonth;
	}

	/**
	 * 获取系统当前中文年月日时分的数据字符串
	 * 
	 * @return String 类型，格式 yyyy年MM月dd日 HH:mm
	 */
	public static String getYearMonthDayHMCn() {
		String strYearMonth = "";
		Date currentDate = new Date();
		strYearMonth = sdfCnDateTime.format(currentDate);
		return strYearMonth;
	}

	/**
	 * 获取系统当前年月日时分秒的数据字符串
	 * 
	 * @return String 类型，格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getYearMonthDayHMS() {
		String strYearMonth = "";
		Date currentDate = new Date();
		strYearMonth = sdfEnDateSecs.format(currentDate);
		return strYearMonth;
	}

	/**
	 * 获取系统当前中文年月日时分秒的数据字符串
	 * 
	 * @return String 类型，格式 yyyy年MM月dd日 HH:mm:ss
	 */
	public static String getYearMonthDayHMSCn() {
		String strYearMonth = "";
		Date currentDate = new Date();
		strYearMonth = sdfCnDateSecs.format(currentDate);
		return strYearMonth;
	}

	/**
	 * 获取系统当前年月日时分秒毫秒的数据字符串
	 * 
	 * @return String 类型，格式 yyyy-MM-dd HH:mm:ss:SS
	 */
	public static String getYearMonthDayHMSS() {
		String strYearMonth = "";
		Date currentDate = new Date();
		strYearMonth = sdfEnDateMsel.format(currentDate);
		return strYearMonth;
	}

	/**
	 * 获取系统当前中文年月日时分秒毫秒的数据字符串
	 * 
	 * @return String 类型，格式 yyyy年MM月dd日 HH:mm:ss:SS
	 */
	public static String getYearMonthDayHMSSCn() {
		String strYearMonth = "";
		Date currentDate = new Date();
		strYearMonth = sdfCnDateMsel.format(currentDate);
		return strYearMonth;
	}

	/**
	 * 获取系统当前年月的数据字符串
	 * 
	 * @return String 类型，格式 yyyyMM
	 */
	public static String getYM() {
		String strYMD = "";
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		strYMD = formatter.format(currentTime);
		return strYMD;
	}

	/**
	 * 获取系统当前年月日的数据字符串
	 * 
	 * @return String 类型，格式 yyyyMMdd
	 */
	public static String getYMD() {
		String strYMD = "";
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		strYMD = formatter.format(currentTime);
		return strYMD;
	}

	public static String getH() {
		String strYMD = "";
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		strYMD = formatter.format(currentTime);
		return strYMD;
	}

	/**
	 * 获取系统当前时分秒的数据字符串
	 * 
	 * @return String 类型，格式 HHmmss
	 */
	public static String getHMS() {
		String strHMS = "";
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
		strHMS = formatter.format(currentTime);
		return strHMS;
	}

	/**
	 * 获取系统当前年月日时分的数据字符串
	 * 
	 * @return String 类型，格式 yyyyMMddHHmm
	 */
	public static String getYMDHM() {
		String strYMDHM = "";
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		strYMDHM = formatter.format(currentTime);
		return strYMDHM;
	}

	/**
	 * 获取系统当前年月日时分秒的数据字符串
	 * 
	 * @return String 类型，格式 yyyyMMddHHmmss
	 */
	public static String getYMDHMS() {
		String strYMDHMS = "";
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		strYMDHMS = formatter.format(currentTime);
		return strYMDHMS;
	}

	/**
	 * 获取系统当前年月日时分秒毫秒的数据字符串
	 * 
	 * @return String 类型，格式 yyyyMMddHHmmssSS
	 */
	public static String getYMDHMSS() {
		String strYMDHMSS = "";
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSS");
		strYMDHMSS = formatter.format(currentTime);
		return strYMDHMSS;
	}

	/**
	 * 日期格式转换<br>
	 * yyyy-MM-dd 到 yyyyMMdd<br>
	 * yyyy年MM月dd日 到 yyyyMMdd<br>
	 * yyyy-MM-dd HH:mm:ss 到 yyyyMMddHHmmss<br>
	 * yyyy年MM月dd日 HH:mm:ss 到 yyyyMMddHHmmss<br>
	 * yyyy-MM-dd HH:mm:ss:SS 到 yyyyMMddHHmmssSS<br>
	 * yyyy年MM月dd日 HH:mm:ss:SS 到 yyyyMMddHHmmssSS
	 * 
	 * @param strDateTime
	 *            日期格式为<br>
	 *            yyyy-MM-dd<br>
	 *            yyyy年MM月dd日<br>
	 *            yyyy-MM-dd HH:mm:ss<br>
	 *            yyyy年MM月dd日 HH:mm:ss<br>
	 *            yyyy-MM-dd HH:mm:ss:SS<br>
	 *            yyyy年MM月dd日 HH:mm:ss:SS
	 * @return String 类型 格式 yyyyMMdd或yyyyMMddHHmmss或yyyyMMddHHmmssSS
	 */
	public static String stringToNumber(String strDateTime) {
		String strYMDHMS = "";
		if (StringUtil.toString(strDateTime).length() == 10) {
			strYMDHMS = strDateTime.substring(0, 4) + strDateTime.substring(5, 7) + strDateTime.substring(8, 10);
		}
		if (StringUtil.toString(strDateTime).length() == 19) {
			strYMDHMS = strDateTime.substring(0, 4) + strDateTime.substring(5, 7) + strDateTime.substring(8, 10)
					+ strDateTime.substring(11, 13) + strDateTime.substring(14, 16) + strDateTime.substring(17, 19);
		}
		if (StringUtil.toString(strDateTime).length() == 20) {
			strYMDHMS = strDateTime.substring(0, 4) + strDateTime.substring(5, 7) + strDateTime.substring(8, 10)
					+ strDateTime.substring(12, 14) + strDateTime.substring(15, 17) + strDateTime.substring(18, 20);
		}
		if (StringUtil.toString(strDateTime).length() > 20) {
			if (strDateTime.indexOf("-") > 0) {
				strYMDHMS = strDateTime.substring(0, 4) + strDateTime.substring(5, 7) + strDateTime.substring(8, 10)
						+ strDateTime.substring(11, 13) + strDateTime.substring(14, 16) + strDateTime.substring(17, 19)
						+ strDateTime.substring(20);
			} else {
				strYMDHMS = strDateTime.substring(0, 4) + strDateTime.substring(5, 7) + strDateTime.substring(8, 10)
						+ strDateTime.substring(12, 14) + strDateTime.substring(15, 17) + strDateTime.substring(18, 20)
						+ strDateTime.substring(21);
			}
		}
		return strYMDHMS;
	}

	/**
	 * 日期格式转换<br>
	 * yyyyMMdd 到 yyyy-MM-dd<br>
	 * yyyyMMdd 到 yyyy年MM月dd日<br>
	 * yyyyMMddHHmmss 到 yyyy-MM-dd HH:mm:ss<br>
	 * yyyyMMddHHmmss 到 yyyy年MM月dd日 HH:mm:ss<br>
	 * yyyyMMddHHmmssSS 到 yyyy-MM-dd HH:mm:ss:SS<br>
	 * yyyyMMddHHmmssSS 到 yyyy年MM月dd日 HH:mm:ss:SS
	 * 
	 * @param strNumber
	 *            字符串数字格式为<br>
	 *            yyyyMMdd<br>
	 *            yyyyMMddHHmmss<br>
	 *            yyyyMMddHHmmssSS
	 * @param sign
	 *            转换格式yyyy-MM-dd(sign=1)或yyyy年MM月dd日(sign=2)或yyyy/MM/dd(sign=3)
	 * @return String 类型 格式<br>
	 *         yyyy-MM-dd<br>
	 *         yyyy年MM月dd日<br>
	 *         yyyy-MM-dd HH:mm:ss<br>
	 *         yyyy年MM月dd日 HH:mm:ss<br>
	 *         yyyy-MM-dd HH:mm:ss:SS<br>
	 *         yyyy年MM月dd日 HH:mm:ss:SS
	 */
	public static String numberToString(String strNumber, int sign) {
		String strDateTime = "";
		if (StringUtil.toString(strNumber).length() == 8) {
			if (sign == 2) {
				strDateTime = strNumber.substring(0, 4) + "年" + strNumber.substring(4, 6) + "月"
						+ strNumber.substring(6, 8) + "日";
			} else if (sign == 3) {
				strDateTime = strNumber.substring(0, 4) + "/" + strNumber.substring(4, 6) + "/"
						+ strNumber.substring(6, 8);
			} else {
				strDateTime = strNumber.substring(0, 4) + "-" + strNumber.substring(4, 6) + "-"
						+ strNumber.substring(6, 8);
			}
		}
		if (StringUtil.toString(strNumber).length() == 14) {
			if (sign == 2) {
				strDateTime = strNumber.substring(0, 4) + "年" + strNumber.substring(4, 6) + "月"
						+ strNumber.substring(6, 8) + "日 " + strNumber.substring(8, 10) + ":"
						+ strNumber.substring(10, 12) + ":" + strNumber.substring(12, 14);
			} else if (sign == 3) {
				strDateTime = strNumber.substring(0, 4) + "/" + strNumber.substring(4, 6) + "/"
						+ strNumber.substring(6, 8) + " " + strNumber.substring(8, 10) + ":"
						+ strNumber.substring(10, 12) + ":" + strNumber.substring(12, 14);
			} else {
				strDateTime = strNumber.substring(0, 4) + "-" + strNumber.substring(4, 6) + "-"
						+ strNumber.substring(6, 8) + " " + strNumber.substring(8, 10) + ":"
						+ strNumber.substring(10, 12) + ":" + strNumber.substring(12, 14);
			}
		}
		if (StringUtil.toString(strNumber).length() > 14) {
			if (sign == 2) {
				strDateTime = strNumber.substring(0, 4) + "年" + strNumber.substring(4, 6) + "月"
						+ strNumber.substring(6, 8) + "日 " + strNumber.substring(8, 10) + ":"
						+ strNumber.substring(10, 12) + ":" + strNumber.substring(12, 14) + ":"
						+ strNumber.substring(14);
			} else if (sign == 3) {
				strDateTime = strNumber.substring(0, 4) + "/" + strNumber.substring(4, 6) + "/"
						+ strNumber.substring(6, 8) + " " + strNumber.substring(8, 10) + ":"
						+ strNumber.substring(10, 12) + ":" + strNumber.substring(12, 14) + ":"
						+ strNumber.substring(14);
			} else {
				strDateTime = strNumber.substring(0, 4) + "-" + strNumber.substring(4, 6) + "-"
						+ strNumber.substring(6, 8) + " " + strNumber.substring(8, 10) + ":"
						+ strNumber.substring(10, 12) + ":" + strNumber.substring(12, 14) + ":"
						+ strNumber.substring(14);
			}
		}
		return strDateTime;
	}

	/**
	 * 得到对比日期变化的目标日期 getDateChange("2007-08-10",1)="2007-08-11";
	 * 
	 * @param strDate
	 *            日期格式 "2000-08-10"
	 * @param iQuantity
	 *            变化的数量 以天为单位
	 * @return String 类型，格式 "2007-08-11"
	 */
	public static String getDateChange(String strDate, int iQuantity) {
		String strTarget = "";
		int iYear = Integer.parseInt(strDate.substring(0, 4));
		int iMonth = Integer.parseInt(strDate.substring(5, 7));
		int iDay = Integer.parseInt(strDate.substring(8, 10));

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, iYear);
		cal.set(Calendar.MONTH, iMonth - 1);
		cal.set(Calendar.DAY_OF_MONTH, iDay);
		cal.add(Calendar.DATE, iQuantity);
		Date currentDate = cal.getTime();
		strTarget = sdfEnDateOnly.format(currentDate);
		return strTarget;
	}

	/**
	 * 得到对比日期变化的目标日期包含时间 getDateTimeChange("2007-08-10 15:23:12",1)="2007-08-11
	 * 15:23:12";
	 * 
	 * @param strCurrentTime
	 *            当前日期 格式 "2007-08-10 15:23:12"
	 * @param iQuantity
	 *            变化的数量 以天为单位
	 * @return String 类型，格式 "2007-08-11 15:23:12"
	 */
	public static String getDateTimeChange(String strDateTimeTime, int iQuantity) {
		String strTarget = "";
		String strHHMMSS = strDateTimeTime.substring(10, 19);
		int iYear = Integer.parseInt(strDateTimeTime.substring(0, 4));
		int iMonth = Integer.parseInt(strDateTimeTime.substring(5, 7));
		int iDay = Integer.parseInt(strDateTimeTime.substring(8, 10));

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, iYear);
		cal.set(Calendar.MONTH, iMonth - 1);
		cal.set(Calendar.DAY_OF_MONTH, iDay);
		cal.add(Calendar.DATE, iQuantity);
		Date currentDate = cal.getTime();
		strTarget = sdfEnDateOnly.format(currentDate);
		strTarget = strTarget + strHHMMSS;
		return strTarget;
	}

	/**
	 * Parse date like "yyyy-MM-dd".
	 */
	public static Date parseDateOnly(String d) {
		try {
			if (StringUtil.isNotEmpty(d)) {
				return new SimpleDateFormat(DATE_FORMAT_EN_DATEONLY).parse(d);
			}
		} catch (ParseException e) {

		}
		return null;
	}

	/**
	 * Parse date and time like "yyyy-MM-dd HH:mm".
	 */
	public static Date parseDateTime(String dt) {
		try {
			return new SimpleDateFormat(DATE_FORMAT_EN_DATETIME).parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parseDateTimes(String dt) {
		try {
			if ("".equals(dt) || null == dt) {
				return null;
			}
			return new SimpleDateFormat(DATE_FORMAT_EN_DATESECS).parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parseDate(String df, String dt) {
		try {
			return new SimpleDateFormat(df).parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取上月最后一天 严玉成 2013-11-12
	 * 
	 * @param date
	 *            yyyy-MM
	 * @return yyyy-MM-dd
	 */
	public static String lastDayOfMonth(String date) {
		Date dat = null;
		try {
			dat = new SimpleDateFormat("yyyy-MM").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.roll(Calendar.DAY_OF_MONTH, -1);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}

	/**
	 * 获取上月 严玉成 2013-11-12
	 */
	public static String preYearMonth(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

		Date dt = null;
		try {
			dt = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.YEAR, 0);
		rightNow.add(Calendar.MONTH, -1);
		Date dt1 = rightNow.getTime();
		return sdf.format(dt1);
	}

	/**
	 * 日期转换 将yy-MM-dd 转换成 yyyy-mm-dd 严玉成 2013-11-21
	 * 
	 * @param args
	 */
	public static String dateChange(String str) {
		String y = "yy-MM-dd";
		String x = "yyyy-MM-dd";
		SimpleDateFormat format1 = new SimpleDateFormat(y);
		SimpleDateFormat format = new SimpleDateFormat(x);
		try {
			format1.parse(str);// 这个是按照y的格式，将s变成一个Date对象
			return format.format(format1.parse(str));// 按照X的格式,输出Date对象的值
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 获取当月第一天
	 * 
	 * @author ycyan
	 * @创建时间：2014-4-8 下午2:28:32
	 * @param args
	 */
	public static String getFirstDay() {
		// 获取前月的第一天
		Calendar cal_1 = Calendar.getInstance();// 获取当前日期
		cal_1.add(Calendar.MONTH, 0);
		cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String firstDay = format.format(cal_1.getTime());
		return firstDay;

	}

	/**
	 * 获取当月最后一天
	 * 
	 * @author ycyan
	 * @创建时间：2014-4-8 下午2:28:32
	 * @param args
	 */
	public static String getLastMonthDay() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String last = format.format(ca.getTime());
		return last;
	}

	/**
	 * 获取某年第一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */

	public static String getCurrYearFirst() {
		Calendar calendar = Calendar.getInstance();
		int cur_year = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(Calendar.YEAR, cur_year);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currYearFirst = format.format(calendar.getTime());
		return currYearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static String getCurrYearLast() {
		Calendar calendar = Calendar.getInstance();
		int cur_year = calendar.get(Calendar.YEAR);
		calendar.clear();
		calendar.set(Calendar.YEAR, cur_year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currYearlast = format.format(calendar.getTime());
		return currYearlast;
	}

	/**
	 * 将yyyy-mm-dd 转换成 yyyy年mm月dd日
	 */
	public static String numberToStringCN(String strNumber) {
		String strDateTime = "";
		strDateTime = strNumber.substring(0, 4) + "年" + strNumber.substring(5, 7) + "月" + strNumber.substring(8, 10)
				+ "日";
		return strDateTime;
	}

	/**
	 * 获取当前日期所在周的第一天（国外）
	 * 
	 * @return
	 */
	public static String getFirstDayOfweek() {
		Calendar c = Calendar.getInstance();
		int weekday = c.get(7) - 1;
		c.add(5, -weekday);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String firstDayOfweek = format.format(c.getTime());
		return firstDayOfweek;
	}

	/**
	 * 获取当前日期所在周的最后一天（国外）
	 * 
	 * @return
	 */
	public static String getlastDayOfweek() {
		Calendar c = Calendar.getInstance();
		int weekday = c.get(7) - 1;
		c.add(5, -weekday);
		c.add(5, 6);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfweek = format.format(c.getTime());
		return lastDayOfweek;
	}

	/**
	 * 计算两个日期相差的天数
	 * 
	 * @param smdate
	 * @return
	 */
	public static String daysBetween(String smdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(smdate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long time1 = cal.getTimeInMillis();

		long time2 = new Date().getTime();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return String.valueOf(between_days);
	}

	public static Timestamp stringToTimestamp(String date) throws ParseException {
		Format f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = (Date) f.parseObject(date);
		Timestamp ts = new Timestamp(d.getTime());
		return ts;
	}

	public static Timestamp stringToTimestamp(String date, String format) throws ParseException {
		Format f = new SimpleDateFormat(format);
		Date d = (Date) f.parseObject(date);
		Timestamp ts = new Timestamp(d.getTime());
		return ts;
	}

	public static String remainDays(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long time1 = cal.getTimeInMillis();

		long time2 = new Date().getTime();
		long between_days = (time1 - time2) / (1000 * 3600 * 24) + 1;

		return String.valueOf(between_days);
	}

	/**
	 * 将日期转换为 11:50 02 Aug 2011
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(String date) {
		Locale l = new Locale("en");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatDate = "";
		try {
			Date date_temp = sdf.parse(date);
			String day = String.format("%td", date_temp);
			String month = String.format(l, "%tb", date_temp);
			String year = String.format("%tY", date_temp);
			String hourMin = String.format("%tH", date_temp) + ":" + String.format("%tM", date_temp);
			formatDate = hourMin + ", " + day + " " + month + " " + year;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatDate;
	}

	/**
	 * 将日期转换为 Aug 2011
	 * 
	 * @param date
	 * @param type
	 *            MY（like Jun 2016） 、DMY（like 03 Jun 2016）
	 * @return
	 */
	public static String formatDate_en(String date, String type) {
		Locale l = new Locale("en");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String formatDate = "";
		try {
			Date date_temp = sdf.parse(date);
			String day = String.format("%td", date_temp);
			String month = String.format(l, "%tb", date_temp);
			String year = String.format("%tY", date_temp);
			if ("MY".equals(type)) {
				formatDate = month + " " + year;
			}
			if ("DMY".equals(type)) {
				formatDate = day + " " + month + " " + year;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatDate;
	}

	/**
	 * 获取两个日期之间所有的星期天
	 * 
	 * @param dBegin
	 * @param dEnd
	 * @return
	 */
	public static List<String> findSunday(Date dBegin, Date dEnd) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> listDate = new ArrayList<String>();
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		calBegin.add(Calendar.DAY_OF_MONTH, -1);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			int dayofweek = calBegin.get(Calendar.DAY_OF_WEEK) - 1;
			// 只取星期天
			if (dayofweek == 0) {
				listDate.add(sdf.format(calBegin.getTime()));
			}
		}
		Collections.sort(listDate);
		return listDate;
	}

	/**
	 * 获取一个指定日期向前推几天的日期
	 * 
	 * @param date
	 *            指定的日期 variable 向前或向后推的天数
	 */
	public static String getBeforeSomeDay(String date, int variable) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calBegin = Calendar.getInstance();
		try {
			calBegin.setTime(sdf.parse(date));
			calBegin.add(Calendar.DAY_OF_MONTH, variable);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(calBegin.getTime());
	}

	/**
	 * 将yyyyMMdd转换成yyyy-MM-dd
	 * 
	 * @param date
	 *            指定的日期
	 */
	public static long toYYYY_MM_DD(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + " 00:00:00";
		Date dates = new Date();
		try {
			dates = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dates.getTime();
	}

	/**
	 * 判断一个日期是否在一个日期区间内
	 * 
	 * @param date
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean dateBetween(String date, String startDate, String endDate) {
		boolean isBefore = true;
		boolean isAfter = true;
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		date = date.substring(4, 6) + "-" + date.substring(6, 8);
		Date someDate = new Date();
		Date startDate_temp = new Date();
		Date endDate_temp = new Date();
		try {
			someDate = sdf.parse(date);
			startDate_temp = sdf.parse(startDate);
			endDate_temp = sdf.parse(endDate);

			isBefore = someDate.before(startDate_temp);
			isAfter = someDate.after(endDate_temp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (!isBefore && !isAfter) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断一个日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getFirstDayOfWeek(String date) {
		String firstDayOfWeek = DateUtil.getDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Date someDate = new Date();
		try {
			someDate = sdf.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(someDate);
			// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
			int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
			if (1 == dayWeek) {
				cal.add(Calendar.DAY_OF_MONTH, -1);
			}
			cal.setFirstDayOfWeek(Calendar.SUNDAY);// 设置一个星期的第一天，按国外的习惯一个星期的第一天是星期天
			int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
			cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
			firstDayOfWeek = sdf.format(cal.getTime());
			// System.out.println("所在周第一天："+imptimeBegin);
			// cal.add(Calendar.DATE, 6);
			// String imptimeEnd = sdf.format(cal.getTime());
			// System.out.println("所在周最后一天："+imptimeEnd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return firstDayOfWeek;
	}

	public static String dateToString(Date date) throws ParseException {
		Format f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String d = f.format(date);
		return d;
	}

	/**
	 * 时间戳转化为Sting
	 * 
	 * @param times
	 * @return
	 * @throws ParseException
	 */
	public static String timeStampToDate(String times) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(Long.valueOf(times + "000")));
	}

	/**
	 * 时间戳转化为Sting
	 * 
	 * @param times
	 * @param formatString
	 * @return
	 * @throws ParseException
	 */
	public static String timeStampToDate(String times, String formatString) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		return format.format(new Date(Long.valueOf(times)));
	}

	/**
	 * 时间戳转化为Sting
	 * 
	 * @param times
	 * @return
	 * @throws ParseException
	 */
	public static String timeStampToDate3(String times) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		return format.format(new Date(Long.valueOf(times)));
	}

	/**
	 * <pre>
	 * 
	 * 根据指定的日期字符串获取星期几
	 * </pre>
	 * 
	 * @param strDate
	 *            时间戳
	 * @return week
	 *         星期几(MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY,SUNDAY)
	 */
	public static String getWeekByDateStr(String strDate) {
		int year = Integer.parseInt(strDate.substring(0, 4));
		int month = Integer.parseInt(strDate.substring(5, 7));
		int day = Integer.parseInt(strDate.substring(8, 10));

		Calendar c = Calendar.getInstance();

		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, day);

		String week = "";
		int weekIndex = c.get(Calendar.DAY_OF_WEEK);

		switch (weekIndex) {
		case 1:
			week = "7";
			break;
		case 2:
			week = "1";
			break;
		case 3:
			week = "2";
			break;
		case 4:
			week = "3";
			break;
		case 5:
			week = "4";
			break;
		case 6:
			week = "5";
			break;
		case 7:
			week = "6";
			break;
		}
		return week;
	}

	/**
	 * string to Timestamp
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Timestamp stringToTimestamp2(String date) throws ParseException {
		Format f = new SimpleDateFormat("yyyy-MM-dd");
		Date d = (Date) f.parseObject(date);
		Timestamp ts = new Timestamp(d.getTime());
		return ts;
	}

	/**
	 * 根据管理员上班时间获取预约取书时间
	 * 
	 * @param workDay
	 *            周几工作，用逗号隔开
	 * @param startTime
	 *            工作开始时间点
	 * @param endTime
	 *            工作结束时间点
	 * @param currentDate当前时间（点击借书时的时间）
	 * @return
	 * @throws ParseException
	 */
	public static List<Timestamp> getPickUpDate(String workDay, String startTime, String endTime, String currentDate) {
		List<Timestamp> list = new ArrayList<Timestamp>();
		// try {
		// System.out.println(timeStampToDate2(currentDate+"000"));
		// } catch (ParseException e1) {
		// e1.printStackTrace();
		// }
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long currentTimeApp = Long.valueOf(currentDate + "000");

		long currentTimeServer = new Date().getTime();
		// 如果手机传过来的时间小于当前服务器时间，则取服务器时间；这是为了防止手机时间是错误的，比如今天是2015年10月10日，手机的时间还是2015年10月08日，这样取的时间就是错误的
		if (currentTimeApp < currentTimeServer) {
			currentTimeApp = currentTimeServer;
		}
		String currentDate_ = format.format(new Date(Long.valueOf(currentTimeApp)));
		String currentDate_ymd = currentDate_.substring(0, 10);

		// 获取endTime_的小时
		int endTime_ = Integer.parseInt(endTime.substring(0, 2));
		// 获取currentDate的小时
		int hour = Integer.parseInt(currentDate_.substring(11, 13));
		String[] workDay_ = workDay.split(",");
		// 获取当前日期往后的50天
		for (int i = 0; i < 50; i++) {
			String temp = getBeforeSomeDay(currentDate_, i);
			// 计算出来日期是星期几
			Integer some_workday_;
			try {
				some_workday_ = Integer.parseInt(getWeekByDateStr(stringToTimestamp2(temp).toString()));
				for (int j = 0; j < workDay_.length; j++) {
					// 管理员上班的星期几
					Integer some_workday = Integer.parseInt(workDay_[j]);
					if (some_workday_ == some_workday) {
						if (currentDate_ymd.equals(temp)) {
							if (hour < endTime_) {
								list.add(stringToTimestamp2(temp));
								// System.out.println(stringToTimestamp2(temp)+"---"+some_workday_);
							}
						} else {
							list.add(stringToTimestamp2(temp));
							// System.out.println(stringToTimestamp2(temp)+"---"+some_workday_);
						}
					}
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 只取四个日期
			if (list.size() == 4) {
				break;
			}
		}
		return list;
	}

	/**
	 * 获取一个指定日期向前推几天的日期
	 * 
	 * @param date
	 *            指定的日期 variable 向前或向后推的天数
	 */
	public static String getBeforeSomeDayYYMMDDHHMMSS(String date, int variable) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calBegin = Calendar.getInstance();
		try {
			calBegin.setTime(sdf.parse(date));
			calBegin.add(Calendar.DAY_OF_MONTH, variable);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(calBegin.getTime());
	}

	/**
	 * 获取一个指定时间向后推几分钟的日期
	 * 
	 * @param currentEndtime
	 * @throws ParseException
	 */
	public static String getBeforeSomeMinute(String timeIn, Integer minute) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(timeIn);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		String timeOut = sdf.format(calendar.getTime());
		return timeOut;
	}

	/**
	 * 时间戳转化为Sting
	 * 
	 * @param times
	 * @return
	 * @throws ParseException
	 */
	public static String timeStampToDate2(String times) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(Long.valueOf(times)));
	}

	/**
	 * 传递时间戳转换成日期型
	 * 
	 * @param unixDate
	 * @return
	 * @throws Exception
	 */
	public static Date getDate(String unixDate) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long unixLong = 0;
		Date date = new Date();
		try {
			unixLong = Long.parseLong(unixDate) * 1000;
		} catch (Exception ex) {
			throw new ValidateException("news.requestDateError");
		}
		try {
			date = format.parse(format.format(unixLong));
		} catch (Exception ex) {
			throw new ValidateException("news.requestDateError");
		}
		return date;
	}

	/**
	 * 将日期的开始时间和结束时间重置
	 * 
	 * @param date
	 * @param startOrEnd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static Date updateDate(Date date, String startOrEnd) {
		if ("start".equals(startOrEnd)) {
			date.setHours(00);
			date.setMinutes(00);
			date.setSeconds(00);
		} else {
			date.setHours(23);
			date.setMinutes(59);
			date.setSeconds(59);
		}
		return date;
	}

	public static Date formatDate(Date date, String formatStr) throws Exception {
		if ("".equals(formatStr) || formatStr == null) {
			formatStr = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.parse(format.format(date));
	}

	@SuppressWarnings("static-access")
	public static String getInvalidtime(int count) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, count);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime();
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String downloadtime = sdFormat.format(date);
		return downloadtime;
	}

	private static final String CRON_DATE_FORMAT = "ss mm HH dd MM ? yyyy";

	/***
	 *
	 * @param date
	 *            时间
	 * @return cron类型的日期
	 */
	public static String getCron(final Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(CRON_DATE_FORMAT);
		String formatTimeStr = "";
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}

	/**
	 * 比较两个固定格式yyyy-MM-dd HH:mm:ss的String日期
	 * 
	 * @return 0:date1=date2, 1:date1在date2之前, -1:date1在date2之后，null：有错
	 */
	public static Integer compareStringDate(String date1, String date2) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = dateFormat.parse(date1);
			Date d2 = dateFormat.parse(date2);
			if (d1.equals(d2)) {
				return 0;
			} else if (d1.before(d2)) {
				return 1;
			} else if (d1.after(d2)) {
				return -1;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Date转指定格式String，默认格式yyyy-MM-dd HH:mm:ss
	 */
	public static String dateToString(Date date, String formatStr) throws Exception {
		if ("".equals(formatStr) || formatStr == null) {
			formatStr = "yyyy-MM-dd HH:mm:ss";
		}
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(date);
	}

	/**
	 * 格式化时间
	 * 
	 * @return 是昨天就：Yesterday at 2:59 PM PM/AM；否则：小于1分钟：Just Now；
	 *         小于1小时：显示分钟数2min； 小于1天：显示小时1h； 大于2天：28 Nov 2018 at 3:1 PM
	 */
	public static String staffTime(String time) throws Exception {
		// 传入时间格式化成毫秒值
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date myTime = sdf.parse(time);
		long millionSeconds = myTime.getTime();// 毫秒
		// 当前系统时间毫秒值
		long now = new Date().getTime();
		// long twoday = 1000 * 60 * 60 * 24 * 2;// 2天的毫秒值
		long oneday = 1000 * 60 * 60 * 24;// 1天的毫秒值
		long onehour = 60 * 60 * 1000;// 1小时的毫秒值
		long onemin = 60 * 1000;// 1分钟的毫秒值
		// 昨天
		String beforeSomeMinute = getBeforeSomeMinute(formatCurrentDateTime("yyyy-MM-dd HH:mm:ss"), -1 * 24 * 60);
		String substring = beforeSomeMinute.substring(0, 10);
		String startdate1 = substring + " 00:00:00";
		String enddate1 = substring + " 23:59:59";
		Date startdate = parseDateTimes(startdate1);
		Date enddate = parseDateTimes(enddate1);
		SimpleDateFormat sd = new SimpleDateFormat("dd MMM yyyy h:mm aa", Locale.ENGLISH);
		if ((myTime.before(enddate) || myTime.getTime() == enddate.getTime())
				&& (myTime.after(startdate) || myTime.getTime() == startdate.getTime())) {
			// 将毫秒格式化时间
			String str = sd.format(myTime);
			return "昨天 " + str.substring(12);
		} else if (now - millionSeconds < onemin && now - millionSeconds > 0) {// 小于1分钟
			return "刚刚";
		} else if (now - millionSeconds < onehour && now - millionSeconds >= onemin) {// 小于1小时
			return formatMinuteData(millionSeconds, now);
			// return (now - millionSeconds) / 1000 / 60 + "分钟前";
		} else if (now - millionSeconds < oneday && now - millionSeconds >= onehour) {// 小于1天
			return formatHourData(millionSeconds, now);
			// return minutes / 60 + "h ago";
		} else {
			// 大于两天
			String format = sd.format(myTime);
			String sub = format.substring(0, 11);
			sub = sub + " at" + format.substring(11, format.length());
			return sub;
		}
	}

	private static String formatHourData(long millionSeconds, long now) {
		long minutes = (now - millionSeconds) / 1000 / 60;
		if (1 == (minutes / 60)) {
			return 1 + "小时前";
		} else {
			return minutes / 60 + "小时前";
		}
	}

	private static String formatMinuteData(long millionSeconds, long now) {
		long i = (now - millionSeconds) / 1000 / 60;
		if (1 == i) {
			return i + "分钟前";
		} else {
			return i + "分钟前";
		}
	}

	public static void main(String[] args) throws Exception {

		String just = "2018-11-28 15:01:00";
		System.out.println(staffTime(just));
		String just1 = "2018-11-28 17:30:00";
		System.out.println(staffTime(just1));
		String qqq = "2018-11-29 14:59:00";
		System.out.println(staffTime(qqq));
		String ww = "2018-11-29 17:30:00";
		System.out.println(staffTime(ww));
		String ee = "2018-11-30 17:30:00";
		System.out.println(staffTime(ee));
		String hh = "2018-11-30 12:30:00";
		System.out.println(staffTime(hh));
		String nn = "2018-11-30 12:30:00";
		System.out.println(staffTime(nn));
		String gg = "2018-12-01 12:30:00";
		System.out.println(staffTime(gg));
		String jj = "2018-12-02 19:30:00";
		System.out.println(staffTime(jj));

	}

	/**
	 * 是否过期
	 */
	public static Integer isTimeOut(String failureTime, Integer minutes) throws Exception {

		String beforeSomeMinute = DateUtil.getBeforeSomeMinute(failureTime, minutes);
		String nowStr = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
		Integer compareStringDate = DateUtil.compareStringDate(beforeSomeMinute, nowStr);
		return compareStringDate;
	}

	public static String getDataTimeStr(Date date) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSS");
		return format.format(date);
	}

}
