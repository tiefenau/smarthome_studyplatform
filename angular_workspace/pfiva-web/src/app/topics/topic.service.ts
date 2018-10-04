import { Http, Response } from "@angular/http";
import { Injectable } from "@angular/core";
import { Constants } from "../shared/constants";
import { map } from 'rxjs/operators';
import { Topic } from "../data-model/Topic";

@Injectable()
export class TopicService {
    
    topics: Topic[];
    constructor(private http: Http) {
        this.topics = [];
    }

    getTopics() {
        let url: string = Constants.PFIVA_BASE_URL + Constants.PFIVA_GET_TOPICS;
        return this.http.get(url).pipe(map(
            (response: Response) => {
                const data = response.json();
                return data;
            }
        ));
    }

    deleteTopicWithAssociatedData(topicId: number) {
        let url: string = Constants.PFIVA_BASE_URL
             + Constants.PFIVA_GET_TOPICS + '/' + topicId;
        return this.http.delete(url).pipe(map(
            (response: Response) => {
                const data = response.json();
                return data
            }
        ));
    }

    /*getTopics() {
        let promise = new Promise((resolve, reject) => {
            let url: string = Constants.PFIVA_BASE_URL + Constants.PFIVA_GET_TOPICS;
            this.http.get(url).toPromise()
            .then(
                res => {
                    //console.log(res.json());
                    //this.topics = res.json().results;
                    this.topics = res.json().results.map(item => {
                        let topic = new Topic();
                        topic.Id = item.id;
                        topic.Topicname = item.topicname;
                        topic.CreationDate = item.creationDate;
                        topic.ModificationDate = item.modificationDate;
                        return topic;
                    });
                    resolve(this.topics);
                },
                msg => {
                    reject(msg);
                }
            )
        });
        return promise;
    }*/
}