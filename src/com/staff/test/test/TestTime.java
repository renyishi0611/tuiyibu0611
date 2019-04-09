package com.staff.test.test;

import com.none.core.common.utils.DateUtil;

public class TestTime {
	public static void main(String[] args) throws Exception{
		String time="2018-11-23 15:27:10";
		System.out.println(DateUtil.staffTime(time));
		String time1="2018-11-23 14:27:10";
		System.out.println(DateUtil.staffTime(time1));
		String time2="2018-11-22 14:27:10";
		System.out.println(DateUtil.staffTime(time2));
		String time3="2018-11-20 15:27:10";
		System.out.println(DateUtil.staffTime(time3));
		String time4="2018-11-23 16:17:10";
		System.out.println(DateUtil.staffTime(time4));
		
	}
}
