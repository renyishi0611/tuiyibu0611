package com.none.web.service.moments.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.none.core.model.Pager;
import com.none.web.dao.CommentsDao;
import com.none.web.dao.UserAppDao;
import com.none.web.po.CommentPO;
import com.none.web.po.UserAppPO;
import com.none.web.service.moments.ICommentsService;

@Service
public class CommentsServiceImpl implements ICommentsService {
	@Autowired
	private CommentsDao appCommentsDao;
	@Autowired
	private UserAppDao userAppDao;
	/**
	 * 根据momentsId查询评论列表，显示最近的两条
	 */
	@Override
	public List<CommentPO> getAppCommentsList(int appmomentsId) throws Exception {
		
		return appCommentsDao.selectAppCommentsListByMomentsId(appmomentsId);
	}
	/**
	 * 根据momentsId查询评论总数
	 */
	@Override
	public int getAppCommentsCount(int appmomentsId) throws Exception {
		return appCommentsDao.selectAppCommentsCountByMomentsId(appmomentsId);
	}

	@Override
	public Object getAppCommentsByMomId(Pager pager, Integer momentsId) throws Exception {
		pager.setStartRow((pager.getPageNo() - 1) * pager.getPageSize());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pager", pager);
		map.put("momentsId", momentsId);
		
		List<CommentPO> list = appCommentsDao.getAppCommentsByMomId(map);
		for (CommentPO commentPO : list) {
			UserAppPO userAppPO = userAppDao.selectUserAppById(commentPO.getUserId());
			commentPO.setUserPhoto(userAppPO.getPhoto());
		}
		return list;
	}
	
	@Override
	public Pager getCommentCountByMomId(Pager pager, Integer momentsId) throws Exception {
		pager.setStartRow((pager.getPageNo() - 1) * pager.getPageSize());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pager", pager);
		map.put("momentsId", momentsId);
		Integer totalRows = appCommentsDao.getCommentCountByMomId(map);
		
		pager.setTotalRows(totalRows);
		pager.setTotalPages(totalRows / pager.getPageSize() == 0 ? (totalRows / pager.getPageSize()) : (totalRows / pager.getPageSize()) + 1);

		return pager;
	}
	
	@Override
	public String getCurrentUserPhoto(String currentLogonUserId) throws Exception {
		return appCommentsDao.selectCurrentUserPhoto(currentLogonUserId);
	}
}
