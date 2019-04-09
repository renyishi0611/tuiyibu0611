package com.none.web.po;

import java.util.List;
import java.util.Map;

/**
 * moment实体类
 * 
 * @author winter
 *
 */
public class MomentPO {
	private Integer id;// 主键
	private String title;// 文章标题
	private String content;// 提交文本内容
	private List<ImageForBase64PO> imageForBase64List;// moment图片的base64字符串,目前需求改成手机可上传多个图片
	private Integer isDelete;// 是否删除(0-未删除,1-已删除)
	private String submitType;// 提交类型(publish,submit,draft,reject)
	private String submitUser;// 用户id
	private String submitTime;// 提交时间
	private List<MomsPathPO> filePaths;// 上传路径列表
	private List<CommentPO> comments;// moment的评论列表
	private String lastUpdateUser;// 最后修改人
	private String lastUpdateTime;// 最后修改时间
	private String approveUser;// 审核人
	private String approveTime;// 审核时间
	////////// 整合添加的字段
	private String category;// 分享的类别
	private String userId;// 用户id
	private int status;// 状态
	private int type;// 是否删除
	private List<MomsPathPO> filePath;// 图片列表这个表里有filePaths，那标记修改自己相关的配置
	private int pathNum;// 图片总数
	private int commentsNum;// 评论总数
	private Map<String, Object> usernameAndImg;// 用户的姓名和头像
	private int likeNum;// 点赞总数
	private boolean activedStatus;
	private int collectionNum;
	private boolean collectionStatus;
	private String contents;// 评论信息
	private String displayPhoto;

	public String getDisplayPhoto() {
		return displayPhoto;
	}

	public void setDisplayPhoto(String displayPhoto) {
		this.displayPhoto = displayPhoto;
	}

	public List<MomsPathPO> getFilePath() {
		return filePath;
	}

	public void setFilePath(List<MomsPathPO> filePath) {
		this.filePath = filePath;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getSubmitType() {
		return submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getSubmitUser() {
		return submitUser;
	}

	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public List<MomsPathPO> getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(List<MomsPathPO> filePaths) {
		this.filePaths = filePaths;
	}

	public List<CommentPO> getComments() {
		return comments;
	}

	public void setComments(List<CommentPO> comments) {
		this.comments = comments;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getApproveUser() {
		return approveUser;
	}

	public void setApproveUser(String approveUser) {
		this.approveUser = approveUser;
	}

	public String getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(String approveTime) {
		this.approveTime = approveTime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Map<String, Object> getUsernameAndImg() {
		return usernameAndImg;
	}

	public void setUsernameAndImg(Map<String, Object> usernameAndImg) {
		this.usernameAndImg = usernameAndImg;
	}

	public int getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ImageForBase64PO> getImageForBase64List() {
		return imageForBase64List;
	}

	public void setImageForBase64List(List<ImageForBase64PO> imageForBase64List) {
		this.imageForBase64List = imageForBase64List;
	}

	public int getCollectionNum() {
		return collectionNum;
	}

	public void setCollectionNum(int collectionNum) {
		this.collectionNum = collectionNum;
	}

	public boolean isActivedStatus() {
		return activedStatus;
	}

	public void setActivedStatus(boolean activedStatus) {
		this.activedStatus = activedStatus;
	}

	public boolean isCollectionStatus() {
		return collectionStatus;
	}

	public void setCollectionStatus(boolean collectionStatus) {
		this.collectionStatus = collectionStatus;
	}
	
}
