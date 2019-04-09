package com.none.web.po;

import java.util.Map;

/**
 * 资源文件(图片、pdf、视频等)存放地址po
 * 
 * @author Jan
 *
 */
public class MomsPathPO {
	private Integer id;// 主键
	private Integer momentsId;// momentsid
	private String submitTime;// 提交时间
	private String userId;// userid
	private String fileScreenshot;// 文件缩略图
	private String filePath;// 资源文件地址
	private String fileName;// 资源文件名称
	private String fileType;// 资源文件类型
	private String isDelete;// 删除与否
	////
	private String username;// 用户名，用来展示
	private String isVIP;// 用户是否为vip，用来展示
	private String headPicture;// 用户头像，用来展示
	private int count;// 含有文件的moments的个数，用来展示
	private Map<String, Object> userInfo;// 用户信息，用于展示
	private String displayName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIsVIP() {
		return isVIP;
	}

	public void setIsVIP(String isVIP) {
		this.isVIP = isVIP;
	}

	public String getHeadPicture() {
		return headPicture;
	}

	public void setHeadPicture(String headPicture) {
		this.headPicture = headPicture;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Map<String, Object> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Map<String, Object> userInfo) {
		this.userInfo = userInfo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMomentsId() {
		return momentsId;
	}

	public void setMomentsId(Integer momentsId) {
		this.momentsId = momentsId;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileScreenshot() {
		return fileScreenshot;
	}

	public void setFileScreenshot(String fileScreenshot) {
		this.fileScreenshot = fileScreenshot;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return "MomsPathPO [id=" + id + ", momentsId=" + momentsId + ", submitTime=" + submitTime + ", userId=" + userId
				+ ", fileScreenshot=" + fileScreenshot + ", filePath=" + filePath + ", fileName=" + fileName
				+ ", fileType=" + fileType + ", isDelete=" + isDelete + "]";
	}

}
