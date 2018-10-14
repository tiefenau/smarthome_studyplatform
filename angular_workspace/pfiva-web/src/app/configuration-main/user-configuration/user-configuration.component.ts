import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { MatPaginator } from '@angular/material';
import { UserTableDataSource } from './user-table-datasource';
import { ConfigurationService } from '../configuration.service';
import { User } from '../../data-model/User';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-user-configuration',
  templateUrl: './user-configuration.component.html',
  styleUrls: ['./user-configuration.component.css']
})
export class UserConfigurationComponent implements OnInit {

  @ViewChild('userData') addUserForm: NgForm;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  displayedColumns = ['id', 'username', 'deviceId', 'actions'];
  dataSource: UserTableDataSource;

  constructor(private configurationService: ConfigurationService,
    private changeDetectorRefs: ChangeDetectorRef) {

  }

  ngOnInit() {
    this.fetchUsers(false);
  }

  onDelete(userId: number) {
    this.configurationService.deleteUser(userId).subscribe(
      (status: boolean) => {
        if(status) {
          this.fetchUsers(true);
        }
      }
    );
  }

  onSubmit() {
    this.addNewUser();
    this.addUserForm.reset();
    //this.fetchUsers(true);
  }

  private addNewUser() {
    let user = new User();
    user.Username = this.addUserForm.form.value.username;
    let value:string = this.addUserForm.form.value.deviceId;
    user.DeviceId = value.toUpperCase();
    this.configurationService.addNewUser(user).subscribe(
      (status: boolean) => {
        if(status) {
          this.fetchUsers(true);
        }
      }
    );
  }

  fetchUsers(refresh: boolean) {
    this.configurationService.getPfivaUsers().subscribe(
      (users: User[]) => {
        this.dataSource = new UserTableDataSource(this.paginator, users);
        if(refresh) {
          this.paginator._changePageSize(this.paginator.pageSize);
          //this.changeDetectorRefs.detectChanges();
        }
      },
      (error) => console.log(error)
    );
  }

}
