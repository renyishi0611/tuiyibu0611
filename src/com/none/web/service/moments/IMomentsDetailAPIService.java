package com.none.web.service.moments;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.none.core.model.Pager;
import com.none.web.po.CommentPO;
import com.none.web.po.MomsPathPO;

public interface IMomentsDetailAPIService {

	public Map<String, Object> queryMomentsByMomentsId(String logonUserId, Integer momentsId, Integer cCurrentPage, Integer cNumperPage)  throws Exception;

	public List<CommentPO> queryCommentByMomentsId(Integer momentsId);

	public List<Map<String, Object>> queryMomsPathByMomentsId(Integer momentsId) throws DataAccessException;

	public Map<String,Object> queryMomentsByMomentsId(Integer momentsId) throws Exception;
	
	public List<Map<String, Object>> queryCommentByMomentsIdForPage(Integer momentsId, Integer currentPage, Integer numPerPage) throws Exception;
	
	public void getMomentDetail(String logonUserId, Map<String, Object> mommentMap, Integer cCurrentPage, Integer cNumPerPage) throws Exception;
	
	public boolean insertCommentByMomentsId(CommentPO po, String logonUserId);

	public Map<String, Object> queryMomentsByUserId(String logonUserId, String searchUserId, Integer mCurrentPage, Integer mNumperPage, Integer cCurrentPage, Integer cNumperPage) throws Exception ;
	
	public List<Map<String, Object>> queryForPage(String sql, int currentPage, int numPerPage);
	
	public List<MomsPathPO> showPicList(Integer currentPage, Integer numPerPage) throws Exception;
	
	public int totalMomsLikesByMomentsId(Integer momentsId) throws Exception;
	
	public List<Map<String, Object>> queryNewCommentByMomentsId(Integer momentsId, Pager pager) throws Exception;

	public int getCommentCountByMomentId(int momentsId) throws Exception;

	public String getUserPhotoByUserId(String logonUserId) throws Exception;
}
