package com.none.web.model;

public class PollAnswer {

	private int id;
	private int pollId;
	private String answer;
	private int pollScore;
	private int isDelete;
	private String udid;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPollId() {
		return pollId;
	}
	public void setPollId(int pollId) {
		this.pollId = pollId;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getPollScore() {
		return pollScore;
	}
	public void setPollScore(int pollScore) {
		this.pollScore = pollScore;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public String getUdid() {
		return udid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	
	
}
