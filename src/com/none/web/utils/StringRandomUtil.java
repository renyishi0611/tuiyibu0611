package com.none.web.utils;

import java.util.Random;

/**
 * 随机生成字符串工具类
 * 
 * @author winter
 *
 */
public class StringRandomUtil {

	/**
	 * 随机生成可选长度的数字加字母组合的字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String getStringRandom(int length) {

		String val = "";
		Random random = new Random();
		// length为几位密码
		for (int i = 0; i < length; i++) {
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

}
