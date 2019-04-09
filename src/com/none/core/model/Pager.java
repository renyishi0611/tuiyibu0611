package com.none.core.model;

public class Pager {

	private int totalRows; // 总数据条数
	private int pageSize = 10; // 当前每页多少条记录
	private int pageNo = 1; // 当前应该是第几页
	// private int startPage=pageNo-1;//
	private int totalPages = 0; // 总页数
	private int startRow; // 当前页在数据库中的起始行
	private String sort; // 排序字段
	private String direction; // 排序方向
	private Integer pagesNum;// 当前页之前数据都取

	private String logonUserId;

	public String getLogonUserId() {
		return logonUserId;
	}

	public void setLogonUserId(String logonUserId) {
		this.logonUserId = logonUserId;
	}

	private String udid;

	// public int getStartPage() {
	// return startPage;
	// }
	//
	// public void setStartPage(int startPage) {
	// this.startPage = startPage;
	// }

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Integer getPagesNum() {
		return pagesNum;
	}

	public void setPagesNum(Integer pagesNum) {
		this.pagesNum = pagesNum;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

}