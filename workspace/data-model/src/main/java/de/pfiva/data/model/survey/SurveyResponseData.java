package de.pfiva.data.model.survey;

import java.util.List;

public class SurveyResponseData {

	private Survey survey;
	private List<Response> responses;

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public List<Response> getResponses() {
		return responses;
	}

	public void setResponses(List<Response> responses) {
		this.responses = responses;
	}

	@Override
	public String toString() {
		return "SurveyResponseData [survey=" + survey + ", responses=" + responses + "]";
	}
}
