package com.none.web.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 创建文件夹的util
 * 
 * @author winter
 *
 */
public class BuildFolderUtil {

	private static Logger logger = Logger.getLogger(BuildFolderUtil.class);

	/** 上传文件的保存路径 */
	public static final String FILE_UPLOAD_DIR = "/upload";

	/** 上传文件的保存的下一级路径，标示存储类型 */
	public static final String FILE_UPLOAD_SUB_IMG_DIR = "/img";

	/** 为了能让CKEDITOR加载到上传的图片，此处将位置限制在了freemarker下 */
	public static final String FOR_FREEMARKER_LOAD_DIR = "/freemarker";

	/** 系统默认建立和使用的以时间字符串作为文件名称的时间格式 */
	public static final String DEFAULT_SUB_FOLDER_FORMAT_AUTO = "yyyyMMdd";

	/** 这里扩充一下格式，防止手动建立的不统一 */
	public static final String DEFAULT_SUB_FOLDER_FORMAT_NO_AUTO = "yyyy-MM-dd";

	/** 每个上传子目录保存的文件的最大数目 */
	public static final int MAX_NUM_PER_UPLOAD_SUB_DIR = 500;

	/**
	 * 创建一个新文件
	 * 
	 * @param path
	 * @return
	 */
	public static File buildNewFile(String path) {
		// 不含有子文件夹，新建一个，通常系统首次上传会有这个情况
		File newFile = buildFileBySysTime(path);
		if (null == newFile) {
			logger.error("创建文件夹失败！newFile=" + newFile);
		}

		return newFile;
	}

	/**
	 * 根据当前的时间建立文件夹，时间格式yyyyMMdd
	 * 
	 * @param path
	 * @return
	 */
	public static File buildFileBySysTime(String path) {
		DateFormat df = new SimpleDateFormat(DEFAULT_SUB_FOLDER_FORMAT_AUTO);
		String fileName = df.format(new Date());
		File file = new File(path + File.separator + fileName);
		if (!file.mkdir()) {
			return null;
		}
		return file;
	}

	/**
	 * 创建目录
	 * 
	 * @return
	 */
	public static File buildFolder(HttpServletRequest request) {
		// 在控件中无法正常操作
		String realPath = request.getSession().getServletContext().getRealPath("/");
		realPath = realPath.substring(0, (realPath.length() - 7)) + FOR_FREEMARKER_LOAD_DIR;

		logger.info(realPath);
		File firstFolder = new File(realPath + FILE_UPLOAD_DIR);
		// freemarker目录，如果不存在，创建
		File freemarker = new File(realPath);
		if (!freemarker.exists()) {
			if (!freemarker.mkdir()) {
				return null;
			}
		}
		// 一级目录，如果不存在，创建
		if (!firstFolder.exists()) {
			if (!firstFolder.mkdir()) {
				return null;
			}
		}

		// 二级目录，如果不存在，创建
		String folderdir = realPath + FILE_UPLOAD_DIR + FILE_UPLOAD_SUB_IMG_DIR;
		if (logger.isDebugEnabled()) {
			logger.debug("folderdir" + folderdir);
		}

		if (StringUtils.isBlank(folderdir)) {
			logger.error("路径错误:" + folderdir);
			return null;
		}

		File floder = new File(folderdir);
		if (!floder.exists()) {
			if (!floder.mkdir()) {
				logger.error("创建文件夹出错！path=" + folderdir);
				return null;
			}

		}
		// 再往下的文件夹都是以时间字符串来命名的，所以获取最新时间的文件夹即可
		String[] files = floder.list();
		if (null != files && 0 < files.length) {
			// 含有子文件夹，则获取最新的一个
			Date oldDate = null;
			int index = -1;
			for (int i = 0; i < files.length; i++) {
				String fileName = files[i];

				try {
					Date thisDate = DateUtils.parseDate(fileName,
							new String[] { DEFAULT_SUB_FOLDER_FORMAT_AUTO, DEFAULT_SUB_FOLDER_FORMAT_NO_AUTO });
					if (oldDate == null) {
						oldDate = thisDate;
						index = i;
					} else {
						if (thisDate.after(oldDate)) {
							// 保存最新的时间和数组中的下标
							oldDate = thisDate;
							index = i;
						}
					}
				} catch (ParseException e) {
					// 这里异常吃掉，不用做什么，如果解析失败，会建立新的文件夹，防止人为的建立文件夹导致的异常。
				}
			} // for

			// 判断当前最新的文件夹下是否已经存在了最大数目的图片
			if (null != oldDate && -1 != index) {
				File pointfloder = new File(folderdir + File.separator + files[index]);
				if (!pointfloder.exists()) {
					if (!pointfloder.mkdir()) {
						logger.error("创建文件夹出错！path=" + folderdir);
						return null;
					}
				}

				// 如果文件夹下的文件超过了最大值，那么也需要新建一个文件夹
				String[] pointfloderFiles = pointfloder.list();
				if (null != pointfloderFiles && MAX_NUM_PER_UPLOAD_SUB_DIR < pointfloderFiles.length) {
					return buildNewFile(folderdir);
				}

				return pointfloder;
			}

			// 查找当前子文件夹失败，新建一个
			return buildNewFile(folderdir);
		} else {
			// 不含有子文件夹，新建一个，通常系统首次上传会有这个情况
			return buildNewFile(folderdir);
		}

	}

}
