package com.none.web.service.moments;

import java.util.List;

import com.none.core.model.Pager;
import com.none.web.po.CommentPO;

public interface ICommentsService {
	//根据momentsId查询评论列表，显示最近的两条
	List<CommentPO> getAppCommentsList(int appmomentsId) throws Exception;
	//根据momentsId查询评论的总数
	int getAppCommentsCount(int appmomentsId) throws Exception;
	/**
	 * 查询该momentsId下的评论列表
	 * @param pager 分页对象
	 * @param momentsId 文章id
	 * @return
	 * @throws Exception
	 */
	Object getAppCommentsByMomId(Pager pager, Integer momentsId) throws Exception;
	/**
	 * 查询该momentsId下评论列表的总数
	 * @param pager
	 * @param momentsId
	 * @return
	 * @throws Exception
	 */
	Pager getCommentCountByMomId(Pager pager, Integer momentsId) throws Exception;
	/**
	 * 获取当前登录人的头像
	 * @param currentLogonUserId
	 * @return
	 * @throws Exception
	 */
	String getCurrentUserPhoto(String currentLogonUserId) throws Exception;
}
