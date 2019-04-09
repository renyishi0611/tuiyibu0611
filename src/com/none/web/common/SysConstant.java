package com.none.web.common;

public class SysConstant {

	/**
	 * 成功
	 */
	public static final Integer STATE_SUCCESS = 0;

	/**
	 * 失败
	 */
	public static final Integer STATE_FAILURE = -1;

	/**
	 * 可用
	 */
	public static final Integer STATE_AVAILABLE = 1;// 可用

	/**
	 * 不可用
	 */
	public static final Integer STATE_NOT_AVAILABLE = 0;// 不可用

	/**
	 * 离职
	 */
	public static final Integer STATE_RESIGNED = 2;// resigned 辞职
	/**
	 * 锁定
	 */
	public static final Integer STATE_IS_LOCKED = 3;// resigned 辞职
	/**
	 * 错误
	 */
	public static final Integer STATE_ERROR = 100;

	/**
	 * 请求错误
	 */
	public static final Integer REQUEST_ERROR = 403;

	/**
	 * cunzai
	 */
	public static final Integer STATE_FAILURE1 = 10000;

	public static final String FREEMARKER_IMAGE = "freemarker/image/";

	public static final String FREEMARKER_PDF = "freemarker/pdf/";

	public static final String FREEMARKER_VIDEO = "freemarker/video/";

	/**
	 * moments模块
	 */
	public static final String MOMENTS = "moments";
	/**
	 * 图片
	 */
	public static final String IMAGE_TYPE = "image";
	/**
	 * pdf
	 */
	public static final String PDF_TYPE = "pdf";
	/**
	 * 视频
	 */
	public static final String VIDEO_TYPE = "video";
	/**
	 * 发布
	 */
	public static final String PUBLISH = "Published";
	/**
	 * 保存为草稿
	 */
	public static final String DRAFT = "Draft";
	/**
	 * 提交审核
	 */
	public static final String PENDINGREVIEW = "Pending Review";
	/**
	 * 审核拒绝
	 */
	public static final String REJECT = "Rejected";

	public static String screenshotHigh = "220";
	public static String screenshotWidth = "380";
	public static String screenshotMseconds = "1000";

	public static final String IMAGE_SCREENSHOT_API = "?x-oss-process=image/resize,m_fixed,h_" + screenshotHigh + ",w_"
			+ screenshotWidth;

	public static final String IMAGE_SCREENSHOT_API_EQUALRATIO = "?x-oss-process=image/resize,m_fixed,w_"
			+ screenshotWidth;

	public static final String VIDEO_SCREENSHOT_API = "?x-oss-process=video/snapshot,t_" + screenshotMseconds
			+ ",f_jpg,w_" + screenshotWidth + ",h_" + screenshotHigh + ",m_fast";

	public static final String PDF_SCREENSHOT_API = "";

	/**
	 * news或activity content中是否包含startString 、endString
	 */
	public static String startString = "<video class=\"edui-upload-video";
	public static String endString = "type=\"video/mp4\"/></video>";

	public static final String cmsUserId = "AuthenticatedUserId";
	public static final String appUserId = "app_AuthenticatedUserId";
	public static final String cmsPlatform = "cms";
	public static final String appPlatform = "app";
}
