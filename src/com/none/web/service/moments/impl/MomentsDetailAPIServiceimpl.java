package com.none.web.service.moments.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.none.core.common.utils.DateUtil;
import com.none.core.model.Pager;
import com.none.web.dao.BuDao;
import com.none.web.dao.CommentsDao;
import com.none.web.dao.MomsLikeDao;
import com.none.web.po.CommentPO;
import com.none.web.po.MomentsPO;
import com.none.web.po.MomsPathPO;
import com.none.web.service.moments.IMomentsDetailAPIService;
import com.none.web.service.user.IUserService;
import com.none.web.utils.PageHelper;

@Service
public class MomentsDetailAPIServiceimpl implements IMomentsDetailAPIService {

	public static Logger logger = Logger.getLogger(MomentsDetailAPIServiceimpl.class);

	@Autowired
	private CommentsDao commentsDao;

	@Autowired
	private MomsLikeDao momsLikeDao;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private IUserService userService;

	@Autowired
	private BuDao budao;

	private int totalPage;

	@Override
	public Map<String, Object> queryMomentsByMomentsId(String logonUserId, Integer momentsId, Integer cCurrentPage,
			Integer cNumperPage) throws Exception {

		String sqlWhere = "id=" + momentsId;
		List<Map<String, Object>> momentsPOList = queryMomentsByWhere(logonUserId, sqlWhere, 1, 10, cCurrentPage,
				cNumperPage);
		if (!momentsPOList.isEmpty() && null != momentsPOList.get(0)) {
			return momentsPOList.get(0);
		}
		return null;
	}

	@Override
	public List<CommentPO> queryCommentByMomentsId(Integer momentsId) {
		String tenItemCommentSql = "select id, moments_id, submit_time, user_id, contents, `status` from t_moms_comments where moments_id="
				+ momentsId;

		List<CommentPO> commentPOs = jdbcTemplate.query(tenItemCommentSql, new RowMapper<CommentPO>() {

			public CommentPO mapRow(ResultSet rs, int index) throws SQLException {
				CommentPO commentPO = new CommentPO();
				commentPO.setId(rs.getInt("id"));
				commentPO.setMomentsId(rs.getInt("moments_id"));
				commentPO.setStatus(rs.getInt("status"));
				commentPO.setSubmitTime(rs.getString("submit_time"));
				commentPO.setUserId(rs.getString("user_id"));
				return commentPO;
			}
		});

		return commentPOs;
	}

	@Override
	public List<Map<String, Object>> queryMomsPathByMomentsId(Integer momentsId) throws DataAccessException {
		String detailSql = "select file_screenshot as fileScreenshot, file_path as filePath,file_type as fileType from t_moms_paths where is_delete =0 and moments_id="
				+ momentsId + " ORDER BY id";
		List<Map<String, Object>> pathPOs = jdbcTemplate.queryForList(detailSql);

		return pathPOs;
	}

	@Override
	public Map<String, Object> queryMomentsByMomentsId(Integer momentsId) throws Exception {
		return queryMomentsByMomentsId(null, momentsId, 1, 2);

	}

	@Override
	public List<Map<String, Object>> queryCommentByMomentsIdForPage(Integer momentsId, Integer currentPage,
			Integer numPerPage) throws Exception {
		Pager pager = new Pager();
		pager.setPageNo(currentPage);
		pager.setPageSize(numPerPage);
		return queryNewCommentByMomentsId(momentsId, pager);
	}

