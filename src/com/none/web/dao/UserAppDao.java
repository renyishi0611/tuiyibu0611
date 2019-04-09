package com.none.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.none.web.po.ControlActivePO;
import com.none.web.po.LotteryPO;
import com.none.web.po.PrizepeoPO;
import com.none.web.po.UserAppPO;
import com.none.web.utils.BaseDao;

@Repository
public interface UserAppDao extends BaseDao<UserAppPO> {

	/**
	 * 修改
	 */
	// int update(String userId, String accept) throws Exception;

	int update(@Param("userId") String userId, @Param("accept") String accept) throws Exception;

	/**
	 * 添加
	 */
	int saveUserApp(UserAppPO userApp) throws Exception;

	/**
	 * 查询所有
	 */
	List<UserAppPO> selectUserAppList() throws Exception;

	/**
	 * 按id查询
	 */
	UserAppPO selectUserAppById(String userId) throws Exception;

	/**
	 * 按id查询
	 */
	UserAppPO selectUserById(String userId) throws Exception;

	/**
	 * 按条件查询App端的用户
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<UserAppPO> selectUserAppList(Map<String, Object> map) throws Exception;

	/**
	 * 按条件查询App端的用户的数量
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int selectUserAppListCount(Map<String, Object> map) throws Exception;

	/**
	 * 查询当前数据库在职员工
	 * 
	 * @return
	 * @throws Exception
	 */
	List<String> selectStaffIds() throws Exception;

	/**
	 * 插入白名单
	 * 
	 * @param staffIds
	 * @throws Exception
	 */
	void insertWhiteList(List<Map<String, String>> staffIds) throws Exception;

	/**
	 * 修改白名单状态
	 * 
	 * @param list
	 * @throws Exception
	 */
	void updateWhieListStatus(List<Map<String, String>> list) throws Exception;

	/**
	 * 修改userAPP中的状态
	 * 
	 * @param list
	 * @throws Exception
	 */
	void updateUserAppStatus(List<Map<String, String>> list) throws Exception;

	List<UserAppPO> getUserInfoByUDID(@Param("udid") String udid) throws Exception;

	UserAppPO selectUser(@Param("userId") String userId, @Param("userUDID") String udid) throws Exception;

	/**
	 * 查询MyAccount
	 */
	UserAppPO selectMyAccount(String userId) throws Exception;

	/**
	 * 修改MyAccount信息
	 */
	void updateMyAccount(UserAppPO userAppPO) throws Exception;

	/**
	 * 第一次插入用户信息
	 * 
	 * @param userAppPO
	 * @return
	 * @throws Exception
	 */
	int insertUserInfo(UserAppPO userAppPO) throws Exception;

	/**
	 * 根据staffid查询user信息
	 * 
	 * @param staffId
	 * @return
	 * @throws Exception
	 */
	UserAppPO selectUserInfoByStaffId(String staffId) throws Exception;

	/**
	 * 根据userID修改摇奖次数
	 * 
	 * @param userId
	 * @throws Exception
	 */
	void udateLotteryNum(LotteryPO po) throws Exception;

	/**
	 * 查询排行榜
	 * 
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectRankingList() throws Exception;

	/**
	 * 控制活动的状态和中奖人数
	 * 
	 * @param po
	 * @throws Exception
	 */
	void updateActiveStaus(ControlActivePO po) throws Exception;

	void insertPrizePeo(@Param("id") String id, @Param("activeId") String activeId, @Param("staffId") String staffId,
			@Param("prizeLevel") int prizeLev, @Param("bu") String bu) throws Exception;

	Map<String, Object> selectIfLucky(@Param("staffId") String staffId, @Param("activeId") String activeId)
			throws Exception;

	String selectActiveStatus(@Param("id") String id) throws Exception;

	void insertPrizePeoBatch(List<PrizepeoPO> list) throws Exception;

	int selectUserCountByUserId(String userId) throws Exception;

	String selectStaffIdByUserId(String logonUserId) throws Exception;

	void insertPrize(PrizepeoPO prizepeoPO) throws Exception;
	
	int selectPrizeUserByMomentId(int momentId) throws Exception;
	
	void deletePrizeUserByMomentId(int momentId) throws Exception;

	List<Map<String, String>> selectActiveStatusMap() throws Exception;

	List<UserAppPO> selectTopThreePrizePeople() throws Exception;

	void deleteTop3PrizePeoples() throws Exception;
}
