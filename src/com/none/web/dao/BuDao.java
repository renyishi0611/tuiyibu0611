package com.none.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.none.web.po.BuPO;
import com.none.web.utils.BaseDao;

/**
 * 
 * @ClassName: BuDao
 * @Description: TODO(budao)
 * @author Jan
 * @date Dec 17, 2018 5:36:13 PM
 */
@Repository
public interface BuDao extends BaseDao<BuPO> {

	/**
	 * 
	 * @Title: selectBuName
	 * @Description: TODO(查询bu名称是否被占用)
	 * @param bu
	 * @throws Exception
	 *             设定文件
	 * @return Integer 返回类型
	 */
	Integer selectBuName(String bu) throws Exception;

	/**
	 * 
	 * @Title: createBu
	 * @Description: TODO(创建bu)
	 * @param buPO
	 * @throws Exception
	 *             设定文件
	 * @return Integer 返回类型
	 */
	Integer createBu(BuPO buPO) throws Exception;

	/**
	 * 
	 * @Title: updateBu
	 * @Description: TODO(修改bu)
	 * @param buPO
	 * @throws Exception
	 *             设定文件
	 * @return Integer 返回类型
	 */
	Integer updateBu(BuPO buPO) throws Exception;

	/**
	 * 
	 * @Title: searchBu
	 * @Description: TODO(分页查询bu)
	 * @param po
	 * @throws Exception
	 *             设定文件
	 * @return List<BuPO> 返回类型
	 */
	List<BuPO> searchBu(BuPO po) throws Exception;

	/**
	 * 
	 * @Title: searchCount
	 * @Description: TODO(查询bu个记录数)
	 * @param po
	 * @throws Exception
	 *             设定文件
	 * @return int 返回类型
	 */
	int searchCount(BuPO po) throws Exception;

	/**
	 * 
	 * @Title: detailBu @Description: TODO(查询bu的详细信息) @param buPO @throws
	 *         Exception 设定文件 @return BuPO 返回类型 @throws
	 */
	BuPO detailBu(BuPO buPO) throws Exception;

	/**
	 * 
	 * @Title: getBuNameAndPhotoByUserId
	 * @Description: TODO(根据用户id查询用户所属bu的名称和头像)
	 * @param userId
	 * @throws Exception
	 *             设定文件
	 * @return Map<String,Object> 返回类型
	 */
	Map<String, Object> getBuNameAndPhotoByUserId(String userId) throws Exception;

	/**
	 * 根据bu名称模糊查询bu对象
	 * 
	 * @param name
	 * @return List<BuPO>
	 * @throws Exception
	 */
	List<BuPO> selectBuByBuname(String name) throws Exception;

	/**
	 * 
	 * @Title: selectBuById
	 * @Description: TODO(查询传入id是否存在)
	 * @param id
	 * @return Integer 返回类型
	 * @throws Exception
	 */
	Integer selectBuById(String id) throws Exception;

	/**
	 * 
	 * @Title: selectCountFromUserByGroupId
	 * @Description: TODO( 查询一个组id下面的的用户数)
	 * @param id
	 * @throws Exception
	 * @return int 返回类型
	 */
	int selectCountFromUserByGroupId(String id) throws Exception;

	/**
	 * 根据id查询专题策划下的未被删除的文章
	 * 
	 * @param id
	 *            专题策划id
	 * @return
	 * @throws Exception
	 */
	int selectCountFromMom(String id) throws Exception;

	/**
	 * 根据id查询投票排行下的未被删除的文章
	 * 
	 * @param id
	 *            专题策划id
	 * @return
	 * @throws Exception
	 */
	int selectCountFromLive(String id) throws Exception;

}
