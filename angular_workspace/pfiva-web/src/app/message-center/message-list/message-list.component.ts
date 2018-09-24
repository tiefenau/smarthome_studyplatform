import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MatSort, MatPaginator } from '@angular/material';
import { MessageDataTableDataSource } from './message-data-table-datasource';
import { MessageService } from '../message.service';
import { Message } from '../../data-model/message/Message';

@Component({
  selector: 'app-message-list',
  templateUrl: './message-list.component.html',
  styleUrls: ['./message-list.component.css']
})
export class MessageListComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  dataSource: MessageDataTableDataSource;
  displayedColumns = ['id', 'messageText', 'deliveryDate', 'status', 'action'];
  
  constructor(private messageService: MessageService,
    private changeDetectorRefs: ChangeDetectorRef,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.messageService.getMessages().subscribe(
      (messages: Message[]) => {
        this.dataSource = new MessageDataTableDataSource(this.paginator, messages);
      },
      (error) => console.log(error)
    );
  }

  cancelScheduledMessage(messageId: number) {
    this.messageService.cancelScheduledMessage(messageId).subscribe(
        (status: boolean) => {
          if(status) {
            this.messageService.getMessages().subscribe(
              (messages: Message[]) => {
                this.dataSource = new MessageDataTableDataSource(this.paginator, messages);
                   this.changeDetectorRefs.detectChanges();
              },
              (error) => console.log(error)
            );
          }
        }
      );
  }

  showMessageDetails(messageId: number) {
    this.router.navigate([messageId], {relativeTo:this.route});
  }

}
