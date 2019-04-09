package com.none.web.service.userApp.impl;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.none.core.common.utils.DateUtil;
import com.none.core.common.utils.ExportExcelUtil;
import com.none.core.common.utils.PathUtil;
import com.none.core.common.utils.PropertyUtil;
import com.none.core.common.utils.StringUtil;
import com.none.core.common.utils.WDWUtil;
import com.none.core.exception.ValidateException;
import com.none.core.service.BaseServiceSupport;
import com.none.web.QRcode.coder.NewUserSignUpQRCoder;
import com.none.web.QRcode.constants.AppConstants;
import com.none.web.QRcode.util.AESUtil;
import com.none.web.common.SysConstant;
import com.none.web.dao.DepartmentDao;
import com.none.web.dao.UserAppDao;
import com.none.web.model.TDepartment;
import com.none.web.model.TUserApp;
import com.none.web.po.ControlActivePO;
import com.none.web.po.LotteryPO;
import com.none.web.po.PrizepeoPO;
import com.none.web.po.ReqAppUserListPO;
import com.none.web.po.UserAppPO;
import com.none.web.service.userApp.IUserAppService;
import com.none.web.utils.Base64Util;
import com.none.web.utils.UploadImgUtil;

@Service("userAppService")
public class UserAppServiceImpl extends BaseServiceSupport implements IUserAppService {

	public static Logger logger = Logger.getLogger(UserAppServiceImpl.class);
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private UserAppDao userAppDao;
	@Resource
	private HttpServletRequest request;

	// @Autowired
	// private WebSocketServer webSocketServer;

	// 活动的map
	public static Map<String, String> activeMap;
	static {
		activeMap = new HashMap<String, String>();
		activeMap.put("11", "1");
		activeMap.put("12", "1");
		activeMap.put("13", "1");
		activeMap.put("14", "1");
		activeMap.put("41", "1");
		activeMap.put("42", "1");
		activeMap.put("43", "1");
		activeMap.put("44", "1");
	}
	// }

	// 所有摇奖的人
	private static Map<String, Float> map = new HashMap<String, Float>();
	// 中奖人
	private static List<Map.Entry<String, Float>> list = new ArrayList<Map.Entry<String, Float>>();
	// 前端展示的人
	private static List<Entry<String, Float>> subList = new ArrayList<Map.Entry<String, Float>>();
	// 已中奖人
	private static List<String> missList = new ArrayList<String>();
	// 活动开关
	// private static boolean controlStatus = false;
	private static Integer peoNum = 10;

