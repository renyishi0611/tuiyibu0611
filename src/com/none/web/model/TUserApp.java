package com.none.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_user_app")
public class TUserApp implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields
	private String userId;// 主键值为staffId
	private String userName;// 用户名只做显示
	private String staffId;// staffId
	private String decryptStaffId;// 解密的staffId
	private String userUDID;// 用户绑定设备的udid
	private Integer status; // 状态:0超时，1激活
	private String createTime;// 创建时间
	private String lastVisitTime;// 最近一次登陆时间
	private String lastVisitTimestr;// 最近一次登陆时间
	private String firstLoginTime;// 激活后第一次登陆时间
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
	private String activationTime;// 激活时间
	private String accept;// 接受协议，同意Y，不同意N

	public TUserApp() {
	}

	public TUserApp(String userId) {
		this.userId = userId;
	}

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "user_name")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_visit_time")
	public String getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(String lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	@Transient
	public String getLastVisitTimestr() {
		return lastVisitTimestr;
	}

	public void setLastVisitTimestr(String lastVisitTimestr) {
		this.lastVisitTimestr = lastVisitTimestr;
	}

	@Column(name = "first_login_time")
	public String getFirstLoginTime() {
		return firstLoginTime;
	}

	public void setFirstLoginTime(String firstLoginTime) {
		this.firstLoginTime = firstLoginTime;
	}

	@Column(name = "user_UDID", length = 40)
	public String getUserUDID() {
		return userUDID;
	}

	public void setUserUDID(String userUDID) {
		this.userUDID = userUDID;
	}

	@Column(name = "branch", length = 40)
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	@Column(name = "staffId")
	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	@Transient
	public String getDecryptStaffId() {
		return decryptStaffId;
	}

	public void setDecryptStaffId(String decryptStaffId) {
		this.decryptStaffId = decryptStaffId;
	}

	@Column(name = "english_name")
	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	@Column(name = "chinese_name")
	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	@Column(name = "dept_id")
	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	@Column(name = "position")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "qrcode")
	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	@Column(name = "i_d_card")
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	@Column(name = "telephone")
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name = "gender")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "photo")
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Transient
	public String getIsTimeOut() {
		return isTimeOut;
	}

	public void setIsTimeOut(String isTimeOut) {
		this.isTimeOut = isTimeOut;
	}

	@Column(name = "is_delete")
	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	@Column(name = "activation_time")
	public String getActivationTime() {
		return activationTime;
	}

	public void setActivationTime(String activationTime) {
		this.activationTime = activationTime;
	}

	@Column(name = "accept")
	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

}