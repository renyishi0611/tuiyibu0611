package com.none.web.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "t_online_user")
public class TOnlineuser {
	@Id
	private String userId;
	private Timestamp loginTime;
	private Timestamp opttime;
	private Integer status;//1--在线    2--不在线
	private String ip;
	private String mac;
	
	private Timestamp autoopttime;
	
	@Id
	@Column(name = "userId", unique = true, nullable = false, length = 40)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name = "loginTime")
	public Timestamp getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}
	
	@Column(name = "opttime")
	public Timestamp getOpttime() {
		return opttime;
	}
	public void setOpttime(Timestamp opttime) {
		this.opttime = opttime;
	}
	
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "ip")
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Column(name = "mac")
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	@Column(name = "autoopttime")
	public Timestamp getAutoopttime() {
		return autoopttime;
	}

	public void setAutoopttime(Timestamp autoopttime) {
		this.autoopttime = autoopttime;
	}

	@Override
	public String toString() {
		return "TOnlineuser [userId=" + userId + ", loginTime=" + loginTime + ", opttime=" + opttime + ", status="
				+ status + ", ip=" + ip + ", mac=" + mac + ", autoopttime=" + autoopttime + "]";
	}
	
}
