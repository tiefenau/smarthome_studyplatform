import { Feedback } from "./Feedback";
import { SnipsOutput } from "./snips/SnipsOutput";

export class NLUData {

    private _feedback: Feedback;
    private _snipsOutput: SnipsOutput;

	public get feedback(): Feedback {
		return this._feedback;
	}

	public set feedback(value: Feedback) {
		this._feedback = value;
    }
    
	public get snipsOutput(): SnipsOutput {
		return this._snipsOutput;
	}

	public set snipsOutput(value: SnipsOutput) {
		this._snipsOutput = value;
	}
}
