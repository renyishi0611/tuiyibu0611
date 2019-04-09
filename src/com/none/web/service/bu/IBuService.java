package com.none.web.service.bu;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.none.web.po.BuPO;

/**
 * 
 * @ClassName: IBuService
 * @Description: TODO(buService接口)
 * @author Jan
 * @date Dec 14, 2018 5:13:24 PM
 */
public interface IBuService {

	/**
	 * 
	 * @Title: save
	 * @Description: TODO(创建bu)
	 * @param buPO
	 * @param request
	 * @throws Exception
	 * @return int 返回类型
	 */
	int save(BuPO buPO, HttpServletRequest request) throws Exception;

	/**
	 * 
	 * @Title: update
	 * @Description: TODO(修改bu)
	 * @param buPO
	 * @throws Exception
	 * @return int 返回类型
	 */
	int update(BuPO buPO) throws Exception;

	/**
	 * 
	 * @Title: seachBuListByPage
	 * @Description: TODO(分页查询bu信息)
	 * @param buPO
	 * @throws Exception
	 * @return Map<String,Object> 返回类型
	 */
	Map<String, Object> seachBuListByPage(BuPO buPO) throws Exception;

	/**
	 * 
	 * @Title: detailBu
	 * @Description: TODO(查询bu详细信息)
	 * @param buPO
	 * @throws Exception
	 * @return BuPO 返回类型
	 */
	BuPO detailBu(BuPO buPO) throws Exception;

	/**
	 * 
	 * @Title: selectBuById
	 * @Description: TODO(查询传入id是否存在)
	 * @param id
	 * @throws Exception
	 * @return Integer 返回类型
	 */
	Integer selectBuById(String id) throws Exception;

	/**
	 * 
	 * @Title: deleteBu
	 * @Description: TODO(删除bu)
	 * @param buPO
	 * @throws Exception
	 * @return Integer 返回类型
	 */
	Integer deleteBu(BuPO buPO) throws Exception;
}
