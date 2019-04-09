package com.none.web.service.moments.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.none.web.dao.MomsLikeDao;
import com.none.web.po.MomentCollectionPO;
import com.none.web.po.MomsLikePO;
import com.none.web.service.moments.IMomsLikeService;

@Service
public class MomsLikeServiceImpl implements IMomsLikeService {

	@Autowired
	private MomsLikeDao appMomsLikeDao;

	// 需求：将userId改成logonUserId
	// 通过logonUserId和momentsId查询status，如果查询结果返回null，就是没有这条记录，name就是没有点赞
	// 返回一个activeNum，取消点赞返回1，点赞返回0
	// 这里的逻辑主要是要筛分掉，查出来没哟这条记录的情况。
	@Override
	public Integer selectIsLikeForMoments(@Param("userId") String logonUserId, @Param("momentsId") int momentsId)
			throws Exception {
		Integer activeNum = 0;
		Integer num = appMomsLikeDao.selectIsLikeForMoments(logonUserId, momentsId);
		if (num == null || num == 1) {
			activeNum = 1;// 取消点赞
		}
		return activeNum;
	}

	@Override
	public Map<String, Object> saveOrUpdatecollectionMom(MomentCollectionPO collectionPO) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		boolean result = true;

		// 查询这个人是否收藏过该文章
		MomentCollectionPO resPO = appMomsLikeDao.selectIsCollection(collectionPO);

		// 说明收藏过
		if (null != resPO) {
			result = collectionOrNot(collectionPO, resPO, result);

			appMomsLikeDao.updateCollection(collectionPO);
		} else {
			appMomsLikeDao.insertCollection(collectionPO);
		}

		int i = appMomsLikeDao.selectCountOfColl(collectionPO.getMomentId());

		map.put("result", result);
		map.put("count", i);
		return map;

	}

	private boolean collectionOrNot(MomentCollectionPO collectionPO, MomentCollectionPO resPO, boolean result) {
		// 判断是收藏还是取消收藏
		if (0 == resPO.getStatus()) {
			result = false;
			collectionPO.setStatus(1);
		} else {
			collectionPO.setStatus(0);
		}
		collectionPO.setCollectionTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		return result;
	}

	@Override
	public Map<String, Object> updateActiveStatus(MomsLikePO momsLikePO) throws Exception {

		boolean result = true;
		// 查询用户id对moments的点赞状态，0：点赞，1：取消点赞，null：没有记录
		Integer status = appMomsLikeDao.selectLikeStatus(momsLikePO);

		if (status != null) {// 能查询到，说明有记录，只是需要更新点赞状态。
			// 有点赞信息
			// 更新点赞信息传入状态0或者1(0：点赞，1：取消点赞)
			if (0 == status) {
				momsLikePO.setStatus(1);
				result = false;
			} else {
				momsLikePO.setStatus(0);
			}
			appMomsLikeDao.updateActiveStatus(momsLikePO);
		} else {// 插入点赞信息，第一次插入是点赞,传入0
			appMomsLikeDao.insertLikeRecord(momsLikePO);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 点赞数量
		map.put("likeNum", appMomsLikeDao.selectLikeCountByMomentsId(momsLikePO.getMomentsId()));
		// 点赞状态
		map.put("activeStatus", result);
		return map;
	}

}
