package com.none.core.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 过滤XSS脚本攻击
 * 
 * @author Season
 *
 */
public class XSSFilter {
	
	public static String encode(String value){
		if(StringUtils.isNotBlank(value)){
			if(value.toLowerCase().indexOf("<script>") != -1 || value.toLowerCase().indexOf("javascript:") != -1){
				value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
				value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
				value = value.replaceAll("'", "&#39;");
				//value = value.replaceAll("eval\\((.*)\\)", "");
				//value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
			}
		}
		return value;
	}

}
