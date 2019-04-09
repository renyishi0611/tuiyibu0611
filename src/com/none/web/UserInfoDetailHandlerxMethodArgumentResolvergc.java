package com.none.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

import com.none.web.common.SysConstant;

public class UserInfoDetailHandlerxMethodArgumentResolvergc implements HandlerMethodArgumentResolver {

	private static Logger logger = Logger.getLogger(UserInfoDetailHandlerxMethodArgumentResolvergc.class);

	/** 登录用户信息session中的key */
	private String cmdSessionKey = SysConstant.cmsUserId;
	private String appSessionKey = SysConstant.appUserId;

	/** 登录用户信息的class类型 */
	private Class<?> sessionMemberClass = String.class;

	// public static final String cmdUserId="AuthenticatedUserId";
	// public static final String appUserId="app_AuthenticatedUserId";
	// public static final String cmsPlatform="cms";
	// public static final String appPlatform="app";
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// 过滤出符合条件的参数，这里指的是加了UserInfoDetail注解的参数
		if (parameter.hasParameterAnnotation(UserInfoDetail.class)) {
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class); // 获取session中存放的用户信息
		Object sessionMember = WebUtils.getSessionAttribute(request, cmdSessionKey);
		if (null == sessionMember) {

			sessionMember = WebUtils.getSessionAttribute(request, appSessionKey);

		}
		if (null == sessionMember) {
			return null;
		}
		// session中对象与加了MemberDetail注解的参数的类型相同是赋值给这个参数
		Class<?> klass = parameter.getParameterType();
		if (klass.isAssignableFrom(sessionMemberClass)) {
			logger.info("insert into the param(logonUserId): " + sessionMember);
			return sessionMember;
		}
		return null;
	}
}
