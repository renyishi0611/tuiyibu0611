package com.none.web.controller.voice;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.none.core.common.utils.JSONUtil;
import com.none.core.controller.BaseControllerSupport;
import com.none.core.model.Result;
import com.none.web.UserInfoDetail;
import com.none.web.common.SysConstant;
import com.none.web.po.CommentPO;
import com.none.web.po.ReqVoiceListPO;
import com.none.web.po.ReqVoiceOfComListPO;
import com.none.web.po.VoiceOfStaffPO;
import com.none.web.service.moments.IMomentsService;
import com.none.web.service.voice.IVoiceOfStaffService;
import com.none.web.utils.HtmlUtil;

/**
 * 员工心声controller
 * 
 * @author winter
 *
 */
@Controller
@RequestMapping("voice")
public class VoiceOfStaffController extends BaseControllerSupport {

	private static Logger logger = Logger.getLogger(VoiceOfStaffController.class);
	@Autowired
	private IVoiceOfStaffService voiceSerice;
	@Autowired
	private IMomentsService momentService;

	/**
	 * 员工心声列表展示
	 */
	@ResponseBody
	@RequestMapping("getVoiceList")
	public Object getVoiceList(@RequestBody final ReqVoiceListPO po) throws Exception {

		Map<String, Object> map = voiceSerice.selectVoiceList(po);
		Result result = new Result(SysConstant.STATE_SUCCESS, "voices.seachSuccess", map);

		return JSONUtil.toJSON(result);
	}

	/**
	 * 员工心声详情
	 */
	@ResponseBody
	@RequestMapping("getVoiceDetail")
	public Object getVoiceDetail(@RequestBody final ReqVoiceListPO po) throws Exception {

		VoiceOfStaffPO detail = voiceSerice.selectVoiceDetail(po);
		Result result = new Result(SysConstant.STATE_SUCCESS, "voicesDetail.getSuccess", detail);

		return JSONUtil.toJSON(result);
	}

