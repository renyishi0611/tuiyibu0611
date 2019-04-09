package com.qikemi.packages.alibaba.aliyun.oss.properties;

public class OSSClientConf {

	private String bucketName = "";
	private boolean useStatus = true;
	private String key = "";
	private String secret = "";
	private String endPoint = "";
	private String downloadDomain="";
	private String env="";//环境
	
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public boolean isUseStatus() {
		return useStatus;
	}
	public void setUseStatus(boolean useStatus) {
		this.useStatus = useStatus;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public String getDownloadDomain() {
		return downloadDomain;
	}
	public void setDownloadDomain(String downloadDomain) {
		this.downloadDomain = downloadDomain;
	}
	public String getEnv() {
		return env;
	}
	public void setEnv(String env) {
		this.env = env;
	}
	

}
