package com.none.core.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Encoder;

public class StringUtil {
	/**
	 * 字符串分割
	 * 
	 * @param str
	 *            将要分割的字符串
	 * @param charSet
	 *            需要用哪些字符来分割 ex:{",", " ", "|"}
	 * @return 分割后的List
	 */
	public static List<String> split(String str, String[] charSet) {
		String splitRex = "\\" + charSet[0];
		for (int i = 1; i < charSet.length; i++) {
			splitRex = splitRex + "|\\" + charSet[i];
		}
		String[] strBuf = str.split(splitRex);
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < strBuf.length; i++) {
			String string = strBuf[i].trim();
			if (string.length() <= 0) {
				continue;
			}
			result.add(string);
		}
		return result;
	}

	/**
	 * 正则匹配
	 * 
	 * @param str
	 *            需要匹配的字符串
	 * @param pattern
	 *            正则表达式
	 * @return
	 */
	public static boolean regex(String str, String pattern) {
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(pattern);

		// 现在创建 matcher 对象
		Matcher m = r.matcher(str);
		if (m.find()) {
			return true;
		}

		return false;
	}

	public static String base64DeCodeGBK(Object ob) {
		BASE64Encoder bs = new BASE64Encoder();
		String result = "";
		if (ob != null) {
			result = new String(bs.encode(ob.toString().getBytes()));
		}
		return result;
	}

	/**
	 * 取出以'-'分隔的字符串中两个数字,如：1.25%-50元/0.6%-20/0.86%
	 * 
	 * @param srcString
	 *            源字符串
	 * @return Double[] 双精度数组,存放分割后的两个数字,若无则返回0L
	 */
	public static Double[] getDoubles(String srcString) {
		Double[] ret = { 0d, 0d };
		String[] s = split(srcString, "-");
		if (s[0] != null && s[0].indexOf("%") > 0)
			ret[0] = toDouble(s[0].substring(0, s[0].indexOf("%")));
		if (s.length > 1 && s[1] != null)
			if (s[1].indexOf("元") > 0) {
				ret[1] = toDouble(s[1].substring(0, s[1].indexOf("元")));
			} else {
				ret[1] = toDouble(s[1]);
			}
		return ret;
	}

	/**
	 * 取出两个数字拼接的字符串,如：1.25%-50元/20元/0.86%
	 * 
	 * @param d1
	 * @param d2
	 * @return String 拼接的字符串
	 */
	public static String getDoubleString(Double d1, Double d2) {
		String ret = "";
		if (d1 == 0L) {
			ret = d2 + "元";
		} else if (d2 == 0L) {
			ret = d1 + "%";
		} else {
			ret = d1 + "%-" + d2 + "元";
		}
		return ret;
	}

	/**
	 * 取出用分隔符分隔的字符串中的每一段字符
	 * 
	 * @param pStrSrc
	 *            源字符串
	 * @param pStrReg
	 *            分隔符
	 * @return 字符串数组,存放分隔后的每一段字符串
	 */
	public static String[] split(String pStrSrc, String pStrReg) {
		String[] splitRes = null;
		int regLeng = pStrReg.length();
		if (pStrSrc == null || pStrReg == null) {
			;
		} else {
			int begin = 0, pos = 0, l = pStrSrc.length();
			int counter = 0;
			while (begin < l) {
				pos = pStrSrc.indexOf(pStrReg, begin);
				if (pos >= 0) {
					counter++;
				} else {
					counter++;
					break;
				}
				begin = pos + 1;
			}
			splitRes = new String[counter];
			begin = pos = counter = 0;
			while (begin < l) {
				pos = pStrSrc.indexOf(pStrReg, begin);
				if (pos >= 0) {
					splitRes[counter] = pStrSrc.substring(begin, pos);
				} else {
					splitRes[counter] = pStrSrc.substring(begin, l);
					break;
				}
				counter++;
				begin = pos + regLeng;
			}
		}
		return splitRes;
	}

	/**
	 * 字符串截取函数
	 * 
	 * @param str
	 *            待截取的字符串
	 * @param cnt
	 *            截取多少位
	 * @param flag
	 *            true-
	 * @return
	 */
	public static String substring(String str, int cnt, boolean flag) {
		int reInt = 0;
		String reStr = "";
		if (str == null)
			return "";
		char[] strChar = str.toCharArray();
		for (int i = 0; (i < strChar.length && cnt > reInt); i++) {
			String tmp = String.valueOf(strChar[i]);
			byte[] b = tmp.getBytes();
			reInt += b.length;
			if (flag) {
				if (cnt >= reInt) {
					reStr += strChar[i];
				}
			} else {
				reStr += strChar[i];
			}
		}
		return reStr;
	}

	public static String substring(String str, int cnt, String chr) {
		if (str == null) {
			return "";
		}
		int reInt = 0;
		int idx = 0;
		int tmp = 0;
		int c = 0;
		char[] strChar = str.toCharArray();
		for (int i = 0; i < strChar.length; i++) {
			if (String.valueOf(strChar[i]).getBytes().length > 1) {
				reInt += 2;
			} else {
				reInt += 1;
			}
			idx = i;
			if (reInt == cnt) {
				break;
			} else if (reInt > cnt) {
				idx = i - 1;
				c = reInt - cnt;
				break;
			}
		}
		if (strChar.length != idx + 1) {
			for (int i = idx; i >= 0; i--) {
				if (String.valueOf(strChar[i]).getBytes().length > 1) {
					tmp += 2;
				} else {
					tmp += 1;
				}
				if (tmp >= chr.length() - c) {
					idx = i;
					break;
				}
			}
			return str.substring(0, idx) + chr;
		} else {
			return str;
		}

	}

	/**
	 * 格式化数据,保留两位小数,并四舍五入
	 * 
	 * @param number
	 * @return
	 */
	public static String moneyFormat(Object obj) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(obj);
	}

	public static String toString(Object obj) {
		return obj == null ? "" : obj.toString().trim();
	}

	public static String toStringNoTrim(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static Integer toInteger(Object obj) {
		return StringUtil.isEmpty(obj) ? null : Integer.valueOf(obj.toString());
	}

	public static Long toLong(Object obj) {
		return StringUtil.isEmpty(obj) ? null : Long.valueOf(obj.toString());
	}

	public static Double toDouble(Object obj) {
		return StringUtil.isEmpty(obj) ? null : Double.valueOf(obj.toString());
	}

	public static Double toSum(Double... obj) {
		Double rs = 0d;
		for (Double d : obj) {
			if (d != null) {
				rs += d;
			}
		}
		return rs;
	}

	public static Double getDouble(Object obj) {
		return obj == null ? 0d : Double.valueOf(obj.toString());
	}

	public static boolean isEmpty(Object obj) {
		return toString(obj).equals("") ? true : false;
	}

	public static boolean isNotEmpty(Object obj) {
		return toString(obj).equals("") ? false : true;
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 格式化数字：大于0小于10 前面补“0”，大于等于10，不变
	 * 
	 * @author ycyan @创建时间：2014-6-13 上午10:48:51
	 * @param integer
	 * @return
	 */
	public static String formatInteger(Integer integer) {
		String str = "";
		if (integer >= 0 && integer < 10) {
			str = "0" + integer;
		}
		if (integer >= 10) {
			str = integer + "";
		}
		return str;

	}

	public static String doubleToBecimal(Double b) {
		if (b == null) {
			return "";
		}
		BigDecimal bd = new BigDecimal(b);
		return bd.toPlainString();
	}

	/**
	 * 转换百分比
	 * 
	 * @param value
	 *            要转换的值
	 * @param maximumFractionDigits
	 *            小数位
	 * @param maximumIntegerDigits
	 *            整数位
	 * @return
	 */
	public static String toPercentFormat(String value) {
		NumberFormat fmt = NumberFormat.getPercentInstance();
		fmt.setMaximumFractionDigits(1);// 最多两位百分小数，如25.23%
		return fmt.format(Double.valueOf(value));
	}

	public static String toStringNumber(Object obj) {
		return obj == null ? "0" : obj.toString().trim();
	}

	/**
	 * 随机产生指定位数的随机数
	 * 
	 * @param n
	 * @return
	 */
	public static String randomNumbers(int n) {
		String result = "";
		for (int i = 0; i < n; i++) {
			result = result + ((int) (Math.random() * 10) % 9 + 1);
		}
		// System.out.print(result);
		return result;
	}

	/**
	 * 随机生成指定长度的字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		String string = "abcdefghijklmnopqrstuvwxyz0123456789";
		StringBuffer sb = new StringBuffer();
		int len = string.length();
		for (int i = 0; i < length; i++) {
			sb.append(string.charAt((int) (Math.round(Math.random() * (len - 1)))));
		}
		return sb.toString();
	}

	/**
	 * 截取一段文字的前两个字符
	 * 
	 * @param string
	 * @return
	 */
	public static String getTwoWords(String string) {
		String[] str = string.split(" ");
		String result = "";
		if (str.length == 1) {
			result = str[0];
		} else if (str.length > 1) {
			result = str[0] + str[1];
		}
		return result;
	}

	/**
	 * 随机指定范围内N个不重复的数 最简单最基本的方法
	 * 
	 * @param min
	 *            指定范围最小值
	 * @param max
	 *            指定范围最大值
	 * @param n
	 *            随机数个数
	 */
	public static int[] randomCommon(int min, int max, int n) {
		if (n > (max - min + 1) || max < min) {
			return null;
		}
		int[] result = new int[n];
		int count = 0;
		while (count < n) {
			int num = (int) (Math.random() * (max - min)) + min;
			boolean flag = true;
			for (int j = 0; j < n; j++) {
				if (num == result[j]) {
					flag = false;
					break;
				}
			}
			if (flag) {
				result[count] = num;
				count++;
			}
		}
		return result;
	}

	/**
	 * 从list中随机抽取元素 @return @Title: createRandomList @Description: @param
	 * list @param i @return void @throws
	 */
	public static <T> List<T> createRandomList(List<T> list, int n) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		List<T> listNew = new ArrayList<T>();
		if (list.size() <= n) {
			return list;
		}
		while (map.size() < n) {
			int random = (int) (Math.random() * list.size());
			if (!map.containsKey(random)) {
				map.put(random, "");
				listNew.add(list.get(random));
			}
		}
		return listNew;
	}
}
