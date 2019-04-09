package com.none.web.service.clickCount;

import javax.servlet.http.HttpServletResponse;

import com.none.web.model.TClickCount;

public interface IClickCountService {

	int save(TClickCount clickCount) throws Exception;

	boolean export(HttpServletResponse response) throws Exception;

	String getBrowseTimes(String contentId, String type) throws Exception;

}