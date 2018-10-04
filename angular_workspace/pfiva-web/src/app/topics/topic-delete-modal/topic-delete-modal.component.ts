import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Topic } from '../../data-model/Topic';

@Component({
  selector: 'app-topic-delete-modal',
  templateUrl: './topic-delete-modal.component.html',
  styleUrls: ['./topic-delete-modal.component.css']
})
export class TopicDeleteModalComponent {

  @Input() topic: Topic;
  constructor(public activeModal: NgbActiveModal) { }
  
}
