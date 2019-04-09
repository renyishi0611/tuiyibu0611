package com.none.core.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/** 
 * @Description: 计算CA复制日期工具类 
 * @author Bing
 * @date 2016年9月27日 下午6:12:16 
 */
public class CaCopyDateUtil {
	/**
	 * 计算CA复制的日期  按天
	 * @param caExamDate  CA 考试日期
	 * @param repeatEvery 复制频率：每隔几天、几周等复制
	 * @param endType     结束复制类型：发生几次后（After）、在某一天结束（On）
	 * @param ends  结束复制条件   数字或一个日期  如果是次数，这个次数包含母本
	 * @return
	 */
	public static List<String> getCopyDateOfCa_daily(String caExamDate,int repeatEvery,String endType,String ends){
		List<String> list = new ArrayList<String>();
		int times = 0;
		// 计算要循环的次数
		if("On".equals(endType)){
			//从考试日期到结束结束日期之间的天数除以复制频率
			times = Integer.parseInt(daysBetween(ends,caExamDate))/repeatEvery;
		}else{
			times = Integer.parseInt(ends)-1;//复制次数减去第一个副本
		}
		//list.add(caExamDate);//相当于母本
		//推算日期的基础
		String copyDate = caExamDate;
		//循环计算日期
		for(int i=0;i<times;i++){
			if(list.size()==9999){
				break;
			}
			String copyDate_temp = getBeforeSomeDay(copyDate,repeatEvery);
			//验证日期是否存在
			if(isValidDate(copyDate_temp)){
				list.add(copyDate_temp);
				//将推算日期的基础设置为新计算出来的日期
				copyDate = copyDate_temp;
			}
			
		}
		
		return list;
	
	}
	
	/**
	 * 计算CA复制的日期 按周
	 * @param caExamDate  CA 考试日期
	 * @param repeatEvery 复制频率：每隔几天、几周等复制
	 * @param endType     结束复制类型：发生几次后（After）、在某一天结束（On）
	 * @param ends  结束复制条件   数字或一个日期  如果是次数，这个次数包含母本
	 * @param weekStr    周几复制，多个用逗号隔开、星期天为1，星期一为2 ，以此类推 星期六为7
	 * @return
	 */
	public static List<String> getCopyDateOfCa_weekly(String caExamDate,int repeatEvery,String endType,String ends,String weekStr){
		List<String> list = new ArrayList<String>();
		if("After".equals(endType)){
			//循环的次数
			int times = Integer.parseInt(ends)-1;//计算次数，因为包含母本，所以减1
			list = getDateWeekly_times(caExamDate,times,weekStr,repeatEvery);
			//list.add(caExamDate);
		}else{
			list = getDateWeekly_onDate(caExamDate,ends,weekStr,repeatEvery);
			//list.add(caExamDate);
			
		}
		return list;
	}
	
