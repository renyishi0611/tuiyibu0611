package com.none.web.po;

public class MomsForwardPO {
	private Integer id;
	private Integer momentsId;
	private String userId;
	private String forwardTime;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getForwardTime() {
		return forwardTime;
	}
	public void setForwardTime(String forwardTime) {
		this.forwardTime = forwardTime;
	}
	@Override
	public String toString() {
		return "MomsForwardPO [id=" + id + ", momentsId=" + momentsId + ", userId=" + userId + ", forwardTime="
				+ forwardTime + "]";
	}
	
}
