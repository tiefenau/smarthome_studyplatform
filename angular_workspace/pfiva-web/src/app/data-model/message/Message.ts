import { User } from "../User";


export class Message {
	private id: number;
	private messageText: string;
	private deliveryDateTime: string;
	private messageStatus: string;
	private users: User[];
	private topic: string;

	public get Id(): number {
		return this.id;
	}
	public set Id(value: number) {
		this.id = value;
	}
	public get MessageText(): string {
		return this.messageText;
	}
	public set MessageText(value: string) {
		this.messageText = value;
	}
	public get DeliveryDateTime(): string {
		return this.deliveryDateTime;
	}
	public set DeliveryDateTime(value: string) {
		this.deliveryDateTime = value;
	}
	public get MessageStatus(): string {
		return this.messageStatus;
	}
	public set MessageStatus(value: string) {
		this.messageStatus = value;
	}
	public get Users(): User[] {
		return this.users;
	}
	public set Users(value: User[]) {
		this.users = value;
	}
	public get Topic(): string {
		return this.topic;
	}
	public set Topic(value: string) {
		this.topic = value;
	}
}