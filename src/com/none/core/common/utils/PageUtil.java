package com.none.core.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.none.core.model.Pager;

/**
 * 分页工具类
 * 
 * @author Season
 *
 */
public class PageUtil {

	/**
	 * 生成用于分页所需的JSON字符串
	 * 
	 * @param pager
	 * @param list
	 * @return Map
	 */
	public static Map<String, Object> getPageDataJSON(Pager pager, List<?> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pager.pageNo", pager.getPageNo());
		map.put("pager.totalRows", pager.getTotalRows());
		map.put("rows", list);
		return map;
	}

	/**
	 * 获取总页数
	 * 
	 * @param count
	 *            总数量
	 * @param pageSize
	 *            每页显示的数量
	 * @return
	 */
	public static int reckonTotalPage(int count, int pageSize) {
		int totalPage;
		if (count % pageSize == 0) {
			totalPage = count / pageSize;
		} else {
			totalPage = count / pageSize + 1;
		}
		return totalPage;
	}

}