	/**
	 * app 扫描二维码激活用户
	 */
	@Override
	public Object activeQRCode(String code, String udid, HttpServletRequest request) throws Exception {

		String staffId = null;
		String staffIdEncrypt = null;
		String userName = null;
		String english_name = null;
		String chinese_name = null;
		String entity = null;
		String department = null;
		String email = null;
		String actionType = null;// 二维码对应操作标志： 激活用户：101，更改密码：102，更换设备：103
		String branch = null;
		List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
		// dept默认：others
		Integer deptId = departmentDao.selectDepartmentIdByName("others");
		String failureTime = "";// 二维码生成时间：失效开始计时时间
		try {
			String[] coderInfos = null;// 二维码内容
			if (code.contains(",")) {
				logger.info("激活异常***：二维码内容存在逗号");
				throw new ValidateException("userApp.QRcodeNotRight");
			} else {// 解析二维码
				coderInfos = NewUserSignUpQRCoder.decrypt(code, AppConstants.QR_CODER_AES_STATIC_KEY);
			}
			int length = coderInfos.length;
			if (coderInfos != null && (length == 7 || length == 8 || length == 9 || length == 10)) {
				if (length == 10) {
					staffId = coderInfos[0];
					entity = coderInfos[1];
					branch = coderInfos[2];
					department = coderInfos[3];
					email = coderInfos[4];
					chinese_name = coderInfos[5];
					actionType = coderInfos[length - 2];
					// 二维码失效开始计时时间
					failureTime = coderInfos[length - 1];
				} else {
					staffId = coderInfos[0];
					branch = coderInfos[1];
					department = coderInfos[2];
					email = coderInfos[3];
					chinese_name = coderInfos[4];
					actionType = coderInfos[length - 2];
					// 二维码失效开始计时时间
					failureTime = coderInfos[length - 1];
				}
				if (email != null) {
					if (email.contains("@")) {
						userName = email.substring(0, email.indexOf("@"));
						english_name = userName.replace(".", " ");
						userName = userName.replace(".", "").replace(" ", "");
					} else if (email.contains("/")) {
						userName = email.substring(0, email.indexOf("/"));
						english_name = userName.replace(".", " ");
						userName = userName.replace(".", "").replace(" ", "");
					} else {
						userName = email;
						english_name = userName.replace(".", " ");
					}
				}
			} else {
				logger.info("激活异常***：二维码解析的数组长度错误");
				throw new ValidateException("userApp.QRcodeNotRight");
			}
			if (length == 8 || length == 9 || length == 10) {
				// entity传递的值为id
				TDepartment dept = findEntity(TDepartment.class, Integer.valueOf(entity));
				if (dept != null) {
					deptId = Integer.valueOf(entity);
				}
			}
		} catch (NumberFormatException e) {
			// entity传递的值不为id
			List<String> list = jdbcTemplate.queryForList("SELECT id FROM t_department WHERE dept_name=?",
					new Object[] { entity }, String.class);
			if (!list.isEmpty()) {
				deptId = Integer.valueOf(list.get(0));
			} else if (StringUtils.isNotBlank(entity)) {
				// 当传递的entity名称不存在时 数据库新增该entity 并返回插入数据的ID
				jdbcTemplate.update("insert into `t_department`(dept_name) values(?)", entity);
				deptId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
			}
		} catch (Exception e) {
			logger.error("激活异常***：二维码解析等步骤异常捕捉" + e);
			throw new ValidateException("userApp.QRcodeNotRight");
		}
		// 验证操作：是激活用户操作
		if (!AppConstants.ACTIONTYPE_NEWUSERSIGNUP.equals(actionType)) {
			logger.info("激活异常***：不是激活操作的二维码");
			throw new ValidateException("userApp.QRcodeNotRight");
		}
		// 验证二维码是否失效，失效则提示
		// failureTime = DateUtil.formatCurrentDateTime("yyyy-MM-dd
		// HH:mm:ss");// 当前时间9999999999
		String timeOut = PropertyUtil.getProperty("SystemConfig.properties", "QR.timeOut");
		Integer qRcodeTimeOut = DateUtil.isTimeOut(failureTime, Integer.valueOf(timeOut));
		// Integer qRcodeTimeOut = DateUtil.isTimeOut(failureTime, 15);
		if (qRcodeTimeOut == 1) {// 验证二维码是否失效
			logger.info("激活异常***：激活的二维码超时失效");
			throw new ValidateException("userApp.QRcodeTimeOut");
		}
		// 加密staffId
		staffIdEncrypt = AESUtil.encrypt(staffId, AppConstants.QR_CODER_AES_STATIC_KEY);
		// 9999999999999999999999999999
		// 验证是否在白名单存在：存在则正常激活
		// List<String> whiteList = jdbcTemplate.queryForList("SELECT id FROM
		// t_whitelist WHERE staffId=? and is_delete=0",
		// new Object[] { staffIdEncrypt }, String.class);
		// if (whiteList.isEmpty()) {
		// logger.info("异常***：该staffId不在白名单");
		// throw new ValidateException("userApp.notwhitelist");
		// }
		// 验证：存在staffId已激活且没有离职的员工
		Criteria userCriteria = getCriteria(TUserApp.class);
		userCriteria.add(Restrictions.eq("staffId", staffIdEncrypt));
		userCriteria.add(Restrictions.eq("isDelete", "0"));// 是否离职,0:在职
		List<?> userList = userCriteria.list();
		String now = DateUtil.formatCurrentDateTime("yyyy-MM-dd HH:mm:ss");
		Serializable userId = null;
		if (userList != null && userList.size() > 0) {// 该staffId存在已激活用户
			if (userList.size() > 1) {
				logger.info("激活异常***：本StaffId存在多个正常用户");
				throw new ValidateException("userApp.multiple");
			}
			TUserApp oldUser = (TUserApp) userList.get(0);
			// 验证已激活用户状态，已激活可用状态不能再次激活
			// if (oldUser.getStatus().equals(SysConstant.STATE_AVAILABLE)) {
			// logger.info("异常***：本StaffId有已激活可用状态不能再次激活");
			// throw new ValidateException("userApp.haveActivated");
			// }
			// 验证设备
			if (udid.equals(oldUser.getUserUDID())) {

				// oldUser.setUserName(userName);
				oldUser.setStaffId(staffIdEncrypt);
				oldUser.setUserUDID(udid);
				oldUser.setStatus(SysConstant.STATE_AVAILABLE);// 状态激活可用
				oldUser.setCreateTime(now);
				oldUser.setBranch(branch);
				oldUser.setDeptId(deptId);
				oldUser.setPosition(department);
				oldUser.setEnglishName(english_name);
				oldUser.setChineseName(chinese_name);
				oldUser.setEmail(email);
				oldUser.setLastVisitTime(null);// 置空最近一次登录时间
				oldUser.setFirstLoginTime(now);// 第一次登录
				oldUser.setIsDelete("0");
				oldUser.setActivationTime(now);// 激活时间
				oldUser.setAccept("N");
				updateEntity(oldUser);
			} else {
				logger.info("激活异常***：不是之前绑定设备，不能再次激活,提示更换设备udid:" + udid);
				throw new ValidateException("userApp.udidNotIdentical");
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("userId", oldUser.getUserId());
			map.put("staffId", staffId);
			map.put("userName", oldUser.getUserName());
			map.put("accept", "N");
			rsList.add(map);
			return rsList;
		} else {

			// 防止同设备多账号注册
			List<UserAppPO> userInfoByUDID = userAppDao.getUserInfoByUDID(udid);
			if (!userInfoByUDID.isEmpty()) {
				logger.info("激活异常***：不是之前激活二维码，请使用绑定的二维码登陆udid:" + udid);
				throw new ValidateException("appUser.otherQR");
			}
			TUserApp user = new TUserApp();
			// 第一次登录
			user.setFirstLoginTime(now);
			user.setLastVisitTime(null);// 更新最近一次登录时间
			user.setUserName(chinese_name);
			user.setStaffId(staffIdEncrypt);
			user.setUserUDID(udid);
			user.setStatus(SysConstant.STATE_AVAILABLE);
			user.setCreateTime(now);
			user.setBranch(branch);
			user.setDeptId(deptId);
			user.setPosition(department);
			user.setEnglishName(english_name);
			user.setChineseName(chinese_name);
			user.setEmail(email);
			user.setIsDelete("0");
			user.setActivationTime(now);
			String fileName = PropertyUtil.getProperty("SystemConfig.properties", "userApp.DefaultHeadPortrait");
			String folderPath = PropertyUtil.getProperty("SystemConfig.properties", "oss.freemarker_img");
			user.setPhoto(PathUtil.getOssPath(fileName, folderPath));// 设置默认图像
			user.setAccept("N");
			userId = saveEntity(user);
			if (userId == null) {
				logger.info("激活异常***：用户添加错误");
				throw new ValidateException("common.sysException");
			}
			logger.info("添加用户，用户id：" + userId.toString() + "，StaffId：" + staffId + ",udid:" + udid);
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", userId.toString());
		map.put("staffId", staffId);
		map.put("userName", userName);
		map.put("accept", "N");
		rsList.add(map);
		logger.info("激活成功userID：" + userId + ",udid:" + udid + "，StaffId：" + staffId);
		request.getSession().setAttribute("userId", userId);
		// 以秒为单位，即在没有活动30分钟后，session将失效
		// request.getSession().setMaxInactiveInterval(30 * 60);
		return rsList;
	}

	/**
	 * app用户登录
	 * 
	 * @param isFirstLogin
	 *            激活或change device后的第一次登录为：Y ，隐藏登陆为：N
	 */
	@Override
	public TUserApp mergeLoginUser(String userId, String udid, String isFirstLogin) throws Exception {

		Criteria userCriteria = getCriteria(TUserApp.class);
		userCriteria.add(Restrictions.eq("userId", userId));
		List<?> userList = userCriteria.list();
		TUserApp tUser = null;
		// 验证有无此用户
		if (userList.isEmpty()) {
			logger.info("登录异常***：不存在此用户userId:" + userId + ",udid:" + udid);
			throw new ValidateException("userApp.needActive");
		}
		// 存在多个用户
		if (userList.size() > 1) {
			logger.info("登录异常***：此userId存在多个用户");
			throw new ValidateException("userApp.multiple");
		}
		tUser = (TUserApp) userList.get(0);
		String isDelete = tUser.getIsDelete();
		// 验证是否离职： 0:在职,未删除 1:离职,已删除
		if (!StringUtils.equals(isDelete, "0")) {
			logger.info("登录异常***：此userId已经离职");
			throw new ValidateException("userApp.needActive");
		}
		String staffId = AESUtil.decrypt(tUser.getStaffId(), AppConstants.QR_CODER_AES_STATIC_KEY);
		// 验证设备
		if (udid.equals(tUser.getUserUDID()) || "ingore".equals(tUser.getUserUDID()) || "ingore".equals(udid)) {
			// 验证用户激活状态
			if (tUser.getStatus() != SysConstant.STATE_AVAILABLE) {
				logger.info("登录异常***：请先激活用户");
				throw new ValidateException("userApp.needActive");
			}
			String now = DateUtil.formatCurrentDateTime("yyyy-MM-dd HH:mm:ss");
			tUser.setIsTimeOut("0");// 直接登录
			// 激活或change device后的第一次登录
			if (StringUtils.equals(isFirstLogin, "Y")) {
				tUser.setFirstLoginTime(now);
				tUser.setLastVisitTime(null);
			} else {// 隐藏登陆

				String lastVisitTime = tUser.getLastVisitTime();
				// 格式化后的最近一次登录时间
				if (StringUtils.isNotBlank(lastVisitTime)) {
					tUser.setLastVisitTimestr(DateUtil.staffTime(lastVisitTime));
				}
				// 更新最近一次登录时间
				tUser.setLastVisitTime(now);
			}
			updateEntity(tUser);
		} else {
			logger.info("登陆异常***：不是之前注册设备，如需登陆请先更换设备");
			throw new ValidateException("userApp.needActive");
		}
		logger.info("登陆成功userID：" + tUser.getUserId() + ",staffId:"
				+ AESUtil.decrypt(tUser.getStaffId(), AppConstants.QR_CODER_AES_STATIC_KEY) + ",userName:"
				+ tUser.getUserName() + ",udid:" + tUser.getUserUDID());
		tUser.setDecryptStaffId(staffId);
		request.getSession().setAttribute("userId", userId);
		// 以秒为单位，即在没有活动30分钟后，session将失效
		// request.getSession().setMaxInactiveInterval(30 * 60);
		return tUser;
	}

	/**
	 * 扫描二维码更换设备
	 */
	@Override
	public Object activeChangeDeviceQRCode(String code, String udid, HttpServletRequest request) throws Exception {
		if (StringUtils.isBlank(code)) {
			throw new ValidateException("user.codeUnknow");
		}
		if (StringUtils.isBlank(udid) || StringUtils.equals(udid, "null")) {
			throw new ValidateException("parameter.error");
		}
		List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
		String staffIdEncrypt = null;
		String staffId = null;
		String failureTime = "";// 二维码生成时间
		String actionType = null;
		try {
			// 解析二维码
			String[] coderInfos = NewUserSignUpQRCoder.decrypt(code, AppConstants.QR_CODER_AES_STATIC_KEY);
			if (coderInfos != null && (coderInfos.length == 8 || coderInfos.length == 7 || coderInfos.length == 9
					|| coderInfos.length == 10 || coderInfos.length == 4)) {
				if (coderInfos.length == 10) {
					staffId = coderInfos[0];
					actionType = coderInfos[coderInfos.length - 2];
					failureTime = coderInfos[coderInfos.length - 1];
				} else if (coderInfos.length == 4) {
					staffId = coderInfos[1];
					actionType = coderInfos[coderInfos.length - 2];
					failureTime = coderInfos[coderInfos.length - 1];
				} else {
					staffId = coderInfos[0];
					actionType = coderInfos[coderInfos.length - 2];
					failureTime = coderInfos[coderInfos.length - 1];
				}
			} else {
				logger.info("更换设备异常***：二维码长度错误");
				throw new ValidateException("userApp.QRcodeNotRight");
			}
		} catch (Exception e) {
			logger.info("更换设备异常***：二维码解析错误");
			throw new ValidateException("userApp.QRcodeNotRight");
		}
		// 验证二维码操作
		if (!AppConstants.ACTIONTYPE_DEVICECHANGE.equals(actionType)) {
			logger.info("更换设备异常***：不是更换设备操作");
			throw new ValidateException("userApp.QRcodeNotRight");
		}
		// 验证二维码是否失效，失效则提示
		// failureTime = DateUtil.formatCurrentDateTime("yyyy-MM-dd
		// HH:mm:ss");// 99999999999999999999
		String timeOut = PropertyUtil.getProperty("SystemConfig.properties", "QR.timeOut");
		Integer qRcodeTimeOut = DateUtil.isTimeOut(failureTime, Integer.valueOf(timeOut));
		// Integer qRcodeTimeOut = DateUtil.isTimeOut(failureTime, 15);
		if (qRcodeTimeOut == 1) {// 验证二维码是否失效
			logger.info("更换设备异常***：二维码超时");
			throw new ValidateException("userApp.QRcodeTimeOut");
		}
		// 加密 2018
		staffIdEncrypt = AESUtil.encrypt(staffId, AppConstants.QR_CODER_AES_STATIC_KEY);
		// （如扫描别人的change二维码）先删除自己之前设备绑定的账号
		List<UserAppPO> userAppPOList = userAppDao.getUserInfoByUDID(udid);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (UserAppPO userAppPO : userAppPOList) {
			String staffId2 = userAppPO.getStaffId();
			if (!StringUtils.equals(staffIdEncrypt, staffId2)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("staffId", AESUtil.encrypt(staffId2, AppConstants.QR_CODER_AES_STATIC_KEY));
				list.add(map);
			}
		}
		if (!list.isEmpty()) {
			userAppDao.updateUserAppStatus(list);
		}
		Criteria userCriteria = getCriteria(TUserApp.class);
		userCriteria.add(Restrictions.eq("staffId", staffIdEncrypt));
		userCriteria.add(Restrictions.eq("isDelete", "0"));// 在职,未删除
		List<?> userList = userCriteria.list();
		if (userList != null && userList.size() == 1) {// 有此在职用户
			TUserApp user = (TUserApp) userList.get(0);
			user.setUserUDID(udid);
			user.setFirstLoginTime(DateUtil.formatCurrentDateTime("yyyy-MM-dd HH:mm:ss"));// 激活后的第一次登录
			user.setLastVisitTime(null);// 更新最近一次登录时间
			user.setStatus(1);// 激活状态
			user.setAccept("N");
			// 进入同意条款页面
			updateEntity(user);
			Map<String, String> map = new HashMap<String, String>();
			map.put("userId", user.getUserId());
			map.put("staffId", staffId);
			map.put("userName", user.getUserName());
			map.put("accept", "N");
			map.put("udid", user.getUserId());
			rsList.add(map);
		} else {
			logger.info("更换设备异常***：用户staffId:" + staffId + "不存在");
			throw new ValidateException("user.userNull");
		}
		logger.info("更换设备成功userID：" + rsList.get(0).get("userId") + ",staffId:" + staffId + ",userName:"
				+ rsList.get(0).get("userName") + ",udid:" + rsList.get(0).get("udid"));
		request.getSession().setAttribute("userId", rsList.get(0).get("userId"));
		// 以秒为单位，即在没有活动30分钟后，session将失效
		// request.getSession().setMaxInactiveInterval(30 * 60);
		return rsList;
	}

	@Override
	public Integer changeAcceptTerms(String userId, String accept) throws Exception {

		userAppDao.update(userId, accept);
		return null;
	}

	@Override
	public Map<String, Object> selectAppUserList(ReqAppUserListPO po) throws Exception {

		// 验证参数
		if (null != po) {
			if (StringUtil.isEmpty(po.getPageNo()) || StringUtil.isEmpty(po.getPageSize())) {
				throw new ValidateException("param.error");
			}
		} else {
			throw new ValidateException("param.error");
		}

		Map<String, Object> map = new HashMap<String, Object>();

		// 设置查询页数
		map.put("startPage", (po.getPageNo() - 1) * po.getPageSize());
		map.put("pageSize", po.getPageSize());
		// 设置条件查询参数
		if (StringUtil.isNotEmpty(po.getStatus())) {
			map.put("status", po.getStatus());
		}

		// 按条件查询出user
		List<UserAppPO> appUserList = userAppDao.selectUserAppList(map);
		if (null != appUserList && appUserList.size() > 0) {
			for (int i = 0; i < appUserList.size(); i++) { // 循环解密staffID
				appUserList.get(i).setStaffId(
						AESUtil.decrypt(appUserList.get(i).getStaffId(), AppConstants.QR_CODER_AES_STATIC_KEY));
			}
		}
		int count = userAppDao.selectUserAppListCount(map);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("userList", appUserList);
		resultMap.put("totalRows", count);
		resultMap.put("pageSize", po.getPageSize());
		int totalPage;
		if (count % po.getPageSize() == 0) {
			totalPage = count / po.getPageSize();
		} else {
			totalPage = count / po.getPageSize() + 1;
		}
		resultMap.put("totalPage", totalPage);

		return resultMap;
	}

	/**
	 * app menu菜单查询用户名、用户图像
	 */
	@Override
	public UserAppPO searchUserAppById(String userId, HttpServletRequest request) throws Exception {

		UserAppPO userAppInfo = userAppDao.selectUserAppById(userId);
		if (userAppInfo != null) {
			String photo = userAppInfo.getPhoto();
			// 设置用户默认头像
			String fileName = PropertyUtil.getProperty("SystemConfig.properties", "userApp.DefaultHeadPortrait");
			String folderPath = PropertyUtil.getProperty("SystemConfig.properties", "oss.freemarker_img");
			String headPortrait = PathUtil.getOssPath(fileName, folderPath);
			if (StringUtils.isBlank(photo)) {
				userAppInfo.setPhoto(headPortrait);
			}
			// userAppInfo.setUserName(userAppInfo.getEnglishName());
		} else {
			logger.info("不存在用户：" + userId + ",或用户已被删除");
		}
		return userAppInfo;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void exportAppUserExcel(HttpServletResponse response) throws Exception {
		// 设置表头
		List<String> titelList = new ArrayList<String>();
		titelList.add("is activation");
		titelList.add("staffId");
		titelList.add("display name");
		titelList.add("last login data");
		titelList.add("activation data");
		// 查询出所有的在职的appuser
		Map<String, Object> map = new HashMap<String, Object>();
		List<UserAppPO> userList = userAppDao.selectUserAppList(map);
		// 声明一个工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();

		// 声明一个表格
		HSSFSheet sheet = workbook.createSheet();
		sheet.setDefaultColumnWidth((short) 22);// 设置列宽
		// 表格标题行
		HSSFRow row = sheet.createRow(0);

		HSSFCell cellHeader;
		// 插入标题
		for (int i = 0; i < titelList.size(); i++) {
			cellHeader = row.createCell(i);
			cellHeader.setCellValue(titelList.get(i));
		}
		// 插入内容
		if (null == userList || userList.size() == 0) {
			throw new ValidateException("data.empty");
		}
		for (int i = 0; i < userList.size(); i++) {
			row = sheet.createRow(sheet.getLastRowNum() + 1);
			Integer status = userList.get(i).getStatus();
			if (null != status && 1 == status) {
				row.createCell(0).setCellValue("YES");
				row.createCell(2).setCellValue(userList.get(i).getUserName());
				row.createCell(3).setCellValue(userList.get(i).getLastVisitTime());
				row.createCell(4).setCellValue(userList.get(i).getActivationTime());
			} else {
				row.createCell(0).setCellValue("NO");
			}

			row.createCell(1)
					.setCellValue(AESUtil.decrypt(userList.get(i).getStaffId(), AppConstants.QR_CODER_AES_STATIC_KEY));
		}

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式

		String filename = df.format(new Date()) + "satff.xls";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();

	}

	@SuppressWarnings("unchecked")
	@Override
	public void importAppUserExcel(MultipartFile file, HttpSession httpSession) throws Exception {

		Map<String, Object> excelStaff = new HashMap<String, Object>();
		// 判断文件是否为空
		if (file != null) {
			// 获取文件名
			String fileName = file.getOriginalFilename();
			/** 判断文件的类型，是2003还是2007 */
			boolean isExcel2003 = true;
			if (WDWUtil.isExcel2007(fileName)) {
				isExcel2003 = false;
			}
			InputStream input = file.getInputStream();
			if (isExcel2003) {
				excelStaff = ExportExcelUtil.readExcel(input);
			} else {
				excelStaff = ExportExcelUtil.readXlsx(input);
			}
		}
		List<Map<String, Object>> staffIdMap = (List<Map<String, Object>>) excelStaff.get("user");
		// 获得excel中的最新在职员工数据
		List<String> eList = new ArrayList<String>();
		for (Map<String, Object> map : staffIdMap) {
			String staffId = (String) map.get("Employee ID");

			if (staffId.indexOf("E") != -1) {
				staffId = new BigDecimal(staffId).toPlainString();
			}

			eList.add(staffId);
		}
		List<String> newList = new ArrayList<String>();
		newList.addAll(eList); // 备份一份表格数据
		// 数据库查询出当前在职员工数据
		List<String> sList = userAppDao.selectStaffIds();
		List<String> dsList = new ArrayList<String>(); // 存储解密后的staffId,拿解密后的和excel表作对比
		for (String string : sList) {
			dsList.add(AESUtil.decrypt(string, AppConstants.QR_CODER_AES_STATIC_KEY));
		}

		// 拿表格和数据库取差集,得到表格多余的数据
		eList.removeAll(dsList);
		if (eList.size() > 0) {
			List<Map<String, String>> newWhiteList = new ArrayList<Map<String, String>>();
			String userId = (String) httpSession.getAttribute("userId");
			for (String string : eList) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("staffId", AESUtil.encrypt(string, AppConstants.QR_CODER_AES_STATIC_KEY));
				map.put("createUser", userId);
				newWhiteList.add(map);
			}
			userAppDao.insertWhiteList(newWhiteList);
		}
		// 拿数据库和备份表格数据取差集,得到数据库多余的数据
		dsList.removeAll(newList);
		if (dsList.size() > 0) {
			List<Map<String, String>> newWhiteList = new ArrayList<Map<String, String>>();
			for (String string : dsList) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("staffId", AESUtil.encrypt(string, AppConstants.QR_CODER_AES_STATIC_KEY));
				newWhiteList.add(map);
			}
			userAppDao.updateWhieListStatus(newWhiteList);// 更改白名单的状态
			userAppDao.updateUserAppStatus(newWhiteList);// 更改userapp的状态
		}
	}

	// 查询MyAccount信息
	@Override
	public UserAppPO selectMyAccount() throws Exception {

		String userId = (String) request.getSession().getAttribute("userId");

		logger.info("session里边的userId是===" + userId);

		if (StringUtils.isBlank(userId)) {
			throw new ValidateException("user.userId");
		}

		// String userId = "243fc266def747c0b5f3419d842f6337";

		UserAppPO userAppPO = userAppDao.selectUserAppById(userId);

		return userAppPO;
	}

	// 上传MyAccount用户头像
	@Override
	public String uploadImgToMyAccount(Map<String, String> map) throws Exception {

		String imageForBase64 = map.get("imageForBase64");

		if (!StringUtils.startsWithIgnoreCase(imageForBase64, "http")) {
			// 转化为multipartfile
			MultipartFile multipartFile = Base64Util.base64ToMultipart(imageForBase64);
			String filePath = UploadImgUtil.uploadImg(multipartFile, "appUser", request);
			return filePath;
		}

		return null;
	}

	@Override
	public int selectUserCountByUserId(String userid) throws Exception {
		return userAppDao.selectUserCountByUserId(userid);
	}

	@Override
	public String selectStaffIdByUserId(String logonUserId) throws Exception {
		String staffId = userAppDao.selectStaffIdByUserId(logonUserId);
		if(StringUtils.isBlank(staffId)){
			logger.info("该用户不存在或已被删除！");
		}
		return staffId;
	}

	@Override
	public void addPrize(String staffId,String activeid,Integer prizeLevel,String bu) throws Exception {
		PrizepeoPO prizePo = new PrizepeoPO();
		String id = UUID.randomUUID().toString().replace("-", "");
		prizePo.setId(id);
		prizePo.setActiveId(activeid);
		prizePo.setStaffId(staffId);
		prizePo.setPrizeLevel(prizeLevel);
		prizePo.setBu(bu);
		userAppDao.insertPrize(prizePo);
	}
	
	@Override
	public List<UserAppPO> getTopThreePrizePeople() throws Exception {
		
		List<UserAppPO> userInfoList = userAppDao.selectTopThreePrizePeople();
		int m = 1;
		for (int i = 0; i < userInfoList.size(); i++) {
			UserAppPO userAppPO = userInfoList.get(i);
			logger.info("该获奖人基本信息：" + userAppPO.toString());
			logger.info("该获奖人名次是第" + m + "名");
			userAppPO.setRanking(m++);
		}
		return userInfoList;
	}
	
	@Override
	public void removeTop3PrizePeoples() throws Exception {
		userAppDao.deleteTop3PrizePeoples();
	}

	/**
	 * saveMyAccount
	 */
	@Override
	public UserAppPO saveMyAccount(UserAppPO userAppPO) throws Exception {

		String imageForBase64 = userAppPO.getImageForBase64();

		if (StringUtils.isNotBlank(imageForBase64)) {
			if (!StringUtils.startsWithIgnoreCase(imageForBase64, "http")) {
				// 转化为multipartfile
				MultipartFile multipartFile = Base64Util.base64ToMultipart(imageForBase64);
				String filePath = UploadImgUtil.uploadImg(multipartFile, "appUser", request);
				userAppPO.setPhoto(filePath);
			}
		}

		String userId = (String) request.getSession().getAttribute("userId");
		logger.info("session里边的userId是===" + userId);
		if (StringUtils.isBlank(userId)) {
			throw new ValidateException("user.userId");
		}

		// String userId = "1ad0861d9b324bcda614a612a6d40869";

		userAppPO.setUserId(userId);
		logger.info("user nickName --->" + userAppPO.getNickName() + ", user Photo ---->" + userAppPO.getPhoto());
		// 更新头像和姓名
		userAppDao.updateMyAccount(userAppPO);
		return userAppPO;
	}

	@Override
	public UserAppPO selectUserByUserIdAndUdid(String userId, String udid) throws Exception {
		return userAppDao.selectUser(userId, udid);
	}

	@Override
	public UserAppPO insertOrUpdateUser(UserAppPO userAppPO) throws Exception {

		String staffId = userAppPO.getStaffId();

		// 根据staffid查询该用户是否存在,不存在插入
		UserAppPO po = userAppDao.selectUserInfoByStaffId(staffId);

		if (null == po) {
			// 设置id
			String id = UUID.randomUUID().toString().replace("-", "");
			// 设置默认头像
			String serverPath = PathUtil.getServerPath(request);
			String photo = serverPath + File.separator + "freemarker" + File.separator + "defaultImg" + File.separator
					+ "userAppDefaultHeadPortrait.jpg";
			userAppPO.setPhoto(photo);
			userAppPO.setUserId(id);
			userAppDao.insertUserInfo(userAppPO);
			request.getSession().setAttribute("userId", id);
			logger.info("登录时存进去的session的userId是===" + request.getSession().getAttribute("userId"));
			return userAppPO;
		}
		request.getSession().setAttribute("userId", po.getUserId());
		return po;
	}

	// private static List<LotteryPO> list = new ArrayList<LotteryPO>();
	Float value = 10f;
	private static String key;

	@SuppressWarnings("boxing")
	@Override
	public String addLotteryNum(final LotteryPO po) throws Exception {

		String status = activeMap.get(po.getActiveId());

		if ("1".equals(status)) {
			return "1";
		} else if ("3".equals(status)) {
			return "3";
		}

		if (missList.contains(po.getStaffId())) {
			return "2";
		}

		if (list.size() > 0) {
			list.remove(0);
		}

		int random = (int) (Math.random() * 10 + 1);
		map.put(po.getStaffId(), map.get(po.getStaffId()) == null ? po.getLotteryNum() + random
				: map.get(po.getStaffId()) + po.getLotteryNum() + random);

		list = new ArrayList<Map.Entry<String, Float>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
			// 降序排序
			@Override
			public int compare(Entry<String, Float> o1, Entry<String, Float> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}

		});


