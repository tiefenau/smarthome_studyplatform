import { Message } from "./Message";
import { Response } from "./Response";

export class MessageData {
    
	private message: Message;
	private responses: Response[];

    constructor() {}

    public get Message(): Message {
		return this.message;
	}
	public set Message(value: Message) {
		this.message = value;
	}

	public get Responses(): Response[] {
		return this.responses;
	}
	public set Responses(value: Response[]) {
		this.responses = value;
	}
}