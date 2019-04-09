package com.none.web.po;

/**
 * 导出moment的excel的po
 * 
 * @author winter
 *
 */
public class MomsExcelPO {

	private Integer id;
	private String momentContent;
	private String userId;
	private String staffId;
	private String userName;
	private String cmsUserName;
	private String commentContent;
	private String commentTime;
	private String isLike;
	private String likeTime;

	public String getCmsUserName() {
		return cmsUserName;
	}

	public void setCmsUserName(String cmsUserName) {
		this.cmsUserName = cmsUserName;
	}

	public String getLikeTime() {
		return likeTime;
	}

	public void setLikeTime(String likeTime) {
		this.likeTime = likeTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMomentContent() {
		return momentContent;
	}

	public void setMomentContent(String momentContent) {
		this.momentContent = momentContent;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getIsLike() {
		return isLike;
	}

	public void setIsLike(String isLike) {
		this.isLike = isLike;
	}

}
