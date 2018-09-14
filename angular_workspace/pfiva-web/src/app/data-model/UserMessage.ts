import { User } from "./User";

export class UserMessage {

    private _messageText: string;
    private _deliveryDateTime: string;
    private _users: User[];
    
    constructor() {    
	}

	public get messageText(): string {
		return this._messageText;
	}
	public set messageText(value: string) {
		this._messageText = value;
	}
	public get deliveryDateTime(): string {
		return this._deliveryDateTime;
	}
	public set deliveryDateTime(value: string) {
		this._deliveryDateTime = value;
	}
	public get users(): User[] {
		return this._users;
	}
	public set users(value: User[]) {
		this._users = value;
	}

	public toMessageJSON() {
		return {
			messageText: this._messageText,
			deliveryDateTime: this._deliveryDateTime,
			users: this._users
		};
	}
}