	/**
	 * 计算CA复制的日期 按月
	 * @param caExamDate  CA 考试日期
	 * @param repeatEvery 复制频率：每隔几天、几周等复制
	 * @param endType     结束复制类型：发生几次后（After）、在某一天结束（On）
	 * @param ends  结束复制条件   数字或一个日期  如果是次数，这个次数包含母本
	 * @param repeatBy    Daymonth / Dayweek
	 * @return
	 */
	public static List<String> getCopyDateOfCa_monthly(String caExamDate,int repeatEvery,String endType,String ends,String repeatBy){
		List<String> list = new ArrayList<String>();
		int times = 0;
		//考试日期数组
		String[] caDateArray = caExamDate.split("-");
		
		Calendar ca = Calendar.getInstance();
		ca.set(Integer.parseInt(caDateArray[0]), Integer.parseInt(caDateArray[1])-1, Integer.parseInt(caDateArray[2]));
		//计算考试日期是当月的第几天
		int dayOfMonth = ca.get(Calendar.DAY_OF_MONTH);
		//int n = (ca.get(Calendar.MONTH)+1)%2;
		//System.out.println("n==="+n);
		//发生几次
		if("After".equals(endType)){
			times = Integer.parseInt(ends)-1;//计算次数，因为包含母本，所以减1
			//按照 day of the month的方式，主要看caExamDate是当月的第几天，以后的数据都是那个月的那一天
			if("Daymonth".equals(repeatBy)){
				//list.add(caExamDate);//相当于母本
				int i=0;
				while(times>=0){
					//最多9999条数据，需求是这样
					if(list.size()==9999){
						break;
					}
					//计算caExamDate 的年月，向后i*repeatEvery个月的年月
					String dateStr = getDateSomeMonth(caDateArray[0]+"-"+caDateArray[1],i*repeatEvery);
					//将日拼接到后面，如果日小于10，前面补0
					if(dayOfMonth<10){
						dateStr =dateStr+"-0"+dayOfMonth;
					}else{
						dateStr =dateStr+"-"+dayOfMonth;
					}
					//验证数据并添加到list
					if(isValidDate(dateStr)&&!list.contains(dateStr)){
						//System.out.println("s==="+Integer.parseInt(dateStr.split("-")[1])%2);
						//if(Integer.parseInt(dateStr.split("-")[1])%2==n){
							list.add(dateStr);
							times--;
						//}
					}
					i++;
				}
			}else{
				//List<String> list_ = new ArrayList<String>();
				list = getDateMonthly_times(caExamDate,times,repeatEvery);
				//list.add(caExamDate);
			}
		}
		//到某个时间为止
		else{
			if("Daymonth".equals(repeatBy)){
				//list.add(caExamDate);//相当于母本
				
				//结束日期数组
				String[] endDateArray = ends.split("-");
				//开始日期年月
				int startYear_moth = Integer.parseInt(caDateArray[0]+caDateArray[1]);
				//结束日期年月
				int endYear_moth = Integer.parseInt(endDateArray[0]+endDateArray[1]);
				
				//开始循环的条件
				String copyDate_YM = caDateArray[0]+"-"+caDateArray[1];
				
				for(int i=startYear_moth;i<=endYear_moth;){
					if(list.size()==9999){
						break;
					}
					//计算出来的日期的年月
					String dateStr = getDateSomeMonth(copyDate_YM,repeatEvery);
					//将日拼接到后面，如果日小于10，前面补0
					if(dayOfMonth<10){
						dateStr =dateStr+"-0"+dayOfMonth;
					}else{
						dateStr =dateStr+"-"+dayOfMonth;
					}
					//验证数据并添加到list
					if(isValidDate(dateStr)&&!list.contains(dateStr)){
						//if(i==endYear_moth){
							int caDate_Md = Integer.parseInt(dateStr.split("-")[0]+dateStr.split("-")[1]+dateStr.split("-")[2]);
							int endDate_Md = Integer.parseInt(endDateArray[0]+endDateArray[1]+endDateArray[2]);
							if(endDate_Md>=caDate_Md){
								list.add(dateStr);
							}
						//}else{
						//	list.add(dateStr);
						//}
						
					}
					copyDate_YM = dateStr.substring(0,7);
					i = Integer.parseInt(dateStr.substring(0,7).replace("-", ""));
				}
			}else{
				//List<String> list_ = new ArrayList<String>();
				list = getDateMonthly_onDate(caExamDate,ends,repeatEvery);
				//list.add(caExamDate);
			}
		}
		if(list.contains(caExamDate)){
			list.remove(caExamDate);
		}
		return list;
	
	}
	
	
	
