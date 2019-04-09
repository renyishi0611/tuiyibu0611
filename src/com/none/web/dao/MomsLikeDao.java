package com.none.web.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.none.web.po.MomentCollectionPO;
import com.none.web.po.MomsLikePO;
import com.none.web.utils.BaseDao;

@Repository
public interface MomsLikeDao extends BaseDao<MomsLikePO> {

	/**
	 * 
	 * @Title: selectLikeCountByMomentsId @Description:
	 *         TODO(获取点赞总数) @param @param
	 *         appmomentsId @param @return @param @throws Exception 参数 @return
	 *         int 返回类型 @throws
	 */
	int selectLikeCountByMomentsId(int appmomentsId) throws Exception;

	/**
	 * 
	 * @Title: selectIsLikeForMoments @Description:
	 *         TODO(判断当前用户该条appMoments点赞状态) @param @param
	 *         appmomentsId @param @param userId @param @return @param @throws
	 *         Exception 参数 @return Integer 返回类型 @throws
	 */
	Integer selectIsLikeForMoments(@Param("userId") String logonUserId, @Param("momentsId") int appmomentsId)
			throws Exception;

	/**
	 * 通过momentsId删除点赞
	 * 
	 * @param momentsId
	 * @throws Exception
	 */
	void deleteLikeByMomentsId(int momentsId) throws Exception;

	/**
	 * 查询该这个人对这个文章是否收藏过
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	MomentCollectionPO selectIsCollection(MomentCollectionPO collectionPO) throws Exception;

	/**
	 * 查询收藏数量
	 * 
	 * @param collectionPO
	 * @return
	 * @throws Exception
	 */
	int selectCountOfColl(int momentId) throws Exception;

	/**
	 * 收藏文章
	 * 
	 * @param map
	 * @throws Exception
	 */
	void insertCollection(MomentCollectionPO collectionPO) throws Exception;

	/**
	 * 跟新收藏状态
	 * 
	 * @param map
	 * @throws Exception
	 */
	void updateCollection(MomentCollectionPO collectionPO) throws Exception;

	// 更新点赞状态
	void updateActiveStatus(MomsLikePO momsLikePO) throws Exception;

	// 插入点赞信息
	void insertLikeRecord(MomsLikePO momsLikePO) throws Exception;

	// 查询点赞状态
	Integer selectLikeStatus(MomsLikePO momsLikePO) throws Exception;

	// 查询当前用户当前文章的收藏状态
	Integer selectStatusOfColl(Map<String, Object> map) throws Exception;

	// 查询当前用户是否有收藏记录
	int selectCurrentCollNum(Map<String, Object> map) throws Exception;

	void deleteCollectionByMomId(int momentId) throws Exception;
}
