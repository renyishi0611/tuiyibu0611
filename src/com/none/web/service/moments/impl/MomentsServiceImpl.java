package com.none.web.service.moments.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.none.core.common.utils.DateUtil;
import com.none.core.common.utils.PageUtil;
import com.none.core.common.utils.StringUtil;
import com.none.core.exception.ValidateException;
import com.none.core.model.Pager;
import com.none.web.QRcode.constants.AppConstants;
import com.none.web.QRcode.util.AESUtil;
import com.none.web.common.SysConstant;
import com.none.web.dao.CommentsDao;
import com.none.web.dao.MomentsDao;
import com.none.web.dao.MomsLikeDao;
import com.none.web.dao.MomsPathDao;
import com.none.web.dao.UserAppDao;
import com.none.web.po.CMSMomentListPO;
import com.none.web.po.CommentPO;
import com.none.web.po.ImageForBase64PO;
import com.none.web.po.MomentDetailPO;
import com.none.web.po.MomentDetailPathPO;
import com.none.web.po.MomentPO;
import com.none.web.po.MomsExcelPO;
import com.none.web.po.MomsPathPO;
import com.none.web.po.ReqMomListPO;
import com.none.web.po.UserAppPO;
import com.none.web.service.moments.IMomentsService;
import com.none.web.service.moments.IMomsLikeService;
import com.none.web.utils.Base64Util;
import com.none.web.utils.UploadImgUtil;
import com.none.web.utils.UploadToOSSUtil;

/**
 * moments实现
 * 
 * @author king
 *
 */
@Service
public class MomentsServiceImpl implements IMomentsService {

	private static Logger logger = Logger.getLogger(MomentsServiceImpl.class);

	@Autowired
	private MomentsDao momDao;
	@Autowired
	private MomsLikeDao momLikeDao;
	@Autowired
	private MomsPathDao momPathDao;
	@Autowired
	private CommentsDao momCommentDao;
	@Autowired
	private CommentsDao commentsDao;
	@Autowired
	private IMomsLikeService iMomsLikeService;
	@Autowired
	private UserAppDao userAppDao;

//	@Override
//	public String uploadMomentsFile(MultipartFile multipartFile) throws Exception {
//
//		String originalFilename = multipartFile.getOriginalFilename().toLowerCase();
//		String fileType = UploadToOSSUtil.getFileType(originalFilename);
//		if (StringUtils.isBlank(fileType)) {
//			throw new ValidateException("user.codeNoNeedActive");
//		}
//		return UploadToOSSUtil.uploadAliCloudOss(multipartFile, fileType, SysConstant.MOMENTS);
//	}
	
	@Override
	public String uploadMomentsFile(MultipartFile multipartFile,HttpServletRequest request) throws Exception {

		String fileType = getUploadFileType(multipartFile);
		if (StringUtils.isBlank(fileType)) {
			throw new ValidateException("user.codeNoNeedActive");
		}
		return UploadToOSSUtil.uploadFileToServer(multipartFile, fileType, SysConstant.MOMENTS, request);
	}

	@Override
	public String getUploadFileType(MultipartFile multipartFile) throws Exception {
		return UploadToOSSUtil.getFileType(multipartFile.getOriginalFilename().toLowerCase());
	}

	@Override
	public int reviewMoments(String momId, String state) throws Exception {
		logger.info("momId==" + momId + ";state===" + state);
		Map<String, String> map = new HashMap<String, String>();
		map.put("momId", momId);
		map.put("state", state);
		momDao.updateMomState(map);
		return 0;
	}

	@Override
	public int saveComment(CommentPO commentPo) throws Exception {
		if (StringUtil.isEmpty(commentPo.getMomentsId())) {
			throw new ValidateException("param.error");
		}
		if (StringUtil.isEmpty(commentPo.getUserId())) {
			throw new ValidateException("param.error");
		}
		if (StringUtil.isEmpty(commentPo.getPlatform())) {
			throw new ValidateException("param.error");
		}

		// 插入评论
		momDao.insertComment(commentPo);

		return 0;
	}

