package de.pfiva.data.model.notification;

import java.io.Serializable;

import de.pfiva.data.model.survey.Survey;

public class SurveyData extends Data implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Survey survey;
	
	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	@Override
	public String toString() {
		return "SurveyData [survey=" + survey + "]";
	}
}
