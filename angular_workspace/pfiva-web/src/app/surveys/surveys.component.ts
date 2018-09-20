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

  constructor(private router: Router,
    private route: ActivatedRoute,
    private location: PlatformLocation) { 
      location.onPopState(() => { this.isComposeSurvey = false;});
    }

  ngOnInit() {
    this.isComposeSurvey = false;
  }

  navigateToComposeSurvey() {
    this.isComposeSurvey = true;
    this.router.navigate(['compose-survey'], {relativeTo:this.route});
  }

}
