package com.none.web.controller.banner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.none.core.common.utils.DateUtil;
import com.none.core.common.utils.JSONUtil;
import com.none.core.common.utils.ParamUtils;
import com.none.core.exception.ValidateException;
import com.none.core.model.Result;
import com.none.web.common.SysConstant;
import com.none.web.controller.userApp.UserAppController;
import com.none.web.model.TMainBanner;
import com.none.web.service.banner.IMainBannerService;

import spring.mvc.bind.annotation.FormModel;

@Controller
@RequestMapping("mainBanner")
public class MainBannerController {

	public static Logger logger = Logger.getLogger(UserAppController.class);
	@Autowired
	private IMainBannerService iMainBannerService;

	@ResponseBody
	@RequestMapping("uploadBanner")
	public Object uploadBanner(@RequestParam("file") MultipartFile file) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("path", iMainBannerService.uploadBanner(file));
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * admin 添加或修改Banner
	 */
	@ResponseBody
	@RequestMapping("saveMainBanner")
	public Object saveMainBanner(@FormModel("banner") final TMainBanner banner, String startTime, String endTime,
			HttpServletRequest request, String userId) throws Exception {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		ParamUtils.validateParam(paramMap, request);
		Map<String, Object> data = new HashMap<String, Object>();
		banner.setLastUpdateUser(userId);
		banner.setStartTime(new Timestamp(Long.valueOf(startTime.trim())));
		banner.setEndTime(new Timestamp(Long.valueOf(endTime.trim())));
		banner.setLastUpdateTime(new Timestamp(new Date().getTime()));
		banner.setStatus(0);
		String path = banner.getPath().replace("&#47;", "/");
		banner.setPath(path);
		data.put("banner", iMainBannerService.saveMainBanner(banner, request));
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * admin backup轮播图列表
	 */
	@ResponseBody
	@RequestMapping("getBannerByCms")
	public Object getBannerByCms() throws Exception {

		String[] filter = new String[] { "lastUpdateTime", "lastUpdateUser", "status" };
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("list", iMainBannerService.showMainBannerByCms());
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", data);
		return JSONUtil.toJSON(result, filter);
	}

	/**
	 * app backup轮播图列表
	 */
	@ResponseBody
	@RequestMapping("getBannerByApp")
	public Object getBannerByApp(String userId) throws Exception {

		String[] filter = new String[] { "lastUpdateTime", "lastUpdateUser", "startTime", "endTime", "status", "flag",
				"bannerOrder", "browseTimes" };
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("list", iMainBannerService.showMainBannerByApp());
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", data);
		return JSONUtil.toJSON(result, filter);
	}

	@ResponseBody
	@RequestMapping("getAllBanner")
	public Object getAllBanner() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("list", iMainBannerService.getAllBanner());
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * 详情 （如果是backup的banner返回对应current banner的endTime加一分钟）
	 */
	@ResponseBody
	@RequestMapping("findById")
	public Object findById(Integer id) throws Exception {
		if (id == null) {
			throw new ValidateException("banner.idNotNull");
		}
		String[] filter = new String[] { "lastUpdateTime", "lastUpdateUser", "status" };
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("banner", iMainBannerService.findById(id));
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", data);
		return JSONUtil.toJSON(result, filter);
	}

	@ResponseBody
	@RequestMapping("delById")
	public Object delById(Integer id) throws Exception {
		if (id == null) {
			throw new ValidateException("banner.idNotNull");
		}
		Map<String, Object> data = new HashMap<String, Object>();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(id);
		iMainBannerService.deleteByIds(ids);
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", data);
		return result;
	}

	@ResponseBody
	@RequestMapping("findCurrentEndTime")
	public Object findCurrentEndTime(String bannerGroup) throws Exception {

		TMainBanner findByGroupAndOrder = iMainBannerService.findByGroupAndOrder(bannerGroup.toString(), "1");
		Map<String, String> map = new HashMap<String, String>();
		if (findByGroupAndOrder != null) {
			// 加一分钟
			String endTime = DateUtil.getBeforeSomeMinute(findByGroupAndOrder.getEndTime().toString(), 1);
			if (endTime != null) {
				map.put("endDate", endTime.substring(0, 10));
				map.put("endHour", endTime.substring(11, 13));
				map.put("endMinute", endTime.substring(14, 16));
			}
		}
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", map);
		return result;
	}

	@ResponseBody
	@RequestMapping("getBanner")
	public Object getBanner() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("list", iMainBannerService.getPath());
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", data);
		return JSONUtil.toJSON(result);
	}

}
