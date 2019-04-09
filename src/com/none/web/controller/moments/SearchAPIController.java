package com.none.web.controller.moments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.none.core.common.utils.JSONUtil;
import com.none.core.model.Result;
import com.none.web.common.SysConstant;
import com.none.web.po.BuPO;
import com.none.web.service.moments.ISearchAPIService;
import com.none.web.utils.ValidateFieldUtils;

@Controller
@RequestMapping("queryUserMoments")
public class SearchAPIController {

	public static Logger logger = Logger.getLogger(SearchAPIController.class);

	@Autowired
	private ISearchAPIService searchAPIService;

	@ResponseBody
	@RequestMapping("searchUserMatch")
	public Object searchUserMatch(String field) {
		logger.info("start search user match from field...: param: " + field);
		Map<String, Object> data = new HashMap<String, Object>();
		List<BuPO> buList = null;
		Integer state = SysConstant.STATE_SUCCESS;
		String resultString = "result.success";
		Result result = null;

		String filterEmoji = ValidateFieldUtils.filterEmoji(field);
		logger.info("filterEmoji===" + filterEmoji);

//		if (!ValidateFieldUtils.validateInputField(field)) {
//			logger.info("please check the input field, thanks");
//			state = SysConstant.STATE_FAILURE;
//			resultString = "validate.fail";
//			result = new Result(state, resultString, "false");
//			return null;
//		}

		try {
			buList = searchAPIService.searchUserMatchField(field);
		} catch (Exception e) {
			logger.error("query has error, please check", e);

			state = SysConstant.STATE_FAILURE;
			resultString = "result.fail";
		}
		data.put("result", buList);
		result = new Result(state, resultString, data);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("searchMomentsByUserId")
	public Object searchMomentsByUserId(String logonUserId, String searchUserId, Integer pageNo, Integer pageSize,
			Integer cCurrentPage, Integer cNumperPage) {

		Result result = null;

		if (null == cCurrentPage || 0 == cCurrentPage) {
			cCurrentPage = 1;
		}

		if (null == cNumperPage || 0 == cNumperPage) {
			cNumperPage = 2;
		}

		Map<String, Object> momentsMap = null;
		Integer state = SysConstant.STATE_SUCCESS;
		String resultString = "result.success";
		try {
			momentsMap = searchAPIService.searchMomentsByUserId(logonUserId, searchUserId, pageNo, pageSize,
					cCurrentPage, cNumperPage);
		} catch (Exception e) {
			logger.error("query has error, please check", e);

			state = SysConstant.STATE_FAILURE;
			resultString = "result.fail";
		}

		result = new Result(state, resultString, momentsMap);
		return JSONUtil.toJSON(result);
	}
}
