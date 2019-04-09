package com.none.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.none.core.model.Pager;
import com.none.web.utils.PageHelper;

@Repository
public class PageUtilTool {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> QueryPerPage(String querySql, Pager pager) {
		PageHelper pageHelp = new PageHelper(querySql, pager.getPageNo(), pager.getPageSize(), jdbcTemplate);
		pager.setTotalPages(pageHelp.getTotalPages());
		List<Map<String, Object>> lists = pageHelp.getResultList();
		return lists;
	}
}
