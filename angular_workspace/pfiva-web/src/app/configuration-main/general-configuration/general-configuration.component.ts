import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-general-configuration',
  templateUrl: './general-configuration.component.html',
  styleUrls: ['./general-configuration.component.css']
})
export class GeneralConfigurationComponent implements OnInit {
  isFeedbackQuery: boolean = false;
  isServerKey: boolean = false;
  constructor() { }

  ngOnInit() {
  }

  toggleQueriesHotword() {
    console.log("hotword toggle")
  }

  toggleFeedbackQuery() {
    this.isFeedbackQuery = !this.isFeedbackQuery;
  }

  toggleServerKey() {
    this.isServerKey = !this.isServerKey;
  }

}
