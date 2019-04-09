package com.none.web.service.voice.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.none.core.common.utils.DateUtil;
import com.none.core.common.utils.PageUtil;
import com.none.core.common.utils.PathUtil;
import com.none.core.common.utils.PropertyUtil;
import com.none.core.common.utils.StringUtil;
import com.none.core.exception.ValidateException;
import com.none.web.QRcode.constants.AppConstants;
import com.none.web.QRcode.util.AESUtil;
import com.none.web.dao.BuDao;
import com.none.web.dao.VoiceOfCommentDao;
import com.none.web.dao.VoiceOfStaffDao;
import com.none.web.po.CommentPO;
import com.none.web.po.ReqVoiceListPO;
import com.none.web.po.ReqVoiceOfComListPO;
import com.none.web.po.VoiceExcelPO;
import com.none.web.po.VoiceOfCommentPO;
import com.none.web.po.VoiceOfStaffPO;
import com.none.web.service.user.IUserService;
import com.none.web.service.voice.IVoiceOfStaffService;

@Service
public class VoiceOfStaffServiceImpl implements IVoiceOfStaffService {

	public static Logger logger = Logger.getLogger(VoiceOfStaffServiceImpl.class);

	@Autowired
	private VoiceOfStaffDao voiceDao;

	@Autowired
	private VoiceOfCommentDao comDao;

	@Autowired
	private BuDao buDao;

	@Autowired
	private IUserService iUserService;

	@Override
	public Map<String, Object> selectVoiceList(ReqVoiceListPO po) throws Exception {

		// 验证参数
		if (null == po) {
			throw new ValidateException("param.error");
		}
		if (StringUtil.isEmpty(po.getPageNo()) || StringUtil.isEmpty(po.getPageSize())) {
			throw new ValidateException("param.error");
		}

		if (StringUtils.isNotBlank(po.getUserType())) { // 说明是cms用户,查询该用户权限
			logger.info("userType ===" + po.getUserType());
			if (!"Central Administrator".equals(po.getUserType())) { // 说明不是CA用户
				po.setStatus(0);
				po.setIsHide(0);
			}
		}

		// 设置分页参数
		po.setStartPage((po.getPageNo() - 1) * po.getPageSize());

		// 查询voiceList,以最后更新时间作为提交时间,按最后更新时间排序
		List<VoiceOfStaffPO> voiceList = voiceDao.selectVoiceList(po);
		// 遍历列表,修改时间类型和set是否是今天
		if (null != voiceList && voiceList.size() > 0) {
			for (VoiceOfStaffPO voiceOfStaffPO : voiceList) {
				String subTime = voiceOfStaffPO.getSubmitTime();
				// 格式化时间
				String staffTime = DateUtil.staffTime(subTime);
				// 重新设置时间格式,yesToday
				voiceOfStaffPO.setSubmitTime(staffTime);
				// 判断是否是今天
				voiceOfStaffPO.setIsToday(isToday(subTime));
				// 设置评论数量
				int i = voiceDao.selectCountOfVoice(voiceOfStaffPO.getId());
				voiceOfStaffPO.setCommentCount(i);
			}
		}

		int count = voiceDao.selectVoiceListCount(po);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("voiceOfStaffList", voiceList);
		resultMap.put("totalRows", count);
		resultMap.put("pageSize", po.getPageSize());
		resultMap.put("pageNum", po.getPageNo());

		int totalPage;
		totalPage = PageUtil.reckonTotalPage(count, po.getPageSize());

		resultMap.put("totalPages", totalPage);
		return resultMap;

	}

