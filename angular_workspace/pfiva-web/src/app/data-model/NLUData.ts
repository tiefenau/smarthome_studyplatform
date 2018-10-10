import { Feedback } from "./Feedback";
import { SnipsOutput } from "./snips/SnipsOutput";
import { User } from "./User";

export class NLUData {

    private _feedback: Feedback;
	private _snipsOutput: SnipsOutput;
	private _user: User;

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

	public get user(): User {
		return this._user;
	}

	public set user(value: User) {
		this._user = value;
	}
}
