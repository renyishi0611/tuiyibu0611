package com.none.web.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.none.web.po.CommentPO;
import com.none.web.utils.BaseDao;

@Repository
public interface CommentsDao extends BaseDao<CommentPO> {
	/**
	 * 
	 * @Title: selectAppCommentsListByMomentsId @Description:
	 * TODO(根据momentsId查询评论列表，显示最近的两条) @param @param
	 * appmomentId @param @return @param @throws Exception 设定文件 @return
	 * List<AppCommentPO> 返回类型 @throws
	 */
	List<CommentPO> selectAppCommentsListByMomentsId(int appmomentId) throws Exception;

	/**
	 * 
	 * @Title: selectAppCommentsCountByMomentsId @Description:
	 * TODO(根据momentsId查询评论的总数) @param @param
	 * appmomentId @param @return @param @throws Exception 设定文件 @return int
	 * 返回类型 @throws
	 */
	int selectAppCommentsCountByMomentsId(int appmomentId) throws Exception;

	/**
	 * 通过momentsId删除评论
	 * 
	 * @param momentsId
	 * @throws Exception
	 */
	void deleteCommentByMomentsId(int momentsId) throws Exception;

	/**
	 * 根据评论id删除评论
	 * 
	 * @param comId 评论id
	 * @throws Exception 抛出异常
	 */
	void delCommentByComId(int comId) throws Exception;
	/**
	 * 根据momentsId查询评论列表
	 * @param pager
	 * @return
	 * @throws Exception
	 */
	List<CommentPO> getAppCommentsByMomId(Map<String, Object> map) throws Exception;
	/**
	 * 查询该momentsId评论列表总数
	 * @param map
	 * @return
	 */
	Integer getCommentCountByMomId(Map<String, Object> map) throws Exception;
	/**
	 * 查询当前登录用户的头像
	 * @param currentLogonUserId
	 * @return
	 * @throws Exception
	 */
	String selectCurrentUserPhoto(String currentLogonUserId) throws Exception;
}