	@Override
	public VoiceOfStaffPO selectVoiceDetail(ReqVoiceListPO po) throws Exception {
		// 验证参数
		if (null == po) {
			throw new ValidateException("param.error");
		}
		if (StringUtil.isEmpty(po.getVoiceId()) || StringUtil.isNotEmpty(po.getPageNo())) {
			throw new ValidateException("param.error");
		}
		// 根据id获取voice详情,调用列表查询dao
		List<VoiceOfStaffPO> voiceList = voiceDao.selectVoiceList(po);

		VoiceOfStaffPO poDetail = voiceList.get(0);

		String subTime = poDetail.getSubmitTime();
		// 格式化时间
		String staffTime = DateUtil.staffTime(subTime);
		// 重新设置时间格式,yesToday
		poDetail.setSubmitTime(staffTime);
		// 判断是否是今天
		poDetail.setIsToday(isToday(subTime));

		// 设置评论数量
		int i = voiceDao.selectCountOfVoice(po.getVoiceId());
		poDetail.setCommentCount(i);

		return poDetail;
	}

	private String isToday(String subTime) {
		Date subDate = DateUtil.parseDateTimes(subTime);
		boolean b = DateUtils.isSameDay(subDate, new Date());

		return b ? "Y" : "N";
	}

	@Override
	public void insertVoiceOfStaff(VoiceOfStaffPO po) throws Exception {

		logger.info("session里边的userId====" + po.getSubUser());
		// 新增时,设置修改人信息为发布人信息,以后修改voice时,只修改lastUpdateUser人信息
		po.setUpdateUser(po.getSubUser());
		po.setIsDelete(0);

		// 插入voice
		voiceDao.insertVoiceOfStaff(po);
	}

	@Override
	public void updateVoice(VoiceOfStaffPO po) throws Exception {

		// 修改voice
		voiceDao.updateVoiceById(po);
		// 判断如果是删除操作,就把该voice的所有评论全部删掉
		if (null != po.getIsDelete() && po.getIsDelete() == 1) {
			comDao.delCommentByVoiceId(po.getId());
		}
	}

	@Override
	public Map<String, Object> selectVoiceOfComList(ReqVoiceOfComListPO po, HttpServletRequest request)
			throws Exception {

		// 验证参数
		if (null == po) {
			throw new ValidateException("param.error");
		}
		if (StringUtil.isEmpty(po.getPageNo()) || StringUtil.isEmpty(po.getPageSize())
				|| StringUtil.isEmpty(po.getVoiceId())) {
			throw new ValidateException("param.error");
		}

		// 设置分页参数
		po.setStartPage((po.getPageNo() - 1) * po.getPageSize());
		List<VoiceOfCommentPO> comList = comDao.selectVoiceOfComList(po);
		if (null != comList && comList.size() > 0) {

			for (VoiceOfCommentPO voiceOfCommentPO : comList) {

				String subTime = voiceOfCommentPO.getSubmitTime();
				// 格式化时间
				String staffTime = DateUtil.staffTime(subTime);
				// 重新设置时间格式,yesToday
				voiceOfCommentPO.setSubmitTime(staffTime);

				// 给每个评论设置用户信息(username和头像)
				setUserInfo(voiceOfCommentPO, request);
			}
		}

		// 查询评论总数
		int count = comDao.selectVoiceOfComListCount(po.getVoiceId());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("commentList", comList);
		resultMap.put("totalRows", count);
		resultMap.put("pageSize", po.getPageSize());
		resultMap.put("pageNum", po.getPageNo());
		int totalPage;
		totalPage = PageUtil.reckonTotalPage(count, po.getPageSize());
		resultMap.put("totalPages", totalPage);

		return resultMap;
	}

