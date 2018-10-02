import { Component, OnInit, ViewChild, ChangeDetectorRef, Input } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
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
  @Input() topic: string;
  displayedColumns = ['id', 'surveyName', 'deliveryDate', 'topic', 'status', 'action'];
  
  constructor(private surveyService: SurveyService,
    private changeDetectorRefs: ChangeDetectorRef,
    private router: Router,
    private route: ActivatedRoute,) { }

  ngOnInit() {
    if(this.topic === undefined) {
      this.surveyService.getSurveys().subscribe(
        (surveys: Survey[]) => {
          this.dataSource = new SurveyDataSource(this.paginator, surveys);
        },
        (error) => console.log(error)
      );
    } else {
      this.surveyService.getSurveysByTopic(this.topic).subscribe(
        (surveys: Survey[]) => {
          this.dataSource = new SurveyDataSource(this.paginator, surveys);
        },
        (error) => console.log(error)
      );
    }
    
  }

  showSurveyDetails(surveyId: number) {
    this.router.navigate([surveyId], {relativeTo:this.route});
  }

  cancelScheduledSurvey(surveyId: number) {
    this.surveyService.cancelScheduledSurvey(surveyId).subscribe(
        (status: boolean) => {
          if(status) {
            if(this.topic === undefined) {
              this.surveyService.getSurveys().subscribe(
                (surveys: Survey[]) => {
                  this.dataSource = new SurveyDataSource(this.paginator, surveys);
                     this.changeDetectorRefs.detectChanges();
                },
                (error) => console.log(error)
              );
            } else {
              this.surveyService.getSurveysByTopic(this.topic).subscribe(
                (surveys: Survey[]) => {
                  this.dataSource = new SurveyDataSource(this.paginator, surveys);
                  this.changeDetectorRefs.detectChanges();
                },
                (error) => console.log(error)
              );
            }
            
          }
        }
      );
  }

}
