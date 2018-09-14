import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort } from '@angular/material';
import { NluDataTableDataSource } from './nlu-data-table-datasource';
import { PfivaDataService } from '../services/pfiva-data.service';

@Component({
  selector: 'app-nlu-data-table',
  templateUrl: './nlu-data-table.component.html',
  styleUrls: ['./nlu-data-table.component.css']
})
export class NluDataTableComponent implements OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;
  dataSource: NluDataTableDataSource;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'input', 'hotword', 'intent', 'timestamp', 'feedbackQuery', 'feedbackUserResponse', 'feedbackTimestamp'];

  constructor(private pfivaDataService: PfivaDataService) {
    
  }

  ngOnInit() {
    this.dataSource = new NluDataTableDataSource(this.paginator, this.sort, this.pfivaDataService);
  }
}