	@Override
	public void getMomentDetail(String logonUserId, Map<String, Object> mommentMap, Integer cCurrentPage,
			Integer cNumPerPage) throws Exception {
		// MomentsPO mommentPO = queryMomentsByMomentsId(momentsId);
		if (null == mommentMap || mommentMap.isEmpty()) {
			return;
		}

		Integer momentsId = (Integer) mommentMap.get("momentsId");
		mommentMap.put("submitTime", DateUtil.staffTime(((Timestamp) mommentMap.get("submitTime")).toString()));
		mommentMap.put("momentUsernameAndImgAndIsVIP", queryUserDetailInfo((String) mommentMap.get("buId")));
		Pager pager = new Pager();
		pager.setPageNo(cCurrentPage);
		pager.setPageSize(cNumPerPage);
		List<Map<String, Object>> commentList = queryNewCommentByMomentsId(momentsId, pager);
		List<Map<String, Object>> imagePathList = queryMomsPathByMomentsId(momentsId);

		mommentMap.put("commentList", commentList);
		mommentMap.put("imagePathList", imagePathList);
		if (null != logonUserId) {
			mommentMap.put("isLike", getCurrentUserIsLike(logonUserId, momentsId));
		}

		mommentMap.put("totalLikes", totalMomsLikesByMomentsId(momentsId));
		mommentMap.put("totalComment", getCommentCountByMomentId(momentsId));
		mommentMap.put("pager", pager);
	}

	@Override
	public boolean insertCommentByMomentsId(CommentPO po, String logonUserId) {
		if (org.apache.commons.lang.StringUtils.isEmpty(logonUserId) || logonUserId.equals("null")) {
			logger.info("the loginUser id is null, please check");
			return false;
		}
		String insertSql = "insert into t_moms_comments (moments_id, submit_time, user_id, contents, `status`, platform) values(?, CURRENT_TIMESTAMP, ?, ?, 0, ?)";
		Object[] args = { po.getMomentsId(), logonUserId, po.getContents(), po.getPlatform() };

		int temp = jdbcTemplate.update(insertSql, args);
		return temp > 0 ? true : false;
	}

	@Override
	public Map<String, Object> queryMomentsByUserId(String logonUserId, String searchUserId, Integer mCurrentPage,
			Integer mNumperPage, Integer cCurrentPage, Integer cNumperPage) throws Exception {

		Map<String, Object> result = queryMomentsByUserIdWithMomentsTotal(logonUserId, searchUserId, mCurrentPage,
				mNumperPage, cCurrentPage, cNumperPage);
		return result;
	}

	@Override
	public List<Map<String, Object>> queryForPage(String sql, int currentPage, int numPerPage) {
		PageHelper pageHelp = new PageHelper(sql, currentPage, numPerPage, jdbcTemplate);
		List<Map<String, Object>> lists = pageHelp.getResultList();
		setTotalPage(pageHelp.getTotalPages());
		return lists;
	}

	@Override
	public List<MomsPathPO> showPicList(Integer currentPage, Integer numPerPage) throws Exception {

		String queryImageSql = "select distinct moments_id, max(submit_time) submit_time, user_id,  file_screenshot, file_path, file_type from t_moms_paths where file_type='image' group by moments_id";

		List<Map<String, Object>> lists = queryForPage(queryImageSql, currentPage.intValue(), numPerPage.intValue());

		List<MomsPathPO> momsPathPOs = new ArrayList<MomsPathPO>();

		for (Map<String, Object> map : lists) {
			MomsPathPO momsPathPO = new MomsPathPO();
			momsPathPO.setMomentsId((Integer) map.get("moments_id"));
			momsPathPO.setSubmitTime(DateUtil.staffTime(((Timestamp) map.get("submit_time")).toString()));
			momsPathPO.setFileScreenshot((String) map.get("file_screenshot"));
			momsPathPO.setUserId((String) map.get("user_id"));
			momsPathPO.setFilePath((String) map.get("file_path"));
			momsPathPO.setFileType((String) map.get("file_type"));
			momsPathPOs.add(momsPathPO);
		}

		return momsPathPOs;
	}

	@Override
	public int totalMomsLikesByMomentsId(Integer momentsId) {
		int totalLikes = 0;

		try {
			totalLikes = momsLikeDao.selectLikeCountByMomentsId(momentsId);
		} catch (Exception e) {
			logger.error("query the data has an issue, please chek", e);
		}

		return totalLikes;
	}

	@Override
	public int getCommentCountByMomentId(int momentsId) throws Exception {
		int totalComments = 0;

		try {
			totalComments = commentsDao.selectAppCommentsCountByMomentsId(momentsId);

		} catch (Exception e) {
			logger.error("query the data has an issue, please chek", e);
		}

		return totalComments;
	}

