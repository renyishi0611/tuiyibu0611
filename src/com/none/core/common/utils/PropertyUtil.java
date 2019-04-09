package com.none.core.common.utils;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.none.core.common.spring.SpringContext;

public class PropertyUtil {

	public static String getProperty(String key, Object[] object) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
		String message = SpringContext.getApplicationContext().getMessage(key, object, locale);
		return message;
	}

	public static String getProperty(String key) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
		String message = SpringContext.getApplicationContext().getMessage(key, null, null, locale);
		return message;
	}

	public static String getProperty(String properFullName, String key) throws IOException {

		Properties prop = new Properties();
		ClassPathResource classPathResource = new ClassPathResource(properFullName);
		prop.load(classPathResource.getInputStream());
		String faceSetName = prop.getProperty(key).trim();
		return faceSetName;
	}

}
