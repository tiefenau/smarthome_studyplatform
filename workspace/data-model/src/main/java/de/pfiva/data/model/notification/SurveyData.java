package de.pfiva.data.model.notification;

import java.io.Serializable;

import de.pfiva.data.model.survey.Survey;

public class SurveyData extends Data implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Survey survey;
	private int userId;
	
	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "SurveyData [survey=" + survey + ", userId=" + userId + "]";
	}
}
