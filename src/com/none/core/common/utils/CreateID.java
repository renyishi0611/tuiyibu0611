package com.none.core.common.utils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用UUID生成主键
 * 
 * @author Season
 * @date 2011年11月17日
 *
 */
public class CreateID {

	private static final AtomicInteger integer = new AtomicInteger(0);

	/**
	 * 一次生成单个主键
	 * 
	 * @return String
	 */
	public static String getID() {
		long time = System.currentTimeMillis();
		StringBuilder idStr = new StringBuilder(20);
		idStr.append(time);
		int intValue = integer.getAndIncrement();
		if (integer.get() >= 1000) {
			integer.set(0);
		}
		if (intValue < 10) {
			idStr.append("000");
		} else if (intValue < 100) {
			idStr.append("00");
		} else if (intValue < 1000) {
			idStr.append("0");
		}
		idStr.append(intValue);
		String returnId = idStr + UUID.randomUUID().toString().replaceAll("-", "").toLowerCase().substring(17, 32);
		return returnId;
	}

	/**
	 * 一次生成多个主键
	 * 
	 * @param num
	 * @return String[]
	 */
	public static String[] getID(int num) {
		if (num < 1) {
			return null;
		}
		String[] uuidStrs = new String[num];
		for (int i = 0; i < num; i++) {
			uuidStrs[i] = getID();
		}
		return uuidStrs;
	}

	private static class Test extends Thread {
		@Override
		public void run() {
			System.out.println(getID());
		}
	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Test().start();
		}
	}

}
