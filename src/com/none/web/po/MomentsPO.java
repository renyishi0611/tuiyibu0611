package com.none.web.po;

import java.util.List;
import java.util.Map;

/**
 * 分享主题实体类
 * 
 * @author winter
 *
 */
public class MomentsPO {
	private int id;// 主键
	private String submitTime;// 提交时间
	private String content;// 提交文本内容
	private String category;// 分享的类别
	private String userId;// 用户id
	private int status;// 分享状态
	private int type;// 是否删除
	private List<MomsPathPO> filePath;
	private List<CommentPO> comments;
	private int pathNum;
	private int commentsNum;
	private Map<String, Object> usernameAndImg;// 用户的姓名和头像
	private int likeNum;//点赞总数
	private String activeStatus;//判断用户点赞状态
	
	
	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public int getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}

	public Map<String, Object> getUsernameAndImg() {
		return usernameAndImg;
	}

	public void setUsernameAndImg(Map<String, Object> usernameAndImg) {
		this.usernameAndImg = usernameAndImg;
	}

	public List<MomsPathPO> getFilePath() {
		return filePath;
	}

	public void setFilePath(List<MomsPathPO> filePath) {
		this.filePath = filePath;
	}

	public List<CommentPO> getComments() {
		return comments;
	}

	public void setComments(List<CommentPO> comments) {
		this.comments = comments;
	}

	public int getPathNum() {
		return pathNum;
	}

	public void setPathNum(int pathNum) {
		this.pathNum = pathNum;
	}

	public int getCommentsNum() {
		return commentsNum;
	}

	public void setCommentsNum(int commentsNum) {
		this.commentsNum = commentsNum;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "MomentsPO [id=" + id + ", submitTime=" + submitTime + ", content=" + content + ", category=" + category
				+ ", userId=" + userId + ", status=" + status + ", type=" + type + ", filePath=" + filePath
				+ ", comments=" + comments + ", pathNum=" + pathNum + ", commentsNum=" + commentsNum + "]";
	}

}
