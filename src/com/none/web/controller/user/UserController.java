package com.none.web.controller.user;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.none.core.common.utils.JSONUtil;
import com.none.core.controller.BaseControllerSupport;
import com.none.core.exception.ValidateException;
import com.none.core.model.Pager;
import com.none.core.model.Result;
import com.none.web.common.SysConstant;
import com.none.web.model.TUser;
import com.none.web.service.user.IUserService;

import spring.mvc.bind.annotation.FormModel;

/**
 * 用户管理Controller
 * 
 * @author winter
 * 
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseControllerSupport {

	public static Logger logger = Logger.getLogger(UserController.class);
	@Autowired
	private IUserService userService;

	/**
	 * CMS用户登录
	 * 
	 * @param userName
	 * @param password
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("loginConsoleUser")
	public Object loginConsoleUser(String userName, String password, HttpSession session, HttpServletRequest request)
			throws Exception {
		String[] filter = new String[] { "password", "lastTime", "tcodes" };
		Map<String, Object> userMap = new HashMap<String, Object>();

		TUser tUser = userService.mergeLoginConsoleUser(userName, password, session, request);
		userMap.put("user", tUser);

		Result result = new Result(SysConstant.STATE_SUCCESS, "user.loginUser", userMap);
		if (tUser == null) {
			result = new Result(SysConstant.STATE_FAILURE, "user.loginConsoleError", null);
		} else {
			session.setAttribute("AuthenticatedUserId", tUser.getUserId());
		}

		return JSONUtil.toJSON(result, filter);
	}

	/**
	 * CMS用户登出
	 * 
	 * @param session
	 * 
	 * @return Object
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("logoffConsoleUser")
	public Object logoffConsoleUser(HttpSession session, String userId) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", userService.deleteOnlineuser(userId));
		Result result = new Result(SysConstant.STATE_SUCCESS, "golbal.success", data);

		session.setAttribute("AuthenticatedUserId", null);

		return JSONUtil.toJSON(result);
	}

	/**
	 * 新增权限用户带img
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("addConsoleUser")
	public Object addConsoleUser(@RequestParam("file") MultipartFile multipartFile, TUser user,
			HttpServletRequest request) throws Exception {
		String[] filter = new String[] { "password", "lastTime", "tcodes" };
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("user", userService.addConsoleUser(multipartFile, user, request));
		Result result = new Result(SysConstant.STATE_SUCCESS, "user.addUser", data);
		return JSONUtil.toJSON(result, filter);
	}

	/**
	 * 新增权限用户不带img
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("addConsoleUserNoImg")
	public Object addConsoleUserNoImg(@FormModel("user") TUser user, HttpServletRequest request) throws Exception {
		String[] filter = new String[] { "password", "lastTime", "tcodes" };
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("user", userService.addConsoleUserNoImg(user, request));
		Result result = new Result(SysConstant.STATE_SUCCESS, "user.addUser", data);
		return JSONUtil.toJSON(result, filter);
	}

	/**
	 * admin console 端每隔一段时间主动请求此接口，为了解决用户非正常退出后不能登录的问题
	 * 
	 * @param userId
	 * @param time
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("updateAutoOpttime")
	public Object updateAutoOpttime(String userId, String time) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("result", userService.updateAutoOpttime(userId, time));
		Result result = new Result(SysConstant.STATE_SUCCESS, "success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * 用户列表
	 * 
	 * @return List<TbUser>
	 */
	@ResponseBody
	@RequestMapping("listConsoleUser")
	public Object listConsoleUser(@FormModel("pager") Pager pager, String keyWord) {
		String[] filter = new String[] { "password", "lastTime", "tcodes" };

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("users", userService.listConsoleUser(pager, keyWord));
		data.put("pager", pager);
		Result result = new Result(SysConstant.STATE_SUCCESS, "user.listUser", data);
		return JSONUtil.toJSON(result, filter);
	}

	/**
	 * 动态的可显示菜单列表
	 * 
	 * @return List<String>
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("menuList")
	public Object getRoleList() throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();

		data.put("menuList", userService.getRoleList());
		Result result = new Result(SysConstant.STATE_SUCCESS, "user.getRole", data);

		return JSONUtil.toJSON(result);
	}

	/**
	 * 动态的可显示菜单列表
	 * 
	 * @return List<String>
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getBuList")
	public Object getBuList() throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();

		data.put("buList", userService.getBuList());
		Result result = new Result(SysConstant.STATE_SUCCESS, "user.getBuList", data);

		return JSONUtil.toJSON(result);
	}

	/**
	 * 创建新的用户的密码
	 * 
	 * 
	 * @return Object
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getNewUserPassword")
	public Object getNewUserPassword() throws Exception {
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("user", userService.getNewUserPassword());
		Result result = new Result(SysConstant.STATE_SUCCESS, "user.createNewPassword", userMap);
		return JSONUtil.toJSON(result);
	}

	/**
	 * 修改个人信息
	 * 
	 * @return Object
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("editPersonInfo")
	public Object editPersonInfo(@RequestParam("file") MultipartFile multipartFile, String userName, String userId)
			throws Exception {

		String[] filter = new String[] { "password", "lastTime", "tcodes" };
		Result result;
		Map<String, Object> data = new HashMap<String, Object>();

		logger.info("userName===" + userName + ";userId======" + userId);
		// 判断userName是否已经被占用
		int isExist = userService.userNameIsExist(userName, userId);
		if (isExist != 0) {
			result = new Result(SysConstant.STATE_FAILURE, "user.userNameIsExist", data);
			return JSONUtil.toJSON(result);
		}

		String filePath = "";
		if (multipartFile != null && multipartFile.getSize() > 0) { // 表示客户选择文件上传,先上传文件获取到路径

			String rootPath = getSession().getServletContext().getRealPath("");
			String substring = rootPath.substring(0, rootPath.length() - 7);
			// C:\tomcat\apache-tomcat-8.0.44\apache-tomcat-8.0.44\webapps\staff\
			// http://localhost:8080/freemarker/upload/201811/12/activity-banner.jpg
			String realPath = substring + "/freemarker/upload";
			Calendar cal = Calendar.getInstance();// 使用日历类
			int year = cal.get(Calendar.YEAR);// 得到年
			int month = cal.get(Calendar.MONTH) + 1;// 得到月，因为从0开始的，所以要加1
			int day = cal.get(Calendar.DAY_OF_MONTH);// 得到天
			realPath = realPath + "/" + year + "" + month + "/" + day;
			// 循环创建文件夹
			// File file = new File(realPath);
			// if (!file.exists()) {
			// file.mkdirs();
			// }
			String myfilename = multipartFile.getOriginalFilename();
			String path = realPath + "/" + myfilename;
			File saveDir = new File(path);
			if (!saveDir.getParentFile().exists()) {
				saveDir.getParentFile().mkdirs();
			}
			long startTime = System.currentTimeMillis();
			multipartFile.transferTo(saveDir);
			// FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),
			// new File(realPath, myfilename));
			long endTime = System.currentTimeMillis();
			logger.info("上传运行时间：" + (endTime - startTime) + "ms");
			String url = getRequest().getRequestURL().toString();
			String uri = getRequest().getRequestURI().toString();
			String domain = url.replace(uri, "");

			filePath = domain + realPath.substring(realPath.indexOf("/freemarker"), realPath.length()) + "/"
					+ myfilename;

		}
		userService.editPersonInfo(filePath, userName, userId);
		TUser userInfo = userService.getUserInfo(userId);
		data.put("userInfo", userInfo);
		result = new Result(SysConstant.STATE_SUCCESS, "user.editPersonInfo", data);

		return JSONUtil.toJSON(result, filter);
	}

	@ResponseBody
	@RequestMapping("editPersonInfoNoImg")
	public Object editPersonInfoNoImg(String userName, String userId) throws Exception {
		String[] filter = new String[] { "password", "lastTime", "tcodes" };
		Result result;
		Map<String, Object> data = new HashMap<String, Object>();

		// 判断userName是否已经被占用
		int isExist = userService.userNameIsExist(userName, userId);
		if (isExist != 0) {
			result = new Result(SysConstant.STATE_FAILURE, "user.userNameIsExist", data);
			return JSONUtil.toJSON(result);
		}
		userService.editPersonInfo("", userName, userId);
		TUser userInfo = userService.getUserInfo(userId);
		data.put("userInfo", userInfo);
		result = new Result(SysConstant.STATE_SUCCESS, "user.editPersonInfo", data);

		return JSONUtil.toJSON(result, filter);

	}

	/**
	 * 得到用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getConsoleUser")
	public Object getConsoleUser(String userId) throws Exception {
		String[] filter = new String[] { "password", "lastTime", "tcodes" };
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("user", userService.getConsoleUser(userId));
		Result result = new Result(SysConstant.STATE_SUCCESS, "success", data);
		return JSONUtil.toJSON(result, filter);
	}

	/**
	 * 修改权限用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("editConsoleUser")
	public Object editConsoleUser(@RequestParam("file") MultipartFile multipartFile, TUser user) throws Exception {
		String[] filter = new String[] { "password", "lastTime", "tcodes" };
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("user", userService.editConsoleUser(multipartFile, user));
		Result result = new Result(SysConstant.STATE_SUCCESS, "success", data);
		return JSONUtil.toJSON(result, filter);
	}

	/**
	 * 修改权限用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("editConsoleUserNoImg")
	public Object editConsoleUserNoImg(@FormModel("user") TUser user) throws Exception {
		String[] filter = new String[] { "password", "lastTime", "tcodes" };
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("user", userService.editConsoleUserNoImg(user));
		Result result = new Result(SysConstant.STATE_SUCCESS, "success", data);
		return JSONUtil.toJSON(result, filter);
	}

	/**
	 * TUser 删除用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delUser")
	public Object delUser(String id, HttpSession httpSession) throws Exception {
		String sessionUserId = (String) httpSession.getAttribute("AuthenticatedUserId");
		logger.info("delUser id:  " + id + " loginUserId :" + sessionUserId);
		if (StringUtils.isNotBlank(sessionUserId)) {
			if (StringUtils.equals(id, sessionUserId)) {
				throw new ValidateException("error");
			}
		}
		userService.delUser(id);
		Map<String, Object> data = new HashMap<String, Object>();
		Result result = new Result(SysConstant.STATE_SUCCESS, "user.delSuccess", data);
		return JSONUtil.toJSON(result);
	}

}
