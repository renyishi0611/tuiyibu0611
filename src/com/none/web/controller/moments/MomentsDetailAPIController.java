package com.none.web.controller.moments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.none.core.common.utils.JSONUtil;
import com.none.core.controller.BaseControllerSupport;
import com.none.core.model.Pager;
import com.none.core.model.Result;
import com.none.web.common.SysConstant;
import com.none.web.po.CommentPO;
import com.none.web.po.MomsPathPO;
import com.none.web.service.moments.IMomentsDetailAPIService;

import spring.mvc.bind.annotation.FormModel;

@Controller
@RequestMapping("momentsDetail")
public class MomentsDetailAPIController extends BaseControllerSupport {
	public static Logger logger = Logger.getLogger(MomentsDetailAPIController.class);

	@Autowired
	private IMomentsDetailAPIService momentsDetailService;

	/**
	 * app 详情
	 */
	@ResponseBody
	@RequestMapping("queryDetailByMomentsId")
	public Object queryDetailByMomentsId(String logonUserId, Integer momentsId, Integer current, Integer numPerPage)
			throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", data);

		if (null == current) {
			current = Integer.valueOf(1);
		}
		if (null == numPerPage) {
			numPerPage = Integer.valueOf(10);
		}
		Map<String, Object> details = null;
		Integer state = SysConstant.STATE_SUCCESS;
		String resultString = "result.success";
		try {
			details = momentsDetailService.queryMomentsByMomentsId(logonUserId, momentsId, current, numPerPage);
		} catch (DataAccessException e) {
			logger.error("query is fail, please check again", e);
			state = SysConstant.STATE_FAILURE;
			resultString = "result.fail";
		}
		result = new Result(state, resultString, details);
		return JSONUtil.toJSON(result);
	}

	// CMS
	@ResponseBody
	@RequestMapping("getmomentsInfById")
	public Object getmomentsInfById(String logonUserId, Integer momentsId, Integer current, Integer numPerPage)
			throws Exception {
		if (null == current) {
			current = Integer.valueOf(1);
		}
		if (null == numPerPage) {
			numPerPage = Integer.valueOf(10);
		}
		Map<String, Object> details = null;
		Integer state = SysConstant.STATE_SUCCESS;
		String resultString = "result.success";
		try {
			details = momentsDetailService.queryMomentsByMomentsId(logonUserId, momentsId, current, numPerPage);
		} catch (DataAccessException e) {
			logger.error("query is fail, please check again", e);
			state = SysConstant.STATE_FAILURE;
			resultString = "result.fail";
		}
		Result result = new Result(state, resultString, details);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("queryMomentsByMomentsId")
	public Object queryMomentsByMomentsId(int momentsId) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> momentsPO = momentsDetailService.queryMomentsByMomentsId(momentsId);

		Integer state = SysConstant.STATE_SUCCESS;
		String resultString = "result.success";
		if (null == momentsPO) {
			state = SysConstant.STATE_FAILURE;
			resultString = "result.fail";
		}

		data.put("data", momentsPO);
		Result result = new Result(state, resultString, data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * app
	 */
	@ResponseBody
	@RequestMapping("queryCommentByMomentsId")
	public Object queryCommentByMomentsIdForPage(int momentsId, int current, int numPerPage) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> details = momentsDetailService.queryCommentByMomentsIdForPage(momentsId, current,
				numPerPage);

		Integer state = SysConstant.STATE_SUCCESS;
		String resultString = "result.success";
		if (null == details || details.isEmpty()) {
			state = SysConstant.STATE_FAILURE;
			resultString = "result.fail";
		}
		data.put("data", details);
		Result result = new Result(state, resultString, data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * app 添加评论
	 */
	@ResponseBody
	@RequestMapping("saveComment")
	public Object saveComment(@FormModel("comment") final CommentPO comment, String logonUserId) throws Exception {

		boolean isSuccess = momentsDetailService.insertCommentByMomentsId(comment, logonUserId);
		Integer state = SysConstant.STATE_SUCCESS;
		String resultString = "result.success";
		if (!isSuccess) {
			state = SysConstant.STATE_FAILURE;
			resultString = "result.fail";
		}
		Result result = new Result(state, resultString);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("showPic")
	public Object showPic(Integer currentPage, Integer numPerPage) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		List<MomsPathPO> momsPathPOs = null;
		if (null == currentPage) {
			currentPage = Integer.valueOf(1);
		}
		if (null == numPerPage) {
			numPerPage = Integer.valueOf(5);
		}
		Integer state = SysConstant.STATE_SUCCESS;
		String resultString = "result.success";
		try {
			momsPathPOs = momentsDetailService.showPicList(currentPage, numPerPage);
		} catch (DataAccessException e) {
			logger.error("query is fail, please check again", e);
			state = SysConstant.STATE_FAILURE;
			resultString = "result.fail";
		}
		data.put("data", momsPathPOs);
		Result result = new Result(state, resultString, data);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("getNewComment")
	public Object getNewComment(@RequestBody Map<String, Integer> map) {		
		
		String uif = getRequest().getHeader("uif");
		String[] info=uif.split("[$]");
//		String logonUserId = null;
//		if (StringUtils.equals(SysConstant.cmsPlatform, info[0])) {
//			logonUserId = (String)getSession().getAttribute(SysConstant.cmsUserId);
//		} else if (StringUtils.equals(SysConstant.appPlatform, info[0])) {
//			logonUserId = (String)getSession().getAttribute("userId");
//		}
		String logonUserId = "154261255201600130423dd76836081d";

		Integer currentPage = map.get("currentPage");
		if (null == currentPage) {
			currentPage = Integer.valueOf(1);
		}
		Integer numPerPage = map.get("numPerPage");
		if (null == numPerPage) {
			numPerPage = Integer.valueOf(2);
		}

		Integer momentsId = map.get("momentsId");
		Pager pager = new Pager();
		pager.setPageNo(currentPage);
		pager.setPageSize(numPerPage);

		Integer state = SysConstant.STATE_SUCCESS;
		String resultString = "result.success";
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> commentPOs;
		try {
			commentPOs = momentsDetailService.queryNewCommentByMomentsId(momentsId, pager);
			if (commentPOs.size() > 0) {
				data.put("momentsId", momentsId);
			}
			int totalComment = momentsDetailService.getCommentCountByMomentId(momentsId);
			if (StringUtils.equals(SysConstant.appPlatform, info[0])) {
				logonUserId = (String)getSession().getAttribute("userId");
				// 获取当前登录用户头像
				String currentUserPhoto = momentsDetailService.getUserPhotoByUserId(logonUserId);
				data.put("currentUserPhoto", currentUserPhoto);
			}
			data.put("commentList", commentPOs);
			data.put("total", totalComment);
			
			pager.setTotalRows(totalComment);
			data.put("pager", pager);
			data.put("momentsId", momentsId);

		} catch (Exception e) {
			logger.info("query has an issue, please check", e);
			state = SysConstant.STATE_FAILURE;
			resultString = "result.fail";
		}

		Result result = new Result(state, resultString, data);

		return JSONUtil.toJSON(result);
	}
}
