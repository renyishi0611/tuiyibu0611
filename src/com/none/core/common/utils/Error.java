package com.none.core.common.utils;

import java.util.HashMap;
import java.util.Map;

public class Error {
	/**
	 * 错误码
	 */
	public static Map<Integer, String> code = new HashMap<Integer, String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put(0, "成功") ;
			put(1, "缺少参数") ;
			put(2, "参数值错误") ;
		}
	};
}
