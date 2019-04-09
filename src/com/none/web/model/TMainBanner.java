package com.none.web.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name = "t_main_banner")
public class TMainBanner implements Serializable{

	@Id
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
	private String linkFlag;// cms详情接口是否有link相关数据,有Y,无N
	private String linkTitle;// cms详情接口跳转详情页面时link的Title
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "path")
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Column(name = "link")
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	@Column(name = "link_type")
	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	@Column(name = "link_id")
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "start_time")
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	
	@Column(name = "end_time")
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	@Column(name = "last_update_time")
	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	@Column(name = "last_update_user")
	public String getLastUpdateUser() {
		return lastUpdateUser;
	}
	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}
	
	@Column(name = "banner_group")
	public Integer getBannerGroup() {
		return bannerGroup;
	}
	public void setBannerGroup(Integer bannerGroup) {
		this.bannerGroup = bannerGroup;
	}
	@Column(name = "banner_order")
	public Integer getBannerOrder() {
		return bannerOrder;
	}
	public void setBannerOrder(Integer bannerOrder) {
		this.bannerOrder = bannerOrder;
	}
	@Transient
	public String getCurrentEndtime() {
		return currentEndtime;
	}
	public void setCurrentEndtime(String currentEndtime) {
		this.currentEndtime = currentEndtime;
	}
	@Transient
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	@Transient
	public String getStartHour() {
		return startHour;
	}
	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}
	@Transient
	public String getStartMinute() {
		return startMinute;
	}
	public void setStartMinute(String startMinute) {
		this.startMinute = startMinute;
	}
	@Transient
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	@Transient
	public String getEndHour() {
		return endHour;
	}
	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}
	@Transient
	public String getEndMinute() {
		return endMinute;
	}
	public void setEndMinute(String endMinute) {
		this.endMinute = endMinute;
	}
	@Transient
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@Transient
	public String getCurrentEndDate() {
		return currentEndDate;
	}
	public void setCurrentEndDate(String currentEndDate) {
		this.currentEndDate = currentEndDate;
	}
	@Transient
	public String getCurrentEndHour() {
		return currentEndHour;
	}
	public void setCurrentEndHour(String currentEndHour) {
		this.currentEndHour = currentEndHour;
	}
	@Transient
	public String getCurrentEndMinute() {
		return currentEndMinute;
	}
	public void setCurrentEndMinute(String currentEndMinute) {
		this.currentEndMinute = currentEndMinute;
	}
	@Transient
	public String getBrowseTimes() {
		return browseTimes;
	}
	public void setBrowseTimes(String browseTimes) {
		this.browseTimes = browseTimes;
	}
	@Transient
	public String getLinkFlag() {
		return linkFlag;
	}
	public void setLinkFlag(String linkFlag) {
		this.linkFlag = linkFlag;
	}
	@Transient
	public String getLinkTitle() {
		return linkTitle;
	}
	public void setLinkTitle(String linkTitle) {
		this.linkTitle = linkTitle;
	}
	@Override
	public String toString() {
		return "MainBanner [id=" + id + ", path=" + path + ", status=" + status + ", bannerGroup=" + bannerGroup
				+ ", bannerOrder=" + bannerOrder + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", lastUpdateTime=" + lastUpdateTime + ", lastUpdateUser=" + lastUpdateUser + "]";
	}
	
	
	
 
	
	 
}
