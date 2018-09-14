export class User {
    private _id: number;
    private _username: string;
	private _deviceId: string;

	public get id(): number {
		return this._id;
	}
	public set id(value: number) {
		this._id = value;
	}
	public get username(): string {
		return this._username;
	}
	public set username(value: string) {
		this._username = value;
	}
	public get deviceId(): string {
		return this._deviceId;
	}
	public set deviceId(value: string) {
		this._deviceId = value;
	}

	public toUserJSON() {
		return {
			id: this._id,
			username: this._username,
			deviceId: this._deviceId
		};
	}
}