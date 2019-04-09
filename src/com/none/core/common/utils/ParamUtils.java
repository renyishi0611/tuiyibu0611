package com.none.core.common.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.none.core.exception.ValidateException;

public class ParamUtils {

	/**
	 * 验证参数：是null或空串，提示 
	 */
	public static void validateParam(Map<String, Object> map, HttpServletRequest request) throws ValidateException {
		
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
            	String key = entry.getKey();
            	Object value = map.get(key);
            	if (value == null) {
            		throw new ValidateException("result.paramNull", request, new Object[] { key });
            	} else if (StringUtils.equals(value.getClass().getSimpleName(), "String")) {
            		if (StringUtils.isBlank(value.toString())) {
            			throw new ValidateException("result.paramNull", request, new Object[] { key });	
            		}
				}
			}
		}
	}
	
	
	
}
