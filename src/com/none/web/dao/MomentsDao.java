package com.none.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.none.core.model.Pager;
import com.none.web.po.CMSMomentListPO;
import com.none.web.po.CommentPO;
import com.none.web.po.MomentDetailPO;
import com.none.web.po.MomentPO;
import com.none.web.po.MomsExcelPO;
import com.none.web.po.PrizepeoPO;
import com.none.web.po.UserAppPO;
import com.none.web.utils.BaseDao;

@Repository
public interface MomentsDao extends BaseDao<MomentsDao> {
	/**
	 * 插入moment
	 * 
	 * @param po
	 *            Momment对象
	 * @throws Exception
	 */
	void insertMoment(@Param("momentPO") MomentPO po) throws Exception;

	/**
	 * 更新moment
	 * 
	 * @param po
	 *            Momment对象
	 * @throws Exception
	 */
	void updateMoment(MomentDetailPO po) throws Exception;

	/**
	 * 通过id删除moment
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	MomentDetailPO selectMomentById(Map<String, Object> map) throws Exception;

	/**
	 * 通过id删除moment
	 * 
	 * @param id
	 * @throws Exception
	 */
	void deleteMomentById(int id) throws Exception;

	/**
	 * 根据id修改moment的状态
	 * 
	 * @param map
	 * @throws Exception
	 */
	void updateMomState(Map<String, String> map) throws Exception;

	/**
	 * 插入评论
	 * 
	 * @param po
	 *            评论实体类
	 * @throws Exception
	 */
	void insertComment(CommentPO po) throws Exception;

	/**
	 * 查询cms的moments列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<CMSMomentListPO> selectMomList(Map<String, Object> map) throws Exception;

	/**
	 * 查询cms的CP用户的moments列表
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<CMSMomentListPO> selectCPMomList(Map<String, Object> map) throws Exception;

	/**
	 * 查询列表总数
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int selectMomListCount(Map<String, Object> map) throws Exception;

	/**
	 * 查询CP列表总数
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int selectCPMomListCount(Map<String, Object> map) throws Exception;

	/**
	 * 按时间查询导出excel所需的数据
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<MomsExcelPO> selectMomsExcel(Map<String, Object> map) throws Exception;

	/**
	 * 通过id查询多条
	 */
	MomentPO selectById(String ids) throws Exception;

	// APP端分页查询////////////////////////////////////
	/**
	 * 
	 * @Title: selectAppMomentsRecordsDESC @Description:
	 *         TODO(分页查询moments列表) @param @param
	 *         pager @param @return @param @throws Exception 设定文件 @return List
	 *         <AppMomentsPO> 返回类型 @throws
	 */
	List<MomentPO> selectAppMomentsDESC(Pager pager) throws Exception;

	/**
	 * 
	 * @Title: searchTotalCount @Description: TODO(查询总记录数) @param @param
	 *         pager @param @return @param @throws Exception 参数 @return int
	 *         返回类型 @throws
	 */
	int searchTotalCount(Pager pager) throws Exception;

	// 根据传入的id来查询用户的id
	String searchUserIdById(int id) throws Exception;
	/**
	 * 查询'第1个帖子，第100个帖子，第200个帖子，第500个帖子'文章列表
	 * @return
	 */
	List<MomentPO> selectLotteryMomentsDESC(Pager pager) throws Exception;
	/**
	 * 查询'第1个帖子，第100个帖子，第200个帖子，第500个帖子'列表总数
	 * @param pager
	 * @return
	 * @throws Exception
	 */
	Integer selectLotteryMomentCount(Pager pager) throws Exception;
	/**
	 * 查询'我的文章'列表
	 * @param pager 入参包含分页参数和logonUserId
	 * @return
	 */
	List<MomentPO> selectMyAppMomentsDESC(Pager pager) throws Exception;
	/**
	 * 查询'我的文章'列表总数
	 * @param pager
	 * @return
	 */
	Integer getMyMomentTotalCount(Pager pager) throws Exception;
	/**
	 * 查询'我的收藏'列表
	 * @param pager
	 * @return
	 */
	List<MomentPO> selectMyCollectionMomentsDESC(Pager pager) throws Exception;
	/**
	 * 查询'我的收藏'列表总数
	 * @param pager
	 * @return
	 */
	Integer selectMyCollectionMomentsCount(Pager pager) throws Exception;
	/**
	 * 获取在app端发布moments的帖子总数
	 * @return
	 * @throws Exception
	 */
	int getMomentTotalCount() throws Exception;
	/**
	 * 获取当前app用户发布的帖子数
	 * @param logonUserId
	 * @return
	 * @throws Exception
	 */
	int selectCurrentUserMomentCount(String logonUserId) throws Exception;
	/**
	 * 获取我的奖品
	 * @param logonUserId
	 * @return
	 * @throws Exception
	 */
	List<PrizepeoPO> getMyPrizeByUserId(String logonUserId) throws Exception;
	/**
	 * 获取我的奖品数
	 * @param logonUserId
	 * @return
	 * @throws Exception
	 */
	int selectPrizeCountByUserId(String logonUserId) throws Exception;
	/**
	 * 获取Top3获奖人员信息
	 * @return
	 * @throws Exception
	 */
	List<UserAppPO> selectTopThreeUserinfoDESC() throws Exception;

	List<UserAppPO> selectThunderUserInfo() throws Exception;

	List<String> selectStaffList() throws Exception;
}
