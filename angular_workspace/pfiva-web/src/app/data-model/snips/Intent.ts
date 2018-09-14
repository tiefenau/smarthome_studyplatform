export class Intent {
    private _intentId: number;
    private _intentName: string;
    private _probability: number;

	public get intentId(): number {
		return this._intentId;
    }
    
	public set intentId(value: number) {
		this._intentId = value;
	}

	public get intentName(): string {
		return this._intentName;
	}

	public set intentName(value: string) {
		this._intentName = value;
	}

	public get probability(): number {
		return this._probability;
    }
    
	public set probability(value: number) {
		this._probability = value;
	}
}
