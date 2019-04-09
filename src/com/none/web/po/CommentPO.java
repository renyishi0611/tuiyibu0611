package com.none.web.po;

import java.util.Map;

/**
 * 评论实体类
 * 
 * @author winter
 *
 */
public class CommentPO {

	/** 主键id */
	private int id;
	/** momentId */
	private int momentsId;
	/** voiceId */
	private int voiceId;
	/** 用户id */
	private String userId;
	/** 设备id */
	private String udid;
	/** 评论内容 */
	private String contents;
	/** 评论状态 */
	private int status;
	/** 提交时间 */
	private String submitTime;
	/** 提交平台 */
	private String platform;
	/** 评论者头像*/
	private String userPhoto;
	/** 评论者展示名字*/
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public int getVoiceId() {
		return voiceId;
	}

	public void setVoiceId(int voiceId) {
		this.voiceId = voiceId;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	// 评论用户的姓名和头像和是否是VIP
	private Map<String, Object> CommentsUsernameAndImgAndIsVIP;

	public Map<String, Object> getCommentsUsernameAndImgAndIsVIP() {
		return CommentsUsernameAndImgAndIsVIP;
	}

	public void setCommentsUsernameAndImgAndIsVIP(Map<String, Object> commentsUsernameAndImgAndIsVIP) {
		CommentsUsernameAndImgAndIsVIP = commentsUsernameAndImgAndIsVIP;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMomentsId() {
		return momentsId;
	}

	public void setMomentsId(int momentsId) {
		this.momentsId = momentsId;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	@Override
	public String toString() {
		return "CommentPO [id=" + id + ", momentsId=" + momentsId + ", voiceId=" + voiceId + ", userId=" + userId
				+ ", udid=" + udid + ", contents=" + contents + ", status=" + status + ", submitTime=" + submitTime
				+ ", platform=" + platform + ", userPhoto=" + userPhoto + ", userName=" + userName
				+ ", CommentsUsernameAndImgAndIsVIP=" + CommentsUsernameAndImgAndIsVIP + "]";
	}

}
