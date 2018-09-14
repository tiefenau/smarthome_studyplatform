import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserMessageService } from './user-message.service';
import { UserMessage } from '../../data-model/UserMessage';
import { User } from '../../data-model/User';

@Component({
  selector: 'app-compose-message',
  templateUrl: './compose-message.component.html',
  styleUrls: ['./compose-message.component.css'],
  providers: [UserMessageService]
})
export class ComposeMessageComponent implements OnInit {

  @ViewChild('messageData') composeMessageForm: NgForm;
  deliveryDateTime = 'Send Now';
  users: User[];

  constructor(private userMessageService: UserMessageService) { 
    
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
    this.userMessageService.getPfivaUsers()
      .subscribe(
        (users: User[]) => this.users = users,
        (error) => console.log(error)
      );
  }

  private processMessage() {
    let message = new UserMessage();
    message.messageText = this.composeMessageForm.form.value.messageText;
    let deliveryDateValue = this.composeMessageForm.form.value.deliveryDateTime;
    if(deliveryDateValue == 'Send Now') {
      message.deliveryDateTime = 'Now';
    } else {
      // TODO - formate date using pipe
      message.deliveryDateTime = this.composeMessageForm.form.value.deliveryDate + " " + this.composeMessageForm.form.value.delvieryTime;
    }
    let usersArray: string[] = this.composeMessageForm.form.value.users;
    let intendedUsers: User[] = [];
    for(let element of usersArray) {
      for(let user of this.users) {
        if(user.username === element) {
          let newuser = new User();
          newuser.id = user.id;
          newuser.username = user.username;
          newuser.deviceId = user.deviceId;
          intendedUsers.push(newuser);
        }
      }
    }
    message.users = intendedUsers;
    //message.users = this.composeMessageForm.form.value.users;
    let serializedMessage: string = JSON.stringify(message, Object.keys(message.constructor.prototype));
    //console.log(serializedMessage);
    console.log(message.toMessageJSON());
    this.userMessageService.sendUserMessage(message);
  }

}
