import { Survey } from "./Survey";

export class SurveyResponseData {
	private survey: Survey;

	public get Survey(): Survey {
		return this.survey;
	}
	public set Survey(value: Survey) {
		this.survey = value;
	}
}