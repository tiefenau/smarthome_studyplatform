import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { MessageService } from '../message.service';
import { MessageData } from '../../data-model/message/MessageData';

@Component({
  selector: 'app-message-detail',
  templateUrl: './message-detail.component.html',
  styleUrls: ['./message-detail.component.css']
})
export class MessageDetailComponent implements OnInit {

  messageResponseData: MessageData;

  constructor(private route: ActivatedRoute,
    private messageService: MessageService) { }

  ngOnInit() {
    const messageId: number = +this.route.snapshot.params['messageId'];
    this.messageService.getCompleteMessageData(messageId).subscribe(
      (data: MessageData) => this.messageResponseData = data
    );
    this.route.params.subscribe(
      (params: Params) => {
        this.messageService.getCompleteMessageData(params['messageId'])
          .subscribe(
            (data: MessageData) => this.messageResponseData = data
          );
      }
    );
  }
}
