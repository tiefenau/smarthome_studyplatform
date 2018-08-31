package de.pfiva.data.model;

import java.io.Serializable;

public class Feedback implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String feedbackQuery;
	private String userResponse;
	private String timestamp;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFeedbackQuery() {
		return feedbackQuery;
	}
	public void setFeedbackQuery(String feedbackQuery) {
		this.feedbackQuery = feedbackQuery;
	}
	public String getUserResponse() {
		return userResponse;
	}
	public void setUserResponse(String userResponse) {
		this.userResponse = userResponse;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "Feedback [id=" + id + ", feedbackQuery=" + feedbackQuery + ", userResponse=" + userResponse
				+ ", timestamp=" + timestamp + "]";
	}
}
