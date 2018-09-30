import { Survey } from "./Survey";
import { Response } from "./Response";

export class SurveyResponseData {
	private survey: Survey;
	private responses: Response[];

	public get Survey(): Survey {
		return this.survey;
	}
	public set Survey(value: Survey) {
		this.survey = value;
	}
	public get Responses(): Response[] {
		return this.responses;
	}
	public set Responses(value: Response[]) {
		this.responses = value;
	}

}