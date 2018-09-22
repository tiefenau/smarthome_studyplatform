import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { User } from '../../data-model/User';
import { SurveyService } from '../survey.service';
import { NgForm } from '@angular/forms';
import { Survey } from '../../data-model/survey/Survey';
import { Question } from '../../data-model/survey/Question';
import { Option } from '../../data-model/survey/Option';

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
  questions: Question[] = [];

  @ViewChild('questionInput') questionInput: ElementRef;
  @ViewChild('optionsTable') optionsTable: ElementRef;
  @ViewChild('surveyData') composeSurveyForm: NgForm;
  
  constructor(private surveyService: SurveyService) { }

  ngOnInit() {
    this.rowCount = [1];
    this.questions = [];
    this.fetchUsers();
  }

  onSubmit() {
    let survey = new Survey();
    survey.SurveyName = this.composeSurveyForm.form.value.surveyName;
    let deliveryDateValue = this.composeSurveyForm.form.value.deliveryDateTime;
    if(deliveryDateValue == 'Send Now') {
      survey.DeliveryDateTime = 'Now';
    } else {
      // TODO - formate date using pipe
      let date: string = this.composeSurveyForm.form.value.deliveryDate 
      + " " + this.composeSurveyForm.form.value.delvieryTime + ":00.000";
      survey.DeliveryDateTime = date;
    }
    let usersArray: string[] = this.composeSurveyForm.form.value.users;
    let intendedUsers: User[] = [];
    for(let element of usersArray) {
      for(let user of this.users) {
        let userObject = Object.assign(new User(), user);
        if(userObject.Username === element) {
          let newuser = new User();
          newuser.Id = userObject.Id;
          newuser.Username = userObject.Username;
          newuser.DeviceId = userObject.DeviceId;
          intendedUsers.push(newuser);
        }
      }
    }
    survey.Users = intendedUsers;
    survey.Questions = this.questions;
    
    this.surveyService.sendSurvey(survey);

    // Reset form
    this.composeSurveyForm.reset();
    this.composeSurveyForm.form.patchValue({ 'deliveryDateTime':'Send Now' });
    this.questions = [];
    this.composeSurveyForm.form.patchValue({ 'surveyQuestionType':'Text' });
    this.questionInput.nativeElement.value = '';
    this.error = '';
    this.questionTemplateValid = true;
    this.rowCount = [1];
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
    let question = new Question();
    question.Question = this.questionInput.nativeElement.value;
    question.QuestionType = this.surveyQuestionType;
    
    if(this.surveyQuestionType !== 'Text') {
      let options: Option[] = [];
      let tableRows:HTMLCollection[] =  this.optionsTable.nativeElement.rows;
      for(var _i = 0; _i < tableRows.length; _i++) {
        let option = new Option();
        option.Value = this.optionsTable.nativeElement.rows
        .item(_i).children.item(1).children.item(0).value; 
        options.push(option);
      }
      question.Options = options;
    }
    
    // Before pushing in array validate if question contents are valid
    this.questionTemplateValid = this.validateQuestionContent(question);
    if(this.questionTemplateValid) {
      this.questions.push(question);
      // reset fields
      this.questionInput.nativeElement.value = '';
      this.surveyQuestionType = 'Text';
      this.rowCount = [1];
    }
  }

  validateQuestionContent(question: Question) {
    if(question.Question.length == 0) {
      this.error = 'Question cannot be empty.';
      return false;
    }
    if(question.QuestionType == null) {
      this.error = 'Question Type cannot be empty.';
      return false;
    }
    if(question.QuestionType !== 'Text') {
      for(let entry of question.Options) {
        let option = Object.assign(new Option(), entry);
        if(option.Value === '') {
          this.error = 'Option cannot be empty. Remove empty option.';
          return false;
        }
      }
    }
    return true;
  }

  deleteQuestion(entry: Question) {
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
