package com.none.web.controller.userApp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.none.core.common.utils.JSONUtil;
import com.none.core.controller.BaseControllerSupport;
import com.none.core.exception.ValidateException;
import com.none.core.model.Result;
import com.none.web.common.SysConstant;
import com.none.web.model.TUserApp;
import com.none.web.po.ControlActivePO;
import com.none.web.po.LotteryPO;
import com.none.web.po.ReqAppUserListPO;
import com.none.web.po.UserAppPO;
import com.none.web.service.userApp.IUserAppService;

/**
 * App用户Controller
 */
@Controller
@RequestMapping("userApp")
public class UserAppController extends BaseControllerSupport {

	@Autowired
	private IUserAppService userAppService;
	public static Logger logger = Logger.getLogger(UserAppController.class);

	/**
	 * app 扫描二维码 激活用户
	 */
	@ResponseBody
	@RequestMapping("activeQRCode")
	public Object activeQRCode(String code, String udid, HttpServletRequest request) throws Exception {

		if (StringUtils.isBlank(code)) {
			throw new ValidateException("userApp.QRcodeNotRight");
		}
		if (StringUtils.isBlank(udid)) {
			throw new ValidateException("parameter.error");
		}
		String[] filter = new String[] { "" };
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("userInfo", userAppService.activeQRCode(code, udid, request));
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", userMap);
		return JSONUtil.toJSON(result, filter);
	}

	/**
	 * app用户登录
	 */
	@ResponseBody
	@RequestMapping("loginUser")
	public Object loginUser(String userId, String udid, String isFirstLogin, HttpServletRequest request)
			throws Exception {

		if (StringUtils.isBlank(udid)) {
			logger.info("参数为空udid：" + udid);
			throw new ValidateException("parameter.error");
		}
		if (StringUtils.isBlank(userId)) {
			logger.info("参数为空userId：" + userId);
			throw new ValidateException("parameter.error");
		}
		if (StringUtils.isBlank(isFirstLogin)) {
			logger.info("参数为空isFirstLogin：" + isFirstLogin);
			throw new ValidateException("parameter.error");
		}
		String[] filter = new String[] { "" };
		Map<String, Object> userMap = new HashMap<String, Object>();
		TUserApp tUser = userAppService.mergeLoginUser(userId, udid, isFirstLogin);
		userMap.put("user", tUser);
		Result result = new Result(SysConstant.STATE_SUCCESS, "userApp.loginUser", userMap);
		if (tUser == null) {
			result = new Result(SysConstant.STATE_ERROR, "userApp.loginError", null);
		}
		return JSONUtil.toJSON(result, filter);
	}

	/**
	 * app 扫描二维码更换设备
	 */
	@ResponseBody
	@RequestMapping("changeDeviceQRCode")
	public Object changeDeviceQRCode(String code, String udid, HttpServletRequest request) throws Exception {

		if (StringUtils.isBlank(code)) {
			throw new ValidateException("userApp.QRcodeNotRight");
		}
		if (StringUtils.isBlank(udid)) {
			logger.info("参数为空udid：" + udid);
			throw new ValidateException("parameter.error");
		}
		String[] filter = new String[] { "" };
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("userInfo", userAppService.activeChangeDeviceQRCode(code, udid, request));
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", userMap);
		return JSONUtil.toJSON(result, filter);
	}

	/**
	 * app T&C页面，勾选同意条款
	 */
	@ResponseBody
	@RequestMapping("acceptTerms")
	public Object acceptTerms(String userId) throws Exception {

		if (StringUtils.isBlank(userId)) {
			throw new ValidateException("parameter.error");
		}
		String[] filter = new String[] { "" };
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("userInfo", userAppService.changeAcceptTerms(userId, "Y"));
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", userMap);
		return JSONUtil.toJSON(result, filter);
	}

