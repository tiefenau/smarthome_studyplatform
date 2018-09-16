import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { MatSort, MatPaginator } from '@angular/material';
import { MessageDataTableDataSource } from './message-data-table-datasource';
import { MessageService } from '../message.service';
import { MessageData } from '../../data-model/MessageData';

@Component({
  selector: 'app-message-list',
  templateUrl: './message-list.component.html',
  styleUrls: ['./message-list.component.css']
})
export class MessageListComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MessageDataTableDataSource;
  displayedColumns = ['id', 'messageText', 'deliveryDate', 'status', 'action'];
  
  constructor(private messageService: MessageService,
    private changeDetectorRefs: ChangeDetectorRef) { }

  ngOnInit() {
    this.messageService.getMessages().subscribe(
      (messageData: MessageData[]) => {
        this.dataSource = new MessageDataTableDataSource(this.paginator,
           this.sort, messageData);
      },
      (error) => console.log(error)
    );
  }

  cancelScheduledMessage(messageId: number) {
    this.messageService.cancelScheduledMessage(messageId).subscribe(
        (status: boolean) => {
          if(status) {
            this.messageService.getMessages().subscribe(
              (messageData: MessageData[]) => {
                this.dataSource = new MessageDataTableDataSource(this.paginator,
                   this.sort, messageData);
                   this.changeDetectorRefs.detectChanges();
              },
              (error) => console.log(error)
            );
          }
        }
      );
  }

}
