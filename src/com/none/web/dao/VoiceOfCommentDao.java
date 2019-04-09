package com.none.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.none.web.po.CommentPO;
import com.none.web.po.ReqVoiceOfComListPO;
import com.none.web.po.UserAppPO;
import com.none.web.po.VoiceOfCommentPO;
import com.none.web.utils.BaseDao;

/**
 * 员工心声列表的dao
 * 
 * @author winter
 *
 */
@Repository
public interface VoiceOfCommentDao extends BaseDao<UserAppPO> {

	/**
	 * 评论列表展示
	 * 
	 * @param po
	 * @return
	 * @throws Exception
	 */
	List<VoiceOfCommentPO> selectVoiceOfComList(ReqVoiceOfComListPO po) throws Exception;

	/**
	 * 查询列表总数
	 * 
	 * @param voiceId
	 *            心声id
	 * @return
	 * @throws Exception
	 */
	int selectVoiceOfComListCount(Integer voiceId) throws Exception;

	/**
	 * 插入评论
	 * 
	 * @param po
	 *            评论实体类
	 * @throws Exception
	 */
	void insertComment(CommentPO po) throws Exception;

	/**
	 * 根据voiceId逻辑删除其下所有评论
	 * 
	 * @param voiceId
	 * @throws Exception
	 */
	void delCommentByVoiceId(int voiceId) throws Exception;

	/**
	 * 根据评论id删除评论
	 * 
	 * @param comId
	 * @throws Exception
	 */
	void delCommentByComId(int comId) throws Exception;

}
