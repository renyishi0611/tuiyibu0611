package com.none.web.po;

import java.io.Serializable;

public class BuPO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String bu;
	private String buPhoto;
	private Integer isDelete;// 0：正常，1：删除
	private String createTime;// 创建时间
	private String creator;// 创建人
	private Integer pageNo;
	private Integer pageSize;
	private Integer startPage;

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

	public Integer getStartPage() {
		return startPage;
	}

	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBu() {
		return bu;
	}

	public void setBu(String bu) {
		this.bu = bu;
	}

	public String getBuPhoto() {
		return buPhoto;
	}

	public void setBuPhoto(String buPhoto) {
		this.buPhoto = buPhoto;
	}

}
