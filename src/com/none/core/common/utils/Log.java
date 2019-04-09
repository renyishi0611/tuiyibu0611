package com.none.core.common.utils;

import org.apache.log4j.Logger;

/**
 * 系统日志记录
 * 
 * @author Season
 *
 */
public class Log {
	
	private static Logger logger = Logger.getLogger("系统信息");
	
	public static void info(String message){
		logger.info(message);
	}
	
	public static void debug(String message){
		logger.debug(message);
	}
	
	public static void error(String message){
		logger.error(message);
	}
	
	public static void error(String message, Throwable t){
		logger.error(message, t);
	}
	
	public static void warn(String message){
		logger.warn(message);
	}

}