	/**
	 * 计算CA复制的日期  按年
	 * @param caExamDate  CA 考试日期
	 * @param repeatType  复制类型：Daily/Weekly/Monthly/Yearly
	 * @param repeatEvery 复制频率：每隔几天、几周等复制
	 * @param endType     结束复制类型：发生几次后（After）、在某一天结束（On）
	 * @param ends  结束复制条件   数字或一个日期  如果是次数，这个次数包含母本
	 * @return
	 */
	public static List<String> getCopyDateOfCA_yearly(String caExamDate,int repeatEvery,String endType,String ends){
		List<String> list = new ArrayList<String>();
		int times = 0;

		//list.add(caExamDate);//相当于母本
		//考试日期数组
		String[] caDateArray = caExamDate.split("-");
		
		//获取考试日期的年份
		int startYear = Integer.parseInt(caDateArray[0]);
		
		
		//如果是发生几次之后结束，根据次数、以及频率计算出结束复制日期，等同于 on方式，注意结束日期的月日和开始日期的日月一样
		if("After".equals(endType)){
			times = Integer.parseInt(ends)-1;
			ends = Integer.parseInt(caDateArray[0])+times*repeatEvery+"-"+caDateArray[1]+"-"+caDateArray[2];
		}
		
		//结束日期数组
		String[] endDateArray = ends.split("-");
		
		//获取结束日期的年份
		int endYear = Integer.parseInt(endDateArray[0]);
		//获取结束日期、考试日期的月日，转成int行，用于比较是否已经过了考试日期日月
		int caDate_Md = Integer.parseInt(caDateArray[1]+caDateArray[2]);
		int endDate_Md = Integer.parseInt(endDateArray[1]+endDateArray[2]);
		
		
		//开始循环 从startYear 到 endYear
		for(int i=startYear;i<=endYear;){
			if (list.size()==9999) {
				break;
			}
			String dateStr = i+"-"+caDateArray[1]+"-"+caDateArray[2];
			//校验日期存不存在、list中是否已经有了
			if(isValidDate(dateStr)&&!list.contains(dateStr)){
				//计算的日期的年与startYear一致,直接进行下一次循环
				if(startYear == i){
					i=i+repeatEvery;
					continue;
				}
				//计算的日期的年与endYear一致，判断结束日期月日是否大于等于考试日期的月日，如果是，则将计算的日期添加到list，否则不添加
				else if(endYear == i){
					if(endDate_Md>=caDate_Md){
						list.add(dateStr);
					}
				}
				//剩余的全部添加进去
				else{
					list.add(dateStr);
				}
			}
			//根据复制频率，计算下一次循环的变量
			i=i+repeatEvery;
			if("After".equals(endType)){
				if(list.size()<times){
					endYear++;
				}
			}
		}
		
		
		return list;
	
	}
	
