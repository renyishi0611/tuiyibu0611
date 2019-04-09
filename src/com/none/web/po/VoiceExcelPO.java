package com.none.web.po;

/**
 * cms导出员工心声时的po
 * 
 * @author winter
 *
 */
public class VoiceExcelPO {

	/** voice创建时间 */
	private String submitTime;

	/** voice内容 */
	private String topic;

	/** 评论内容 */
	private String commentText;

	/** 评论时间 */
	private String commentTime;

	/** 评论人staffID */
	private String staffId;

	/** app端评论人用户名 */
	private String appUserName;

	/** cms端评论人用户名 */
	private String cmsUserName;

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getAppUserName() {
		return appUserName;
	}

	public void setAppUserName(String appUserName) {
		this.appUserName = appUserName;
	}

	public String getCmsUserName() {
		return cmsUserName;
	}

	public void setCmsUserName(String cmsUserName) {
		this.cmsUserName = cmsUserName;
	}

}
