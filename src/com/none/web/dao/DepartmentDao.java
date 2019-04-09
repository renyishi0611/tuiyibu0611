package com.none.web.dao;

import org.springframework.stereotype.Repository;

import com.none.web.po.DepartmentPO;
import com.none.web.utils.BaseDao;

@Repository
public interface DepartmentDao extends BaseDao<DepartmentPO> {

	/**
	 * 添加
	 */
	int saveDepartment(DepartmentPO po) throws Exception;

	/**
	 * 按Name查询
	 */
	DepartmentPO selectDepartmentByName(String deptName) throws Exception;
	
	/**
	 * 按Name查询id
	 */
	Integer selectDepartmentIdByName(String deptName) throws Exception;


}
