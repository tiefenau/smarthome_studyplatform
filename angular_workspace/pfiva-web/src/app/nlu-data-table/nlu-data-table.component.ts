import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';
import { NluDataTableDataSource } from './nlu-data-table-datasource';
import { PfivaDataService } from '../services/pfiva-data.service';
import { NLUData } from '../data-model/NLUData';

@Component({
  selector: 'app-nlu-data-table',
  templateUrl: './nlu-data-table.component.html',
  styleUrls: ['./nlu-data-table.component.css']
})
export class NluDataTableComponent implements OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: NluDataTableDataSource;
  displayedColumns = ['id', 'input', 'hotword', 'intent',
   'timestamp', 'user', 'feedbackUserResponse'];

  constructor(private pfivaDataService: PfivaDataService) {
    
  }

  ngOnInit() {
    this.pfivaDataService.getNLUData().subscribe(
      (nluData: NLUData[]) => {
        this.dataSource = new NluDataTableDataSource(this.paginator,
           this.sort, nluData);
      },
      (error) => console.log(error)
    );
    //this.dataSource = new NluDataTableDataSource(this.paginator, this.sort, this.pfivaDataService);
  }
}
