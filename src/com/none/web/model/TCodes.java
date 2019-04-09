package com.none.web.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TCodes entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_codes")
public class TCodes implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2465367135579314086L;
	private String codeId;
	private String code;
	private Integer state;
	private Timestamp enableTime;
	private Timestamp createTime;
	// private Set<TUser> TUsers = new HashSet<TUser>(0);
//	private TUser tuser;

	// Constructors

	/** default constructor */
	public TCodes() {
	}

	/** minimal constructor */
	public TCodes(String codeId, Timestamp enableTime, Timestamp createTime) {
		this.codeId = codeId;
		this.enableTime = enableTime;
		this.createTime = createTime;
	}

	/** full constructor */
	public TCodes(String codeId, String code, Integer state, Timestamp enableTime, Timestamp createTime) {
		this.codeId = codeId;
		this.code = code;
		this.state = state;
		this.enableTime = enableTime;
		this.createTime = createTime;
 	}

	// Property accessors
	@Id
	@Column(name = "code_id", unique = true, nullable = false, length = 32)
	public String getCodeId() {
		return this.codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	@Column(name = "code", length = 8)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "state")
	public Integer getState() {
		return this.state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "enable_time", nullable = false, length = 19)
	public Timestamp getEnableTime() {
		return this.enableTime;
	}

	public void setEnableTime(Timestamp enableTime) {
		this.enableTime = enableTime;
	}

	@Column(name = "create_time", nullable = false, length = 19)
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	

	// @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "TCodeses")
	// public Set<TUser> getTUsers() {
	// return this.TUsers;
	// }
	//
	// public void setTUsers(Set<TUser> TUsers) {
	// this.TUsers = TUsers;
	// }

}