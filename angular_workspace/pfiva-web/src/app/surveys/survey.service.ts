import { Http, Response } from "@angular/http";
import { Injectable } from "@angular/core";
import { Constants } from "../shared/constants";
import { map } from 'rxjs/operators';
import { Survey } from "../data-model/survey/Survey";

@Injectable()
export class SurveyService {

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

    sendSurvey(survey: Survey) {
        let url: string = Constants.PFIVA_BASE_URL + Constants.PFIVA_SEND_SURVEY;
        this.http.post(url, survey).subscribe(
            (error) => console.log(error)
        );
    }

    cancelScheduledSurvey(surveyId: number) {
        let url: string = Constants.PFIVA_BASE_URL 
            + Constants.PFIVA_CANCEL_SURVEY + "/" + surveyId;
        return this.http.put(url, null).pipe(map(
            (response: Response) => {
                const data = response.json()
                return data;
            }
        ));
    }
}