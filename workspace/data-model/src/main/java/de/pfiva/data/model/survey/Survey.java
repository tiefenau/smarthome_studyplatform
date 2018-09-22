package de.pfiva.data.model.survey;

import java.io.Serializable;
import java.util.List;

import de.pfiva.data.model.User;

public class Survey implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String surveyName;
	private String deliveryDateTime;
	private SurveyStatus surveyStatus;
	
	private List<User> users;
	private List<Question> questions;
	
	public enum SurveyStatus {
		DELIVERED, PENDING, CANCELLED
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public String getDeliveryDateTime() {
		return deliveryDateTime;
	}

	public void setDeliveryDateTime(String deliveryDateTime) {
		this.deliveryDateTime = deliveryDateTime;
	}

	public SurveyStatus getSurveyStatus() {
		return surveyStatus;
	}

	public void setSurveyStatus(SurveyStatus surveyStatus) {
		this.surveyStatus = surveyStatus;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	@Override
	public String toString() {
		return "Survey [id=" + id + ", surveyName=" + surveyName + ", deliveryDateTime=" + deliveryDateTime
				+ ", surveyStatus=" + surveyStatus + ", users=" + users + ", questions=" + questions + "]";
	}
}
