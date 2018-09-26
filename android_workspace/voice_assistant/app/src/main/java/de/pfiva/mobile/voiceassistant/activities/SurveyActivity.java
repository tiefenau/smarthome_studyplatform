package de.pfiva.mobile.voiceassistant.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import de.pfiva.data.model.notification.SurveyData;
import de.pfiva.data.model.survey.Option;
import de.pfiva.data.model.survey.Question;
import de.pfiva.data.model.survey.Survey;
import de.pfiva.mobile.voiceassistant.Constants;
import de.pfiva.mobile.voiceassistant.R;

public class SurveyActivity extends AppCompatActivity {

    private Button startSurveyButton;
    private ImageButton nextQuestion;
    private ImageButton previousQuestion;
    private LinearLayout instructionsContainer;
    private LinearLayout questionsContainer;
    private LinearLayout textOptionsContainer;
    private LinearLayout mcOptionsContainer;
    private RadioGroup radiogroup;
    private LinearLayout checkboxOptionContainer;
    private TextView questionNumber;
    private TextView surveyQuestion;
    //private SurveyData surveyData;
    private List<Question> questions;
    private int questionCounter = 0; // because list index starts from 0
    private int questionsSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        final SurveyData surveyData = populateSurveyData();
        questions = surveyData.getSurvey().getQuestions();
        questionsSize = surveyData.getSurvey().getQuestions().size();

        instructionsContainer = (LinearLayout) findViewById(R.id.instructions_container);

        questionsContainer = (LinearLayout) findViewById(R.id.questions_container);
        questionsContainer.setVisibility(View.GONE);

        surveyQuestion = (TextView) findViewById(R.id.survey_question);
        surveyQuestion.setVisibility(View.GONE);

        textOptionsContainer = (LinearLayout) findViewById(R.id.text_options_container);
        textOptionsContainer.setVisibility(View.GONE);

        mcOptionsContainer = (LinearLayout) findViewById(R.id.mc_options_container);
        mcOptionsContainer.setVisibility(View.GONE);

        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        //radiogroup.setVisibility(View.GONE);

        checkboxOptionContainer = (LinearLayout) findViewById(R.id.checkbox_option_container);
        checkboxOptionContainer.setVisibility(View.GONE);

        nextQuestion = (ImageButton) findViewById(R.id.next_question);
        nextQuestion.setVisibility(View.GONE);

        questionNumber = (TextView) findViewById(R.id.question_number);
        questionNumber.setVisibility(View.GONE);

        previousQuestion = (ImageButton) findViewById(R.id.previous_question);
        previousQuestion.setVisibility(View.GONE);
        previousQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionCounter >= 0) {
                    questionNumber.setText(String.valueOf(questionCounter));
                    final Question question = questions.get(questionCounter - 1);
                    surveyQuestion.setText(question.getQuestion());
                    displayOptions(question.getQuestionType(), question.getOptions());
                    questionCounter = questionCounter - 1;
                    if(questionCounter == 0) {
                        previousQuestion.setVisibility(View.GONE);
                    }
                }
            }
        });

        startSurveyButton = (Button) findViewById(R.id.start_survey);

        startSurveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSurvey(surveyData);
            }
        });

        /*final Intent intent = getIntent();
        if(intent.hasExtra(Constants.SURVEY_DATA_KEY)) {
            surveyData = (SurveyData) intent.getSerializableExtra(Constants.SURVEY_DATA_KEY);
            if(surveyData != null) {

            }
        }*/
    }

    private void startSurvey(SurveyData surveyData) {
        instructionsContainer.setVisibility(View.GONE);

        questionsContainer.setVisibility(View.VISIBLE);

        startSurveyButton.setVisibility(View.GONE);

        if(surveyData != null) {
            questionNumber.setVisibility(View.VISIBLE);
            questionNumber.setText(String.valueOf(questionCounter + 1));

            surveyQuestion.setVisibility(View.VISIBLE);
            final Question question = questions.get(questionCounter);
            surveyQuestion.setText(question.getQuestion());
            displayOptions(question.getQuestionType(), question.getOptions());

            nextQuestion.setVisibility(View.VISIBLE);
            nextQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousQuestion.setVisibility(View.VISIBLE);
                    if(questionCounter < questionsSize) {
                        if(questionCounter == (questionsSize - 1)) {
                            // last question reached, display confirmation screen
                        } else {
                            questionCounter = questionCounter + 1;
                            questionNumber.setText(String.valueOf(questionCounter + 1));
                            final Question question = questions.get(questionCounter);
                            surveyQuestion.setText(question.getQuestion());
                            displayOptions(question.getQuestionType(), question.getOptions());
                        }
                    }
                }
            });
        }
    }

    private void displayOptions(String questionType, List<Option> options) {
        if(questionType.equals("Text")) {
            textOptionsContainer.setVisibility(View.VISIBLE);
            mcOptionsContainer.setVisibility(View.GONE);
            checkboxOptionContainer.setVisibility(View.GONE);
        } else if(questionType.equals("Multiple Choice")) {
            mcOptionsContainer.setVisibility(View.VISIBLE);
            textOptionsContainer.setVisibility(View.GONE);
            checkboxOptionContainer.setVisibility(View.GONE);
            if(radiogroup.getChildCount() == 0) {
                for(Option option : options) {
                    RadioButton radioButton = new RadioButton(SurveyActivity.this);
                    radioButton.setText(option.getValue());
                    radiogroup.addView(radioButton);
                }
            }
        } else if(questionType.equals("Checkboxes")) {
            checkboxOptionContainer.setVisibility(View.VISIBLE);
            textOptionsContainer.setVisibility(View.GONE);
            mcOptionsContainer.setVisibility(View.GONE);
            if(checkboxOptionContainer.getChildCount() == 0) {
                for(Option option : options) {
                    CheckBox checkBox = new CheckBox(SurveyActivity.this);
                    checkBox.setText(option.getValue());
                    checkboxOptionContainer.addView(checkBox);
                }
            }
        }
    }

    private SurveyData populateSurveyData() {
        SurveyData data = new SurveyData();
        Survey survey = new Survey();
        survey.setId(1);
        survey.setSurveyName("Dummy Survey");

        List<Question> questions = new LinkedList<>();
        Question question1 = new Question();
        question1.setId(1);
        question1.setQuestion("This is text question");
        question1.setQuestionType("Text");

        Question question2 = new Question();
        question2.setId(2);
        question2.setQuestion("This is mcq question");
        question2.setQuestionType("Multiple Choice");
        List<Option> options2 = new LinkedList<>();
        Option option2_1 = new Option();
        option2_1.setId(1);
        option2_1.setValue("Yes");
        options2.add(option2_1);
        Option option2_2 = new Option();
        option2_2.setId(2);
        option2_2.setValue("No");
        options2.add(option2_2);
        question2.setOptions(options2);

        Question question3 = new Question();
        question3.setId(3);
        question3.setQuestion("This is checkboxes");
        question3.setQuestionType("Checkboxes");
        List<Option> options3 = new LinkedList<>();
        Option option3_1 = new Option();
        option3_1.setId(1);
        option3_1.setValue("choice1");
        options3.add(option3_1);
        Option option3_2 = new Option();
        option3_2.setId(2);
        option3_2.setValue("choice2");
        options3.add(option3_2);
        question3.setOptions(options3);

        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        survey.setQuestions(questions);

        data.setSurvey(survey);
        return data;
    }
}
