package de.pfiva.data.model;

import java.io.Serializable;

public class Topic implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String topicname;
	private String creationDate;
	private String modificationDate;
	
	private int surveyCount;
	private int messageCount;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTopicname() {
		return topicname;
	}
	public void setTopicname(String topicname) {
		this.topicname = topicname;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(String modificationDate) {
		this.modificationDate = modificationDate;
	}
	public int getSurveyCount() {
		return surveyCount;
	}
	public void setSurveyCount(int surveyCount) {
		this.surveyCount = surveyCount;
	}
	public int getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}
	@Override
	public String toString() {
		return "Topic [id=" + id + ", topicname=" + topicname + ", creationDate=" + creationDate + ", modificationDate="
				+ modificationDate + ", surveyCount=" + surveyCount + ", messageCount=" + messageCount + "]";
	}
}
