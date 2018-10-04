import { Component, OnInit } from '@angular/core';
import { TopicService } from './topic.service';
import { Topic } from '../data-model/Topic';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TopicDeleteModalComponent } from './topic-delete-modal/topic-delete-modal.component';

@Component({
  selector: 'app-topics',
  templateUrl: './topics.component.html',
  styleUrls: ['./topics.component.css']
})
export class TopicsComponent implements OnInit {

  topics: Topic[];

  constructor(private topicService: TopicService,
    private modalService: NgbModal) {
      this.topics = [];
  }

  ngOnInit() {
    this.fetchTopicDetails();
  }

  fetchTopicDetails() {
    this.topicService.getTopics().subscribe(
      (data: Topic[]) => {
        this.topics = data;  
      },
      (error) => console.log(error)
    );    
  }

  deleteTopic(topic: Topic) {
    const modalRef = this.modalService.open(TopicDeleteModalComponent);
    modalRef.componentInstance.topic = topic;
    modalRef.result
      .then((result: number) => {
        this.topicService.deleteTopicWithAssociatedData(result)
          .subscribe(
            (status: boolean) => {
              if(status) {
                this.fetchTopicDetails();
              }
            }
          );
      },
      (error) => {
        
      });
  }
  
}
