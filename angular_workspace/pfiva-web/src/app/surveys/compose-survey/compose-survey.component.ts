import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { User } from '../../data-model/User';
import { SurveyService } from '../survey.service';

@Component({
  selector: 'app-compose-survey',
  templateUrl: './compose-survey.component.html',
  styleUrls: ['./compose-survey.component.css']
})
export class ComposeSurveyComponent implements OnInit {

  surveyQuestionType = 'Text';
  deliveryDateTime = 'Send Now';
  //questionTemplateValid = false;
  rowCount: number[] = [];
  users: User[];

  @ViewChild('questionInput') questionInput: ElementRef;
  @ViewChild('optionsTable') optionsTable: ElementRef;
  
  constructor(private surveyService: SurveyService) { }

  ngOnInit() {
    this.rowCount = [1];
    this.fetchUsers();
  }

  onSubmit() {

  }

  onQuestionTypeChange() {
    if(this.surveyQuestionType === 'Text') {
      this.rowCount = [1];
    }
  }

  addNewOptionRow() {
    let id: number = this.rowCount[this.rowCount.length-1] + 1;
    this.rowCount.push(id);
  }

  deleteNewOptionRow(value) {
    var index = this.rowCount.indexOf(value);
    this.rowCount.splice(index, 1);
  }

  addQuestion() {
    console.log(this.questionInput.nativeElement.value);
    console.log(this.surveyQuestionType);

    if(this.surveyQuestionType !== 'Text') {
      let tableRows:HTMLCollection[] =  this.optionsTable.nativeElement.rows;
      for(var _i = 0; _i < tableRows.length; _i++) {
        console.log(this.optionsTable.nativeElement.rows
          .item(_i).children.item(1).children.item(0).value);
      }
    }

    // reset fields
    this.questionInput.nativeElement.value = '';
    this.surveyQuestionType = 'Text';
  }

  onQuestionInputChange() {
    //this.validateQuestion();
  }

  /*validateQuestion() {
    let status: boolean = false;
    if(this.surveyQuestionType === 'Text') {
      if(this.questionInput.nativeElement.value.length == 0) {
        status = false;
      } else {
        status = true;
      }
    } else if(this.surveyQuestionType === 'Multiple Choice' 
      || this.surveyQuestionType === 'Checkboxes') {
        console.log(this.optionsTable.nativeElement.tbody.tr)
      }
    this.questionTemplateValid = status;
  }*/

  private fetchUsers() {
    this.surveyService.getPfivaUsers()
      .subscribe(
        (users: User[]) => this.users = users,
        (error) => console.log(error)
      );
  }

}
