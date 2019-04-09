package com.none.web.utils;

import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.none.core.exception.ValidateException;

public class ValidateFileUtil {

	private static Logger logger = Logger.getLogger(ValidateFileUtil.class);

	public static boolean validateImgFile(MultipartFile multipartFile) throws Exception {

		if (multipartFile == null) {
			throw new ValidateException("file.parameter.error");
		}
		HashSet<String> set = new HashSet<String>() {
			{
				add(".jpg");
				add(".png");
				add(".gif");
				add(".jpeg");
			}
		};
		String fileName = multipartFile.getOriginalFilename();
		String suffix = "";
		if (StringUtils.isNotBlank(fileName)) {
			suffix = fileName.substring(fileName.lastIndexOf("."));
		}
		if (!set.contains(suffix)) {
			logger.error("Illegal file with" + suffix);
			throw new ValidateException("Illegal file with type with" + suffix);
		}

		return true;
	}

}