	@ResponseBody
	@RequestMapping("searchAppUserList")
	public Object searchList(@RequestBody final ReqAppUserListPO appUserPO) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> resMp = userAppService.selectAppUserList(appUserPO);
		data.put("result", resMp);
		Result result = new Result(SysConstant.STATE_SUCCESS, "appUser.seachSuccess", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * app menu菜单查询用户名、用户图像
	 */
	@ResponseBody
	@RequestMapping("searchUserAppById")
	public Object searchUserAppById(String userId, HttpServletRequest request) throws Exception {

		if (StringUtils.isBlank(userId)) {
			logger.info("查询的用户：" + userId + "不存在");
			throw new ValidateException("parameter.error");
		}
		String[] filter = new String[] { "" };
		Map<String, Object> data = new HashMap<String, Object>();
		UserAppPO userApp = userAppService.searchUserAppById(userId, request);
		data.put("result", userApp);
		Result result = new Result(SysConstant.STATE_SUCCESS, "appUser.seachSuccess", data);
		return JSONUtil.toJSON(result, filter);
	}

	@ResponseBody
	@RequestMapping("exportAPPUserExcel")
	public Object exportCMSMomExcel(HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		userAppService.exportAppUserExcel(response);
		Result result = new Result(SysConstant.STATE_SUCCESS, "appUser.exportSuccess", data);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("importAPPUserExcel")
	public Object importCMSMomExcel(@RequestParam("file") MultipartFile multfile, HttpSession httpSession)
			throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		userAppService.importAppUserExcel(multfile, httpSession);
		Result result = new Result(SysConstant.STATE_SUCCESS, "appUser.importSuccess", data);
		return JSONUtil.toJSON(result);
	}

	/*
	 * 根据userId查询myAccount
	 */
	@ResponseBody
	@RequestMapping("selectMyAccount")
	public Object selectMyAccount() throws Exception {

		String[] filter = new String[] { "password" };
		Result result = new Result(SysConstant.STATE_SUCCESS, "userApp.selectMyAccount",
				userAppService.selectMyAccount());

		return JSONUtil.toJSON(result, filter);
	}

	@ResponseBody
	@RequestMapping("uploadImg")
	public Object uploadImgToMyAccount(@RequestBody final Map<String, String> map) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		String filePath = userAppService.uploadImgToMyAccount(map);
		data.put("path", filePath);
		Result result = new Result(SysConstant.STATE_SUCCESS, "userApp.uploadImgToMyAccount", data);
		return JSONUtil.toJSON(result);
	}

	/*
	 * saveMyAccount修改头像和姓名信息,根据前端的要求来更新字段
	 */
	@ResponseBody
	@RequestMapping("saveMyAccount")
	public Object saveMyAccount(@RequestBody final UserAppPO userAppPO) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		userAppService.saveMyAccount(userAppPO);
		Result result = new Result(SysConstant.STATE_SUCCESS, "userApp.updateMyAccount", data);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("loginUserInfo")
	public Object loginUserInfo(@RequestBody final UserAppPO userAppPO) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		UserAppPO staffApp = userAppService.insertOrUpdateUser(userAppPO);
		data.put("userId", staffApp.getUserId());
		data.put("staffId", staffApp.getStaffId());
		data.put("userName", staffApp.getUserName());
		data.put("nickName", staffApp.getNickName());
		data.put("photo", staffApp.getPhoto());

		Result result = new Result(SysConstant.STATE_SUCCESS, "wtp.success", data);
		return result;
	}

	@ResponseBody
	@RequestMapping("addLotteryNum")
	public Object addLotteryNum(@RequestBody final LotteryPO po) throws Exception {

		String status = userAppService.addLotteryNum(po);

		Result result = new Result(SysConstant.STATE_SUCCESS, "success", status);
		return result;
	}

	@ResponseBody
	@RequestMapping("controlActive")
	public Object controlActive(@RequestBody final ControlActivePO po) throws Exception {

		String turnOff = userAppService.updatetTurnOff(po);

		Result result = new Result(SysConstant.STATE_SUCCESS, "success", turnOff);
		return result;
	}

	@ResponseBody
	@RequestMapping("selectIfLucky")
	public Object selectIfLucku(@RequestBody final Map<String, String> map) throws Exception {

		Map<String, Object> lucky = userAppService.selectIfLucky(map.get("staffId"), map.get("activeId"));

		Result result = new Result(SysConstant.STATE_SUCCESS, "success", lucky);
		return result;
	}

	@ResponseBody
	@RequestMapping("selectActiveStatus")
	public Object selectActiveStatus(@RequestBody final ControlActivePO po) throws Exception {

		String status = userAppService.selectActiveStatus(po.getId());

		Result result = new Result(SysConstant.STATE_SUCCESS, "success", status);
		return result;
	}

	@ResponseBody
	@RequestMapping("clearMissList")
	public Object clearMissList() throws Exception {

		userAppService.updateMissList();

		Result result = new Result(SysConstant.STATE_SUCCESS, "success");
		return result;
	}

	@ResponseBody
	@RequestMapping("selectPeoNum")
	public Object selectPeoNum() throws Exception {

		Integer peoNum = userAppService.selectPeoNum();

		Result result = new Result(SysConstant.STATE_SUCCESS, "success", peoNum);
		return result;
	}

	@ResponseBody
	@RequestMapping("selectResultOneByOne")
	public Object selectResultOneByOne() throws Exception {

		List<Entry<String, Float>> list = userAppService.selectResultOneByOne();

		Result result = new Result(SysConstant.STATE_SUCCESS, "success", list);
		return result;
	}

	@ResponseBody
	@RequestMapping("updateKey")
	public Object updateKey(@RequestBody Map<String, String> map) throws Exception {

		userAppService.updateKey(map.get("key"));

		Result result = new Result(SysConstant.STATE_SUCCESS, "success");
		return result;
	}

}