	/**
	 * 新增员工心声
	 * 
	 * @param voiceOfStaffPO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("saveVoice")
	public Object insertVoice(@RequestBody final VoiceOfStaffPO voiceOfStaffPO, @UserInfoDetail String logonUserId)
			throws Exception {
		voiceOfStaffPO.setSubUser(logonUserId);
		Result result = null;
		// 校验subUser是否为空
		String isExist = momentService.validateCMSUser(voiceOfStaffPO.getSubUser());
		if ("-1".equals(isExist)) {
			result = new Result(SysConstant.REQUEST_ERROR, "request.error");
			return JSONUtil.toJSON(result);
		}
		if (StringUtils.isNotBlank(voiceOfStaffPO.getContent())) {
			if ("".equals(HtmlUtil.getTextFromHtml(voiceOfStaffPO.getContent()))) {
				result = new Result(SysConstant.STATE_FAILURE, "voice.InputError", null);
				return JSONUtil.toJSON(result);
			} else {
				voiceOfStaffPO.setContent(HtmlUtil.getTextFromHtml(voiceOfStaffPO.getContent()));
			}
		}
		// 插入员工心声
		voiceSerice.insertVoiceOfStaff(voiceOfStaffPO);
		result = new Result(SysConstant.STATE_SUCCESS, "operation success");
		return JSONUtil.toJSON(result);
	}

	/**
	 * 编辑员工心声
	 * 
	 * @param voiceOfStaffPO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("editVoice")
	public Object editVoice(@RequestBody final VoiceOfStaffPO voiceOfStaffPO) throws Exception {
		Result result;
		// 校验编辑人id是否为空
		String isExist = momentService.validateCMSUser(voiceOfStaffPO.getUpdateUser());
		if ("-1".equals(isExist)) {
			result = new Result(SysConstant.REQUEST_ERROR, "request.error");
			return JSONUtil.toJSON(result);
		}
		if (StringUtils.isNotBlank(voiceOfStaffPO.getContent())) {
			if ("".equals(HtmlUtil.getTextFromHtml(voiceOfStaffPO.getContent()))) {
				result = new Result(SysConstant.STATE_FAILURE, "voice.InputError", null);
				return JSONUtil.toJSON(result);
			} else {
				voiceOfStaffPO.setContent(HtmlUtil.getTextFromHtml(voiceOfStaffPO.getContent()));
			}
		}
		// 修改员工心声
		voiceSerice.updateVoice(voiceOfStaffPO);
		result = new Result(SysConstant.STATE_SUCCESS, "operation success");
		return JSONUtil.toJSON(result);
	}

	/**
	 * 发布和隐藏员工心声
	 * 
	 * @param voiceOfStaffPO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("isHide")
	public Object isHide(@RequestBody final VoiceOfStaffPO voiceOfStaffPO) throws Exception {

		// 修改员工心声
		voiceSerice.updateVoice(voiceOfStaffPO);
		Result result = new Result(SysConstant.STATE_SUCCESS, "operation success");
		return JSONUtil.toJSON(result);
	}

	/**
	 * 删除员工心声
	 * 
	 * @param voiceOfStaffPO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("delVoice")
	public Object delVoice(@RequestBody final VoiceOfStaffPO voiceOfStaffPO) throws Exception {

		// 修改员工心声
		voiceSerice.updateVoice(voiceOfStaffPO);
		Result result = new Result(SysConstant.STATE_SUCCESS, "operation success");
		return JSONUtil.toJSON(result);
	}

	/**
	 * 员工心声评论列表展示
	 * 
	 * @param po
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getVoiceOfComList")
	public Object getVoiceOfComList(@RequestBody final ReqVoiceOfComListPO po, HttpServletRequest request)
			throws Exception {

		Map<String, Object> map = voiceSerice.selectVoiceOfComList(po, request);
		Result result = new Result(SysConstant.STATE_SUCCESS, "getAppCommentsList.success", map);

		return JSONUtil.toJSON(result);
	}

	/**
	 * 员工心声发布评论
	 * 
	 * @param po
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("saveComment")
	public Object saveComment(@RequestBody final CommentPO po) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		Result result = null;
		if (StringUtils.isNotBlank(po.getContents())) {
			if ("".equals(HtmlUtil.getTextFromHtml(po.getContents()))) {
				result = new Result(SysConstant.STATE_FAILURE, "comment.inputError", null);
				return JSONUtil.toJSON(result);
			} else {
				po.setContents(HtmlUtil.getTextFromHtml(po.getContents()));
			}
		}
		int i = voiceSerice.saveComment(po);
		if (0 == i) {
			result = new Result(SysConstant.STATE_SUCCESS, "comment.success", data);
		} else {
			result = new Result(SysConstant.STATE_FAILURE, "comment.error", data);
		}
		return JSONUtil.toJSON(result);
	}

	/**
	 * 员工心声CMS删除评论
	 * 
	 * @param po
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("delComment")
	public Object delComment(@RequestBody CommentPO po) throws Exception {

		voiceSerice.delComment(po.getId());
		Result result = new Result(SysConstant.STATE_SUCCESS, "operation success");
		return JSONUtil.toJSON(result);
	}

	/**
	 * cms的voice按照时间导出voice已经评论
	 * 
	 * @param startTime
	 *            报表数据开始时间
	 * @param endTime
	 *            报表数据结束时间
	 * @param request
	 *            网络请求参数
	 * @param response
	 *            网络响应参数
	 * @return 返回状态码和结果信息，数据等
	 * @throws Exception
	 *             抛出异常
	 */
	@ResponseBody
	@RequestMapping("exportCMSVoiceExcel")
	public Object exportCMSMomExcel(String startTime, String endTime, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("start export CMSMomExcel ....");
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		voiceSerice.exportExcel(map, request, response);
		Result result = new Result(SysConstant.STATE_SUCCESS, "moms.exportSuccess", data);
		return JSONUtil.toJSON(result);
	}

}
