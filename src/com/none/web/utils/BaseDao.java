package com.none.web.utils;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BaseDao<T> {

	/**
	 * 添加对象
	 * @param t
	 * @return
	 */
	public Integer add(T t) throws Exception;
	
	/**
	 * 更新对象
	 * @param t
	 */
	public int update(T t) throws Exception;
	
	/**
	 * 根据id删除对象
	 * @param id
	 */
	public int delete(@Param("ids")Long[] ids) throws Exception;
	
	
	public int updatePassword(@Param("ids")Long[] ids) throws Exception;
	/**
	 * 根据id加载对象
	 * @param id
	 * @return
	 */
	public T find(Long id) throws Exception;
	
	/**
	 * 根据条件查询查询记录
	 * @param t
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<T> queryByItem(@Param("object")Object object,@Param("start") int start,@Param("limit") int limit) throws Exception;
	
	/**
	 * 获取结果集
	 * @param t
	 * @return
	 */
	public int getCount(@Param("object")Object object) throws Exception;
	
}
