package com.none.web.po;

/**
 * 查询员工心声列表入参
 * 
 * @author winter
 *
 */
public class ReqVoiceListPO {

	/** 页数 */
	private Integer pageNo;

	/** 每页展示的数量 */
	private Integer pageSize;

	/** 开始页数 */
	private Integer startPage;

	/** 查询list还是查询某条voice */
	private Integer voiceId;

	/** 用户的userName */
	private String userName;

	/** 用户的userType */
	private String userType;

	/** 用户的头像 */
	private String photo;

	/** 状态:0:发布状态,1:隐藏状态 */
	private Integer status;

	/** 是否隐藏:0:显示,1:隐藏 */
	private Integer isHide;

	/** 开始时间 */
	private String startTime;

	/** 结束时间 */
	private String endTime;

	/** 设备id */
	private String udid;

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Integer getIsHide() {
		return isHide;
	}

	public void setIsHide(Integer isHide) {
		this.isHide = isHide;
	}

	public Integer getStartPage() {
		return startPage;
	}

	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
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

	public Integer getVoiceId() {
		return voiceId;
	}

	public void setVoiceId(Integer voiceId) {
		this.voiceId = voiceId;
	}

}
