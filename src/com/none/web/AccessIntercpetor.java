package com.none.web;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.none.core.common.utils.JSONUtil;
import com.none.core.common.utils.Log;
import com.none.core.model.Result;
import com.none.web.common.SysConstant;

public class AccessIntercpetor extends HandlerInterceptorAdapter {

	private static Logger logger = Logger.getLogger(AccessIntercpetor.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	static final List<String> noFilteringMap = new ArrayList<String>();

	static {
		noFilteringMap.add("/userApp/activeQRCode");
		noFilteringMap.add("/userApp/changeDeviceQRCode");
		noFilteringMap.add("/user/loginConsoleUser");
		noFilteringMap.add("/moments/exportCMSMomExcel");
		noFilteringMap.add("/clickCount/export");
		noFilteringMap.add("/voice/exportCMSVoiceExcel");
		noFilteringMap.add("/livePoll/exportLivePoll");
		noFilteringMap.add("/userApp/exportAPPUserExcel");
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

		String uif = request.getHeader("uif");
		String url = request.getServletPath();

		logger.info("start intercpetor..., data: " + uif);
		logger.info("url: " + url);

		if (noFilteringMap.contains(url)) {
			logger.info("the use init page, not need the userId");
			return true;
		} else if (null == uif) {
			logger.info("please correct config the data");
			return false;
		}

		String[] info = uif.split("[$]");

		String platform = info[0];

		if (StringUtils.equals("cms", platform)) {
			logger.info("cms user not need validate ...");
			return true;
		}

		String udid = info[1];
		String logonUserId = info[2];

		if (StringUtils.equals("null", udid) || StringUtils.equals("null", logonUserId)) {
			logger.info("the udid or logonUserId is null, please check");
			return false;
		}

		boolean isExist = validateAppUser(logonUserId, udid);
		logger.info("getAppMomentsList验证用户+device：" + udid);
		if (isExist) {
			logger.info("please logon again ");
			response.reset();
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");

			PrintWriter pw = response.getWriter();

			pw.print(JSONUtil.toJSON(new Result(SysConstant.REQUEST_ERROR, "request.error")));
			pw.flush();
			pw.close();

			return false;
		}
		return true;
	}

	private boolean validateAppUser(String userId, String udid) {
		logger.info("validate app user...");
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(udid)) {
			logger.info("the key is null or ' ', please check again");
			return true;
		}
		try {
			Object[] param = { userId, udid };
			List<Map<String, Object>> result = jdbcTemplate.queryForList(
					"select user_id from t_user_app where user_id= ? and user_UDID=? and is_delete = 0", param);
			logger.info("validate result: " + result);
			if (null == result || result.size() == 0) {
				logger.info("用户已经被删除：userId" + userId + ",udid:" + udid);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("validateAppUser error: " + e);
		}
		return false;
	}
}
