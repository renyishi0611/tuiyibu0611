package com.none.web.controller.exhibition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.none.core.common.utils.JSONUtil;
import com.none.core.controller.BaseControllerSupport;
import com.none.core.model.Pager;
import com.none.core.model.Result;
import com.none.web.common.SysConstant;
import com.none.web.model.LivePoll;
import com.none.web.model.PollAnswer;
import com.none.web.po.UserAppPO;
import com.none.web.service.exhibition.ILivePollAPIService;
import com.none.web.service.exhibition.ILivePollExportAPIService;
import com.none.web.utils.HtmlUtil;

@Controller
@RequestMapping("livePoll")
public class LivePollAPIController extends BaseControllerSupport {
	public static Logger logger = Logger.getLogger(LivePollAPIController.class);

	@Autowired
	private ILivePollAPIService livePollAPIServiceImpl;
	@Autowired
	private ILivePollExportAPIService livePollExcportAPIService;

	@Autowired
	HttpServletRequest request;

	@ResponseBody
	@RequestMapping("queryAllLivePoll")
	public Object queryAllLivePoll(@RequestBody Map<String, Object> paramMap) { // Map<String,
																				// Object>
																				// //
																				// paramMap
		logger.info("start query all live poll ...");
		String platform = (String) paramMap.get("platform");

		String logonUserId = getLogonUserId();
		Result result = null;

		Integer state = SysConstant.STATE_SUCCESS;
		String resultString = "result.success";
		logger.info("start query all param handle ..." + paramMap);
		Map<String, String> pagerMap = (Map<String, String>) paramMap.get("pager");
		Pager pager = new Pager();
		pager.setPageSize(NumberUtils.toInt(pagerMap.get("pageSize"), 1));
		pager.setPageNo(NumberUtils.toInt(pagerMap.get("pageNo"), 5));

		Map<String, Object> map = null;

		try {
			List<Map<String, Object>> list = livePollAPIServiceImpl.queryLivePoll(pager, platform, logonUserId);
			if (null != list && list.size() > 0) {
				map = new HashMap<String, Object>();
				map.put("result", list);
				map.put("pager", pager);
			}
		} catch (Exception e) {
			logger.error("have error, please check", e);
			state = SysConstant.STATE_FAILURE;
			resultString = "result.error";
		}

		result = new Result(state, resultString, map);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("queryLivePollById")
	public Object queryLivePollById(@RequestBody Map<String, Object> paramMap) throws Exception {
		logger.debug("why didn't out the log");
		String logonUserId = getLogonUserId();
		Result result = null;
		logger.info("check the logonUserId: " + logonUserId);

		Integer state = -1;
		String resultString = null;

		Map<String, Object> map = null;
		Integer livePollId = (Integer) paramMap.get("livePollId");
		String platform = (String) paramMap.get("platform");
		try {
			map = livePollAPIServiceImpl.queryLivePollByLivePollId(livePollId, platform, logonUserId);
			state = SysConstant.STATE_SUCCESS;
			resultString = "result.success";
		} catch (Exception e) {
			logger.error("have error, please check", e);
			state = SysConstant.STATE_FAILURE;
			resultString = "result.error";
		}

		result = new Result(state, resultString, map);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("userPolling")
	public Object queryLivePollResult(@RequestBody final Map<String, Object> param) throws Exception {

		String userKey = "app_AuthenticatedUserId";
		// if(StringUtils.equals(platform, "cms"))
		// {
		// userKey="AuthenticatedUserId";
		// }
		String logonUserId = getLogonUserId();
		Result result = null;

		UserAppPO userAppPO = new UserAppPO();
		userAppPO.setUserId((String) param.get("userId"));
		userAppPO.setUserName((String) param.get("userName"));

		PollAnswer pollAnswer = new PollAnswer();
		pollAnswer.setId(NumberUtils.toInt((String) param.get("id")));
		pollAnswer.setPollId(NumberUtils.toInt((String) param.get("pollId")));

		Integer state = -1;
		String resultString = null;
		int updateResult = 0;
		// Result result = null;

		try {
			Integer checkResult = livePollAPIServiceImpl.checkLogonUserIsAnswer(
					NumberUtils.toInt((String) param.get("pollId")), (String) param.get("userId"));

			if (checkResult > 0) {
				state = SysConstant.STATE_SUCCESS;
				resultString = "livePoll.polling.polled";
				result = new Result(state, resultString, (0 == updateResult ? false : true));
				return JSONUtil.toJSON(result);
			}

			updateResult = livePollAPIServiceImpl.updatePollAnswer(pollAnswer, userAppPO);
			state = SysConstant.STATE_SUCCESS;
			resultString = "livePoll.polling.result";
		} catch (Exception e) {
			state = SysConstant.STATE_FAILURE;
			resultString = "livePoll.polling.fail";
			logger.error("please check the data", e);
		}

		result = new Result(state, resultString, 0 == updateResult ? false : true);
		return JSONUtil.toJSON(result);
	}

	private Result getLoginUserIdCheck(String logonUserId) {
		Result result = new Result(SysConstant.REQUEST_ERROR, "request.error");
		return result;
	}

	@ResponseBody
	@RequestMapping("saveLivePoll")
	public Object createLivePoll(@RequestBody final LivePoll livePoll) throws Exception {
		Result result = null;

		// String summitUserId = getLogonUserId();
		String summitUserId = livePoll.getSubmitUser();

		Integer state = 0; // SysConstant.STATE_SUCCESS;
		String resultString = null; // "result.success";

		if (null == summitUserId) {
			state = SysConstant.STATE_FAILURE;
			resultString = "result.error";
			summitUserId = "154227955679700kz85d008202sa1396";
		}
		if (StringUtils.isNotBlank(livePoll.getContent())) {
			if ("".equals(HtmlUtil.getTextFromHtml(livePoll.getContent()))) {
				state = SysConstant.STATE_FAILURE;
				resultString = "livepoll.inputError";
				result = new Result(state, resultString, null);
				return JSONUtil.toJSON(result);
			} else {
				livePoll.setContent(HtmlUtil.getTextFromHtml(livePoll.getContent()));
			}
		}

		livePoll.setSubmitUser(summitUserId);
		livePoll.setLastupdateUser(summitUserId);

		List<PollAnswer> answerList = livePoll.getPollAnswer();

		int resultNum = 0;
		try {
			resultNum = livePollAPIServiceImpl.createLivePoll(livePoll, answerList);
			state = 0;
			resultString = "result.success";
		} catch (Exception e) {
			logger.error("have error, please check", e);
			state = SysConstant.STATE_FAILURE;
			resultString = "result.error";
		}
		result = new Result(state, resultString, resultNum > 0 ? true : false);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("exportLivePoll")
	public Object exportLivePoll(String startTime, String endTime, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// 获取数据
		livePollExcportAPIService.exportLivePollReports(startTime, endTime, request, response);
		Map<String, Object> data = new HashMap<String, Object>();

		Result result = new Result(SysConstant.STATE_SUCCESS, "livePoll.exportSuccess", data);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("modifyLivePoll")
	public Object modifyLivePoll(@RequestBody final LivePoll livePoll) {
		Result result = null;

		String summitUserId = getLogonUserId();

		Integer state = 0; // SysConstant.STATE_SUCCESS;
		String resultString = null; // "result.success";

		if (null == summitUserId) {
			state = SysConstant.STATE_FAILURE;
			resultString = "result.error";
			summitUserId = "154227955679700kz85d008202sa1396";
		}

		if (StringUtils.isNotBlank(livePoll.getContent())) {
			if ("".equals(HtmlUtil.getTextFromHtml(livePoll.getContent()))) {
				state = SysConstant.STATE_FAILURE;
				resultString = "livepoll.inputError";
				result = new Result(state, resultString, null);
				return JSONUtil.toJSON(result);
			} else {
				livePoll.setContent(HtmlUtil.getTextFromHtml(livePoll.getContent()));
			}
		}

		livePoll.setLastupdateUser(summitUserId);
		List<PollAnswer> answerList = livePoll.getPollAnswer();

		boolean modifyResult = false;
		try {
			modifyResult = livePollAPIServiceImpl.modifyLivePoll(livePoll, answerList);
			state = 0;
			resultString = "result.success";
		} catch (Exception e) {
			logger.error("have error, please check", e);
			state = SysConstant.STATE_FAILURE;
			resultString = "result.error";
		}
		result = new Result(state, resultString, modifyResult);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("updateActive")
	public Object updateActiveLivePoll(@RequestBody Map<String, Object> param) {
		String logonUserId = getLogonUserId();

		Integer state = 0; // SysConstant.STATE_SUCCESS;
		String resultString = null; // "result.success";

		int resultNum = 0;
		try {
			resultNum = livePollAPIServiceImpl.updateActivePoll((Integer) param.get("livePollId"), logonUserId);
			state = 0;
			resultString = "result.success";
		} catch (Exception e) {
			logger.error("have error, please check", e);
			state = SysConstant.STATE_FAILURE;
			resultString = "result.error";
		}
		Result result = new Result(state, resultString, resultNum > 0 ? true : false);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("deleteLivePoll")
	public Object deleteLivePollById(@RequestBody Map<String, Object> param) {
		String logonUserId = getLogonUserId();
		Integer state = 0; // SysConstant.STATE_SUCCESS;
		String resultString = null; // "result.success";

		int resultNum = 0;
		try {
			resultNum = livePollAPIServiceImpl.deleteLivePollById((Integer) param.get("livePollId"), logonUserId);
			state = 0;
			resultString = "result.success";
		} catch (Exception e) {
			logger.error("have error, please check", e);
			state = SysConstant.STATE_FAILURE;
			resultString = "result.error";
		}
		Result result = new Result(state, resultString, resultNum > 0 ? true : false);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("checkAnswer")
	public Object checkAnswerById(@RequestBody Map<String, Object> param) {
		String logonUserId = getLogonUserId();
		Integer state = 0; // SysConstant.STATE_SUCCESS;
		String resultString = null; // "result.success";

		Integer resultVaule = 0;
		try {
			resultVaule = livePollAPIServiceImpl.checkLogonUserIsAnswer(NumberUtils.toInt((String) param.get("pollId")),
					logonUserId);
			state = 0;
			resultString = "result.success";
		} catch (Exception e) {
			logger.error("have error, please check", e);
			state = SysConstant.STATE_FAILURE;
			resultString = "result.error";
		}
		Result result = new Result(state, resultString, resultVaule);
		return JSONUtil.toJSON(result);
	}

	private String getLogonUserId() {
		String uif = request.getHeader("uif");
		String[] info = uif.split("[$]");

		String logonUserId = null;
		if (StringUtils.equals(SysConstant.cmsPlatform, info[0])) {
			logonUserId = (String) request.getSession().getAttribute(SysConstant.cmsUserId);
		} else if (StringUtils.equals(SysConstant.appPlatform, info[0])) {
			logonUserId = info[2];
		}
		return logonUserId;
	}
}
