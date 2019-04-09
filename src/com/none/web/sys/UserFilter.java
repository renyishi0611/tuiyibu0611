package com.none.web.sys;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.none.core.common.spring.SpringContext;
import com.none.core.common.utils.Log;
import com.none.web.common.SysConstant;
import com.none.web.po.UserAppPO;
import com.none.web.service.userApp.IUserAppService;

import net.sf.json.JSONObject;

public class UserFilter extends HttpServlet implements Filter {
	private static Logger logger = Logger.getLogger(UserFilter.class);
	private List<String> notFilterUrl = new ArrayList<String>();

	private static final long serialVersionUID = 1L;

	// @Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private IUserAppService userAppService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("start the user filter");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		String url = req.getServletPath();// 当前的访问路径
		// if this url not need filter then return
		logger.info("current url: " + url);
		logger.info(notFilterUrl);
		for (String notUrl : notFilterUrl) {
			if (url.contains(notUrl)) {
				chain.doFilter(request, response);
				return;
			}
		}

		// app$udid&userid
		String uif = req.getHeader("uif");
		logger.info("start uif: " + uif);
		String[] info = uif.split("[$]");
		String platform = info[0];

		String userId = "";
		if (StringUtils.equals("cms", platform)) {
			userId = (String) session.getAttribute(SysConstant.cmsUserId);
			if (StringUtils.isBlank(userId)) {
				this.returnError(response, req, "user.userId");// 用户没有登录
				return;
			}
		} else {
			Log.info("start the user filter  app session ......");
//			// APP
			userId = (String) session.getAttribute("userId");
			if (StringUtils.isBlank(userId)) {
				this.returnError(response, req, "user.userId");// 用户没有登录
				return;
			}
//			String udid = info[1];
//			String logonUserId = info[2];
//			if (StringUtils.isBlank(udid) || StringUtils.isBlank(logonUserId)) {
//				logger.info("the udid or logonUserId is null, please check..." + "udid :" + udid + "  logonUserId "
//						+ logonUserId);
//				return;
//			}
//			boolean isExist = validateAppUser(logonUserId, udid);
//			if (isExist) {
//				response.reset();
//				response.setCharacterEncoding("UTF-8");
//				response.setContentType("application/json;charset=UTF-8");
//				PrintWriter pw = response.getWriter();
//				pw.print(JSONUtil.toJSON(new Result(SysConstant.REQUEST_ERROR, "request.error")));
//				pw.flush();
//				pw.close();
//				return;
//			}
		}
		// 将控制权传递到下一个过滤器
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {

		// 不需要验证登录和权限的操作，在过滤器中会放行
		notFilterUrl.add("userApp/activeQRCode");// 激活
		notFilterUrl.add("userApp/changeDeviceQRCode");// changeDevices
		notFilterUrl.add("user/loginConsoleUser");// CMS登陆
		notFilterUrl.add("user/updateAutoOpttime");
		notFilterUrl.add("index.jsp");// 只要是访问用户的暂时不要求登录
		notFilterUrl.add("testAop/");// 只要是访问用户的暂时不要求登录
		notFilterUrl.add("livePoll/");// 临时加上
		notFilterUrl.add("bu/");// 临时加上
		notFilterUrl.add("userApp/");//// 临时加上
		notFilterUrl.add("moments/");//// 临时加上
		notFilterUrl.add("momentsDetail/");//// 临时加上
		notFilterUrl.add("queryUserMoments/");//// 临时加上
		notFilterUrl.add("websocket/");//// 临时加上
	}

	private void returnError(ServletResponse response, HttpServletRequest req, String msg) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		String msp_locale = SpringContext.getApplicationContext().getMessage(msg, null, null, req.getLocale());
		result.put("code", 403);
		result.put("data", null);
		result.put("msg", msp_locale);
		JSONObject json = JSONObject.fromObject(result);
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setHeader("content-type", "text/html;charset=utf-8");// 通知浏览器 使用
		resp.setContentType("application/x-json");
		PrintWriter out = resp.getWriter();
		out.print(json);
		out.flush();
		out.close();
	}

	/*
	 * 根据用户ID获取用户角色（通过访问数据库）
	 */
	private String getUserRole(ServletRequest request, String userId) {

		HttpServletRequest req = (HttpServletRequest) request;
		// HttpServletResponse resp = (HttpServletResponse)response;       
		ServletContext sc = req.getSession().getServletContext();
		NamedParameterJdbcTemplate namedJdbcTemplate = null;
		XmlWebApplicationContext cxt = (XmlWebApplicationContext) WebApplicationContextUtils
				.getWebApplicationContext(sc);
		if (cxt != null && cxt.getBean("jdbcTemplate") != null) {
			jdbcTemplate = (JdbcTemplate) cxt.getBean("jdbcTemplate");
			namedJdbcTemplate = new NamedParameterJdbcTemplate(this.jdbcTemplate);
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);

		try {
			List<Map<String, Object>> list = namedJdbcTemplate
					.queryForList("select u.accessRight from t_user as u where u.user_id = :userId ", params);
			if (list != null && list.size() > 0) {
				return (String) list.get(0).get("accessRight");
			}
		} catch (Exception e) {

			logger.error("nameJdbcTemplate is null, print jdbcTemplate: " + (null == jdbcTemplate));
			logger.error(e);
		}
		return "";
	}

	private boolean validateAppUser(String userId, String udid) {
		try {
			UserAppPO userAppPO = userAppService.selectUserByUserIdAndUdid(userId, udid);
			if (userAppPO == null) {
				return true;
			}
		} catch (Exception e) {
			return true;
		}
		return false;
	}
}
