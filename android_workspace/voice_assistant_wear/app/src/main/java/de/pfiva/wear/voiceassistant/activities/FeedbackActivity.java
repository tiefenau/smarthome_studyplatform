package de.pfiva.wear.voiceassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.notification.Data;
import de.pfiva.wear.voiceassistant.Constants;
import de.pfiva.wear.voiceassistant.R;
import de.pfiva.wear.voiceassistant.network.RetrofitClientInstance;
import de.pfiva.wear.voiceassistant.service.NLUDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends WearableActivity {

    private TextView feedbackQuery;
    private EditText feedbackUserResponse;
    private ImageButton feedbackSend;

    private Data feedbackData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackQuery = (TextView) findViewById(R.id.feedback_query);
        feedbackUserResponse = (EditText) findViewById(R.id.feedback_user_response);

        feedbackSend = (ImageButton) findViewById(R.id.feedback_send);

        Intent intent = getIntent();
        if(intent.hasExtra(Constants.FEEDBACK_DATA_KEY)) {
            feedbackData = (Data) intent.getSerializableExtra(Constants.FEEDBACK_DATA_KEY);
            if(feedbackData != null) {
                feedbackQuery.setText(feedbackData.getText());
            }
        }

        feedbackSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = feedbackUserResponse.getText().toString();
                if(response != null && !response.trim().isEmpty()) {
                    saveFeedbackResponse(response);
                } else {
                    Toast toast = Toast.makeText(FeedbackActivity.this,
                            "Response cannot be empty string", Toast.LENGTH_SHORT);
                    toast.show();
                    feedbackUserResponse.getText().clear();
                }
            }
        });
    }

    private void saveFeedbackResponse(String feedbackResponse) {
        NLUDataService nluDataService = RetrofitClientInstance.getRetrofitInstance()
                .create(NLUDataService.class);

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

                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivityIntent);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast toast = Toast.makeText(FeedbackActivity.this,
                        "Unable to save feedback response.", Toast.LENGTH_SHORT);
                toast.show();

                Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivityIntent);
            }
        });
    }
}
