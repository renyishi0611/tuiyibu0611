package com.none.web.common.bdPush;

public class PushConf {
	
	private String andriod_apiKey;
	private String andriod_secretKey;
	private String ios_apiKey;
	private String ios_secretKey;
	private String ios_deployStatus; //1：开发状态 2：生产状态
	
	public String getAndriod_apiKey() {
		return andriod_apiKey;
	}
	public void setAndriod_apiKey(String andriod_apiKey) {
		this.andriod_apiKey = andriod_apiKey;
	}
	public String getAndriod_secretKey() {
		return andriod_secretKey;
	}
	public void setAndriod_secretKey(String andriod_secretKey) {
		this.andriod_secretKey = andriod_secretKey;
	}
	public String getIos_apiKey() {
		return ios_apiKey;
	}
	public void setIos_apiKey(String ios_apiKey) {
		this.ios_apiKey = ios_apiKey;
	}
	public String getIos_secretKey() {
		return ios_secretKey;
	}
	public void setIos_secretKey(String ios_secretKey) {
		this.ios_secretKey = ios_secretKey;
	}
	public String getIos_deployStatus() {
		return ios_deployStatus;
	}
	public void setIos_deployStatus(String ios_deployStatus) {
		this.ios_deployStatus = ios_deployStatus;
	}

	
	
	
}
