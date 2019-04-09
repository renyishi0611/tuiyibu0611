package com.none.web.service.user.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.none.core.common.utils.CreateID;
import com.none.core.common.utils.MD5Util;
import com.none.core.common.utils.PathUtil;
import com.none.core.common.utils.PropertyUtil;
import com.none.core.common.utils.StringUtil;
import com.none.core.exception.ValidateException;
import com.none.core.model.Pager;
import com.none.core.service.BaseServiceSupport;
import com.none.web.common.SysConstant;
import com.none.web.model.TOnlineuser;
import com.none.web.model.TUser;
import com.none.web.service.user.IUserService;
import com.none.web.utils.StringRandomUtil;
import com.none.web.utils.UploadToOSSUtil;
import com.none.web.utils.ValidateFileUtil;

@Service("userService")
public class UserServiceImpl extends BaseServiceSupport implements IUserService {

	private static Logger logger = Logger.getLogger(UserServiceImpl.class);

	@Override
	public TUser mergeLoginConsoleUser(String userName, String password, HttpSession session,
			HttpServletRequest request) throws Exception {
		// 删除当前不在线人
		String sql = "delete from t_online_user where status=2";
		this.jdbcTemplate.execute(sql);

		if (StringUtils.isBlank(userName)) {
			throw new ValidateException("user.userNameNull");
		}
		if (StringUtils.isBlank(password)) {
			throw new ValidateException("user.passwordNull");
		}

		TUser user = null;
		Criteria userCriteria = getCriteria(TUser.class);
		userCriteria.add(Restrictions.eq("userName", userName));

		List<?> userList = userCriteria.list();
		if (!userList.isEmpty()) {
			logger.info("userList.size======" + userList.size());
			if (userList.size() == 1) {
				TUser tUser = (TUser) userList.get(0);

				// 判断用户是否已经登录
				TOnlineuser onlineuser = findEntity(TOnlineuser.class, tUser.getUserId());
				if (onlineuser != null) {
					long nowtimes = new Date().getTime();
					long logintimes = onlineuser.getLoginTime().getTime();
					long times = 0l;

					long autoOpttime = onlineuser.getAutoopttime().getTime();
					long times2 = nowtimes - autoOpttime;
					if (times2 > 1 * 60 * 1000) {// 处理非正常退出的情况
						deleteEntity(onlineuser);
					} else {
						if (onlineuser.getOpttime() == null) {
							times = nowtimes - logintimes;
						} else {
							long lastOpttime = onlineuser.getOpttime().getTime();
							times = nowtimes - lastOpttime;
						}

						if (times > 30 * 60 * 1000) {
							deleteEntity(onlineuser);
						} else {
							throw new ValidateException("user.hasLogged");
						}
					}
				}

				if (StringUtils.isBlank(tUser.getAccessRight())) { // 如果用户没有菜单的话，返回null
					return null;
				}
				String userPassword = tUser.getPassword();
				logger.info("userPassword=====" + userPassword + ";password=====" + password);
				if (password.equals(userPassword)) {
					logger.info("比对成功,更新登录时间");
					if (tUser.getState() == SysConstant.STATE_AVAILABLE) {
						user = tUser;
						// 更新最后登录时间
						tUser.setLastTime(new Timestamp(new Date().getTime()));
						updateEntity(tUser);
					} else {
						throw new ValidateException("user.NotExist");
					}
				} else {
					throw new ValidateException("user.userUnExist");
				}
				logger.info("用户头像=====" + tUser.getHeadPortrait());
				if (StringUtils.isBlank(tUser.getHeadPortrait())) { // 如果用户没有头像
					logger.info("设置默认头像");
					// 设置默认头像
					String fileName = PropertyUtil.getProperty("SystemConfig.properties", "user.DefaultHeadPortrait");
					String folderPath = PropertyUtil.getProperty("SystemConfig.properties", "oss.freemarker_img");
					user.setHeadPortrait(PathUtil.getOssPath(fileName, folderPath));
				}
			} else {
				throw new ValidateException("common.illegalRequest");
			}
		} else {
			logger.error("用户不存在" + userName);
			throw new ValidateException("user.userUnExist");
		}

		// 用户不在线，登录成功后生成一条数据
		TOnlineuser onlineuser = new TOnlineuser();
		onlineuser.setUserId(user.getUserId());
		Timestamp time = new Timestamp(new Date().getTime());
		onlineuser.setLoginTime(time);
		onlineuser.setStatus(1);
		onlineuser.setIp("");
		onlineuser.setMac("");
		onlineuser.setAutoopttime(time);

		saveEntity(onlineuser);
		return user;
	}

