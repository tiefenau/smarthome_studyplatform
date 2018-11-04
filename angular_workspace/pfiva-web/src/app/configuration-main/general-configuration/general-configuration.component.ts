import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ConfigurationService } from '../configuration.service';
import { PfivaConfigData } from '../../data-model/PfivaConfigData';

@Component({
  selector: 'app-general-configuration',
  templateUrl: './general-configuration.component.html',
  styleUrls: ['./general-configuration.component.css']
})
export class GeneralConfigurationComponent implements OnInit {
  isFeedbackQuery: boolean = false;
  isServerKey: boolean = false;
  isInstantFeedback: boolean = false;
  isQueriesWithoutHotword: boolean = false;
  defaultFeedbackQuery: string = "";
  firebaseServerKey: string = "";

  @ViewChild('defaultFeedbackQueryInput') defaultFeedbackQueryInput: ElementRef;
  @ViewChild('serverKeyInput') serverKeyInput: ElementRef;
  @ViewChild('instantFeedbackInput') instantFeedbackInput: ElementRef;
  @ViewChild('queriesWithoutHotword') queriesWithoutHotword: ElementRef; 

  constructor(private configurationService: ConfigurationService) { }

  ngOnInit() {
    this.fetchConfigurationData();
  }

  private fetchConfigurationData() {
    this.configurationService.getPfivaConfigData().subscribe(
      (data: PfivaConfigData[]) => {
        for(let entry of data) {
          let confidData = Object.assign(new PfivaConfigData(), entry);
          if(confidData.Key === 'pfiva_instant_feedback') {
            this.isInstantFeedback = this.convertToBoolean(confidData.Value);
          } else if(confidData.Key === 'pfiva_default_feedback_query') {
            this.defaultFeedbackQuery = confidData.Value;
          } else if(confidData.Key === 'pfiva_firebase_server_key') {
            this.firebaseServerKey = confidData.Value;
          } else if(confidData.Key === 'pfiva_capture_queries_without_hotword') {
            this.isQueriesWithoutHotword = this.convertToBoolean(confidData.Value);
          } else {
            console.log('Invalid key for configuration data.')
          }
        }
      }
    );
  }

  private convertToBoolean(input: string): boolean | undefined {
    try {
      return JSON.parse(input);
    }
    catch (e) {
      return undefined;
    }
  }

  toggleQueriesHotword() {
    this.configurationService.saveConfigData('pfiva_capture_queries_without_hotword', 
      this.queriesWithoutHotword.nativeElement.checked);
  }

  toggleFeedbackQuery() {
    this.isFeedbackQuery = !this.isFeedbackQuery;
    if(!this.isFeedbackQuery) {
      this.configurationService.saveConfigData('pfiva_default_feedback_query',
        this.defaultFeedbackQueryInput.nativeElement.value);
    }
  }

  toggleServerKey() {
    this.isServerKey = !this.isServerKey;
    if(!this.isServerKey) {
      this.configurationService.saveConfigData('pfiva_firebase_server_key', 
        this.serverKeyInput.nativeElement.value);
    }
  }

  toggleInstantFeedback() {
    this.configurationService.saveConfigData('pfiva_instant_feedback', 
      this.instantFeedbackInput.nativeElement.checked);
  }
}
