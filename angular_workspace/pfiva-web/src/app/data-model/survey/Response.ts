import { User } from "../User";

export class Response {
	private id: number;
	private questionId: number;
	private values: string[];
	private user: User;

	public get Id(): number {
		return this.id;
	}
	public set Id(value: number) {
		this.id = value;
	}
	public get QuestionId(): number {
		return this.questionId;
	}
	public set QuestionId(value: number) {
		this.questionId = value;
	}
	public get Values(): string[] {
		return this.values;
	}
	public set Values(value: string[]) {
		this.values = value;
	}
	public get User(): User {
		return this.user;
	}
	public set User(value: User) {
		this.user = value;
	}
}