	public boolean getCurrentUserIsLike(String userId, int momentsId) {
		String sql = "SELECT `status` FROM t_moms_likes WHERE user_id= ? AND moments_id=?";
		Map<String, Object> result = null;

		try {
			result = jdbcTemplate.queryForMap(sql, userId, momentsId);
		} catch (DataAccessException e) {
			return false;
		}

		if (null != result && 1 == result.size()) {
			return 0 == (Integer) result.get("status");
		}
		return false;
	}

	public List<Map<String, Object>> queryMomentsByWhere(String logonUserId, String sqlWhere, int currentPage,
			int numPerPage, int cCurrentPage, int cNumperPage) throws Exception {

		String momentSql = "select m.id as momentsId, m.content as content, m.submit_type as submitType, m.submit_user as submitUser, u.branch as displayName,"
				+ "m.submit_time as submitTime, m.lastupdate_time as lastUpdateTime,b.id AS buId"
				+ " from t_moments m LEFT JOIN t_user u ON m.submit_user = u.user_id LEFT JOIN t_bu b ON u.groupId = b.id where 1=1 and m.is_delete=0 "
				+ " and m." + sqlWhere;
		List<Map<String, Object>> momentsPOs = queryForPage(momentSql, currentPage, numPerPage);

		if (null == momentsPOs) {
			return new ArrayList<Map<String, Object>>();
		}

		for (Map<String, Object> map : momentsPOs) {
			getMomentDetail(logonUserId, map, cCurrentPage, cNumperPage);
		}
		return momentsPOs;
	}

	public Map<String, Object> queryMomentsByUserIdWithMomentsTotal(String logonUserId, String searchUserId,
			int currentPage, int numPerPage, int cCurrentPage, int cNumperPage) throws Exception {
		// String momentSql = "select m.id as momentsId, m.content as content,
		// m.submit_type as submitType, "
		// + "m.submit_time as submitTime, m.lastupdate_time as lastUpdateTime"
		// + " from t_moments m where 1=1 and m.is_delete=0 and
		// submit_type='Published'" + " and m.submit_user= '"
		// + searchUserId + "'";

		String momentSql = "select m.id as momentsId, m.content as content, m.submit_type as submitType, "
				+ "m.submit_time as submitTime, m.lastupdate_time as lastUpdateTime"
				+ " from t_moments m where 1=1 and m.is_delete=0 and submit_type='Published'"
				+ " and m.submit_user in (select user_id from t_user where groupId= '" + searchUserId
				+ "' and state = 1)  ORDER BY m.lastupdate_time DESC";

		PageHelper pageHelp = new PageHelper(momentSql, currentPage, numPerPage, jdbcTemplate);
		List<Map<String, Object>> lists = pageHelp.getResultList();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> commentList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> imagePathList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> subPathList = new ArrayList<Map<String, Object>>();
		Pager commentPager = new Pager();
		commentPager.setPageNo(cCurrentPage);
		commentPager.setPageSize(cNumperPage);
		for (Map<String, Object> map : lists) {
			if (null == map || map.isEmpty()) {
				continue;
			}

			Integer momentsId = (Integer) map.get("momentsId");
			map.put("submitTime", DateUtil.staffTime(((Timestamp) map.get("submitTime")).toString()));
			map.put("usernameAndImg", queryUserDetailInfo(searchUserId));
			commentList = queryNewCommentByMomentsId(momentsId, commentPager);
			imagePathList = queryMomsPathByMomentsId(momentsId);
			if (null != imagePathList && imagePathList.size() > 0) {
				subPathList = imagePathList.subList(0, imagePathList.size() > 5 ? 4 : imagePathList.size());
			}

			map.put("comments", commentList);
			map.put("pathNum", null == imagePathList ? 0 : imagePathList.size());

			map.put("filePath", subPathList);

			map.put("activedStatus", getCurrentUserIsLike(logonUserId, momentsId));

			map.put("likeNum", totalMomsLikesByMomentsId(momentsId));
			map.put("commentsNum", getCommentCountByMomentId(momentsId));
		}

		Map<String, Integer> pager = new HashMap<String, Integer>();
		pager.put("pageNo", currentPage);
		pager.put("totalPages", pageHelp.getTotalPages());
		resultMap.put("pager", pager);
		resultMap.put("momentsList", lists);
		return resultMap;
	}

