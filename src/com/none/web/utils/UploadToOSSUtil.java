package com.none.web.utils;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.openservices.oss.OSSClient;
import com.none.core.common.utils.DateUtil;
import com.none.core.exception.ValidateException;
import com.none.web.common.SysConstant;
import com.qikemi.packages.alibaba.aliyun.oss.ObjectService;
import com.qikemi.packages.alibaba.aliyun.oss.properties.OSSClientProperties;

public class UploadToOSSUtil {
	private static Logger logger = Logger.getLogger(UploadToOSSUtil.class);
	/**
	 * 上传至OSS服务器的
	 * 
	 * @param multipartFile
	 *            上传文件的流
	 * @param fileType
	 *            文件类型<"image","pdf","video">
	 * @param module
	 *            模块名称
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String uploadAliCloudOss(MultipartFile multipartFile, String fileType, String module)
			throws Exception {
		String endPoint = OSSClientProperties.endPoint;
		OSSClient client = new OSSClient(endPoint, OSSClientProperties.key, OSSClientProperties.secret);
		String bucketName = OSSClientProperties.bucketName;
		InputStream inputStream = multipartFile.getInputStream();
		String savePath;
		if (SysConstant.IMAGE_TYPE.equals(fileType)) {
			savePath = SysConstant.FREEMARKER_IMAGE + module + "/" + DateUtil.getDataTimeStr(new Date()) + "_"
					+ StringRandomUtil.getStringRandom(4) + "_" + multipartFile.getOriginalFilename();
		} else if (SysConstant.PDF_TYPE.equals(fileType)) {
			savePath = SysConstant.FREEMARKER_PDF + module + "/" + DateUtil.getDataTimeStr(new Date()) + "_"
					+ StringRandomUtil.getStringRandom(4) + "_" + multipartFile.getOriginalFilename();
		} else if (SysConstant.VIDEO_TYPE.equals(fileType)) {
			savePath = SysConstant.FREEMARKER_VIDEO + module + "/" + DateUtil.getDataTimeStr(new Date()) + "_"
					+ StringRandomUtil.getStringRandom(4) + "_" + multipartFile.getOriginalFilename();
		} else {
			throw new ValidateException("fileTypeNoFind");
		}
		ObjectService.putObject(client, bucketName, savePath, inputStream);
		endPoint = endPoint.substring(0, endPoint.indexOf("//") + 2) + OSSClientProperties.bucketName + "."
				+ endPoint.substring(endPoint.indexOf("//") + 2, endPoint.length());
		return endPoint + "/" + savePath;
	}

	public static String getFileType(String originalFilename) {
		String fileType = "";
		if (StringUtils.isNotBlank(originalFilename)) {
			String fileSuffix = StringUtils.substring(originalFilename, StringUtils.lastIndexOf(originalFilename, "."));
			return getSuffix(fileSuffix);
		}
		return fileType;
	}

	public static String getSuffix(String fileSuffix) {
		// String video[] = {".mp4",".flv",".avi",".rmvb",".mtv",".wmv"};
		String video[] = { ".mp4" };
		String pic[] = { ".png", ".jpg", ".jpeg", ".gif" };
		if (Arrays.asList(video).contains(fileSuffix)) {
			return SysConstant.VIDEO_TYPE;
		} else if (Arrays.asList(pic).contains(fileSuffix)) {
			return SysConstant.IMAGE_TYPE;
		} else if (StringUtils.equals(".pdf", fileSuffix)) {
			return SysConstant.PDF_TYPE;
		} else {
			return "";
		}
	}
	
	public static String uploadFileToServer(MultipartFile multipartFile, String fileType, String module, HttpServletRequest request) throws Exception{
		String rootPath = request.getSession().getServletContext().getRealPath("");
		String substring = rootPath.substring(0, rootPath.length() - 7);
		// C:\tomcat\apache-tomcat-8.0.44\apache-tomcat-8.0.44\webapps\staff\
		// http://localhost:8080/freemarker/upload/201811/12/activity-banner.jpg
		String savePath;
		if (SysConstant.IMAGE_TYPE.equals(fileType)) {
			savePath = SysConstant.FREEMARKER_IMAGE + module;
		} else if (SysConstant.PDF_TYPE.equals(fileType)) {
			savePath = SysConstant.FREEMARKER_PDF + module;
		} else if (SysConstant.VIDEO_TYPE.equals(fileType)) {
			savePath = SysConstant.FREEMARKER_VIDEO + module;
		} else {
			throw new ValidateException("fileTypeNoFind");
		}
		
		String realPath = substring + File.separatorChar + savePath;
		logger.info("realPath ----->" + realPath);
		String myfilename = DateUtil.getDataTimeStr(new Date()) + "_" + StringRandomUtil.getStringRandom(4) + "_" + multipartFile.getOriginalFilename();
		String path = realPath + File.separatorChar + myfilename;
		File saveDir = new File(path);
		if (!saveDir.getParentFile().exists()) {
			saveDir.getParentFile().mkdirs();
		}
		long startTime = System.currentTimeMillis();
		multipartFile.transferTo(saveDir);
		long endTime = System.currentTimeMillis();
		logger.info("上传运行时间：" + (endTime - startTime) + "ms");

		String url = request.getRequestURL().toString();
		String uri = request.getRequestURI().toString();
		String domain = url.replace(uri, "");
		logger.info("domain ----->" + domain);
		logger.info("beginIndex ---->" + realPath.indexOf(savePath) + ",   endIndex ---->" + realPath.length());
		String filePath = domain + File.separatorChar
				+ realPath.substring(realPath.indexOf(savePath), realPath.length()) + File.separatorChar + myfilename;
		logger.info("filePath ----->" + filePath);
		return filePath;
	}

}
