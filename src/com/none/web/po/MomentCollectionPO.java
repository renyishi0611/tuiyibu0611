/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT China Mobile (SuZhou) Software Technology Co.,Ltd. 2018
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.none.web.po;

/**
 * 文章收藏po
 * 
 * @author winter
 */
public class MomentCollectionPO {

	/** 主键id */
	private Integer id;

	/** 收藏人id */
	private String collectionUserId;

	/** 收藏文章id */
	private Integer momentId;

	/** 收藏时间 */
	private String collectionTime;

	/** 收藏状态 */
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCollectionUserId() {
		return collectionUserId;
	}

	public void setCollectionUserId(String collectionUserId) {
		this.collectionUserId = collectionUserId;
	}

	public Integer getMomentId() {
		return momentId;
	}

	public void setMomentId(Integer momentId) {
		this.momentId = momentId;
	}

	public String getCollectionTime() {
		return collectionTime;
	}

	public void setCollectionTime(String collectionTime) {
		this.collectionTime = collectionTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
