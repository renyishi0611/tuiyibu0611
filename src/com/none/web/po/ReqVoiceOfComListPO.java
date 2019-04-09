package com.none.web.po;

/**
 * 评论列表的参数
 * 
 * @author winter
 *
 */
public class ReqVoiceOfComListPO {

	/** 页数 */
	private Integer pageNo;

	/** 每页展示的数量 */
	private Integer pageSize;

	/** 开始页数 */
	private Integer startPage;

	/** voice的id */
	private Integer voiceId;

	/** 设备id */
	private String udid;

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
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

	public Integer getStartPage() {
		return startPage;
	}

	public void setStartPage(Integer startPage) {
		this.startPage = startPage;
	}

	public Integer getVoiceId() {
		return voiceId;
	}

	public void setVoiceId(Integer voiceId) {
		this.voiceId = voiceId;
	}

}
