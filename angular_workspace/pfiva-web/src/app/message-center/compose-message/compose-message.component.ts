import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { User } from '../../data-model/User';
import { MessageService } from '../message.service';
import { Message } from '../../data-model/message/Message';

@Component({
  selector: 'app-compose-message',
  templateUrl: './compose-message.component.html',
  styleUrls: ['./compose-message.component.css']
})
export class ComposeMessageComponent implements OnInit {

  @ViewChild('messageData') composeMessageForm: NgForm;
  deliveryDateTime = 'Send Now';
  users: User[];

  constructor(private messageService: MessageService) { 
    
  }

  ngOnInit() {
    this.fetchUsers();
  }

  onSubmit() {
    this.processMessage();

    this.composeMessageForm.reset();
    this.composeMessageForm.form.patchValue({ 'deliveryDateTime':'Send Now' });
  }

  private fetchUsers() {
    this.messageService.getPfivaUsers()
      .subscribe(
        (users: User[]) => this.users = users,
        (error) => console.log(error)
      );
  }

  private processMessage() {
    let message = new Message();
    message.MessageText = this.composeMessageForm.form.value.messageText;
    let deliveryDateValue = this.composeMessageForm.form.value.deliveryDateTime;
    if(deliveryDateValue == 'Send Now') {
      message.DeliveryDateTime = 'Now';
    } else {
      // TODO - formate date using pipe
      let date: string = this.composeMessageForm.form.value.deliveryDate 
      + " " + this.composeMessageForm.form.value.delvieryTime + ":00.000";
      message.DeliveryDateTime = date;
    }
    let usersArray: string[] = this.composeMessageForm.form.value.users;
    let intendedUsers: User[] = [];
    for(let element of usersArray) {
      for(let user of this.users) {
        let userObject = Object.assign(new User(), user);
        if(userObject.Username === element) {
          let newuser = new User();
          newuser.Id = userObject.Id;
          newuser.Username = userObject.Username;
          newuser.DeviceId = userObject.DeviceId;
          intendedUsers.push(newuser);
        }
      }
    }
    message.Users = intendedUsers;
    this.messageService.sendUserMessage(message);
  }

}
