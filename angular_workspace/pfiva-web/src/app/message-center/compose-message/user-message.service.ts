import { UserMessage } from "../../data-model/UserMessage";
import { Injectable } from "@angular/core";
import { Http, Response } from "@angular/http";
import { Constants } from "../../shared/constants";
import { map } from 'rxjs/operators';

@Injectable()
export class UserMessageService {

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

    sendUserMessage(userMessage: UserMessage) {
        console.log("Details : " + userMessage.messageText + ", " + userMessage.deliveryDateTime + ", " + userMessage.users);
        let url: string = Constants.PFIVA_BASE_URL + Constants.PFIVA_SEND_MESSAGE;
        this.http.post(url, userMessage).subscribe(
            (error) => console.log(error)
        );
    }
}