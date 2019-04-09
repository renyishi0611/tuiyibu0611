package com.none.web.po;

/**
 * cms的moment列表入参
 * 
 * @author winter
 *
 */
public class ReqMomListPO {

	private Integer pageNo;
	private Integer pageSize;
	private String userName;
	private String startTime;
	private String endTime;
	private String status;
	private String userId;
	private String userType;
	private String branch;

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	@Override
	public String toString() {
		return "ReqMomListPO [pageNo=" + pageNo + ", pageSize=" + pageSize + ", userName=" + userName + ", startTime="
				+ startTime + ", endTime=" + endTime + ", status=" + status + ", userId=" + userId + ", userType="
				+ userType + ", branch=" + branch + "]";
	}

}