	@Override
	public Object addConsoleUser(MultipartFile multipartFile, TUser user, HttpServletRequest request) throws Exception {
		logger.info("user===" + user.toString());
		// 判断参数
		judgeIsBlank(user);
		// 判断文件格式
		ValidateFileUtil.validateImgFile(multipartFile);
		// 判断用户名是否已经存在
		judgeIsExist(user);
		// 上传图片
		// String filePath = uploadImg(multipartFile, request);
		String filePath = UploadToOSSUtil.uploadAliCloudOss(multipartFile, "image", "headPortraitImg");

		// 设置用户id
		String userId = CreateID.getID();
		user.setUserId(userId);
		// 设置用户密码
		user.setPassword(MD5Util.getMD5String(user.getPassword()).toLowerCase());
		// 默认可用
		user.setState(SysConstant.STATE_AVAILABLE);
		// 设置用户的时间
		user.setCreateTime(new Timestamp(new Date().getTime()));
		user.setLastTime(new Timestamp(new Date().getTime()));
		user.setLoginTime(new Timestamp(new Date().getTime()));
		// 设置用户默认头像
		user.setHeadPortrait(filePath);

		// 保存用户信息
		if (null == saveEntity(user)) {
			throw new ValidateException("common.sysException");
		}

		return user;
	}

	private void judgeIsExist(TUser user) throws ValidateException {
		// 根据username查询改用户是否已经存在
		Criteria userCriteria = getCriteria(TUser.class);
		userCriteria.add(Restrictions.eq("userName", user.getUserName()));
		List<?> userList1 = userCriteria.list();
		if (userList1.size() > 0) {
			throw new ValidateException("user.userNameIsExist");
		}

	}

	private void judgeIsBlank(TUser user) throws ValidateException {
		// 判断各参数是否为空
		if (StringUtils.isBlank(user.getUserName())) {
			throw new ValidateException("user.userNameNull");
		}

		// 用户名不能大于10.
		if (user.getUserName().length() > 10) {
			throw new ValidateException("user.userNameTooLong");
		}
		if (StringUtils.isBlank(user.getPassword())) {
			throw new ValidateException("user.passwordNull");
		}
		if (StringUtils.isBlank(user.getAccessRight())) {
			throw new ValidateException("user.accessRightNull");
		}
		if (StringUtils.isBlank(user.getBranch())) {
			throw new ValidateException("user.branchNull");
		}
		if (StringUtils.isBlank(user.getGroupId())) {
			throw new ValidateException("user.groupIdNull");
		}
	}

	@Override
	public Object addConsoleUserNoImg(TUser user, HttpServletRequest request) throws Exception {
		// 判断参数
		judgeIsBlank(user);
		// 判断用户名或昵称是否已经存在
		judgeIsExist(user);
		// 设置用户id
		String userId = CreateID.getID();
		user.setUserId(userId);
		// 设置用户密码
		user.setPassword(MD5Util.getMD5String(user.getPassword()).toLowerCase());
		// 默认可用
		user.setState(SysConstant.STATE_AVAILABLE);
		// 设置用户的时间
		user.setCreateTime(new Timestamp(new Date().getTime()));
		user.setLastTime(new Timestamp(new Date().getTime()));
		user.setLoginTime(new Timestamp(new Date().getTime()));
		// 设置用户角色权限
		user.setRole(user.getRole());
		// 设置用户默认头像
		String fileName = PropertyUtil.getProperty("SystemConfig.properties", "user.DefaultHeadPortrait");
		String folderPath = PropertyUtil.getProperty("SystemConfig.properties", "oss.freemarker_img");
		user.setHeadPortrait(PathUtil.getOssPath(fileName, folderPath));

		// 保存用户信息
		if (null == saveEntity(user)) {
			throw new ValidateException("common.sysException");
		}

		return user;
	}

	@SuppressWarnings("unused")
	private String uploadImg(MultipartFile multipartFile, HttpServletRequest request) throws IOException {
		String rootPath = request.getSession().getServletContext().getRealPath("");
		String substring = rootPath.substring(0, rootPath.length() - 7);
		// C:\tomcat\apache-tomcat-8.0.44\apache-tomcat-8.0.44\webapps\staff\
		// http://localhost:8080/freemarker/upload/201811/12/activity-banner.jpg
		String realPath = substring + "/freemarker/upload/headPortraitImg";
		String myfilename = StringRandomUtil.getStringRandom(4) + multipartFile.getOriginalFilename();
		String path = realPath + "/" + myfilename;
		File saveDir = new File(path);
		if (!saveDir.getParentFile().exists()) {
			saveDir.getParentFile().mkdirs();
		}
		long startTime = System.currentTimeMillis();
		multipartFile.transferTo(saveDir);
		long endTime = System.currentTimeMillis();
		logger.info("上传运行时间：" + (endTime - startTime) + "ms");

		String url = request.getRequestURL().toString();
		String uri = request.getRequestURI().toString();
		String domain = url.replace(uri, "");

		String filePath = domain + realPath.substring(realPath.indexOf("/freemarker"), realPath.length()) + "/"
				+ myfilename;
		return filePath;
	}

