import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { PlatformLocation } from '@angular/common';

@Component({
  selector: 'app-surveys',
  templateUrl: './surveys.component.html',
  styleUrls: ['./surveys.component.css']
})
export class SurveysComponent implements OnInit {

  isComposeSurvey = false;
  queryPath: string;
  queryParam: string;

  constructor(private router: Router,
    private route: ActivatedRoute,
    private location: PlatformLocation) { 
      this.location.onPopState(() => { this.isComposeSurvey = false;});
    }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.queryParam = params['topic'];
      this.queryPath = '/surveys?topic=' + encodeURI(params['topic']);
    });
    this.isComposeSurvey = false;
  }

  navigateToComposeSurvey() {
    this.isComposeSurvey = true;
    this.router.navigate(['compose-survey'], {relativeTo:this.route});
  }

}
