package com.none.web.po;

/**
 * cms 端moments列表展示
 * 
 * @author winter
 *
 */
public class CMSMomentListPO {

	private String userId;
	private String userName;
	private String momentId;
	private String title;
	private String momentContent;
	private String submitTime;
	private Integer totalItem;
	private String status;
	private String buName;
	private String buPhoto;

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public String getBuPhoto() {
		return buPhoto;
	}

	public void setBuPhoto(String buPhoto) {
		this.buPhoto = buPhoto;
	}

	public String getMomentContent() {
		return momentContent;
	}

	public void setMomentContent(String momentContent) {
		this.momentContent = momentContent;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTotalItem() {
		return totalItem;
	}

	public void setTotalItem(Integer totalItem) {
		this.totalItem = totalItem;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMomentId() {
		return momentId;
	}

	public void setMomentId(String momentId) {
		this.momentId = momentId;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