	private Map<String, Object> queryUserDetailInfo(String buId) throws DataAccessException {
		String userSql = "select buPhoto as headPortrait, bu as displayName from t_bu where id='" + buId + "'";
		Map<String, Object> userInfo = null;
		try {
			userInfo = jdbcTemplate.queryForMap(userSql);
		} catch (DataAccessException e) {
			return null;
		}

		return userInfo;
	}

	class MomsPathRowMapper implements RowMapper<MomsPathPO> {

		public MomsPathPO mapRow(ResultSet rs, int index) throws SQLException {
			MomsPathPO pathPO = new MomsPathPO();
			pathPO.setId(rs.getInt("id"));
			pathPO.setMomentsId(rs.getInt("moments_id"));
			pathPO.setSubmitTime(rs.getString("submit_time"));
			pathPO.setUserId(rs.getString("user_id"));
			pathPO.setFilePath(rs.getString("file_path"));
			pathPO.setFileType(rs.getString("file_type"));
			return pathPO;
		}
	}

	class MomentsRowMapper implements RowMapper<MomentsPO> {

		public MomentsPO mapRow(ResultSet rs, int index) throws SQLException {
			MomentsPO momentsPO = new MomentsPO();
			momentsPO.setId(rs.getInt("id"));
			momentsPO.setSubmitTime(rs.getString("submit_time"));
			momentsPO.setContent(rs.getString("content"));
			momentsPO.setUserId(rs.getString("user_id"));
			momentsPO.setStatus((Integer) rs.getInt("status"));
			momentsPO.setType(rs.getInt("type"));
			return momentsPO;
		}
	}

	public List<Map<String, Object>> queryNewCommentByMomentsId(Integer momentsId, Pager pager) throws Exception {
		String pageSql = " select c.id as commentId, c.submit_time as submitTime, c.user_id as userId, c.contents as contents, c.`status` as `status`, c.platform as platform from t_moms_comments c "
				+ "  where moments_id=" + momentsId + " and `status`=0 order by submit_time desc";

		// PageHelper pageHelp = new PageHelper(tenItemCommentSql, 1, 10,
		// jdbcTemplate);

		List<Map<String, Object>> lists = queryForPage(pageSql, pager);
		List<Map<String, Object>> commentList = new ArrayList<Map<String, Object>>();
		Map<String, Object> userMap = new HashMap<String, Object>();
		Map<String, Object> comment = new HashMap<String, Object>();
		for (Map<String, Object> map : lists) {
			String platform = (String) map.get("platform");
			if ("app".equals(platform)) {
				userMap = userService.getUserInfoByUserIdAndPlatform((String) map.get("userId"), platform);
			} else {
				userMap = budao.getBuNameAndPhotoByUserId((String) map.get("userId"));
				userMap.put("isVIP", "false");
			}

			comment = new HashMap<String, Object>();
			comment.put("commentId", map.get("commentId"));
			comment.put("userId", map.get("userId"));
			comment.put("contents", map.get("contents"));
			comment.put("status", map.get("status"));
			comment.put("commentsUsernameAndImgAndIsVIP", userMap);
			comment.put("submitTime", null == map.get("submitTime") ? null
					: DateUtil.staffTime(((Timestamp) map.get("submitTime")).toString()));
			commentList.add(comment);
		}

		return commentList;
	}
	
	@Override
	public String getUserPhotoByUserId(String logonUserId) throws Exception {
		return commentsDao.selectCurrentUserPhoto(logonUserId);
	}

	private List<Map<String, Object>> queryForPage(String pageSql, Pager pager) {
		PageHelper pageHelp = new PageHelper(pageSql, pager.getPageNo(), pager.getPageSize(), jdbcTemplate);
		List<Map<String, Object>> lists = pageHelp.getResultList();
		pager.setPageNo(pageHelp.getCurrentPage());
		pager.setTotalPages(pageHelp.getTotalPages());
		return lists;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}
