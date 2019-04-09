package com.none.web.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ValidateFieldUtils {

	public static Logger logger = Logger.getLogger(ValidateFieldUtils.class);

	private static String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
	
	public static boolean validateInputField(String field){
		
		logger.info("validate input field...");
		return StringUtils.isNotBlank(StringUtils.trim(field)) ? field.matches(regex) : false;
	}
	
	public static String filterEmoji(String source) {
		if (source != null) {
			Pattern emoji = Pattern.compile(
					"[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|/[\uD800-\uDBFF][\uDC00-\uDFFF]/g",
					Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
			Matcher emojiMatcher = emoji.matcher(source);
			if (emojiMatcher.find()) {
				logger.info("找到表情:");
				source = emojiMatcher.replaceAll("*");
				return source;
			}
			return source;
		}
		return source;
	}
}
