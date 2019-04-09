package com.none.web.controller.moments;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.none.core.common.utils.StringUtil;
import com.none.core.controller.BaseControllerSupport;
import com.none.core.exception.ValidateException;
import com.none.core.model.Pager;
import com.none.core.model.Result;
import com.none.web.common.SysConstant;
import com.none.web.controller.userApp.UserAppController;
import com.none.web.po.CommentPO;
import com.none.web.po.MomentCollectionPO;
import com.none.web.po.MomentPO;
import com.none.web.po.MomsLikePO;
import com.none.web.po.ReqMomListPO;
import com.none.web.service.moments.IMomentsService;
import com.none.web.service.moments.IMomsForwardService;
import com.none.web.service.moments.IMomsLikeService;
import com.none.web.service.moments.IMomsPathService;
import com.none.web.service.userApp.IUserAppService;
import com.none.web.utils.HtmlUtil;
import com.none.web.utils.HttpUtils;

/**
 * 分享管理Controller
 * 
 * @author King
 * 
 */
@Controller
@RequestMapping("moments")
public class MomentsController extends BaseControllerSupport {

	public static Logger logger = Logger.getLogger(UserAppController.class);
	@Autowired
	private IMomentsService momentService;
	@Autowired
	private IMomsLikeService momLikeService;
	@Autowired
	private IMomsPathService momsPathService;
	@Autowired
	private IMomsForwardService momsForwardService;
	@Autowired
	private IUserAppService userAppService;

