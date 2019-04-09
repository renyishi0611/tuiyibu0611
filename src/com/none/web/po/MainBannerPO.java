package com.none.web.po;

import java.sql.Timestamp;

public class MainBannerPO {
	
	private Integer id;
	private String path;
	private String link;
	private String linkType;// 要跳转页面表识：ml为moments的列表页面， md为moments的详情页面
	private String linkId;// 跳转页面为详情页面时，对应具体数据的id
	private Integer status;// 0:exist  1:dalete
	private Integer bannerGroup;// 分5组:1~5
	private Integer bannerOrder;// 1:current  2:backup
	private Timestamp startTime;// backup banner图的开始时间必须大于等于current banner图的结束时间
	private Timestamp endTime;
	private Timestamp lastUpdateTime;
	private String lastUpdateUser;
	private String flag;// 是否是默认图片：0不是，1是
	private String currentEndtime;// backup的banner对应current banner的endTime加一分钟
	private String startDate;
	private String startHour;
	private String startMinute;
	private String endDate;
	private String endHour;
	private String endMinute;
	private String currentEndDate;
	private String currentEndHour;
	private String currentEndMinute;
	private String browseTimes;
	private String linkFlag;// 详情接口是否有link相关数据
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getLinkType() {
		return linkType;
	}
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getBannerGroup() {
		return bannerGroup;
	}
	public void setBannerGroup(Integer bannerGroup) {
		this.bannerGroup = bannerGroup;
	}
	public Integer getBannerOrder() {
		return bannerOrder;
	}
	public void setBannerOrder(Integer bannerOrder) {
		this.bannerOrder = bannerOrder;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getLastUpdateUser() {
		return lastUpdateUser;
	}
	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCurrentEndtime() {
		return currentEndtime;
	}
	public void setCurrentEndtime(String currentEndtime) {
		this.currentEndtime = currentEndtime;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStartHour() {
		return startHour;
	}
	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}
	public String getStartMinute() {
		return startMinute;
	}
	public void setStartMinute(String startMinute) {
		this.startMinute = startMinute;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getEndHour() {
		return endHour;
	}
	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}
	public String getEndMinute() {
		return endMinute;
	}
	public void setEndMinute(String endMinute) {
		this.endMinute = endMinute;
	}
	public String getCurrentEndDate() {
		return currentEndDate;
	}
	public void setCurrentEndDate(String currentEndDate) {
		this.currentEndDate = currentEndDate;
	}
	public String getCurrentEndHour() {
		return currentEndHour;
	}
	public void setCurrentEndHour(String currentEndHour) {
		this.currentEndHour = currentEndHour;
	}
	public String getCurrentEndMinute() {
		return currentEndMinute;
	}
	public void setCurrentEndMinute(String currentEndMinute) {
		this.currentEndMinute = currentEndMinute;
	}
	public String getBrowseTimes() {
		return browseTimes;
	}
	public void setBrowseTimes(String browseTimes) {
		this.browseTimes = browseTimes;
	}
	public String getLinkFlag() {
		return linkFlag;
	}
	public void setLinkFlag(String linkFlag) {
		this.linkFlag = linkFlag;
	}



}
