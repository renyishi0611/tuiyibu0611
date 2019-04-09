package com.none.web.po;

import java.sql.Timestamp;

/**
 * 
 * @ClassName: MomsLikePO
 * @Description: TODO(点赞PO)
 * @author Jan
 * @date 2018年11月22日
 *
 */
public class MomsLikePO {
	private int id;// 主键
	private int momentsId;// moments的id
	private String userId;// userId
	private String likeTime;// 点赞时间
	private int status;// 点赞状态

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMomentsId() {
		return momentsId;
	}

	public void setMomentsId(int momentsId) {
		this.momentsId = momentsId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLikeTime() {
		return likeTime;
	}

	public void setLikeTime(String likeTime) {
		this.likeTime = likeTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "MomsLikePO [id=" + id + ", momentsId=" + momentsId + ", userId=" + userId + ", likeTime=" + likeTime
				+ ", status=" + status + "]";
	}

}
