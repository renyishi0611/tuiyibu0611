package com.none.web.model;

import java.util.List;

public class LivePoll {

	private int id;
	private String content;
	private String imgPath;
	private int isDelete;
	private String submitUser;
	private String submitTime;
	private String lastupdateUser;
	private String lastupdateTime;
	private String startTime;
	private String endTime;
	private String activeOrHide;
	private StatusEnum status;
	private String voteNow;
	private List<PollAnswer> pollAnswer;

	private String submitType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getSubmitUser() {
		return submitUser;
	}

	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getLastupdateUser() {
		return lastupdateUser;
	}

	public void setLastupdateUser(String lastupdateUser) {
		this.lastupdateUser = lastupdateUser;
	}

	public String getLastupdateTime() {
		return lastupdateTime;
	}

	public void setLastupdateTime(String lastupdateTime) {
		this.lastupdateTime = lastupdateTime;
	}

	public String getSubmitType() {
		return submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getActiveOrHide() {
		return activeOrHide;
	}

	public void setActiveOrHide(String activeOrHide) {
		this.activeOrHide = activeOrHide;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public String getVoteNow() {
		return voteNow;
	}

	public void setVoteNow(String voteNow) {
		this.voteNow = voteNow;
	}

	public List<PollAnswer> getPollAnswer() {
		return pollAnswer;
	}

	public void setPollAnswer(List<PollAnswer> pollAnswer) {
		this.pollAnswer = pollAnswer;
	}
}
