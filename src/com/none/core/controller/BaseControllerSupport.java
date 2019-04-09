package com.none.core.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.none.core.common.spring.SpringContext;

public class BaseControllerSupport extends HandlerInterceptorAdapter{
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	
	public HttpServletRequest getRequest() {
		this.request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	public HttpServletResponse getResponse() {
		this.response = ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
		return response;
	}

	public HttpSession getSession() {
		this.session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		return session;
	}
	
	public String getProperty(String key){
		Locale locale = RequestContextUtils.getLocaleResolver(getRequest()).resolveLocale(getRequest()); 
		return SpringContext.getApplicationContext().getMessage(key, null, null, locale);  
	}
	
}