		if (list.size() > peoNum) {
			subList = list.subList(0, peoNum);
		} else {
			subList = list;
		}
		// webSocketServer.sendMessage3(subList);

		return "2";

	}

	@Override
	public String updatetTurnOff(ControlActivePO po) throws Exception {

		String status = activeMap.get(po.getId());
		if ("3".equals(status)) {
			throw new ValidateException("active.error");
		}
		activeMap.put(po.getId(), po.getStatus());
		if ("2".equals(po.getStatus())) { // 点击go,开始进行活动,传2
			// peoNum = po.getPeoNum();
			// controlStatus = true;
			userAppDao.updateActiveStaus(po);
		} else { // 活动结束,传3
			// if (subList.size() > 0) { // 插入中奖人名单
			// insertPrizePeo(po);
			// }
			// 添加已中奖人名单
			missList.add(subList.get(0).getKey());
			String id = UUID.randomUUID().toString().replace("-", "");
			String staffId = subList.get(0).getKey().substring(0, 10);
			userAppDao.insertPrizePeo(id, po.getId(), staffId, 1, po.getBu());
			key = "";
			map.clear();// 恢复默认值
			list.clear();
			// peoNum = 10;
			// controlStatus = false;
			userAppDao.updateActiveStaus(po);
		}

		return po.getStatus();
	}

	private void insertPrizePeo(ControlActivePO po) throws Exception {
		List<PrizepeoPO> prizepoList = new ArrayList<PrizepeoPO>();
		for (int i = 0; i < subList.size(); i++) {
			String staffId = subList.get(i).getKey().substring(0, 10);
			// 已中奖人staffId存入内存
			missList.add(subList.get(i).getKey());
			PrizepeoPO prizePo = new PrizepeoPO();
			String id = UUID.randomUUID().toString().replace("-", "");
			prizePo.setId(id);
			prizePo.setActiveId(po.getId());
			prizePo.setStaffId(staffId);
			prizePo.setPrizeLevel(i + 1);
			prizePo.setBu(po.getBu());
			// userAppDao.insertPrizePeo(id, po.getId(), staffId, i + 1,
			// po.getBu());
			prizepoList.add(prizePo);
		}
		// 批量插入中奖人名单
		userAppDao.insertPrizePeoBatch(prizepoList);
	}

	@Override
	public Map<String, Object> selectIfLucky(String staffId, String activeId) throws Exception {

		Map<String, Object> ifLucky = userAppDao.selectIfLucky(staffId, activeId);
		if (null == ifLucky) {
			Map<String, Object> resMap = new HashMap<String, Object>();
			resMap.put("ifLucky", "false");
			return resMap;
		} else {
			ifLucky.put("ifLucky", "true");
			return ifLucky;
		}

	}

	@Override
	public String selectActiveStatus(String id) throws Exception {

		String status = userAppDao.selectActiveStatus(id);

		return status;
	}

	@Override
	public void updateMissList() throws Exception {
		List<Map<String, String>> selectActiveStatusList = userAppDao.selectActiveStatusMap();
		for (Map<String, String> listMap : selectActiveStatusList) {
			activeMap.put(listMap.get("id"), listMap.get("status"));
		}
		list.clear();
		missList.clear();

	}

	@Override
	public Integer selectPeoNum() throws Exception {
		return list.size();
	}

	@Override
	public List<Entry<String, Float>> selectResultOneByOne() throws Exception {
		return subList;
	}

	@Override
	public void updateKey(String updateData) throws Exception {
		key = updateData;
	}
}