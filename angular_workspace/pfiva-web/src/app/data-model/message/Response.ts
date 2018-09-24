import { User } from "../User";

export class Response {
	private id: number;
	private value: string;
	private user: User;

	public get Id(): number {
		return this.id;
	}
	public set Id(value: number) {
		this.id = value;
	}

	public get Value(): string {
		return this.value;
	}
	public set Value(value: string) {
		this.value = value;
	}

	public get User(): User {
		return this.user;
	}
	public set User(value: User) {
		this.user = value;
	}
}