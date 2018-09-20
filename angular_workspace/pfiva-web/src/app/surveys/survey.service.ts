import { Http, Response } from "@angular/http";
import { Injectable } from "@angular/core";
import { Constants } from "../shared/constants";
import { map } from 'rxjs/operators';

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
}