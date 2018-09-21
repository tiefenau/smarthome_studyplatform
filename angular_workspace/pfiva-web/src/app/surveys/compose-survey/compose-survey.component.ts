import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { User } from '../../data-model/User';
import { SurveyService } from '../survey.service';
import { SurveyQuestion } from '../../data-model/SurveyQuestion';

@Component({
  selector: 'app-compose-survey',
  templateUrl: './compose-survey.component.html',
  styleUrls: ['./compose-survey.component.css']
})
export class ComposeSurveyComponent implements OnInit {

  surveyQuestionType = 'Text';
  deliveryDateTime = 'Send Now';
  questionTemplateValid: boolean = true;
  error: string = '';
  rowCount: number[] = [];
  users: User[];
  questions: SurveyQuestion[] = [];

  @ViewChild('questionInput') questionInput: ElementRef;
  @ViewChild('optionsTable') optionsTable: ElementRef;
  
  constructor(private surveyService: SurveyService) { }

  ngOnInit() {
    this.rowCount = [1];
    this.questions = [];
    this.fetchUsers();
  }

  onSubmit() {
  }

  onQuestionTypeChange() {
    if(this.surveyQuestionType === 'Text') {
      this.rowCount = [1];
      this.questionTemplateValid = true;
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
    let surveyQuestion = new SurveyQuestion();
    surveyQuestion.Question = this.questionInput.nativeElement.value;
    surveyQuestion.QuestionType = this.surveyQuestionType;
    
    if(this.surveyQuestionType !== 'Text') {
      let options: string[] = [];
      let tableRows:HTMLCollection[] =  this.optionsTable.nativeElement.rows;
      for(var _i = 0; _i < tableRows.length; _i++) {
        options.push(this.optionsTable.nativeElement.rows
          .item(_i).children.item(1).children.item(0).value);
      }
      surveyQuestion.Options = options;
    }
    
    // Before pushing in array validate if question contents are valid
    this.questionTemplateValid = this.validateQuestionContent(surveyQuestion);
    if(this.questionTemplateValid) {
      this.questions.push(surveyQuestion);
      // reset fields
      this.questionInput.nativeElement.value = '';
      this.surveyQuestionType = 'Text';
      this.rowCount = [1];
    }
  }

  validateQuestionContent(surveyQuestion: SurveyQuestion) {
    if(surveyQuestion.Question.length == 0) {
      this.error = 'Question cannot be empty.';
      return false;
    }
    if(surveyQuestion.QuestionType === '') {
      this.error = 'Question Type cannot be empty.';
      return false;
    }
    if(surveyQuestion.QuestionType !== 'Text') {
      for(let entry in surveyQuestion.Options) {
        if(surveyQuestion.Options[entry] === '') {
          this.error = 'Option cannot be empty. Remove empty option.';
          return false;
        }
      }
    }
    return true;
  }

  deleteQuestion(entry: SurveyQuestion) {
    var index = this.questions.indexOf(entry);
    this.questions.splice(index, 1);
  }

  private fetchUsers() {
    this.surveyService.getPfivaUsers()
      .subscribe(
        (users: User[]) => this.users = users,
        (error) => console.log(error)
      );
  }

}
