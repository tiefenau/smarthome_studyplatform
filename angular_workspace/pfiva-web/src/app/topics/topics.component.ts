import { Component, OnInit } from '@angular/core';
import { TopicService } from './topic.service';
import { Topic } from '../data-model/Topic';

@Component({
  selector: 'app-topics',
  templateUrl: './topics.component.html',
  styleUrls: ['./topics.component.css']
})
export class TopicsComponent implements OnInit {

  topics: Topic[];

  constructor(private topicService: TopicService) {
      this.topics = [];
  }

  ngOnInit() {
    this.topicService.getTopics().subscribe(
      (data: Topic[]) => {
        this.topics = data;
        
      },
      (error) => console.log(error)
    );

    //this.topicService.getTopics();
    /*this.topicService.getTopics().then(
      (res)=> {
        //console.log(res);
        this.topics = res;
      });*/
  }

}
