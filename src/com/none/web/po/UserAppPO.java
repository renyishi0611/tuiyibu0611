package com.none.web.po;

public class UserAppPO {

	private String userId;// 主键值为staffId
	private String userName;// 用户名只做显示
	private String staffId;// staffId
	private String password;// 密码
	private String nickName;// 昵称
	private String userUDID;// 用户绑定设备的udid
	private Integer status; // 状态:0超时，1激活，2未激活
	private String createTime;// 创建时间
	private String lastVisitTime;// 最近一次登陆时间
	private String firstLoginTime;// 激活后第一次登陆时间
	private String activationTime;// 激活时间
	private String branch;// 部门,displayName
	private Integer deptId;// 部门entityId
	private String position;
	private String englishName;// 英文名字
	private String chineseName;// 中文名字
	private String qrcode;// 用户二维码
	private String photo;// 图像
	private String telephone;
	private String email;// email
	private String gender;
	private String idCard;
	private String isTimeOut;// 是否超时：1超时（控制页面跳转，是否需要跳转重新激活页面），0直接登录
	private String isDelete; // 是否离职,离职做逻辑删除 0:正常;1:删除
	private String accept; // 同意条款
	private String imageForBase64;// 头像的base64信息
	private String udid;// udid
	private Integer ranking;

	
	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public String getImageForBase64() {
		return imageForBase64;
	}

	public void setImageForBase64(String imageForBase64) {
		this.imageForBase64 = imageForBase64;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getUserUDID() {
		return userUDID;
	}

	public void setUserUDID(String userUDID) {
		this.userUDID = userUDID;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(String lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	public String getFirstLoginTime() {
		return firstLoginTime;
	}

	public void setFirstLoginTime(String firstLoginTime) {
		this.firstLoginTime = firstLoginTime;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getIsTimeOut() {
		return isTimeOut;
	}

	public void setIsTimeOut(String isTimeOut) {
		this.isTimeOut = isTimeOut;
	}

	public String getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(String activationTime) {
		this.activationTime = activationTime;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	@Override
	public String toString() {
		return "UserAppPO [userId=" + userId + ", userName=" + userName + ", staffId=" + staffId + ", userUDID="
				+ userUDID + ", status=" + status + ", createTime=" + createTime + ", lastVisitTime=" + lastVisitTime
				+ ", firstLoginTime=" + firstLoginTime + ", activationTime=" + activationTime + ", branch=" + branch
				+ ", deptId=" + deptId + ", position=" + position + ", englishName=" + englishName + ", chineseName="
				+ chineseName + ", qrcode=" + qrcode + ", photo=" + photo + ", telephone=" + telephone + ", email="
				+ email + ", gender=" + gender + ", idCard=" + idCard + ", isTimeOut=" + isTimeOut + ", isDelete="
				+ isDelete + "]";
	}

}