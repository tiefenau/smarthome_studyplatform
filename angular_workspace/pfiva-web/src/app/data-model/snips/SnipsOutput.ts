import { Intent } from "./Intent";
import { Slot } from "./Slot";

export class SnipsOutput {

    private _id: string;
    private _input: string;
    private _intent: Intent;
    private _slots: Slot[];
    private _sessionId: string;

    private _filename: string;
    private _filePath: string;
    private _hotword: string;
    private _timestamp: string;

	public get id(): string {
		return this._id;
	}

	public set id(value: string) {
		this._id = value;
    }
    
	public get input(): string {
		return this._input;
	}

	public set input(value: string) {
		this._input = value;
	}

	public get intent(): Intent {
		return this._intent;
	}

	public set intent(value: Intent) {
		this._intent = value;
    }
    
	public get slots(): Slot[] {
		return this._slots;
	}

	public set slots(value: Slot[]) {
		this._slots = value;
    }
    
	public get sessionId(): string {
		return this._sessionId;
	}

	public set sessionId(value: string) {
		this._sessionId = value;
	}

	public get filename(): string {
		return this._filename;
	}

	public set filename(value: string) {
		this._filename = value;
    }
    
	public get filePath(): string {
		return this._filePath;
	}

	public set filePath(value: string) {
		this._filePath = value;
	}

	public get hotword(): string {
		return this._hotword;
	}

	public set hotword(value: string) {
		this._hotword = value;
    }
    
	public get timestamp(): string {
		return this._timestamp;
	}

	public set timestamp(value: string) {
		this._timestamp = value;
	}
}
