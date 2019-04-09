package com.none.web.service.moments;

import java.util.Map;

import com.none.web.po.MomentCollectionPO;
import com.none.web.po.MomsLikePO;

public interface IMomsLikeService {

	/**
	 * 
	 * @Title: selectIsLikeForMoments @Description:
	 *         TODO(查询当前用户是否点赞) @param @param userId @param @param
	 *         momentsId @param @return @param @throws Exception 参数 @return
	 *         Integer 返回类型 @throws
	 */
	Integer selectIsLikeForMoments(String logonUserId, int momentsId) throws Exception;// userId改成logonUserId

	/**
	 * 收藏或者取消收藏文章
	 * 
	 * @param collectionPO
	 * @throws Exception
	 */
	Map<String, Object> saveOrUpdatecollectionMom(MomentCollectionPO collectionPO) throws Exception;

	// 点赞
	Map<String, Object> updateActiveStatus(MomsLikePO momsLikePO) throws Exception;

}
