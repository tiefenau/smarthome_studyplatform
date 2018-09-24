import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { SurveyService } from '../survey.service';
import { SurveyResponseData } from '../../data-model/survey/SurveyResponseData';

@Component({
  selector: 'app-survey-detail',
  templateUrl: './survey-detail.component.html',
  styleUrls: ['./survey-detail.component.css']
})
export class SurveyDetailComponent implements OnInit {
  
  surveyResponseData: SurveyResponseData;
  
  constructor(private route: ActivatedRoute, 
    private surveyService: SurveyService) { }

  ngOnInit() {
    const surveyId: number = +this.route.snapshot.params['surveyId'];
    this.surveyService.getCompleteSurveyData(surveyId).subscribe(
      (data: SurveyResponseData) => this.surveyResponseData = data
    );
    this.route.params.subscribe(
      (params: Params) => {
        this.surveyService.getCompleteSurveyData(params['surveyId'])
          .subscribe(
            (data: SurveyResponseData) => this.surveyResponseData = data
          );
      }
    );
  }

}
