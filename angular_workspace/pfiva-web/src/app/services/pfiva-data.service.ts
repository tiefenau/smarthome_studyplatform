import { Http, Response } from "@angular/http";
import { Constants } from "../shared/constants";
import { Injectable } from "@angular/core";
import { map } from 'rxjs/operators';

@Injectable()
export class PfivaDataService {
    constructor(private http: Http) {
    }

    getNLUData() {
        let url: string = Constants.PFIVA_BASE_URL + Constants.PFIVA_NLU_DATA;
        return this.http.get(url).pipe(map(
            (response: Response) => {
                const data = response.json();
                return data;
            }
        ));
    }
}