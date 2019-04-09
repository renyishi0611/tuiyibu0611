package com.none.web.model;

public class PollResult {
	
	private int id;
	private String userId;
	private String userName;
	private int pollAnswerId;
	private int live_pollId;
	private String answerDate;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getPollAnswerId() {
		return pollAnswerId;
	}
	public void setPollAnswerId(int pollAnswerId) {
		this.pollAnswerId = pollAnswerId;
	}
	public int getLive_pollId() {
		return live_pollId;
	}
	public void setLive_pollId(int live_pollId) {
		this.live_pollId = live_pollId;
	}
	public String getAnswerDate() {
		return answerDate;
	}
	public void setAnswerDate(String answerDate) {
		this.answerDate = answerDate;
	}
}
