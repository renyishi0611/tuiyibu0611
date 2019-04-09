package com.none.core.exception;

import javax.servlet.http.HttpServletRequest;

/**
 * 验证异常
 * 
 * @author Season
 *
 */
public class ValidateException extends Exception {

	private static final long serialVersionUID = 1L;

	public ValidateException() {
		super();
	}

	public ValidateException(String message, Throwable cause) {
		
		super(message, cause);
	}

	public ValidateException(String message) {
		super(message);
	}

	public ValidateException(Throwable cause) {
		super(cause);
	}

	public ValidateException(String message, HttpServletRequest request, Object[] obj) {
		super(message);
		request.setAttribute("obj", obj);
	}
	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
	
}
