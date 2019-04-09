package com.none.web.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * TUser entity. @author MyEclipse Persistence Tools use in the PC
 * 
 */
@Entity
@Table(name = "t_user")
public class TUser implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private String userId;
	private String userName;
	private String password;
	private String face; // 脸型
	private Integer state; // 状态
	private String role; // 权限
	private String position; // 位置
	private String email;// email
	// private TCodes tcodes;// 激活码
	private Timestamp createTime;// 创建时间
	private Timestamp lastTime;// 最后修改时间
	private Timestamp loginTime;// 最后登陆时间
	private String userUDID;// app用户的udid
	private String userType;// 用户权限类型
	private String branch;// 部门
	private String displayName;// 展示名
	private String groupId;
	private String headPortrait;// 用户头像
	private String isVIP;// 是否是vip
	private String accessRight;// 可显示菜单

	@Column(name = "groupId", length = 11)
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Column(name = "accessRight", length = 30)
	public String getAccessRight() {
		return accessRight;
	}

	public void setAccessRight(String accessRight) {
		this.accessRight = accessRight;
	}

	@Column(name = "isVIP", length = 10)
	public String getIsVIP() {
		return isVIP;
	}

	public void setIsVIP(String isVIP) {
		this.isVIP = isVIP;
	}

	@Column(name = "headPortrait", length = 255)
	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	/** default constructor */
	public TUser() {
	}

	/** minimal constructor */
	public TUser(String userId) {
		this.userId = userId;
	}

	/** full constructor */
	public TUser(String userId, String userName, String password, String email, String face, Integer state,
			String position, Timestamp createTime, Timestamp lastTime, Timestamp loginTime, String userUDID,
			String role, String userType, String branch, String headPortrait) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.face = face;
		this.state = state;
		this.position = position;
		this.email = email;
		// this.tcodes = tcodes;
		this.createTime = createTime;
		this.lastTime = lastTime;
		this.loginTime = loginTime;
		this.userUDID = userUDID;
		this.role = role;
		this.userType = userType;
		this.branch = branch;
		this.headPortrait = headPortrait;
	}

	// Property accessors
	@Id
	@Column(name = "user_id", unique = true, nullable = false, length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "user_name")
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "password", length = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "face", length = 50)
	public String getFace() {
		return this.face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	@Column(name = "state")
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "position", length = 10)
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	// @OneToOne
	// @JoinColumn(name = "code_id")
	// public TCodes getTcodes() {
	// return tcodes;
	// }

	// public void setTcodes(TCodes tcodes) {
	// this.tcodes = tcodes;
	// }

	@Column(name = "create_time", nullable = false, length = 19)
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_time", nullable = false, length = 19)
	public Timestamp getLastTime() {
		return lastTime;
	}

	public void setLastTime(Timestamp lastTime) {
		this.lastTime = lastTime;
	}

	@Column(name = "login_time", nullable = false, length = 19)
	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	@Column(name = "user_UDID", length = 40)
	public String getUserUDID() {
		return userUDID;
	}

	public void setUserUDID(String userUDID) {
		this.userUDID = userUDID;
	}

	@Column(name = "role", length = 200)
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column(name = "user_type", length = 40)
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Column(name = "branch", length = 40)
	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	@Transient
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	// @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	// @JoinTable(name = "t_user_code", catalog = "staff", joinColumns = {
	// @JoinColumn(name = "user_id", updatable = false) }, inverseJoinColumns =
	// { @JoinColumn(name = "code_id", updatable = false) })
	// public Set<TCodes> getTCodeses() {
	// return this.TCodeses;
	// }
	//
	// public void setTCodeses(Set<TCodes> TCodeses) {
	// this.TCodeses = TCodeses;
	// }

}