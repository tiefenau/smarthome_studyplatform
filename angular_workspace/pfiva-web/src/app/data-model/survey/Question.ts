import { Option } from "./Option";

export class Question {

	private id: number;
	private question: string;
	private questionType: string;
	private options: Option[];

	public get Id(): number {
		return this.id;
	}
	public set Id(value: number) {
		this.id = value;
	}
	public get Question(): string {
		return this.question;
	}
	public set Question(value: string) {
		this.question = value;
	}
	public get QuestionType(): string {
		return this.questionType;
	}
	public set QuestionType(value: string) {
		this.questionType = value;
	}
	public get Options(): Option[] {
		return this.options;
	}
	public set Options(value: Option[]) {
		this.options = value;
	}
}