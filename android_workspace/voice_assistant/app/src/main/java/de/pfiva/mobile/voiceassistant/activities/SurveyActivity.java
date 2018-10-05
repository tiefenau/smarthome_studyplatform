package de.pfiva.mobile.voiceassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.pfiva.data.model.User;
import de.pfiva.data.model.notification.SurveyData;
import de.pfiva.data.model.survey.Option;
import de.pfiva.data.model.survey.Question;
import de.pfiva.data.model.survey.Response;
import de.pfiva.data.model.survey.Survey;
import de.pfiva.mobile.voiceassistant.Constants;
import de.pfiva.mobile.voiceassistant.R;
import de.pfiva.mobile.voiceassistant.network.RetrofitClientInstance;
import de.pfiva.mobile.voiceassistant.web.NLUDataService;
import retrofit2.Call;
import retrofit2.Callback;

public class SurveyActivity extends AppCompatActivity {

    private Button startSurveyButton;
    private Button submitSurvey;
    private ImageButton nextQuestion;
    private ImageButton previousQuestion;
    private LinearLayout instructionsContainer;
    private LinearLayout questionsContainer;
    private LinearLayout textOptionsContainer;
    private LinearLayout mcOptionsContainer;
    private RadioGroup radiogroup;
    private LinearLayout checkboxOptionContainer;
    private LinearLayout confirmationContainer;
    private TextView questionNumber;
    private TextView totalQuestions;
    private TextView questionsSeparator;
    private TextView surveyQuestion;

    private SurveyData surveyData;
    private List<Question> questions;
    private Map<Integer, Response> responseMap = new HashMap<>();
    private int questionCounter = 0; // because list index starts from 0
    private int questionsSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        final Intent intent = getIntent();
        if(intent.hasExtra(Constants.SURVEY_DATA_KEY)) {
            SurveyData data = (SurveyData) intent.getSerializableExtra(Constants.SURVEY_DATA_KEY);
            if(data != null) {
                surveyData = data;
            }
        }

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

        checkboxOptionContainer = (LinearLayout) findViewById(R.id.checkbox_option_container);
        checkboxOptionContainer.setVisibility(View.GONE);

        confirmationContainer = (LinearLayout) findViewById(R.id.confirmation_container);
        confirmationContainer.setVisibility(View.GONE);

        nextQuestion = (ImageButton) findViewById(R.id.next_question);
        nextQuestion.setVisibility(View.GONE);

        questionNumber = (TextView) findViewById(R.id.question_number);
        questionNumber.setVisibility(View.GONE);

        questionsSeparator = (TextView) findViewById(R.id.questions_separator);
        questionsSeparator.setVisibility(View.GONE);

        totalQuestions = (TextView) findViewById(R.id.total_questions);
        totalQuestions.setVisibility(View.GONE);

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

        submitSurvey = (Button) findViewById(R.id.submit_survey);
        submitSurvey.setVisibility(View.GONE);
        submitSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSurveyResponse();
            }
        });
    }

    private void sendSurveyResponse() {
        NLUDataService nluDataService = getNLUDataServiceInstance();
        if(!responseMap.isEmpty()) {
            List<Response> responses = new LinkedList<>(responseMap.values());
            Call<Boolean> surveyResponseCall = nluDataService
                    .saveSurveyResponse(surveyData.getSurvey().getId(), responses);

            surveyResponseCall.enqueue(new Callback<Boolean>() {
                Toast toast;
                @Override
                public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                    if(response.body().booleanValue()) {
                        toast = Toast.makeText(SurveyActivity.this,
                                "Survey response saved successfully.", Toast.LENGTH_SHORT);
                    } else {
                        toast = Toast.makeText(SurveyActivity.this,
                                "Unable to save survey response.", Toast.LENGTH_SHORT);
                    }
                    toast.show();

                    Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainIntent);
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast toast = Toast.makeText(SurveyActivity.this,
                            "Unable to save survey response.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    private void startSurvey(SurveyData surveyData) {
        instructionsContainer.setVisibility(View.GONE);

        questionsContainer.setVisibility(View.VISIBLE);

        startSurveyButton.setVisibility(View.GONE);

        if(surveyData != null) {
            questionNumber.setVisibility(View.VISIBLE);
            questionNumber.setText(String.valueOf(questionCounter + 1));

            questionsSeparator.setVisibility(View.VISIBLE);
            totalQuestions.setVisibility(View.VISIBLE);
            totalQuestions.setText(String.valueOf(questionsSize));

            surveyQuestion.setVisibility(View.VISIBLE);
            final Question question = questions.get(questionCounter);
            surveyQuestion.setText(question.getQuestion());
            displayOptions(question.getQuestionType(), question.getOptions());

            nextQuestion.setVisibility(View.VISIBLE);
            nextQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousQuestion.setVisibility(View.VISIBLE);
                    // Save user response
                    // Clicking on next button increases the question counter,
                    // hence next question is displayed. But as we need to capture user
                    // response for current question, the call to captureUserResponse is made here
                    captureUserResponse(questions.get(questionCounter));
                    if(questionCounter < questionsSize) {
                        if(questionCounter == (questionsSize - 1)) {
                            // last question reached, display confirmation screen
                            displayConfirmationScreen();
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

    private void captureUserResponse(Question question) {
        if(question != null) {
            Response response = new Response();
            response.setQuestionId(question.getId());
            User user = new User();
            user.setId(surveyData.getUserId());
            response.setUser(user);
            List<String> values = new LinkedList<>();
            if(question.getQuestionType().equals("Text")) {
                EditText textbox = (EditText) textOptionsContainer.getChildAt(1);
                String value = textbox.getText().toString();
                values.add(value);
            } else if (question.getQuestionType().equals("Multiple Choice")) {
                int selectedId = radiogroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                String value = radioButton.getText().toString();
                values.add(value);
            } else if (question.getQuestionType().equals("Checkboxes")) {
                final int childCount = checkboxOptionContainer.getChildCount();
                for(int i = 0; i < childCount; i++) {
                    CheckBox checkBox = (CheckBox) checkboxOptionContainer.getChildAt(i);
                    if(checkBox.isChecked()) {
                        values.add(checkBox.getText().toString());
                    }
                }
            }
            response.setValues(values);
            responseMap.put(question.getId(), response);
        }
    }

    private void displayConfirmationScreen() {
        instructionsContainer.setVisibility(View.GONE);
        questionsContainer.setVisibility(View.GONE);
        textOptionsContainer.setVisibility(View.GONE);
        mcOptionsContainer.setVisibility(View.GONE);
        checkboxOptionContainer.setVisibility(View.GONE);

        startSurveyButton.setVisibility(View.GONE);
        questionNumber.setVisibility(View.GONE);
        questionsSeparator.setVisibility(View.GONE);
        totalQuestions.setVisibility(View.GONE);
        nextQuestion.setVisibility(View.GONE);
        previousQuestion.setVisibility(View.GONE);

        confirmationContainer.setVisibility(View.VISIBLE);
        submitSurvey.setVisibility(View.VISIBLE);
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

    private NLUDataService getNLUDataServiceInstance() {
        return RetrofitClientInstance.getRetrofitInstance().create(NLUDataService.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        responseMap.clear();
    }
}
