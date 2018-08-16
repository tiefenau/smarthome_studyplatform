package de.pfiva.data.ingestion.model;

import java.time.LocalDateTime;

public class Feedback {

	private int id;
	private String feedbackQuery;
	private String userResponse;
	private LocalDateTime timestamp;
	
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
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return "Feedback [id=" + id + ", feedbackQuery=" + feedbackQuery + ", userResponse=" + userResponse
				+ ", timestamp=" + timestamp + "]";
	}
}
