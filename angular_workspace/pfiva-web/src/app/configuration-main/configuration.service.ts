import { Http, Response } from "@angular/http";
import { Injectable } from "@angular/core";
import { Constants } from "../shared/constants";
import { map } from 'rxjs/operators';
import { User } from "../data-model/User";

@Injectable()
export class ConfigurationService {

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

    addNewUser(user: User) {
        let url: string = Constants.PFIVA_BASE_URL + Constants.PFIVA_ADD_USER;
        return this.http.post(url, user).pipe(map(
            (response: Response) => {
                const data = response.json();
                return data;
            }
        ));
        /*this.http.post(url, user).subscribe(
            (error) => console.log(error)
        );*/
    }

    deleteUser(userId: number) {
        let url: string = Constants.PFIVA_BASE_URL 
            + Constants.PFIVA_DELETE_USER + "/" + userId;
            return this.http.delete(url).pipe(map(
            (response: Response) => {
                const data = response.json();
                return data;
            }
        ));
    }
}