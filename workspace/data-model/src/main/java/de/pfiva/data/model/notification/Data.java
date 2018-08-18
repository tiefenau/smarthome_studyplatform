package de.pfiva.data.model.notification;

import java.io.Serializable;

import de.pfiva.data.model.FeedbackType;

public class Data implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String text;
	private int feedbackId;
	private FeedbackType feedbackType;
	
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
	public FeedbackType getFeedbackType() {
		return feedbackType;
	}
	public void setFeedbackType(FeedbackType feedbackType) {
		this.feedbackType = feedbackType;
	}
	@Override
	public String toString() {
		return "Data [text=" + text + ", feedbackId=" + feedbackId + ", feedbackType=" + feedbackType + "]";
	}
}
