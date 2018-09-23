import { Component, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { MatPaginator } from '@angular/material';
import { SurveyService } from '../survey.service';
import { SurveyDataSource } from './survey-datasource';
import { Survey } from '../../data-model/survey/Survey';

@Component({
  selector: 'app-survey-list',
  templateUrl: './survey-list.component.html',
  styleUrls: ['./survey-list.component.css']
})
export class SurveyListComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  dataSource: SurveyDataSource;
  displayedColumns = ['id', 'surveyName', 'deliveryDate', 'status', 'action'];
  
  constructor(private surveyService: SurveyService,
    private changeDetectorRefs: ChangeDetectorRef) { }

  ngOnInit() {
    this.surveyService.getSurveys().subscribe(
      (surveys: Survey[]) => {
        this.dataSource = new SurveyDataSource(this.paginator, surveys);
      },
      (error) => console.log(error)
    );
  }

  showSurveyDetails(surveyId: number) {
    console.log(surveyId);
  }

  cancelScheduledSurvey(surveyId: number) {
    this.surveyService.cancelScheduledSurvey(surveyId).subscribe(
        (status: boolean) => {
          if(status) {
            this.surveyService.getSurveys().subscribe(
              (surveys: Survey[]) => {
                this.dataSource = new SurveyDataSource(this.paginator, surveys);
                   this.changeDetectorRefs.detectChanges();
              },
              (error) => console.log(error)
            );
          }
        }
      );
  }

}
