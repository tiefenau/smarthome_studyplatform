package de.pfiva.data.model.notification;

public class Data {

	private String text;
	private int feedbackId;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(int feedbackId) {
		this.feedbackId = feedbackId;
	}
	@Override
	public String toString() {
		return "Data [text=" + text + ", feedbackId=" + feedbackId + "]";
	}
}
