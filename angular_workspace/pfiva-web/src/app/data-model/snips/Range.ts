export class Range {
    private _start: number;
    private _end: number;
    
	public get start(): number {
		return this._start;
	}

	public set start(value: number) {
		this._start = value;
	}

	public get end(): number {
		return this._end;
	}

	public set end(value: number) {
		this._end = value;
	}    
}
