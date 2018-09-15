import { Http, Response } from "@angular/http";
import { Injectable } from "@angular/core";
import { Constants } from "../shared/constants";
import { map } from 'rxjs/operators';
import { Message } from "../data-model/Message";

@Injectable()
export class MessageService {
    constructor(private http: Http) {
    }

    getPfivaUsers() {
        let url: string = Constants.PFIVA_BASE_URL + Constants.PFIVA_USERS;
        return this.http.get(url).pipe(map(
            (response: Response) => {
                const data = response.json();
                return data;
            }
        ));
    }

    sendUserMessage(message: Message) {
        let url: string = Constants.PFIVA_BASE_URL + Constants.PFIVA_SEND_MESSAGE;
        this.http.post(url, message).subscribe(
            (error) => console.log(error)
        );
    }

    getMessages() {
        let url: string = Constants.PFIVA_BASE_URL + Constants.PFIVA_MESSAGES;
        return this.http.get(url).pipe(map(
            (response: Response) => {
                const data = response.json();
                return data;
            }
        ));
    }
}