	@Override
	public Object addMoment(MomentPO momentPO, HttpServletRequest request) throws Exception {
		
		List<MomsPathPO> filePaths = new ArrayList<MomsPathPO>();
		String filePath = "";
		
		String uif = request.getHeader("uif");
		String[] info = uif.split("[$]");
		if (StringUtils.equals(SysConstant.cmsPlatform, info[0])) {
			filePaths = momentPO.getFilePaths();
		} else if (StringUtils.equals(SysConstant.appPlatform, info[0])) {
			List<ImageForBase64PO> imageForBase64List = momentPO.getImageForBase64List();// 图片不变：url或图片修改：base64码
			if(imageForBase64List != null && imageForBase64List.size() > 0){
				for (int i = 0; i < imageForBase64List.size(); i++) {
					ImageForBase64PO imageForBase64PO = imageForBase64List.get(i);
					if (!StringUtils.startsWithIgnoreCase(imageForBase64PO.getImageForBase64(), "http")) {
						// 转化为multipartfile
						MultipartFile multipartFile = Base64Util.base64ToMultipart(imageForBase64PO.getImageForBase64());
						filePath = UploadImgUtil.uploadImg(multipartFile, "freemarker"+File.separatorChar+"image"+File.separatorChar+"moments", request);
						MomsPathPO momsPathPO = new MomsPathPO();
						momsPathPO.setFilePath(filePath);
						filePaths.add(momsPathPO);
					}
				}
			}
		}
		momentPO.setFilePaths(filePaths);
		
		momDao.insertMoment(momentPO);
		logger.info(momentPO.toString());
		if (null != momentPO.getFilePaths()) {
			for (MomsPathPO momsPathPO : momentPO.getFilePaths()) {
				MomentDetailPathPO pathPO = new MomentDetailPathPO();
				pathPO.setMomentsId(momentPO.getId());
				pathPO.setUserId(momentPO.getSubmitUser());
				pathPO.setFilePath(momsPathPO.getFilePath());
				pathPO.setFileName(momsPathPO.getFilePath().substring(momsPathPO.getFilePath().lastIndexOf("/") + 1));
				pathPO.setFileType(UploadToOSSUtil.getFileType(
						momsPathPO.getFilePath().substring(momsPathPO.getFilePath().lastIndexOf("/") + 1)));

				momPathDao.insertMomPath(pathPO);
			}
		}
		return "success";
	}

	@Override
	public Object updateMoment(MomentPO momentPO) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", momentPO.getId());
		map.put("submitUser", momentPO.getSubmitUser());
		MomentDetailPO moment = momDao.selectMomentById(map);
		
