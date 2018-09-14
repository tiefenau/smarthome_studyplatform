export class Value {

    private _kind: string;
    private _value: string;
    private _grain: string;
    private _precision: string;

	public get kind(): string {
		return this._kind;
	}

	public set kind(value: string) {
		this._kind = value;
    }

	public get value(): string {
		return this._value;
	}

	public set value(value: string) {
		this._value = value;
    }
    
	public get grain(): string {
		return this._grain;
	}

	public set grain(value: string) {
		this._grain = value;
	}

	public get precision(): string {
		return this._precision;
	}

	public set precision(value: string) {
		this._precision = value;
	}
}
