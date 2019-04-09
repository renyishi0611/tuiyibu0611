package com.none.core.common.utils;

import org.apache.log4j.Logger;



public class TransactSQLInjectionUtil {
	private static Logger logger = Logger.getLogger(TransactSQLInjectionUtil.class);
	public static boolean sql_inj(String str){
        logger.debug("str:"+str);
        if(str==null||"".equals(str)){
        	return false;
        }
		String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|";
		String inj_stra[] = inj_str.split("\\|");
		for (int i=0 ; i <inj_stra.length ; i++ ){
			if (str.indexOf(inj_stra[i])>=0){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(sql_inj("appInstallPath"));
	}
}
