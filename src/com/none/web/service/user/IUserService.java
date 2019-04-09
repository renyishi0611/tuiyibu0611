package com.none.web.service.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.none.core.model.Pager;
import com.none.web.model.TUser;

/**
 * 用户管理Service
 * 
 * @author winter
 * 
 */
public interface IUserService {

	/**
	 * CMS 用户登录
	 * 
	 * @param userName
	 * @param password
	 * @param session
	 * @return
	 * @throws Exception
	 */
	TUser mergeLoginConsoleUser(String userName, String password, HttpSession session, HttpServletRequest request)
			throws Exception;

	/**
	 * CMS退出登录后删除该在线用户
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Object deleteOnlineuser(String userId) throws Exception;

	/**
	 * CMS中ConsoleUser新增用户带img
	 * 
	 * @return
	 */
	Object addConsoleUser(MultipartFile multipartFile, TUser user, HttpServletRequest request) throws Exception;

	/**
	 * CMS中ConsoleUser新增用户时不带img
	 * 
	 * @return
	 */
	Object addConsoleUserNoImg(TUser user, HttpServletRequest request) throws Exception;

	/**
	 * admin console 端每隔一段时间主动请求此API，为了解决用户非正常退出后不能登录的问题
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Object updateAutoOpttime(String userId, String time) throws Exception;

	/**
	 * 有权限用户列表
	 * 
	 * @param pager
	 *            分页参数
	 * @param keyword
	 *            搜索关键词
	 * 
	 * @return
	 */
	Object listConsoleUser(Pager pager, String username);

	/**
	 * 获取所有的role
	 * 
	 * @return
	 */
	List<String> getRoleList() throws Exception;

	/**
	 * 获取所有的BU
	 * 
	 * @return
	 */
	List<Map<String, Object>> getBuList() throws Exception;

	/**
	 * 创建新的password
	 * 
	 * @return
	 */
	Object getNewUserPassword() throws Exception;

	/**
	 * 修改个人信息
	 * 
	 * @param request
	 */
	int editPersonInfo(String headPath, String userName, String userId);

	/**
	 * 获取个人信息
	 * 
	 * @param userId
	 * @return
	 */
	TUser getUserInfo(String userId);

	/**
	 * 得到权限用户的所有信息
	 * 
	 * @param userId
	 * @return
	 */
	Object getConsoleUser(String userId) throws Exception;

	/**
	 * 修改权限用户信息
	 * 
	 * @param user
	 * @return
	 */
	Object editConsoleUser(MultipartFile multipartFile, TUser user) throws Exception;

	/**
	 * 修改权限用户信息
	 * 
	 * @param user
	 * @return
	 */
	Object editConsoleUserNoImg(TUser user) throws Exception;

	/**
	 * 删除用户
	 * 
	 * @return
	 */
	boolean delUser(String id) throws Exception;

	/**
	 * 查询改用户名是否已经被占用
	 * 
	 * @param userName
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	int userNameIsExist(String userName, String userId) throws Exception;

	/**
	 * 
	 * @Title: geUsernameAndImg @Description:
	 *         TODO(根据用户id查询用户姓名和头像以及是否是VIP) @param @param
	 *         userId @param @return @param @throws Exception 设定文件 @return TUser
	 *         返回类型 @throws
	 */
	Map<String, Object> geUsernameAndImgAndIsVIP(String userId) throws Exception;

	
	
	 Map<String, Object> getUserInfoByUserIdAndPlatform (String userId,String platform) throws Exception;
}
