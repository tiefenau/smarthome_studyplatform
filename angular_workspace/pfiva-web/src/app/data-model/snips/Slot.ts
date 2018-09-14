import { Range } from "./Range";
import { Value } from "./Value";

export class Slot {

    private _range: Range;
    private _rawValue: string;
    private _value: Value;
    private _entity: string;
    private _slotName: string;

	public get range(): Range {
		return this._range;
	}

	public set range(value: Range) {
		this._range = value;
	}

	public get rawValue(): string {
		return this._rawValue;
	}

	public set rawValue(value: string) {
		this._rawValue = value;
    }
    
	public get value(): Value {
		return this._value;
	}

	public set value(value: Value) {
		this._value = value;
	}

	public get entity(): string {
		return this._entity;
	}

	public set entity(value: string) {
		this._entity = value;
    }
    
	public get slotName(): string {
		return this._slotName;
	}

	public set slotName(value: string) {
		this._slotName = value;
	}
}