		if (null != momentPO.getId()) {
			if (StringUtils.isNotBlank(momentPO.getContent())) {
				moment.setTitle(momentPO.getTitle());
				moment.setContent(momentPO.getContent());
				if (null != momentPO.getFilePaths()) {
					momPathDao.deletePathByMomId(momentPO.getId());
					for (MomsPathPO momsPathPO : momentPO.getFilePaths()) {
						MomentDetailPathPO pathPO = new MomentDetailPathPO();
						pathPO.setMomentsId(momentPO.getId());
						pathPO.setUserId(momentPO.getSubmitUser());
						pathPO.setFilePath(momsPathPO.getFilePath());
						pathPO.setFileName(
								momsPathPO.getFilePath().substring(momsPathPO.getFilePath().lastIndexOf("/") + 1));
						pathPO.setFileType(UploadToOSSUtil.getFileType(
								momsPathPO.getFilePath().substring(momsPathPO.getFilePath().lastIndexOf("/") + 1)));
						momPathDao.insertMomPath(pathPO);
					}
				}
			}
			moment.setSubmitType(momentPO.getSubmitType());
			momDao.updateMoment(moment);
		}
		return "success";
	}

	@Override
	public Object getMomentById(MomentPO momentPO) throws Exception {
		
		MomentDetailPO detailPO = null;

		if (null == momentPO.getId()) {
			throw new ValidateException("param.error");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", momentPO.getId());
		map.put("submitUser", momentPO.getSubmitUser());
		detailPO = momDao.selectMomentById(map);
		logger.info(detailPO.toString());
		List<MomentDetailPathPO> filePaths = new ArrayList<MomentDetailPathPO>();
		// 代码和了之后
		filePaths = momPathDao.selectCMSPathListByMomentsId(momentPO.getId());
		detailPO.setFilePaths(filePaths);

		return detailPO;
	}

	@Override
	public Object removeMoment(MomentPO momentPO) throws Exception {
		
		if (null != momentPO.getId()) {
			momDao.deleteMomentById(momentPO.getId());
			// 如果该moment有上传文件，级联删除该momentId的上传文件路径列表
			int pathCount = momPathDao.selectPathCountByMomentsId(momentPO.getId());
			if (pathCount > 0) {
				momPathDao.deletePathByMomId(momentPO.getId());
			}
			// 如果该moment有评论，级联删除该momentId下的评论列表
			int commentCount = momCommentDao.selectAppCommentsCountByMomentsId(momentPO.getId());
			if (commentCount > 0) {
				momCommentDao.deleteCommentByMomentsId(momentPO.getId());
			}
			// 如果该moment有点赞，级联删除该momentId下的点赞列表
			int likeCount = momLikeDao.selectLikeCountByMomentsId(momentPO.getId());
			if (likeCount > 0) {
				momLikeDao.deleteLikeByMomentsId(momentPO.getId());
			}
			// 如果该moments被收藏，级联删除该momentId下的被收藏记录
			int collectionCount = momLikeDao.selectCountOfColl(momentPO.getId());
			if(collectionCount > 0){
				momLikeDao.deleteCollectionByMomId(momentPO.getId());
			}
			// 如果该moment踩雷活动中奖，级联删除该momentId下的获奖人记录
			int prizeUserCount = userAppDao.selectPrizeUserByMomentId(momentPO.getId());
			if(prizeUserCount > 0){
				userAppDao.deletePrizeUserByMomentId(momentPO.getId());
			}
		}
		return "success";
	}

	@Override
	public Map<String, Object> selectMomList(ReqMomListPO po) throws Exception {
		// 验证参数
		if (null != po) {
			if (StringUtil.isEmpty(po.getPageNo()) || StringUtil.isEmpty(po.getPageSize())) {
				throw new ValidateException("param.error");
			}
		} else {
			throw new ValidateException("param.error");
		}

		logger.info("po===" + po.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("branch", po.getBranch());
		map.put("userId", po.getUserId());
		// 设置分页参数
		map.put("startPage", (po.getPageNo() - 1) * po.getPageSize());
		map.put("pageSize", po.getPageSize());
		// 设置条件查询参数
		if (StringUtils.isNoneBlank(po.getUserName())) {
			map.put("userName", po.getUserName());
		}
		if (StringUtils.isNoneBlank(po.getStatus())) {
			map.put("type", po.getStatus());
		}
		if (StringUtils.isNoneBlank(po.getStartTime())) {
			map.put("startTime", po.getStartTime());
		}
		if (StringUtils.isNoneBlank(po.getEndTime())) {
			map.put("endTime", po.getEndTime());
		}
		List<CMSMomentListPO> momList = null;
		int count = 0;
		// 根据userType查询列表
		if ("Central Administrator".equals(po.getUserType())) { // CA用户
			// 因为查询所有,所以设置部门为空
			map.remove("branch");
			momList = momDao.selectMomList(map);
			count = momDao.selectMomListCount(map);
		} else if ("Content Provider".equals(po.getUserType())) { // CP用户
			momList = momDao.selectCPMomList(map);
			count = momDao.selectCPMomListCount(map);
		} else if ("Content Approver".equals(po.getUserType())) { // CC用户
			momList = momDao.selectMomList(map);
			count = momDao.selectMomListCount(map);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("momList", momList);
		resultMap.put("totalRows", count);
		resultMap.put("pageSize", po.getPageSize());
		int totalPage = PageUtil.reckonTotalPage(count, po.getPageSize());
		resultMap.put("totalPage", totalPage);
		return resultMap;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void exportExcel(Map<String, String> reqMap, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
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
		titelList.add("moment content");
		titelList.add("staff id");
		titelList.add("username");
		titelList.add("comment text");
		titelList.add("conment time");
		titelList.add("like?");
		titelList.add("like time");

		// 查询contentList
		long start = System.currentTimeMillis();
		List<MomsExcelPO> contentList = momDao.selectMomsExcel(map);

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

		for (int i = 0; i < contentList.size(); i++) {
			// 创建内容行,从第二行开始
			row = sheet.createRow(sheet.getLastRowNum() + 1);
			String momentContent = contentList.get(i).getMomentContent();
			String trimBr = trimBr(momentContent);
			row.createCell(0).setCellValue(trimBr.length() > 200 ? trimBr.substring(0, 200) : trimBr);
			row.createCell(1).setCellValue(StringUtil.isNotEmpty(contentList.get(i).getStaffId())
					? AESUtil.decrypt(contentList.get(i).getStaffId(), AppConstants.QR_CODER_AES_STATIC_KEY) : "");
			row.createCell(2).setCellValue(StringUtils.isNoneBlank(contentList.get(i).getStaffId())
					? contentList.get(i).getUserName() : contentList.get(i).getCmsUserName());
			String commentContent = contentList.get(i).getCommentContent();
			String trimBr2 = trimBr(commentContent);
			row.createCell(3).setCellValue(trimBr2);
			row.createCell(4).setCellValue(contentList.get(i).getCommentTime());
			if (StringUtils.isBlank(contentList.get(i).getIsLike())) { // 没点赞
				row.createCell(5).setCellValue("NO");
			} else {
				row.createCell(5).setCellValue("YES");
			}
			row.createCell(6).setCellValue(contentList.get(i).getLikeTime());
		}
		String filename = formatDateString(startTime) + "-" + formatDateString(endTime) + "moments.xls";
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

	private String trimBr(String content) {
		return content.replaceAll("<br/>", "");
	}

	private String formatDateString(String dateString) {

		return dateString.replace("-", "").replace(":", "").replace(" ", "");
	}

	// -----------App

	@Override
	public List<MomentPO> getAppMomentsList(Pager pager) throws Exception {//

		pager.setStartRow((pager.getPageNo() - 1) * pager.getPageSize());

		List<MomentPO> list = momDao.selectAppMomentsDESC(pager);
		for (MomentPO appMomentsPO : list) {
			// 获取该条appMoment记录的id
			int appMomentsId = appMomentsPO.getId();

			logger.info("该条moment发布人===>" + appMomentsPO.getSubmitUser());
			// 文件列表，这里可以直接调用dao
			List<MomsPathPO> appMomsPathPOList = momPathDao.selectPathListByMomentsId(appMomentsId);
			// 文件总数
			int appMomentsPathCount = momPathDao.selectPathCountByMomentsId(appMomentsId);

			// 评论总数
			int commentCount = commentsDao.selectAppCommentsCountByMomentsId(appMomentsId);
			// 点赞总数
			int likeNum = momLikeDao.selectLikeCountByMomentsId(appMomentsId);
			// 上面有逻辑判断，不能直接使用dao
			boolean activeStatus = false;

			int activeNum = iMomsLikeService.selectIsLikeForMoments(pager.getLogonUserId(), appMomentsId);
			if (activeNum == 0) {// activeNum返回0，就是点赞；返回1就是取消点赞。
				activeStatus = true;
			}
			// 收藏总数
			int collectionNum = momLikeDao.selectCountOfColl(appMomentsId);
			// 收藏状态
			boolean collectionStatus = false;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("momentId", appMomentsPO.getId());
			map.put("userId", pager.getLogonUserId());
			int currentUserCollNum = momLikeDao.selectCurrentCollNum(map);

			if (currentUserCollNum > 0) {
				collectionStatus = true;
			}

			// 将值set进list集合
			appMomentsPO.setId(appMomentsId);// 用户id
//			appMomentsPO.setUsernameAndImg(buNameAndPhotoByUserId);// 用户信息
			appMomentsPO.setSubmitTime(DateUtil.staffTime(appMomentsPO.getSubmitTime()));// 转换时间格式并set进去
			appMomentsPO.setFilePaths(appMomsPathPOList);// 图片文件列表
			appMomentsPO.setPathNum(appMomentsPathCount);// 文件总数
			appMomentsPO.setCommentsNum(commentCount);// 评论总数
			appMomentsPO.setLikeNum(likeNum);// 点赞总数
			appMomentsPO.setActivedStatus(activeStatus);
			appMomentsPO.setCollectionNum(collectionNum);
			appMomentsPO.setCollectionStatus(collectionStatus);
		}

		return list;
	}
	
	@Override
	public Object getMyAppMomentsList(Pager pager) throws Exception {
		if (StringUtils.isBlank(pager.getLogonUserId())) {
			throw new ValidateException("user.null");
		}
		logger.info("logonUserId ------->" + pager.getLogonUserId());
		
		pager.setStartRow((pager.getPageNo() - 1) * pager.getPageSize());

		List<MomentPO> list = momDao.selectMyAppMomentsDESC(pager);
		for (MomentPO appMomentsPO : list) {
			// 获取该条appMoment记录的id
			int appMomentsId = appMomentsPO.getId();

			logger.info("该条moment发布人===>" + appMomentsPO.getSubmitUser());
			// 文件列表，这里可以直接调用dao
			List<MomsPathPO> appMomsPathPOList = momPathDao.selectPathListByMomentsId(appMomentsId);
			// 文件总数
			int appMomentsPathCount = momPathDao.selectPathCountByMomentsId(appMomentsId);

			// 评论总数
			int commentCount = commentsDao.selectAppCommentsCountByMomentsId(appMomentsId);
			// 点赞总数
			int likeNum = momLikeDao.selectLikeCountByMomentsId(appMomentsId);
			// 上面有逻辑判断，不能直接使用dao
			boolean activeStatus = false;

			int activeNum = iMomsLikeService.selectIsLikeForMoments(pager.getLogonUserId(), appMomentsId);
			if (activeNum == 0) {// activeNum返回0，就是点赞；返回1就是取消点赞。
				activeStatus = true;
			}
			// 收藏总数
			int collectionNum = momLikeDao.selectCountOfColl(appMomentsId);
			// 收藏状态
			boolean collectionStatus = false;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("momentId", appMomentsPO.getId());
			map.put("userId", pager.getLogonUserId());
			int currentUserCollNum = momLikeDao.selectCurrentCollNum(map);

			if (currentUserCollNum > 0) {
				collectionStatus = true;
			}

			// 将值set进list集合
			appMomentsPO.setId(appMomentsId);// 用户id
//			appMomentsPO.setUsernameAndImg(buNameAndPhotoByUserId);// 用户信息
			appMomentsPO.setSubmitTime(DateUtil.staffTime(appMomentsPO.getSubmitTime()));// 转换时间格式并set进去
			appMomentsPO.setFilePaths(appMomsPathPOList);// 图片文件列表
			appMomentsPO.setPathNum(appMomentsPathCount);// 文件总数
			appMomentsPO.setCommentsNum(commentCount);// 评论总数
			appMomentsPO.setLikeNum(likeNum);// 点赞总数
			appMomentsPO.setActivedStatus(activeStatus);
			appMomentsPO.setCollectionNum(collectionNum);
			appMomentsPO.setCollectionStatus(collectionStatus);
		}

		return list;
	}
	
	@Override
	public Object getMyCollectionMomentList(Pager pager) throws Exception {
		if (StringUtils.isBlank(pager.getLogonUserId())) {
			throw new ValidateException("user.null");
		}
		logger.info("logonUserId ------->" + pager.getLogonUserId());
		
		pager.setStartRow((pager.getPageNo() - 1) * pager.getPageSize());

		List<MomentPO> list = momDao.selectMyCollectionMomentsDESC(pager);
		for (MomentPO appMomentsPO : list) {
			// 获取该条appMoment记录的id
			int appMomentsId = appMomentsPO.getId();

			logger.info("该条moment发布人===>" + appMomentsPO.getSubmitUser());

			// 文件列表，这里可以直接调用dao
			List<MomsPathPO> appMomsPathPOList = momPathDao.selectPathListByMomentsId(appMomentsId);
			// 文件总数
			int appMomentsPathCount = momPathDao.selectPathCountByMomentsId(appMomentsId);

			// 评论总数
			int commentCount = commentsDao.selectAppCommentsCountByMomentsId(appMomentsId);
			// 点赞总数
			int likeNum = momLikeDao.selectLikeCountByMomentsId(appMomentsId);
			// 上面有逻辑判断，不能直接使用dao
			boolean activeStatus = false;

			int activeNum = iMomsLikeService.selectIsLikeForMoments(pager.getLogonUserId(), appMomentsId);
			if (activeNum == 0) {// activeNum返回0，就是点赞；返回1就是取消点赞。
				activeStatus = true;
			}
			// 收藏总数
			int collectionNum = momLikeDao.selectCountOfColl(appMomentsId);
			// 收藏状态
			boolean collectionStatus = false;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("momentId", appMomentsPO.getId());
			map.put("userId", pager.getLogonUserId());
			int currentUserCollNum = momLikeDao.selectCurrentCollNum(map);

			if (currentUserCollNum > 0) {
				collectionStatus = true;
			}

			// 将值set进list集合
			appMomentsPO.setId(appMomentsId);// 用户id
//			appMomentsPO.setUsernameAndImg(buNameAndPhotoByUserId);// 用户信息
			appMomentsPO.setSubmitTime(DateUtil.staffTime(appMomentsPO.getSubmitTime()));// 转换时间格式并set进去
			appMomentsPO.setFilePaths(appMomsPathPOList);// 图片文件列表
			appMomentsPO.setPathNum(appMomentsPathCount);// 文件总数
			appMomentsPO.setCommentsNum(commentCount);// 评论总数
			appMomentsPO.setLikeNum(likeNum);// 点赞总数
			appMomentsPO.setActivedStatus(activeStatus);
			appMomentsPO.setCollectionNum(collectionNum);
			appMomentsPO.setCollectionStatus(collectionStatus);
		}

		return list;
	}

	@Override
	public Pager getTotalCount(Pager pager) throws Exception {

		pager.setStartRow((pager.getPageNo() - 1) * pager.getPageSize());
		
		Integer totalRows = momDao.searchTotalCount(pager);
		pager.setTotalRows(totalRows);
		pager.setTotalPages(totalRows % pager.getPageSize() == 0 ? (totalRows / pager.getPageSize()) : (totalRows / pager.getPageSize()) + 1);

		return pager;
	}
	
	@Override
	public Pager getMyMomentTotalCount(Pager pager) throws Exception {
		pager.setStartRow((pager.getPageNo() - 1) * pager.getPageSize());
		
		Integer totalRows = momDao.getMyMomentTotalCount(pager);

		pager.setTotalRows(totalRows);
		pager.setTotalPages(totalRows % pager.getPageSize() == 0 ? (totalRows / pager.getPageSize()) : (totalRows / pager.getPageSize()) + 1);

		return pager;
	}
	
	@Override
	public Pager getMyCollectionCount(Pager pager) throws Exception {
		pager.setStartRow((pager.getPageNo() - 1) * pager.getPageSize());
		
		Integer totalRows = momDao.selectMyCollectionMomentsCount(pager);

		pager.setTotalRows(totalRows);
		pager.setTotalPages(totalRows % pager.getPageSize() == 0 ? (totalRows / pager.getPageSize()) : (totalRows / pager.getPageSize()) + 1);

		return pager;
	}

	@Override
	public String validateCMSUser(String userId) throws Exception {
		logger.info("传入的userId =====" + userId);
		if (StringUtils.isBlank(userId)) {
			throw new ValidateException("user.userNull");
		}
		UserAppPO po = userAppDao.selectUserById(userId);
		if (null == po) {
			return "-1";
		}
		return "1";
	}

	@Override
	public void delComment(int comId) throws Exception {

		commentsDao.delCommentByComId(comId);

	}
	
	@Override
	public Object getLotteryMomentList(Pager pager) throws Exception {
		pager.setStartRow((pager.getPageNo() - 1) * pager.getPageSize());

		List<MomentPO> list = momDao.selectLotteryMomentsDESC(pager);
		for (MomentPO appMomentsPO : list) {
			// 获取该条appMoment记录的id
			int appMomentsId = appMomentsPO.getId();

			logger.info("该条moment发布人===>" + appMomentsPO.getSubmitUser());

			// 文件列表，这里可以直接调用dao
			List<MomsPathPO> appMomsPathPOList = momPathDao.selectPathListByMomentsId(appMomentsId);
			// 文件总数
			int appMomentsPathCount = momPathDao.selectPathCountByMomentsId(appMomentsId);

			// 评论总数
			int commentCount = commentsDao.selectAppCommentsCountByMomentsId(appMomentsId);
			// 点赞总数
			int likeNum = momLikeDao.selectLikeCountByMomentsId(appMomentsId);
			// 上面有逻辑判断，不能直接使用dao
			boolean activeStatus = false;

			int activeNum = iMomsLikeService.selectIsLikeForMoments(pager.getLogonUserId(), appMomentsId);
			if (activeNum == 0) {// activeNum返回0，就是点赞；返回1就是取消点赞。
				activeStatus = true;
			}
			// 收藏总数
			int collectionNum = momLikeDao.selectCountOfColl(appMomentsId);
			// 收藏状态
			boolean collectionStatus = false;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("momentId", appMomentsPO.getId());
			map.put("userId", pager.getLogonUserId());
			int currentUserCollNum = momLikeDao.selectCurrentCollNum(map);

			if (currentUserCollNum > 0) {
				collectionStatus = true;
			}

			// 将值set进list集合
			appMomentsPO.setId(appMomentsId);// 用户id
//			appMomentsPO.setUsernameAndImg(buNameAndPhotoByUserId);// 用户信息
			appMomentsPO.setSubmitTime(DateUtil.staffTime(appMomentsPO.getSubmitTime()));// 转换时间格式并set进去
			appMomentsPO.setFilePaths(appMomsPathPOList);// 图片文件列表
			appMomentsPO.setPathNum(appMomentsPathCount);// 文件总数
			appMomentsPO.setCommentsNum(commentCount);// 评论总数
			appMomentsPO.setLikeNum(likeNum);// 点赞总数
			appMomentsPO.setActivedStatus(activeStatus);
			appMomentsPO.setCollectionNum(collectionNum);
			appMomentsPO.setCollectionStatus(collectionStatus);
		}

		return list;
	}
	
	@Override
	public Object getLotteryMomentCount(Pager pager) throws Exception {
		pager.setStartRow((pager.getPageNo() - 1) * pager.getPageSize());
		
		Integer totalRows = momDao.selectLotteryMomentCount(pager);

		pager.setTotalRows(totalRows);
		pager.setTotalPages(totalRows % pager.getPageSize() == 0 ? (totalRows / pager.getPageSize()) : (totalRows / pager.getPageSize()) + 1);

		return pager;
	}
	
	@Override
	public List<UserAppPO> getTopThreeUserInfoList() throws Exception {

		List<UserAppPO> userInfoList = momDao.selectTopThreeUserinfoDESC();
		logger.info(userInfoList);
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
	public List<String> getStaffList() throws Exception {
		return momDao.selectStaffList();
	}
	
	@Override
	public List<UserAppPO> getThunderUserInfoList() throws Exception {
		return momDao.selectThunderUserInfo();
	}
	
	@Override
	public int getCurrentMomentIndex() throws Exception {
		// 当前app端的帖子总数，加1，代表当前帖子的下标
		return momDao.getMomentTotalCount() + 1;
	}
	
	@Override
	public int selectCurrentUserMomentCount(String logonUserId) throws Exception {
		return momDao.selectCurrentUserMomentCount(logonUserId);
	}
	
	@Override
	public Object getMyPrizeByUserId(String logonUserId) throws Exception {
		return momDao.getMyPrizeByUserId(logonUserId);
	}
	
	@Override
	public boolean isLunky(String logonUserId) throws Exception {
		boolean islunky = false;
		if(0 < momDao.selectPrizeCountByUserId(logonUserId)){
			islunky = true;
		}
		return islunky;
	}
	
}
