package com.none.web.utils;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

public class UploadImgUtil {

	public static Logger logger = Logger.getLogger(UploadImgUtil.class);

	public static String uploadImg(MultipartFile multipartFile, String folder, HttpServletRequest request)
			throws IOException {
		String rootPath = request.getSession().getServletContext().getRealPath("");
		String substring = rootPath.substring(0, rootPath.length() - 7);
		// C:\tomcat\apache-tomcat-8.0.44\apache-tomcat-8.0.44\webapps\staff\
		// http://localhost:8080/freemarker/upload/201811/12/activity-banner.jpg
		String realPath = substring + File.separatorChar + folder;
		logger.info("realPath ----->" + realPath);
		String myfilename = StringRandomUtil.getStringRandom(4) + multipartFile.getOriginalFilename();
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
		logger.info("beginIndex ---->" + realPath.indexOf(folder) + ",   endIndex ---->" + realPath.length());
		String filePath = domain + File.separatorChar + realPath.substring(realPath.indexOf(folder), realPath.length())
				+ File.separatorChar + myfilename;
		logger.info("filePath ----->" + filePath);
		return filePath;
	}

}
