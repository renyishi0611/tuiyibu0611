package com.none.web.dao;


import java.util.Map;

import org.springframework.stereotype.Repository;

import com.none.web.utils.BaseDao;

@Repository
public interface MomsForwardDao extends BaseDao<MomsForwardDao> {
	/**
	 * 插入转发记录
	 * 
	 * @param momentsId
	 *            转发所属momentsId
	 * @param userId
	 *            转发所属userId
	 * @throws Exception
	 *             抛出异常
	 */
	void insertMomsForward(Map<String, Object> map) throws Exception;
	/**
	 * 查询转发数
	 * @param momentsId 转发所属momentsId
	 * @return 返回转发数
	 * @throws Exception 抛出异常
	 */
	int insertMomsForwardByMomentsId(int momentsId) throws Exception;
}
