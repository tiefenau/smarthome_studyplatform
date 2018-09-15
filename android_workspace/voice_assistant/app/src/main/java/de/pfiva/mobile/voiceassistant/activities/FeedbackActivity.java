package de.pfiva.mobile.voiceassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.FeedbackType;
import de.pfiva.data.model.notification.FeedbackData;
import de.pfiva.mobile.voiceassistant.Constants;
import de.pfiva.mobile.voiceassistant.R;
import de.pfiva.mobile.voiceassistant.network.RetrofitClientInstance;
import de.pfiva.mobile.voiceassistant.web.NLUDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    private TextView userFeedbackQuery;
    private LinearLayout feedbackQueryContainer;
    private LinearLayout intentFeedbackButtonBar;
    private LinearLayout intentFeedbackContainer;
    private Button feedbackResponseYes;
    private Button feedbackResponseOther;
    private EditText feedbackInput;
    private Button feedbackSubmit;

    private FeedbackData feedbackData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackQueryContainer = (LinearLayout) findViewById(R.id.feedback_query_container);
        userFeedbackQuery = (TextView) findViewById(R.id.feedback_query);

        // Intent Feedback components
        intentFeedbackButtonBar = (LinearLayout) findViewById(R.id.intent_feedback_button_bar);
        feedbackResponseYes = (Button) findViewById(R.id.feedback_response_yes);

        feedbackResponseYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFeedbackResponse(Constants.YES_RESPONSE);
            }
        });

        feedbackResponseOther = (Button) findViewById(R.id.feedback_response_other);

        // General Feedback components
        intentFeedbackContainer = (LinearLayout) findViewById(R.id.intent_feedback_container);
        feedbackInput = (EditText) findViewById(R.id.feedback_input);
        feedbackSubmit = (Button) findViewById(R.id.feedback_submit);

        feedbackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = feedbackInput.getText().toString();
                if(response != null && !response.trim().isEmpty()) {
                    // Empty spaces should not be saved
                    saveFeedbackResponse(response);
                } else {
                    Toast toast = Toast.makeText(FeedbackActivity.this,
                            "Response cannot be empty string", Toast.LENGTH_LONG);
                    toast.show();
                    feedbackInput.getText().clear();
                }
            }
        });

        final Intent intent = getIntent();

        if(intent.hasExtra(Constants.FEEDBACK_DATA_KEY)) {
            feedbackData = (FeedbackData) intent.getSerializableExtra(Constants.FEEDBACK_DATA_KEY);
            if(feedbackData != null) {
                userFeedbackQuery.setText(feedbackData.getText());
                if(feedbackData.getFeedbackType() == FeedbackType.GENERAL) {
                    createScreenForGeneralFeedback();
                } else {
                    createScreenForIntentFeedback();
                }
            }
        }
    }

    private void saveFeedbackResponse(String feedbackResponse) {
        NLUDataService nluDataService = getNLUDataServiceInstance();

        Feedback feedback = new Feedback();
        feedback.setId(feedbackData.getFeedbackId());
        feedback.setFeedbackQuery(feedbackData.getText());
        feedback.setUserResponse(feedbackResponse);
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
        feedback.setTimestamp(formatter.format(new Date()));

        Call<Boolean> feedbackResponseCall = nluDataService.saveFeedbackResponse(feedback);
        feedbackResponseCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Toast toast;
                if(response.body().booleanValue()) {
                    toast = Toast.makeText(FeedbackActivity.this,
                            "Feedback response saved successfully.", Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(FeedbackActivity.this,
                            "Unable to save feedback response.", Toast.LENGTH_SHORT);
                }
                toast.show();

                Intent feedbackIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(feedbackIntent);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast toast = Toast.makeText(FeedbackActivity.this,
                        "Unable to save feedback response.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void createScreenForGeneralFeedback() {
        feedbackQueryContainer.setVisibility(View.VISIBLE);
        intentFeedbackButtonBar.setVisibility(View.GONE);
        intentFeedbackContainer.setVisibility(View.VISIBLE);
    }

    private void createScreenForIntentFeedback() {
        feedbackQueryContainer.setVisibility(View.VISIBLE);
        intentFeedbackButtonBar.setVisibility(View.VISIBLE);
        intentFeedbackContainer.setVisibility(View.INVISIBLE);

        feedbackResponseOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentFeedbackContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    private NLUDataService getNLUDataServiceInstance() {
        return RetrofitClientInstance.getRetrofitInstance().create(NLUDataService.class);
    }

}
