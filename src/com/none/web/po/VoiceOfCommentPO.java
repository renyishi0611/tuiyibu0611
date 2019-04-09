package com.none.web.po;

/**
 * 员工心声评论的po
 * 
 * @author winter
 *
 */
public class VoiceOfCommentPO {

	/** 主键id */
	private Integer id;

	/** 心声id */
	private Integer voiceId;

	/** 提交评论的时间 */
	private String submitTime;

	/** 提交评论的用户 */
	private String userId;

	/** 评论内容 */
	private String contents;

	/** 评论的状态 0:正常,1删除 */
	private Integer status;

	/** 评论平台 */
	private String platform;

	/** 用户姓名 */
	private String userName;

	/** 用户头像 */
	private String photo;

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVoiceId() {
		return voiceId;
	}

	public void setVoiceId(Integer voiceId) {
		this.voiceId = voiceId;
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

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

}
