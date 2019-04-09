package com.none.web.sys;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.none.core.common.spring.SpringContext;
import net.sf.json.JSONObject;

public class UserFilter_bak extends HttpServlet implements Filter {
	private static Logger logger = Logger.getLogger(UserFilter_bak.class);
	private List<String> notFilterUrl = new ArrayList<String>();

	private static final long serialVersionUID = 1L;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("start the user filter");
		HttpServletRequest req = (HttpServletRequest) request;

		String url = req.getServletPath();// 当前的访问路径
		// if this url not need filter then return
		for (String notUrl : notFilterUrl) {
			if (url.contains(notUrl)) {
				chain.doFilter(request, response);
				return;
			}
		}
		// test user is login
		// String userId = request.getParameter("userId");
		String userId = "";
		if (userId == null || userId.equals("")) {
			// userId = req.getHeader("BasicAuthUsername");
			userId = (String) req.getSession().getAttribute("AuthenticatedUserId");
		}
		if (StringUtils.isBlank(userId)) {
			this.returnError(response, req, "activity.unLogin");// 用户没有登录
			return;
		}

		// //test user function (判断用户权限)
		String rolestr = this.getUserRole(userId);
		if (rolestr != null && !"".equals(rolestr)) {
			if (url.contains("user/")) {
				if (!rolestr.contains("Console Admin")) {
					this.returnError(response, req, "user.userIsNotRole");// 找不到此用户
					return;
				}
			} else if (url.contains("activity/")) {
				if (!rolestr.contains("Activity user")) {
					this.returnError(response, req, "user.userIsNotRole");// 找不到此用户
					return;
				}
			} else if (url.contains("news/")) {
				if (!rolestr.contains("News user")) {
					this.returnError(response, req, "user.userIsNotRole");// 找不到此用户
					return;
				}
			} else if (url.contains("beacon/")) {
				if (!rolestr.contains("Beacon user")) {
					this.returnError(response, req, "user.userIsNotRole");// 找不到此用户
					return;
				}
			}
		} else {
			this.returnError(response, req, "user.userIsNotRole");// 没有操作权限
			return;
		}
		// 将控制权传递到下一个过滤器
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

		// 不需要验证登录和权限的操作，在过滤器中会放行
		notFilterUrl.add("user/loginConsoleUser");// pc端用户登录
		notFilterUrl.add("user/logoffConsoleUser");// pc端用户退出
		notFilterUrl.add("user/loginUser");// app端用户登录路径
		notFilterUrl.add("user/activeUserByCompare");// app端用户激活, 使用二维码
		notFilterUrl.add("user/activeQRCode");// app端激活二维码
		notFilterUrl.add("user/activeSetPassword");// app端用户激活
		notFilterUrl.add("user/activeUser");// app端用户激活,之前的
		notFilterUrl.add("user/getUserInfo");// app端用户激活,之前的
		notFilterUrl.add("user/addUserApp");// app端用户激活,之前的
		notFilterUrl.add("user/listNewsActivitys");// app端新闻和活动展示
		notFilterUrl.add("beacon/listBeacon");// app端设备展示
		notFilterUrl.add("upload.do");
		notFilterUrl.add("browerServer.do");
		notFilterUrl.add("web/ossController.jsp");// ChangeToUeditor
		notFilterUrl.add("web/testUpload.jsp");// ChangeToUeditor
		// 临时
		notFilterUrl.add("comment/");// 只要是访问用户的暂时不要求登录
		notFilterUrl.add("like/");// 只要是访问用户的暂时不要求登录
		notFilterUrl.add("log/");
		notFilterUrl.add("invoiceClaim/");
		notFilterUrl.add("redPackage/");
		// william add by 2014-12-08
		notFilterUrl.add("user/resetUserPassword");// reset user password
		notFilterUrl.add("user/forgetPasswordQRCode");// forget password QR code
		notFilterUrl.add("user/forgetSetPassword");// forget password QR code
		notFilterUrl.add("user/changeDeviceQRCode");// change device QR code
		notFilterUrl.add("index.jsp");// 只要是访问用户的暂时不要求登录
		notFilterUrl.add("client.jsp");// 只要是访问用户的暂时不要求登录
		notFilterUrl.add("beacon/listNews");// beacon新闻展示
		notFilterUrl.add("user/rememberMe");
		notFilterUrl.add("user/updateUser");
		// Dave add by 2015-07-02
		notFilterUrl.add("user/setPushInfo");// 启动应用更新udid和push所需要的信息（原生调用）
		notFilterUrl.add("user/getMenuInfo");// 加载应用的时候调用
		// Bing add by 2015-08-25
		notFilterUrl.add("user/updateAutoOpttime");
		notFilterUrl.add("user/updateOptTime");
		notFilterUrl.add("user/getTagsByDeptId");
		notFilterUrl.add("user/getAppUserByStaffId");
		notFilterUrl.add("user/changeDevice");
		notFilterUrl.add("user/");
		notFilterUrl.add("mainBanner/");
		notFilterUrl.add("clickCount/");
		notFilterUrl.add("userApp/");
		notFilterUrl.add("moments/");
		notFilterUrl.add("queryUserMoments/");
		notFilterUrl.add("momentsDetail/");
		notFilterUrl.add("voice/");
		notFilterUrl.add("wtp/");
		notFilterUrl.add("livePoll/");
		notFilterUrl.add("bu/");

	}

	private void returnError(ServletResponse response, HttpServletRequest req, String msg) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		String msp_locale = SpringContext.getApplicationContext().getMessage(msg, null, null, req.getLocale());
		result.put("code", -1);
		result.put("data", null);
		result.put("msg", msp_locale);
		JSONObject json = JSONObject.fromObject(result);
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setHeader("content-type", "text/html;charset=utf-8");// 通知浏览器 使用
																	// utf-8 编码
		resp.setContentType("application/x-json");
		PrintWriter out = resp.getWriter();
		out.print(json);
		out.flush();
		out.close();
		
		logger.info("running the user filter");
	}

	/*
	 * 根据用户ID获取用户角色（通过访问数据库）
	 */
	private String getUserRole(String userId) {
		String role = "";
		Connection con = null;
		Statement stmt = null;
		ResultSet r = null;
		// Connection_jdbc connObj=Connection_jdbc.getInstance();
		// con= connObj.getCon();
		// use JNDI db connection
		Context ctx;
		DataSource ds;

		try {
			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/staffApp");
			con = ds.getConnection();
			stmt = con.createStatement();
			r = stmt.executeQuery(" select u.role from t_user as u where u.user_id='" + userId + "'");
			while (r.next()) {
				role = r.getString("role");
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					r.close();
					stmt.close();
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// connObj.colseConnection();
		}
		return role;
	}
}
