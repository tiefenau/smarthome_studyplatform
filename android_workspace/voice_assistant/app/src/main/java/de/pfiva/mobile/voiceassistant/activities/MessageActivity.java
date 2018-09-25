package de.pfiva.mobile.voiceassistant.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import de.pfiva.data.model.User;
import de.pfiva.data.model.message.Response;
import de.pfiva.data.model.notification.MessageData;
import de.pfiva.mobile.voiceassistant.Constants;
import de.pfiva.mobile.voiceassistant.R;
import de.pfiva.mobile.voiceassistant.network.RetrofitClientInstance;
import de.pfiva.mobile.voiceassistant.web.NLUDataService;
import retrofit2.Call;
import retrofit2.Callback;

public class MessageActivity extends AppCompatActivity {

    private TextView messageQuery;
    private EditText messageResponseInput;
    private Button messageSubmit;

    private MessageData messageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageQuery = (TextView) findViewById(R.id.message_query);
        messageResponseInput = (EditText) findViewById(R.id.message_response_input);

        messageSubmit = (Button) findViewById(R.id.message_submit);
        messageSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = messageResponseInput.getText().toString();
                if(response != null && !response.trim().isEmpty()) {
                    // Empty spaces should not be saved
                    saveMessageResponse(response);
                } else {
                    Toast toast = Toast.makeText(MessageActivity.this,
                            "Response cannot be empty string", Toast.LENGTH_LONG);
                    toast.show();
                    messageResponseInput.getText().clear();
                }
            }
        });

        final Intent intent = getIntent();
        if(intent.hasExtra(Constants.MESSAGE_DATA_KEY)) {
            messageData = (MessageData) intent.getSerializableExtra(Constants.MESSAGE_DATA_KEY);
            if(messageData != null) {
                messageQuery.setText(messageData.getMessageText());
            }
        }
    }

    private void saveMessageResponse(String messageResponse) {
        NLUDataService nluDataService = getNLUDataServiceInstance();
        Response response = new Response();
        response.setValue(messageResponse);
        User user = new User();
        user.setId(messageData.getUserId());
        response.setUser(user);
        Call<Boolean> messageResponseCall = nluDataService
                .saveMessageResponse(messageData.getMessageId(), response);
        messageResponseCall.enqueue(new Callback<Boolean>() {
            Toast toast;
            @Override
            public void onResponse(Call<Boolean> call, retrofit2.Response<Boolean> response) {
                if(response.body().booleanValue()) {
                    toast = Toast.makeText(MessageActivity.this,
                            "Message response saved successfully.", Toast.LENGTH_SHORT);
                } else {
                    toast = Toast.makeText(MessageActivity.this,
                            "Unable to save message response.", Toast.LENGTH_SHORT);
                }
                toast.show();

                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainIntent);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast toast = Toast.makeText(MessageActivity.this,
                        "Unable to save message response.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private NLUDataService getNLUDataServiceInstance() {
        return RetrofitClientInstance.getRetrofitInstance().create(NLUDataService.class);
    }
}