	/**
	 * 验证一个日期是否存在
	 * @param str  yyyy-MM-dd
	 * @return
	 */
	public static boolean isValidDate(String str) {
	      boolean convertSuccess=true;
	      // 指定日期格式为四位年-两位月份-两位日期，注意yyyy-MM-dd区分大小写；
	       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	       try {
	    	   // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007-02-29会被接受，并转换成2007-03-01
	          format.setLenient(false);
	          format.parse(str);
	       } catch (Exception e) {
	          // e.printStackTrace();
	    	   // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
	           convertSuccess=false;
	       } 
	       return convertSuccess;
	}
	
	
	/**
	 * 计算两个日期相差的天数
	 * @param smdate
	 * @return
	 */
	public static String daysBetween(String startdate,String endDate){  
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();  
        Calendar cal2 = Calendar.getInstance(); 
        try {
			cal.setTime(sdf.parse(startdate));
			cal2.setTime(sdf.parse(endDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}    
        long time1 = cal.getTimeInMillis();                 
       
        long time2 = cal2.getTimeInMillis();          
        long between_days=Math.abs((time2-time1))/(1000*3600*24);  
            
        return String.valueOf(between_days);
	}
	
	/**
	 * 获取一个指定日期向前推几天的日期
	 * @param date 指定的日期
	 *        variable   向前或向后推的天数
	 */
	public static String getBeforeSomeDay(String date,int variable){
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
	 * 获取一个指定日期向前或向后几个月
	 * @param date 指定的日期
	 *        variable   向前或向后推的月数
	 */
	public static String getDateSomeMonth(String date,int variable){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");    
		Calendar calBegin = Calendar.getInstance();
		try {
			calBegin.setTime(sdf.parse(date));
			calBegin.add(Calendar.MONTH, variable);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf.format(calBegin.getTime());
	}
	
	/**
	 * 获取指定周的第几天
	 * 例如：每月第二周的第二天
	 * @param dBegin 开始日期
	 * @param dEnd  结束日期
	 * @return
	 */
	public static List<String> getDateMonthly_onDate(String dBegin, String dEnd,int repeat) { 
		
		//考试日期数组
		String[] caDateArray = dBegin.split("-");
		Calendar ca = Calendar.getInstance();
		ca.set(Integer.parseInt(caDateArray[0]), Integer.parseInt(caDateArray[1])-1, Integer.parseInt(caDateArray[2]));
		//计算考试日期是当月的第几周
		int weekOfMonth = ca.get(Calendar.WEEK_OF_MONTH);
		//计算考试日期是当周的第几天
		int dayOfWeek = ca.get(Calendar.DAY_OF_WEEK);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		List<String> listDate = new ArrayList<String>();
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		//calBegin.set(Integer.parseInt(dBegin.split("-")[0]), Integer.parseInt(dBegin.split("-")[1])-1, Integer.parseInt(dBegin.split("-")[2]));
		calBegin.set(Integer.parseInt(caDateArray[0]), Integer.parseInt(caDateArray[1])+(repeat)-1, 1);
		
		List<Integer> month = new ArrayList<Integer>();
		for(int i=1;i<=12;i++){
			int m = ca.get(Calendar.MONTH)+1+i*repeat;
			if(m<=12){
				//System.out.println(m);
				month.add(m);
			}else{
				int mm = m-12;
				if(mm<=12&&!month.contains(mm)){
					month.add(mm);
				}
			}
		}
		// 测试此日期是否在指定日期之后
		while (parseDate("yyyy-MM-dd",dEnd).after(calBegin.getTime())) {
			if(listDate.size()==9998){
				break;
			}
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			int dayofweek = calBegin.get(Calendar.DAY_OF_WEEK);
			int weekofmonth = calBegin.get(Calendar.WEEK_OF_MONTH);
			//只取星期天
			if (weekofmonth ==weekOfMonth && dayofweek == dayOfWeek) {
				if(month.contains(calBegin.get(Calendar.MONTH)+1)){
					listDate.add(sdf.format(calBegin.getTime()));
					calBegin.set(calBegin.get(Calendar.YEAR), (calBegin.get(Calendar.MONTH)+repeat), 1);
				}
			}
		}
		 //Collections.sort(listDate);
		return listDate;
	} 
	
	/**
	 * 获取指定周的第几天
	 * 例如：每月第二周的第二天
	 * @param dBegin 开始日期
	 * @param 要计算多少次
	 * @return
	 */
	public static List<String> getDateMonthly_times(String dBegin, int times,int repeat) { 
		
		//考试日期数组
		String[] caDateArray = dBegin.split("-");
		Calendar ca = Calendar.getInstance();
		ca.set(Integer.parseInt(caDateArray[0]), Integer.parseInt(caDateArray[1])-1, Integer.parseInt(caDateArray[2]));
		//计算考试日期是当月的第几周
		int weekOfMonth = ca.get(Calendar.WEEK_OF_MONTH);
		//计算考试日期是当周的第几天
		int dayOfWeek = ca.get(Calendar.DAY_OF_WEEK);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		List<String> listDate = new ArrayList<String>();
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		//calBegin.set(Integer.parseInt(dBegin.split("-")[0]), Integer.parseInt(dBegin.split("-")[1])-1+repeat, Integer.parseInt(dBegin.split("-")[2]));
		
		calBegin.set(Integer.parseInt(caDateArray[0]), Integer.parseInt(caDateArray[1])+(repeat)-1, 1);
		
		List<Integer> month = new ArrayList<Integer>();
		for(int i=1;i<=12;i++){
			int m = ca.get(Calendar.MONTH)+1+i*repeat;
			if(m<=12){
				//System.out.println(m);
				month.add(m);
			}else{
				int mm = m-12;
				if(mm<=12&&!month.contains(mm)){
					month.add(mm);
				}
			}
		}
		
//		for(int i:month){
//			System.out.println(i);
//		}
		
		// 测试此日期是否在指定日期之后
		while (times>0) {
			if(listDate.size()==9998){
				break;
			}
			
			//System.out.println(calBegin.getTime());
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			
			int dayofweek = calBegin.get(Calendar.DAY_OF_WEEK);
			int weekofmonth = calBegin.get(Calendar.WEEK_OF_MONTH);
			
			if (weekofmonth ==weekOfMonth && dayofweek == dayOfWeek) {
				if(month.contains(calBegin.get(Calendar.MONTH)+1)){
					listDate.add(sdf.format(calBegin.getTime()));
					times--;
					calBegin.set(calBegin.get(Calendar.YEAR), (calBegin.get(Calendar.MONTH)+repeat), 1);
				}
			}
		}
		return listDate;
	} 
	
	
	/**
	 * 获取从某个日期开始的每个指定的周几
	 * 例如：获取从2016-09-20 开始，以后的每个Monday,sunday
	 * @param dBegin 开始日期
	 * @param times  要计算多少个
	 * @param needDay  指定的周几字符串，用逗号隔开，比如Monday,sunday，注意要转成 2,1   sunday为每周第一天、Monday为第二天，以此类推
	 * @return
	 */
	public static List<String> getDateWeekly_times(String dBegin, int times,String needDays,int repeat) { 
		
		//考试日期数组
		String[] caDateArray = dBegin.split("-");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		List<String> listDate = new ArrayList<String>();
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.set(Integer.parseInt(caDateArray[0]), Integer.parseInt(caDateArray[1])-1, Integer.parseInt(caDateArray[2]));
		//calBegin.set(Integer.parseInt(caDateArray[0]), Integer.parseInt(caDateArray[1])+(repeat)-1, 1);
		
		String[] needDay = needDays.split(",");
		int maxNeedDay = Integer.parseInt(needDay[0]);
		for (String string : needDay) {
			int string_int =Integer.parseInt(string);
			if(string_int>maxNeedDay){
				maxNeedDay = string_int;
			}
		}
		
		//int times_ = 0;
		// 测试此日期是否在指定日期之后
		while (times>0) {
			if(listDate.size()==9998){
				break;
			}
			
			int dayofweek = calBegin.get(Calendar.DAY_OF_WEEK);
			if(dayofweek == 7){
				//calBegin.add(Calendar.WEEK_OF_MONTH, repeat-1);
				//calBegin.add(Calendar.DAY_OF_WEEK, 1);
				calBegin.add(Calendar.DAY_OF_MONTH, (repeat-1)*7);
			}
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			dayofweek = calBegin.get(Calendar.DAY_OF_WEEK);
			
			dayofweek = calBegin.get(Calendar.DAY_OF_WEEK);
			if (needDays.contains(dayofweek+"")) {
				listDate.add(sdf.format(calBegin.getTime()));
				if(dayofweek == maxNeedDay){
//					calBegin.add(Calendar.WEEK_OF_MONTH, repeat-1);
//					calBegin.add(Calendar.DAY_OF_WEEK, 1);
					times--;
				}
			}
//			if(times_==times+1){
//				break;
//			}
		}
		return listDate;
	} 
	
	
	/**
	 * 获取指定周的第几天
	 * 例如：每月第二周的第二天
	 * @param dBegin 开始日期
	 * @param dEnd  结束日期
	 * @return
	 */
	public static List<String> getDateWeekly_onDate(String dBegin, String dEnd,String needDays,int repeat) { 
		
		String[] needDay = needDays.split(",");
		int maxNeedDay = Integer.parseInt(needDay[0]);
		for (String string : needDay) {
			int string_int =Integer.parseInt(string);
			if(string_int>maxNeedDay){
				maxNeedDay = string_int;
			}
		}
		
		//考试日期数组
		String[] caDateArray = dBegin.split("-");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
		List<String> listDate = new ArrayList<String>();
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		//calBegin.set(Integer.parseInt(dBegin.split("-")[0]), Integer.parseInt(dBegin.split("-")[1])-1, Integer.parseInt(dBegin.split("-")[2]));
		calBegin.set(Integer.parseInt(caDateArray[0]), Integer.parseInt(caDateArray[1])-1, Integer.parseInt(caDateArray[2]));
		// 测试此日期是否在指定日期之后
		while (parseDate("yyyy-MM-dd HH:mm:ss",dEnd).after(calBegin.getTime())) {
//			System.out.println("*****************");
//			System.out.println(parseDate("yyyy-MM-dd",dEnd)+"  end");
//			System.out.println(calBegin.getTime()+"  begin");
//			System.out.println("*****************");
			if(listDate.size()==9998){
				break;
			}
			int dayofweek = calBegin.get(Calendar.DAY_OF_WEEK);
			if(dayofweek == 7){
				calBegin.add(Calendar.DAY_OF_MONTH, (repeat-1)*7);
				//calBegin.add(Calendar.DAY_OF_WEEK, 1);
			}
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			dayofweek = calBegin.get(Calendar.DAY_OF_WEEK);
			//System.out.println(dayofweek);
//			if(dayofweek == 7){
//				calBegin.add(Calendar.DAY_OF_MONTH, (repeat-1)*7);
//				//calBegin.add(Calendar.DAY_OF_WEEK, 1);
//			}
			//只取星期天
			if (needDays.contains(dayofweek+"")) {
				if(parseDate("yyyy-MM-dd HH:mm:ss",dEnd).after(calBegin.getTime())){
					listDate.add(sdf.format(calBegin.getTime()));
				}
				
				//System.out.println(sdf.format(calBegin.getTime())+"--------"+calBegin.get(Calendar.DAY_OF_WEEK));
//				if(dayofweek == maxNeedDay){
//					calBegin.add(Calendar.WEEK_OF_MONTH, repeat-1);
//					calBegin.add(Calendar.DAY_OF_WEEK, 1);
//				}
			}
		}
		return listDate;
	} 
	
	/**
	 * 将字符串转为指定格式的日期
	 * @param df
	 * @param dt
	 * @return
	 */
	public static Date parseDate(String df, String dt) {
		try {
			return new SimpleDateFormat(df).parse(dt+" 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 比较两个时间段的关系
	 * @param startTime  要比较的开始时间
	 * @param endTime  要比较的结束时间
	 * @param startTime_db  目标开始时间
	 * @param endTime_db  目标结束时间
	 * @return
	 */
	public static boolean compareTimes(String startTime,String endTime,String targetStartTime,String targetEndTime){
		int startTime_int = Integer.parseInt(startTime.replace(":", ""));
		int endTime_int = Integer.parseInt(endTime.replace(":", ""));
		int targetStartTime_int = Integer.parseInt(targetStartTime.replace(":", ""));
		int targetEndTime_int = Integer.parseInt(targetEndTime.replace(":", ""));
		//要比较的时间包含在目标时间中
		if(startTime_int>=targetStartTime_int && endTime_int<=targetEndTime_int){
			return true;
		}
		//要比较的开始时间 或结束时间包含在目标时间中
		else if(startTime_int>targetStartTime_int && startTime_int<targetEndTime_int || endTime_int>targetStartTime_int && endTime_int< targetEndTime_int){
			return true;
		}
		//目标时间包含在要比较的时间中
		else if(startTime_int< targetStartTime_int&&endTime_int >targetEndTime_int){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static void main(String[] args) throws ParseException   {

//		System.out.println("************************************");Dayweek  Daymonth
//		List<String> list6 =getCopyDateOfCa_monthly("2020-01-31",1,"On","2021-10-30","Dayweek");
//		for(String s:list6){
//			System.out.println(s);
//		}
//		System.out.println("************************************");
		
		List<String> list7 =getCopyDateOfCa_weekly("2016-11-19",2,"On","2016-12-04","1");
		for(String s:list7){
			System.out.println(s);
		}
//		
////		String ss = "2016-02-29";
////		Calendar ca = Calendar.getInstance();
////		ca.set(Integer.parseInt(ss.split("-")[0]), Integer.parseInt(ss.split("-")[1])-1, Integer.parseInt(ss.split("-")[2]));
////		System.out.println(ca.getTime());
////		
////		System.out.println(ca.get(Calendar.WEEK_OF_MONTH));
////		System.out.println(ca.get(Calendar.DAY_OF_WEEK));
//		
//
//		
//		System.out.println("************************************");
//		List<String> listv = getDateWeekly_onDate("2016-02-29","2016-03-17","2,4,5",1);
//		for(String ssss:listv){
			//System.out.println(parseDate("yyyy-MM-dd HH:mm:ss","2016-12-04"));
//		}
		
		
		
		
		
	}
}