	@ResponseBody
	@RequestMapping("uploadMomentsFiles")
	public Object uploadMomentsFiles(@RequestParam("file") MultipartFile file) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		String filePath = momentService.uploadMomentsFile(file, getRequest());
		data.put("path", filePath);
		data.put("fileName", file.getOriginalFilename());
		data.put("fileType", momentService.getUploadFileType(file));
		Result result = new Result(SysConstant.STATE_SUCCESS, "result.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * X-Planet 发布 moment 支持app,cms端发布和save文章
	 */
	@ResponseBody
	@RequestMapping("publishOrSaveMoment")
	public Object publishOrSaveMoment(@RequestBody final MomentPO momentPO) throws Exception {

		momentPO.setSubmitUser(getLogonUserId());
		// momentPO.setSubmitUser("154261255201600130423dd76836081d");

		Result result = null;
		if (StringUtil.isEmpty(momentPO.getSubmitType())) {
			throw new ValidateException("parameter.error");
		}
		if (StringUtil.isEmpty(momentPO.getSubmitUser())) {
			throw new ValidateException("parameter.error");
		}

		if (StringUtils.isNotBlank(momentPO.getContent())) {
			if ("".equals(HtmlUtil.getTextFromHtml(momentPO.getContent()))) {
				result = new Result(SysConstant.STATE_FAILURE, "moment.inputError", null);
				return JSONUtil.toJSON(result);
			} else {
				momentPO.setContent(HtmlUtil.getTextFromHtml(momentPO.getContent()));
			}
		}
		Map<String, Object> data = new HashMap<String, Object>();
		momentPO.setLastUpdateUser(momentPO.getSubmitUser());

		if (null != momentPO.getId()) {
			data.put("result", momentService.updateMoment(momentPO));
		} else {
			// 判断当前登录用户id是否是app端用户,如果是app端登录，将该用户的帖子编号获取到，用于前台弹出中奖信息
			if (1 == userAppService.selectUserCountByUserId(getLogonUserId())) {
				// 当前登录用户发帖当天有没有发布过帖子，如果已经发布过帖子，不能再发布，一个app用户一天只能发布一条条子
				int currentUserMomentCount = momentService.selectCurrentUserMomentCount(getLogonUserId());
				if (1 > currentUserMomentCount) {
					// 当前帖子下标
					int momentIndex = momentService.getCurrentMomentIndex();
					// 下标是9，19，28，66，111，200的帖子中奖
					if (momentIndex == 9 || momentIndex == 19 || momentIndex == 28 || momentIndex == 66
							|| momentIndex == 111 || momentIndex == 200) {
						// 获取帖子发布人的staffId
						String staffId = userAppService.selectStaffIdByUserId(getLogonUserId());
						// 将中奖人的中奖信息插入中奖表，用于在我的奖品页面查询
						userAppService.addPrize(staffId, "thunder", 3, "1");
						// 将中奖的下标返回给前台，用于弹出框提示用户中奖
						data.put("momentIndex", momentIndex);
					}
					data.put("result", momentService.addMoment(momentPO, getRequest()));
				} else {
					result = new Result(SysConstant.STATE_SUCCESS, "moment.validateSuccess", null);
					return JSONUtil.toJSON(result);
				}
			} else {
				data.put("result", momentService.addMoment(momentPO, getRequest()));
			}
		}
		result = new Result(SysConstant.STATE_SUCCESS, "moment.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * cms 保存为草稿 moment
	 */
	@ResponseBody
	@RequestMapping("saveMoment")
	public Object saveMoment(@RequestBody final MomentPO momentPO) throws Exception {

		momentPO.setSubmitUser(getLogonUserId());

		Result result = null;
		if (StringUtils.isNotBlank(momentPO.getContent())) {
			if ("".equals(HtmlUtil.getTextFromHtml(momentPO.getContent()))) {
				result = new Result(SysConstant.STATE_FAILURE, "moment.inputError", null);
				return JSONUtil.toJSON(result);
			} else {
				momentPO.setContent(HtmlUtil.getTextFromHtml(momentPO.getContent()));
			}
		}
		Map<String, Object> data = new HashMap<String, Object>();
		// data返回一个result
		if (null != momentPO.getId()) {
			data.put("result", momentService.updateMoment(momentPO));
		} else {
			data.put("result", momentService.addMoment(momentPO, getRequest()));
		}
		result = new Result(SysConstant.STATE_SUCCESS, "moment.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * cms 编辑 moment(通过moment_id更新submit_type为submit)
	 */
	@ResponseBody
	@RequestMapping("getMomentById")
	public Object getMomentById(@RequestBody final MomentPO momentPO) throws Exception {

		momentPO.setSubmitUser(getLogonUserId());

		Map<String, Object> data = new HashMap<String, Object>();
		// data返回一个result
		data.put("result", momentService.getMomentById(momentPO));
		Result result = new Result(SysConstant.STATE_SUCCESS, "moment.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * cms 删除 moment(通过moment_id更新is_delete为1,代表软删除,同时级联删除其上传的filePath,同样也是软删除)
	 */
	@ResponseBody
	@RequestMapping("removeMoment")
	public Object removeMoment(@RequestBody final MomentPO momentPO) throws Exception {

		momentPO.setSubmitUser(getLogonUserId());

		Map<String, Object> data = new HashMap<String, Object>();
		// data返回一个result
		data.put("result", momentService.removeMoment(momentPO));
		Result result = new Result(SysConstant.STATE_SUCCESS, "moment.success", data);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("reviewMoments")
	public Object loginConsoleUser(String momId, String state) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		momentService.reviewMoments(momId, state);
		Result result = new Result(SysConstant.STATE_SUCCESS, "review.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * cms 添加评论
	 */
	@ResponseBody
	@RequestMapping("saveComment")
	public Object commentToMom(@RequestBody CommentPO po) throws Exception {
		Result result;
		// 校验从session中获取的是否为合法用户
		if (!validateLegalUser(getLogonUserId())) {
			result = new Result(SysConstant.STATE_FAILURE, "Your account is illegal!");
		}

		po.setUserId(getLogonUserId());

		Map<String, Object> data = new HashMap<String, Object>();
		// if (StringUtils.isNotBlank(po.getContents())) {
		// if ("".equals(HtmlUtil.getTextFromHtml(po.getContents()))) {
		// result = new Result(SysConstant.STATE_FAILURE, "comment.inputError",
		// null);
		// return JSONUtil.toJSON(result);
		// } else {
		// po.setContents(HtmlUtil.getTextFromHtml(po.getContents()));
		// }
		// }
		int i = momentService.saveComment(po);
		if (0 == i) {
			result = new Result(SysConstant.STATE_SUCCESS, "comment.success", data);
		} else {
			result = new Result(SysConstant.STATE_FAILURE, "comment.error", data);
		}
		return JSONUtil.toJSON(result);
	}

	private boolean validateLegalUser(String logonUserId) throws Exception {
		return HttpUtils.validateEhr(userAppService.selectStaffIdByUserId(logonUserId));
	}

	/**
	 * app moments列表，每一条moments记录评论显示2条，图片等资源文件显示5个，
	 */
	@ResponseBody
	@RequestMapping("getAppMomentsList")
	public Object getAppMomentsList(@RequestBody final Pager pager) throws Exception {

		// 将当前用户userId设置到page对象中
		// pager.setLogonUserId(getLogonUserId());

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("momentsList", momentService.getAppMomentsList(pager));
		data.put("pager", momentService.getTotalCount(pager));
		Result result = new Result(SysConstant.STATE_SUCCESS, "getAppMomentsList.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * my app moments列表，用于"我的文章"页面展示
	 */
	@ResponseBody
	@RequestMapping("getMyAppMomentsList")
	public Object getMyAppMomentsList(@RequestBody final Pager pager) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		// 将当前用户userId设置到page对象中
		pager.setLogonUserId(getLogonUserId());

		data.put("momentsList", momentService.getMyAppMomentsList(pager));
		data.put("pager", momentService.getMyMomentTotalCount(pager));
		Result result = new Result(SysConstant.STATE_SUCCESS, "getMyAppMomentsList.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * my collection moments列表，用于"我的收藏"页面展示
	 */
	@ResponseBody
	@RequestMapping("getMyCollectionMomentList")
	public Object getMyCollectionMomentList(@RequestBody final Pager pager) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		// 将当前用户userId设置到page对象中
		pager.setLogonUserId(getLogonUserId());

		data.put("momentsList", momentService.getMyCollectionMomentList(pager));
		data.put("pager", momentService.getMyCollectionCount(pager));
		Result result = new Result(SysConstant.STATE_SUCCESS, "getMyCollectionMomentList.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * App端moments的slider 获取含有图片的moments最新5个moments
	 */
	@ResponseBody
	@RequestMapping("getSliderMomentsList")
	public Object getSliderMomentsList(HttpServletRequest request) throws Exception {

		String userId = (String) request.getSession().getAttribute("userId");
		String AuthenticatedUserId = (String) request.getSession().getAttribute("AuthenticatedUserId");
		logger.info("****************" + AuthenticatedUserId + "***********************" + userId);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("sliderMomentsList", momsPathService.getSliderMoments());
		Result result = new Result(SysConstant.STATE_SUCCESS, "getAppMomentsList.success", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * isLike 传入userid和momentsid，返回的msg为like success点赞成功，cancel like success取消点赞
	 * 
	 */
	@ResponseBody
	@RequestMapping("isLike")
	public Object isLike(@RequestBody final MomsLikePO momsLikePO) throws Exception {

		Result result;
		// 校验从session中获取的是否为合法用户
		if (!validateLegalUser(getLogonUserId())) {
			result = new Result(SysConstant.STATE_FAILURE, "Your account is illegal!");
		}

		momsLikePO.setUserId(getLogonUserId());

		Map<String, Object> data = momLikeService.updateActiveStatus(momsLikePO);
		if ((Boolean) data.get("activeStatus")) {
			result = new Result(SysConstant.STATE_SUCCESS, "like.success", data);
		} else {
			result = new Result(SysConstant.STATE_SUCCESS, "cancel.like.success", data);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping("searchCMSMomList")
	public Object searchList(@RequestBody final ReqMomListPO po) throws Exception {

		po.setUserId(getLogonUserId());
		// po.setUserId("34d9596b581748368f7e3478ad3966fd");

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> resMp = momentService.selectMomList(po);
		data.put("result", resMp);
		Result result = new Result(SysConstant.STATE_SUCCESS, "moms.seachSuccess", data);
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("exportCMSMomExcel")
	public Object exportCMSMomExcel(String startTime, String endTime, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		momentService.exportExcel(map, request, response);
		Result result = new Result(SysConstant.STATE_SUCCESS, "moms.exportSuccess", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * moment 删除评论
	 * 
	 * @param po
	 *            评论对象参数
	 * @return 返回一个操作结果编码，和操作结果信息
	 * @throws Exception
	 *             抛出异常
	 */
	@ResponseBody
	@RequestMapping("delComment")
	public Object delComment(@RequestBody CommentPO po) throws Exception {

		momentService.delComment(po.getId());
		Result result = new Result(SysConstant.STATE_SUCCESS, "operation success");
		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("momsForward")
	public Object momsForward(@RequestBody MomentPO momentPO) throws Exception {

		momentPO.setSubmitUser(getLogonUserId());

		Result result = null;
		Map<String, Object> data = new HashMap<String, Object>();

		if (StringUtil.isEmpty(momentPO.getId())) {
			throw new ValidateException("parameter.error");
		}
		if (StringUtil.isEmpty(momentPO.getSubmitUser())) {
			throw new ValidateException("parameter.error");
		}
		momsForwardService.insertMomsForward(momentPO);

		int forwardNum = momsForwardService.selectForwardNum(momentPO);
		data.put("forwardNum", forwardNum);
		result = new Result(SysConstant.STATE_SUCCESS, "moment.forwardSuccess", data);
		return JSONUtil.toJSON(result);
	}

	/**
	 * 收藏或者取消收藏文章
	 * 
	 * @param collectionPO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("isCollection")
	public Object delComment(@RequestBody MomentCollectionPO collectionPO) throws Exception {

		// 校验从session中获取的是否为合法用户
		Result result;
		if (!validateLegalUser(getLogonUserId())) {
			result = new Result(SysConstant.STATE_FAILURE, "Your account is illegal!");
		}

		collectionPO.setCollectionUserId(getLogonUserId());

		Map<String, Object> resMap = momLikeService.saveOrUpdatecollectionMom(collectionPO);
		if ((Boolean) resMap.get("result")) {
			result = new Result(SysConstant.STATE_SUCCESS, "collection.success", resMap);
		} else {
			result = new Result(SysConstant.STATE_SUCCESS, "Cancel.collection.success", resMap);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping("getLotteryMoments")
	public Object getLotteryMoments(@RequestBody final Pager pager) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Result result = null;

		data.put("lotteryMomentList", momentService.getLotteryMomentList(pager));
		data.put("pager", momentService.getLotteryMomentCount(pager));
		result = new Result(SysConstant.STATE_SUCCESS, "lottery.success", data);

		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("getTopThreeMoments")
	public Object getTopThreeMoments() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Result result = null;

		List<String> staffList = momentService.getStaffList();
		if(staffList != null){
			userAppService.removeTop3PrizePeoples();
			for (int i = 0; i < staffList.size(); i++) {
				if(i == 0){
					userAppService.addPrize(staffList.get(i),"top3",1,"1");
				}else if(i == 1){
					userAppService.addPrize(staffList.get(i),"top3",2,"1");
				}else{
					userAppService.addPrize(staffList.get(i),"top3",3,"1");
				}
			}
		}

		data.put("winnerList", momentService.getTopThreeUserInfoList());
		data.put("thunderList", momentService.getThunderUserInfoList());
		result = new Result(SysConstant.STATE_SUCCESS, "topthree.success", data);

		return JSONUtil.toJSON(result);
	}

	@ResponseBody
	@RequestMapping("getMyPrize")
	public Object getMyPrizeByUserId() throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		Result result = null;

		data.put("isLunky", momentService.isLunky(getLogonUserId()));
		data.put("myPrizeList", momentService.getMyPrizeByUserId(getLogonUserId()));
		result = new Result(SysConstant.STATE_SUCCESS, "myPrizeList.success", data);

		return JSONUtil.toJSON(result);
	}

	// @ResponseBody
	// @RequestMapping("getAppCommentsByMomId")
	// public Object getAppCommentsByMomId(Pager pager,Integer momentsId) throws
	// Exception {
	// Map<String, Object> data = new HashMap<String, Object>();
	// Result result = null;
	//
	// data.put("commentList",
	// momsCommentService.getAppCommentsByMomId(pager,momentsId));
	// data.put("pager",
	// momsCommentService.getCommentCountByMomId(pager,momentsId));
	// data.put("currentUserPhoto",
	// momsCommentService.getCurrentUserPhoto((String)getSession().getAttribute("userId")));
	// result = new Result(SysConstant.STATE_SUCCESS,"lottery.success",data);
	//
	// return JSONUtil.toJSON(result);
	// }
	/**
	 * 从session 中或当前登录用户
	 * 
	 * @return
	 */
	private String getLogonUserId() {
		String uif = getRequest().getHeader("uif");
		String[] info = uif.split("[$]");
		String logonUserId = null;
		if (StringUtils.equals(SysConstant.cmsPlatform, info[0])) {
			logonUserId = (String) getSession().getAttribute(SysConstant.cmsUserId);
		} else if (StringUtils.equals(SysConstant.appPlatform, info[0])) {
			logonUserId = (String) getSession().getAttribute("userId");
		}
		return logonUserId;
	}
}
