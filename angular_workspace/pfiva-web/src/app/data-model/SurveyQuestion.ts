export class SurveyQuestion {

    private question: string;
    private questionType: string;
    private options: string[];

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
    public get Options(): string[] {
		return this.options;
	}
	public set Options(value: string[]) {
		this.options = value;
    }   
}