	@Override
	public Object updateAutoOpttime(String userId, String time) throws Exception {
		TOnlineuser tUser = null;
		if (StringUtils.isNotBlank(userId)) {
			tUser = findEntity(TOnlineuser.class, userId);
			if (tUser != null) {
				tUser.setAutoopttime(new Timestamp(new Date().getTime()));
				if (StringUtils.isNotBlank(time) && !time.equals("NaN")) {
					tUser.setOpttime(new Timestamp(Long.valueOf(time)));
				}
				updateObj(tUser);
			} else {
				throw new ValidateException("user.expired");
			}
		}
		return true;
	}

	@Override
	public Object listConsoleUser(Pager pager, String keyWord) {
		Criteria criteria = getCriteria(TUser.class);
		criteria.add(Restrictions.ne("accessRight", ""));
		criteria.add(Restrictions.isNotNull("accessRight"));
		if (StringUtils.isNotBlank(keyWord)) {
			criteria.add(Restrictions.or(Restrictions.like("userName", "%" + keyWord + "%"),
					Restrictions.eq("userId", keyWord)));
		}
		criteria.addOrder(Order.desc("createTime"));

		return listPageQBCL(criteria, pager);
	}

	@Override
	public List<String> getRoleList() {
		// 查询所有的role
		String roleSql = "SELECT menu FROM t_menu";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(roleSql);
		List<String> roleList = new ArrayList<String>();
		if (null != list && list.size() > 0) {
			for (Map<String, Object> map : list) {
				roleList.add((String) map.get("menu"));
			}
		}
		return roleList;
	}

	@Override
	public List<Map<String, Object>> getBuList() throws Exception {
		// 查询所有的role
		String buSql = "SELECT id,bu FROM t_bu where isDelete=0";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(buSql);

		return list;
	}

	@Override
	public int editPersonInfo(String headPath, String userName, String userId) {

		TUser tUser = new TUser();
		tUser.setUserId(userId);
		tUser.setHeadPortrait(headPath);
		int i = 0;
		if (StringUtils.isNotEmpty(headPath)) {
			String updateSql = "UPDATE t_user SET user_name = ?,headPortrait = ? WHERE user_id = ?";
			i = jdbcTemplate.update(updateSql, tUser.getUserName(), tUser.getHeadPortrait(), tUser.getUserId());
		} else {
			String updateSql = "UPDATE t_user SET user_name = ? WHERE user_id = ?";
			i = jdbcTemplate.update(updateSql, tUser.getUserName(), tUser.getUserId());
		}

		return i;
	}

	@Override
	public Object getConsoleUser(String userId) throws Exception {
		TUser tUser = null;
		logger.info("userId====" + userId);
		if (StringUtils.isNotBlank(userId)) {
			tUser = findEntity(TUser.class, userId);
			if (tUser != null) {
				return tUser;
			} else {
				throw new ValidateException("user.userUnExist");
			}
		}
		return new TUser();
	}

	@Override
	public Object deleteOnlineuser(String userId) throws Exception {
		// 删除不在线人
		String sql = "delete from t_online_user where status=2";
		this.jdbcTemplate.execute(sql);

		TOnlineuser tUser = null;
		logger.info("userId=====" + userId);
		if (StringUtils.isNotBlank(userId)) {
			tUser = findEntity(TOnlineuser.class, userId);
			if (tUser != null) {
				deleteEntity(tUser);
				return true;
			}
		}
		return true;
	}

	@Override
	public Object getNewUserPassword() throws Exception {
		return StringRandomUtil.getStringRandom(8);
	}

