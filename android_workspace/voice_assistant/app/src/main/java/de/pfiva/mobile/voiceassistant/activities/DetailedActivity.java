package de.pfiva.mobile.voiceassistant.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.NLUData;
import de.pfiva.data.model.snips.SnipsOutput;
import de.pfiva.mobile.voiceassistant.Constants;
import de.pfiva.mobile.voiceassistant.R;

public class DetailedActivity extends AppCompatActivity {

    private TextView detailedActUserQuery;
    private TextView detailedActHotword;
    private TextView detailedActTimestamp;
    private TextView detailedActIntent;
    private TextView detailedActFeedback;
    private TextView detailedActUserResponse;
    private TextView detailedActFeedbackTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        detailedActUserQuery = (TextView) findViewById(R.id.detailedAct_user_query);
        detailedActHotword = (TextView) findViewById(R.id.detailedAct_hotword);
        detailedActTimestamp = (TextView) findViewById(R.id.detailedAct_timestamp);
        detailedActIntent = (TextView) findViewById(R.id.detailedAct_intent);
        detailedActFeedback = (TextView) findViewById(R.id.detailedAct_feedback);
        detailedActUserResponse = (TextView) findViewById(R.id.detailedAct_user_response);
        detailedActFeedbackTimestamp = (TextView) findViewById(R.id.detailedAct_feedback_timestamp);

        final Intent intent = getIntent();

        if(intent.hasExtra(Constants.NLUDATA_KEY)) {
            final NLUData nluData = (NLUData) intent.getSerializableExtra(Constants.NLUDATA_KEY);
            final SnipsOutput snipsOutput = nluData.getSnipsOutput();
            if(snipsOutput != null) {
                detailedActUserQuery.setText(snipsOutput.getInput());
                if(snipsOutput.getHotword() != null && !snipsOutput.getHotword().isEmpty()) {
                    detailedActHotword.setText(snipsOutput.getHotword());
                }
                detailedActTimestamp.setText(snipsOutput.getTimestamp());
                final de.pfiva.data.model.snips.Intent snipsIntent = snipsOutput.getIntent();
                if(snipsIntent != null) {
                    if(snipsIntent.getIntentName() != null && !snipsIntent.getIntentName().isEmpty()) {
                        detailedActIntent.setText(snipsIntent.getIntentName());
                    }
                }
            }
            final Feedback feedback = nluData.getFeedback();
            if(feedback != null) {
                if(feedback.getFeedbackQuery() != null && !feedback.getFeedbackQuery().isEmpty()) {
                    detailedActFeedback.setText(feedback.getFeedbackQuery());
                }
                if(feedback.getUserResponse() != null && !feedback.getUserResponse().isEmpty()) {
                    detailedActUserResponse.setText(feedback.getUserResponse());
                } else {
                    detailedActUserResponse.setTextColor(Color.RED);
                    detailedActUserResponse.setText(R.string.not_available);
                }
                if(feedback.getTimestamp() != null && !feedback.getTimestamp().isEmpty()) {
                    detailedActFeedbackTimestamp.setText(feedback.getTimestamp());
                }
            }
        }
    }
}
