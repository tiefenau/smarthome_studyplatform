import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { MatPaginator } from '@angular/material';
import { UserTableDataSource } from './user-table-datasource';
import { ConfigurationService } from '../configuration.service';
import { User } from '../../data-model/User';

@Component({
  selector: 'app-user-configuration',
  templateUrl: './user-configuration.component.html',
  styleUrls: ['./user-configuration.component.css']
})
export class UserConfigurationComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  displayedColumns = ['id', 'username', 'deviceId'];
  dataSource: UserTableDataSource;

  constructor(private configurationService: ConfigurationService,
    private changeDetectorRefs: ChangeDetectorRef) {

  }

  ngOnInit() {
    this.fetchUsers(false);
  }

  fetchUsers(refresh: boolean) {
    this.configurationService.getPfivaUsers().subscribe(
      (users: User[]) => {
        this.dataSource = new UserTableDataSource(this.paginator, users);
        if(refresh) {
          this.changeDetectorRefs.detectChanges();
        }
      },
      (error) => console.log(error)
    );
  }

}
