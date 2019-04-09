package com.none.web.po;

/**
 * 员工心声实体类
 * 
 * @author winter
 *
 */
public class VoiceOfStaffPO {

	/** 页数 */
	private Integer pageNo;

	/** 每页展示的数量 */
	private Integer pageSize;

	/** 主键id */
	private Integer id;

	/** 发布内容 */
	private String content;

	/** 发布状态: 0:发布,1:草稿 */
	private Integer status;

	/** 发布状态: 0:显示,1:隐藏 */
	private Integer isHide;

	/** 发布人id */
	private String subUser;

	/** 发布时间 */
	private String submitTime;

	/** 修改人id */
	private String updateUser;

	/** 修改时间 */
	private String updateTime;

	/** 删除标记:0:正常,1:已删除 */
	private Integer isDelete;

	/** 发布人信息,app端列表返回展示用 */
	private ReqVoiceListPO user;

	/** 是否是今天: "Y":"N" 用语app端显示时字体加粗 */
	private String isToday;

	/** 评论数量,app端列表展示用 */
	private Integer commentCount;

	private String lastupdateUser;

	private String lastupdateTime;

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

	public Integer getIsHide() {
		return isHide;
	}

	public void setIsHide(Integer isHide) {
		this.isHide = isHide;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSubUser() {
		return subUser;
	}

	public void setSubUser(String subUser) {
		this.subUser = subUser;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public ReqVoiceListPO getUser() {
		return user;
	}

	public void setUser(ReqVoiceListPO user) {
		this.user = user;
	}

	public String getIsToday() {
		return isToday;
	}

	public void setIsToday(String isToday) {
		this.isToday = isToday;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
