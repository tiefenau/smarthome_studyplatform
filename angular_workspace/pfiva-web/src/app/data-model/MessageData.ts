import { Message } from "./Message";

export class MessageData {
    
    private message: Message;

    constructor() {}

    public get Message(): Message {
		return this.message;
	}
	public set Message(value: Message) {
		this.message = value;
	}
}