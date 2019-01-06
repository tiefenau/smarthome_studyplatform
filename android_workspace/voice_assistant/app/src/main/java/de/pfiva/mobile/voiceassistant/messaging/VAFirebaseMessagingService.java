package de.pfiva.mobile.voiceassistant.messaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.Map;

import de.pfiva.data.model.FeedbackType;
import de.pfiva.data.model.notification.Data;
import de.pfiva.data.model.notification.FeedbackData;
import de.pfiva.data.model.notification.MessageData;
import de.pfiva.data.model.notification.SurveyData;
import de.pfiva.data.model.survey.Survey;
import de.pfiva.mobile.voiceassistant.Constants;
import de.pfiva.mobile.voiceassistant.R;
import de.pfiva.mobile.voiceassistant.activities.FeedbackActivity;
import de.pfiva.mobile.voiceassistant.activities.MessageActivity;
import de.pfiva.mobile.voiceassistant.activities.SurveyActivity;

public class VAFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "VAFirebaseMessagingS";
    private static final String CHANNEL_ID = "pfiva_notification_channel_01";
    private static final CharSequence CHANNEL_NAME = "pfiva_notification_channel";

    public VAFirebaseMessagingService() {}

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.i(TAG, "From: " + remoteMessage.getFrom());
        if(remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            Log.i(TAG, "Data payload: " + data);

            Data.DataType datatype = Data.DataType.valueOf(data.get("datatype"));
            String notificationTitle = null;
            if(datatype == Data.DataType.FEEDBACK) {
                FeedbackData feedbackData = new FeedbackData();
                feedbackData.setFeedbackId(Integer.valueOf(data.get("feedbackId")));
                feedbackData.setFeedbackType(FeedbackType.valueOf(data.get("feedbackType")));
                feedbackData.setText(data.get("text"));
                feedbackData.setUserQuery(data.get("userQuery"));
                notificationTitle = "PFIVA Feedback";

                Intent intent = processFeedbackData(feedbackData);
                sendNotification(notificationTitle, intent, feedbackData.getText());
            } else if(datatype == Data.DataType.MESSAGE) {
                MessageData messageData = new MessageData();
                messageData.setMessageId(Integer.valueOf(data.get("messageId")));
                messageData.setMessageText(data.get("messageText"));
                messageData.setUserId(Integer.valueOf(data.get("userId")));
                notificationTitle = "PFIVA Message";

                Intent intent = processMessageData(messageData);
                sendNotification(notificationTitle, intent, messageData.getMessageText());
            } else if(datatype == Data.DataType.SURVEY) {
                SurveyData surveyData = new SurveyData();
                surveyData.setSurvey(extractSurveyData(data.get("survey")));
                surveyData.setUserId(Integer.valueOf(data.get("userId")));
                notificationTitle = "PFIVA Survey";

                Intent intent = processSurveyData(surveyData);
                sendNotification(notificationTitle, intent, surveyData.getSurvey().getSurveyName());
            } else {
                Log.e(TAG, "Data not supported");
            }
        }
    }

    private Survey extractSurveyData(String surveyJSONString) {
        ObjectMapper mapper = new ObjectMapper();
        Survey survey = null;
        try {
            survey = mapper.readValue(surveyJSONString, Survey.class);
        } catch (IOException e) {
            Log.e(TAG, "Error parsing survey", e);
        }

        return survey;
    }

    private void sendNotification(String notificationTitle, Intent intent, String contextText) {
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_name);
        notificationBuilder.setContentTitle(notificationTitle);
        notificationBuilder.setContentText(contextText);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    private Intent processFeedbackData(FeedbackData feedbackData) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.FEEDBACK_DATA_KEY, feedbackData);
        return intent;
    }

    private Intent processMessageData(MessageData messageData) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.MESSAGE_DATA_KEY, messageData);
        return intent;
    }

    private Intent processSurveyData(SurveyData surveyData) {
        Intent intent = new Intent(this, SurveyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.SURVEY_DATA_KEY, surveyData);
        return intent;
    }
}
