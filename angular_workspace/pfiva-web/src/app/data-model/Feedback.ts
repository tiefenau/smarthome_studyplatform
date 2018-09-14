export class Feedback {

    private _id: number;
    private _feedbackQuery: string;
    private _userResponse: string;
    private _timestamp: string;

    public get id(): number {
		return this._id;
    }
    
	public set id(value: number) {
		this._id = value;
	}
    
	public get feedbackQuery(): string {
		return this._feedbackQuery;
	}

	public set feedbackQuery(value: string) {
		this._feedbackQuery = value;
	}

	public get userResponse(): string {
		return this._userResponse;
	}

	public set userResponse(value: string) {
		this._userResponse = value;
	}

	public get timestamp(): string {
		return this._timestamp;
	}

	public set timestamp(value: string) {
		this._timestamp = value;
	}
}
