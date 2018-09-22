import { User } from "../User";
import { Question } from "./Question";

export class Survey {
	private id: number;
	private surveyName: string;
	private deliveryDateTime: string;
	private surveyStatus: string;

	private users: User[];
	private questions: Question[];

	public get Id(): number {
		return this.id;
	}
	public set Id(value: number) {
		this.id = value;
	}
	public get SurveyName(): string {
		return this.surveyName;
	}
	public set SurveyName(value: string) {
		this.surveyName = value;
	}
	public get DeliveryDateTime(): string {
		return this.deliveryDateTime;
	}
	public set DeliveryDateTime(value: string) {
		this.deliveryDateTime = value;
	}
	public get SurveyStatus(): string {
		return this.surveyStatus;
	}
	public set SurveyStatus(value: string) {
		this.surveyStatus = value;
	}
	public get Users(): User[] {
		return this.users;
	}
	public set Users(value: User[]) {
		this.users = value;
	}
	public get Questions(): Question[] {
		return this.questions;
	}
	public set Questions(value: Question[]) {
		this.questions = value;
	}
}