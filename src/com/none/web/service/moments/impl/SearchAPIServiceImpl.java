package com.none.web.service.moments.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.none.web.dao.BuDao;
import com.none.web.po.BuPO;
import com.none.web.service.moments.IMomentsDetailAPIService;
import com.none.web.service.moments.ISearchAPIService;
import com.none.web.utils.ValidateFieldUtils;

@Service
public class SearchAPIServiceImpl implements ISearchAPIService {

	public static Logger logger = Logger.getLogger(SearchAPIServiceImpl.class);

	@Autowired
	private BuDao buDao;

	@Autowired
	private IMomentsDetailAPIService momentsService;

	@Override
	public List<BuPO> searchUserMatchField(String field) throws Exception {

		List<BuPO> list = buDao.selectBuByBuname(field);
		return list;
		// String searchSql = "select user_id, user_name, headPortrait, isVIP
		// from t_user where state = 1 and user_name like '%"
		// + field + "%'";
		// 显示组
		// String searchSql = "select user_id, branch as user_name,
		// headPortrait, isVIP from t_user where state = 1 and branch like '%"
		// + field + "%'";
		//
		// List<TUser> userList = jdbcTemplate.query(searchSql, new
		// RowMapper<TUser>() {
		// @Override
		// public TUser mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TUser user = new TUser();
		// user.setUserId(rs.getString("user_id"));
		// user.setUserName(rs.getString("user_name"));
		// user.setHeadPortrait(rs.getString("headPortrait"));
		// user.setIsVIP(rs.getString("isVIP"));
		// return user;
		// }
		// });
		// return userList;
	}

	// @Override
	// public List<TUser> searchUserMatchField(String field) {
	//
	// if (!validateInputField(field)) {
	// logger.info("please check the input field, thanks");
	// return null;
	// }
	// String searchSql = "select u.user_id, u.user_name,u.branch AS
	// displayName, u.headPortrait, u.isVIP from t_user u LEFT JOIN t_moments m
	// ON m.submit_user = u.user_id WHERE m.content LIKE '%"
	// + field + "%' AND u.state = 1 ";
	// List<TUser> userList = jdbcTemplate.query(searchSql, new
	// RowMapper<TUser>() {
	// @Override
	// public TUser mapRow(ResultSet rs, int rowNum) throws SQLException {
	// TUser user = new TUser();
	// user.setUserId(rs.getString("user_id"));
	// user.setUserName(rs.getString("user_name"));
	// user.setHeadPortrait(rs.getString("headPortrait"));
	// user.setIsVIP(rs.getString("isVIP"));
	// user.setDisplayName(rs.getString("displayName"));
	// return user;
	// }
	// });
	// return userList;
	// }

	@Override
	public Map<String, Object> searchMomentsByUserId(String logonUserId, String searchUserId, Integer pageNo,
			Integer pageSize, Integer cCurrentPage, Integer cNumperPage) throws Exception {

		Map<String, Object> searchMoments = momentsService.queryMomentsByUserId(logonUserId, searchUserId, pageNo,
				pageSize, cCurrentPage, cNumperPage);
		return searchMoments;
	}
}