	private void setUserInfo(VoiceOfCommentPO voiceOfCommentPO, HttpServletRequest request) throws Exception {
		// 分平台查询用户信息
		String platform = voiceOfCommentPO.getPlatform();
		Map<String, Object> userMap = new HashMap<String, Object>();
		if ("app".equals(platform)) {
			userMap = iUserService.getUserInfoByUserIdAndPlatform(voiceOfCommentPO.getUserId(), platform);
		} else {
			userMap = buDao.getBuNameAndPhotoByUserId(voiceOfCommentPO.getUserId());
		}
		String userNameOrGroupName = (String) userMap.get("user_name");
		voiceOfCommentPO.setUserName(
				StringUtil.isNotEmpty(userNameOrGroupName) ? userNameOrGroupName : (String) userMap.get("displayName"));
		String photo = (String) userMap.get("headPortrait");
		if (StringUtil.isEmpty(photo)) { // 头像信息如果为空,设置默认头像,因在App端显示,统一用App端的默认头像
			String fileName = PropertyUtil.getProperty("SystemConfig.properties", "userApp.DefaultHeadPortrait");
			String folderPath = PropertyUtil.getProperty("SystemConfig.properties", "oss.freemarker_img");
			photo = PathUtil.getOssPath(fileName, folderPath);// 设置默认图像
			// String serverPath = PathUtil.getServerPath(request);
			// photo = serverPath + File.separator + "freemarker" +
			// File.separator + "defaultImg" + File.separator
			// + "userAppDefaultHeadPortrait.jpg";
		}
		voiceOfCommentPO.setPhoto(photo);
	}

	@Override
	public int saveComment(CommentPO commentPo) throws Exception {

		if (StringUtil.isEmpty(commentPo.getVoiceId())) {
			throw new ValidateException("param.error");
		}
		if (StringUtil.isEmpty(commentPo.getUserId())) {
			throw new ValidateException("param.error");
		}
		if (StringUtil.isEmpty(commentPo.getPlatform())) {
			throw new ValidateException("param.error");
		}

		comDao.insertComment(commentPo);
		return 0;
	}

	@Override
	public void delComment(int comId) throws Exception {
		comDao.delCommentByComId(comId);

	}

	@Override
	public void exportExcel(Map<String, String> reqMap, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		logger.info("start export excel...");
		String startTime = reqMap.get("startTime");
		String endTime = reqMap.get("endTime");

		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNoneBlank(startTime)) {
			map.put("startTime", DateUtil.parseDateTime(startTime));
		}
		if (StringUtils.isNoneBlank(endTime)) {
			map.put("endTime", DateUtil.parseDateTime(endTime));
		}

		// 创建titelList
		List<String> titelList = new ArrayList<String>();
		titelList.add("data of post");
		titelList.add("topic");
		titelList.add("username");
		titelList.add("comment text");
		titelList.add("comment time");
		titelList.add("Staff ID");

		// 查询contentList
		long start = System.currentTimeMillis();
		List<VoiceExcelPO> contentList = voiceDao.selectVoiceExcel(map);

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
		for (VoiceExcelPO voiceExcelPO : contentList) {
			// 从第二行开始创建
			row = sheet.createRow(sheet.getLastRowNum() + 1);
			row.createCell(0).setCellValue(voiceExcelPO.getSubmitTime());
			String topic = voiceExcelPO.getTopic();
			String trimBrTopic = trimBr(topic);
			row.createCell(1).setCellValue(trimBrTopic);
			String staffId = voiceExcelPO.getStaffId();
			row.createCell(2).setCellValue(
					StringUtil.isNotEmpty(staffId) ? voiceExcelPO.getAppUserName() : voiceExcelPO.getCmsUserName());
			String commentText = voiceExcelPO.getCommentText();
			String trimBrCom = trimBr(commentText);
			row.createCell(3).setCellValue(trimBrCom);
			row.createCell(4).setCellValue(voiceExcelPO.getCommentTime());
			row.createCell(5).setCellValue(StringUtil.isNotEmpty(staffId)
					? AESUtil.decrypt(staffId, AppConstants.QR_CODER_AES_STATIC_KEY) : "");
		}
		String filename = formatDateString(startTime) + "-" + formatDateString(endTime) + "voice.xls";

		// 设置头信息
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();
		long end = System.currentTimeMillis();
		logger.info("耗时:===" + (end - start) + "ms");

	}

	/**
	 * 格式化时间字符串方法
	 * 
	 * @param dateString
	 *            传入一个时间字符串
	 * @return 返回一个格式化后的时间戳字符串
	 */
	private String formatDateString(String dateString) {

		return dateString.replace("-", "").replace(":", "").replace(" ", "");
	}

	private String trimBr(String content) {
		return content.replaceAll("<br/>", "");
	}

}