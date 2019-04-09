package com.none.web.po;

public class MomentDetailPathPO {
	private Integer id;// 主键
	private Integer momentsId;// momentsid
	private String submitTime;// 提交时间
	private String userId;// userid
	private String fileScreenshot;// 文件缩略图
	private String filePath;// 资源文件地址
	private String fileName;// 资源文件名称
	private String fileType;// 资源文件类型
	private String isDelete;// 删除与否
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
	
}