	@Override
	public Object editConsoleUserNoImg(TUser user) throws Exception {
		if (StringUtils.isBlank(user.getAccessRight())) {
			throw new ValidateException("user.accessRightNull");
		}
		TUser tUser = null;
		if (user != null && StringUtils.isNotBlank(user.getUserId())) {
			tUser = findEntity(TUser.class, user.getUserId());
			if (tUser != null) {

				if (StringUtils.isNotBlank(user.getPassword())) {
					tUser.setPassword(MD5Util.getMD5String(user.getPassword()).toLowerCase());
				}
				if (StringUtils.isNotBlank(user.getAccessRight())) {
					tUser.setAccessRight(user.getAccessRight());
				}
				if (StringUtils.isNotBlank(user.getUserType())) {
					tUser.setUserType(user.getUserType());
				}
				if (StringUtils.isNotBlank(user.getBranch())) {
					tUser.setBranch(user.getBranch());
				}
				updateEntity(tUser);
			}
		}
		if (tUser != null) {
			return tUser;
		} else {
			throw new ValidateException("user.userUnExist");
		}
	}

	@Override
	public Object editConsoleUser(MultipartFile multipartFile, TUser user) throws Exception {

		// 判断文件格式
		ValidateFileUtil.validateImgFile(multipartFile);

		// 上传头像
		long startTmie = System.currentTimeMillis();
		// String filePath = uploadImg(multipartFile, request);
		String filePath = UploadToOSSUtil.uploadAliCloudOss(multipartFile, "image", "headPortraitImg");
		long endTmie = System.currentTimeMillis();
		logger.info("上传总用时:=====" + (endTmie - startTmie) + "ms");

		if (StringUtils.isBlank(user.getAccessRight())) {
			throw new ValidateException("user.accessRightNull");
		}
		TUser tUser = null;
		if (user != null && StringUtils.isNotBlank(user.getUserId())) {
			tUser = findEntity(TUser.class, user.getUserId());
			if (tUser != null) {

				if (StringUtils.isNotBlank(user.getPassword())) {
					tUser.setPassword(MD5Util.getMD5String(user.getPassword()).toLowerCase());
				}
				if (StringUtils.isNotBlank(user.getAccessRight())) {
					tUser.setAccessRight(user.getAccessRight());
				}
				if (StringUtils.isNotBlank(user.getUserType())) {
					tUser.setUserType(user.getUserType());
				}
				if (StringUtils.isNotBlank(user.getBranch())) {
					tUser.setBranch(user.getBranch());
				}
				tUser.setHeadPortrait(filePath);
				updateEntity(tUser);
			}
		}
		if (tUser != null) {
			return tUser;
		} else {
			throw new ValidateException("user.userUnExist");
		}
	}

	@Override
	public boolean delUser(String id) throws Exception {
		TUser tUser = null;
		if (StringUtils.isNotBlank(id)) {
			tUser = findEntity(TUser.class, id);
			if (tUser != null) {
				deleteEntity(tUser);
				return true;
			} else {
				throw new ValidateException("user.userUnExist");
			}
		}
		return false;
	}

	@Override
	public int userNameIsExist(String userName, String userId) throws Exception {

		String isExistSql = "SELECT count(1) num FROM t_user WHERE user_name = '" + userName + "' AND user_id != '"
				+ userId + "'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(isExistSql);

		Integer isExist = 0;
		if (null != list && list.size() > 0) {
			isExist = StringUtil.toInteger(list.get(0).get("num"));
		}

		return isExist;
	}

	@Override
	public TUser getUserInfo(String userId) {

		TUser user = findEntity(TUser.class, userId);

		return user;
	}

	/**
	 * 根据id查询用户的username和头像
	 */
	@Override
	public Map<String, Object> geUsernameAndImgAndIsVIP(String userId) throws Exception {
		String sql = "SELECT tu.user_name,tu.branch AS displayName,tu.isVIP,bu.buPhoto AS headPortrait FROM t_user tu"
				+ " LEFT JOIN t_bu bu ON tu.groupId = bu.id WHERE tu.user_id=:userId";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		try {
			Map<String, Object> queryForMap = getNamedJdbcTemplate().queryForMap(sql, paramMap);
			if (queryForMap != null && queryForMap.size() > 0) {
				return queryForMap;
			}
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		return null;
	}

	@Override
	public Map<String, Object> getUserInfoByUserIdAndPlatform(String userId, String platform) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String sql = "";
		if (StringUtils.equals(platform, "cms")) {
			sql = "select user_name,branch as displayName,headPortrait,isVIP FROM t_user WHERE user_id=:userId";
		} else {
			sql = "select user_name as user_name,(case when nickName is not null then nickName else user_name end) as displayName,photo as headPortrait,'false' as isVIP FROM t_user_app WHERE user_id=:userId";
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		List<Map<String, Object>> queryForList = getNamedJdbcTemplate().queryForList(sql, paramMap);
		if (queryForList != null && queryForList.size() > 0) {
			map = queryForList.get(0);
		}
		return map;
	}

}
