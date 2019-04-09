package com.none.web.po;

import java.util.List;

public class MomentDetailPO {
	private Integer id;// 主键
	// private String category;// 专题的类别
	private String title;// 文章标题
	private String content;// 提交文本内容
	private String submitType;// 提交类型(publish,submit,draft,reject)
	private String submitUser;// 用户姓名
	private String submitTime;// 提交时间
	private List<MomentDetailPathPO> filePaths;// 上传路径列表
	private String imageForBase64;// moment图片的base64字符串
	private int commentsNum;// 评论总数
	private int likesNum;// 点赞数
	private int likesStatus;//0-点赞,1-取消点赞
	private int collectionNum;//收藏数
	private int collectionStatus;// 0-收藏,1-取消收藏
	private String headPortrait;//头像
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
//	public String getCategory() {
//		return category;
//	}
//	public void setCategory(String category) {
//		this.category = category;
//	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	
	public List<MomentDetailPathPO> getFilePaths() {
		return filePaths;
	}
	public void setFilePaths(List<MomentDetailPathPO> filePaths) {
		this.filePaths = filePaths;
	}
	public String getImageForBase64() {
		return imageForBase64;
	}
	public void setImageForBase64(String imageForBase64) {
		this.imageForBase64 = imageForBase64;
	}
	public int getCommentsNum() {
		return commentsNum;
	}
	public void setCommentsNum(int commentsNum) {
		this.commentsNum = commentsNum;
	}
	public int getLikesNum() {
		return likesNum;
	}
	public void setLikesNum(int likesNum) {
		this.likesNum = likesNum;
	}
	public String getHeadPortrait() {
		return headPortrait;
	}
	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}
	public int getCollectionNum() {
		return collectionNum;
	}
	public void setCollectionNum(int collectionNum) {
		this.collectionNum = collectionNum;
	}
	
	public int getLikesStatus() {
		return likesStatus;
	}
	public void setLikesStatus(int likesStatus) {
		this.likesStatus = likesStatus;
	}
	public int getCollectionStatus() {
		return collectionStatus;
	}
	public void setCollectionStatus(int collectionStatus) {
		this.collectionStatus = collectionStatus;
	}
	@Override
	public String toString() {
		return "MomentDetailPO [id=" + id + ", title=" + title + ", content=" + content + ", submitType=" + submitType
				+ ", submitUser=" + submitUser + ", submitTime=" + submitTime + ", filePaths=" + filePaths
				+ ", imageForBase64=" + imageForBase64 + ", commentsNum=" + commentsNum + ", likesNum=" + likesNum
				+ ", likesStatus=" + likesStatus + ", collectionNum=" + collectionNum + ", collectionStatus="
				+ collectionStatus + ", headPortrait=" + headPortrait + "]";
	}
	
}
