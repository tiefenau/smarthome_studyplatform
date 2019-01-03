package de.pfiva.data.model.survey;

import java.util.Set;

public class SurveyResponseData {

	private Survey survey;
	private Set<Response> responses;

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public Set<Response> getResponses() {
		return responses;
	}

	public void setResponses(Set<Response> responses) {
		this.responses = responses;
	}

	@Override
	public String toString() {
		return "SurveyResponseData [survey=" + survey + ", responses=" + responses + "]";
	}
}
