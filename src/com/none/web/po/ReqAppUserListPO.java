package com.none.web.po;

/**
 * cms 处appuser列表的展示
 * 
 * @author winter
 *
 */
public class ReqAppUserListPO {

	private Integer pageNo;
	private Integer pageSize;
	private Integer status;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
