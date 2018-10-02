export class Topic {

	private id: number;
	private topicname: string;
	private creationDate: string;
	private modificationDate: string;
	private surveyCount: number;
	private messageCount: number;

	public get Id(): number {
		return this.id;
	}

	public set Id(value: number) {
		this.id = value;
	}
	public get Topicname(): string {
		return this.topicname;
	}

	public set Topicname(value: string) {
		this.topicname = value;
	}
	public get CreationDate(): string {
		return this.creationDate;
	}

	public set CreationDate(value: string) {
		this.creationDate = value;
	}
	public get ModificationDate(): string {
		return this.modificationDate;
	}

	public set ModificationDate(value: string) {
		this.modificationDate = value;
	}
	public get SurveyCount(): number {
		return this.surveyCount;
	}

	public set SurveyCount(value: number) {
		this.surveyCount = value;
	}
	public get MessageCount(): number {
		return this.messageCount;
	}

	public set MessageCount(value: number) {
		this.messageCount = value;
	}
}