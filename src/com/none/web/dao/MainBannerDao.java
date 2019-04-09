package com.none.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.none.web.po.MainBannerPO;
import com.none.web.utils.BaseDao;

@Repository
public interface MainBannerDao extends BaseDao<MainBannerPO> {
	
	MainBannerPO selectByName() throws Exception;
	
	/**
	 * 修改
	 */
	int updateUserApp(Map<String, String> map) throws Exception;

	/**
	 * 添加
	 */
	int saveUserApp(MainBannerPO po) throws Exception;

	/**
	 * 查询所有
	 */
	List<MainBannerPO> selectUserAppList() throws Exception;

	/**
	 * 按id查询
	 */
	MainBannerPO selectUserAppById(Integer id) throws Exception;
	
}
