package com.none.web.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_upload_files")
public class TUploadFiles implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6281810931374180165L;
	
	
	@Id
	private Integer id;
	private String contentId;//活动或新闻等的ID
	private String type;//类型：activity、news等
	private String img_video_path;//图片或者视频文件路径
	private String userId;//上传者
	private Timestamp upload_time;
	private String fileNo;//文件编号
	private String memo;//描述
	private String batch_flag;//
	
	private String userName;
	private String title;
	private String videoImgFlag;
	private String size;
	private String category;
	private int status;

@Transient
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	@Transient
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@Column(name = "status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "contentId")
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	@Column(name = "type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "img_video_path")
	public String getImg_video_path() {
		return img_video_path;
	}
	public void setImg_video_path(String img_video_path) {
		this.img_video_path = img_video_path;
	}
	@Column(name = "userId")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name = "upload_time")
	public Timestamp getUpload_time() {
		return upload_time;
	}
	public void setUpload_time(Timestamp upload_time) {
		this.upload_time = upload_time;
	}
	@Column(name = "fileNo")
	public String getFileNo() {
		return fileNo;
	}
	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}
	@Column(name = "memo")
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	@Column(name = "batch_flag")
	public String getBatch_flag() {
		return batch_flag;
	}
	public void setBatch_flag(String batch_flag) {
		this.batch_flag = batch_flag;
	}
	
	@Transient
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Transient
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Transient
	public String getVideoImgFlag() {
		return videoImgFlag;
	}
	public void setVideoImgFlag(String videoImgFlag) {
		this.videoImgFlag = videoImgFlag;
	}
	
}
