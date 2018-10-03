import { Http, Response } from "@angular/http";
import { Injectable } from "@angular/core";
import { Constants } from "../shared/constants";
import { map } from 'rxjs/operators';
import { Message } from "../data-model/message/Message";

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

    getTopicNames() {
        let url: string = Constants.PFIVA_BASE_URL + Constants.PFIVA_GET_TOPIC_NAMES;
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

    getMessagesByTopic(topic: string) {
        let url: string = Constants.PFIVA_BASE_URL 
            + Constants.PFIVA_MESSAGES + '?topic=' + topic;
        return this.http.get(url).pipe(map(
            (response: Response) => {
                const data = response.json();
                return data;
            }
        ));
    }

    getCompleteMessageData(messageId: number) {
        let url: string = Constants.PFIVA_BASE_URL 
            + Constants.PFIVA_MESSAGES + "/" + messageId;
        return this.http.get(url).pipe(map(
            (response: Response) => {
                const data = response.json();
                return data;
            }
        ));
    }

    cancelScheduledMessage(messageId: number) {
        let url: string = Constants.PFIVA_BASE_URL 
            + Constants.PFIVA_CANCEL_MESSAGE + "/" + messageId;
        return this.http.put(url, null).pipe(map(
            (response: Response) => {
                const data = response.json()
                return data;
            }
        ));
    }
}