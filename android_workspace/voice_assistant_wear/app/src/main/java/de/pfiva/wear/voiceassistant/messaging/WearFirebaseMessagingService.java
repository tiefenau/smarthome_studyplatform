package de.pfiva.wear.voiceassistant.messaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import de.pfiva.data.model.FeedbackType;
import de.pfiva.data.model.notification.Data;
import de.pfiva.wear.voiceassistant.Constants;
import de.pfiva.wear.voiceassistant.R;
import de.pfiva.wear.voiceassistant.activities.FeedbackActivity;

public class WearFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "WearFirebaseMessaging";
    private static final String CHANNEL_ID = "pfiva_wear_notification_channel_01";
    private static final CharSequence CHANNEL_NAME = "pfiva_wear_notification_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.i(TAG, "From: " + remoteMessage.getFrom());
        if(remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            Log.i(TAG, "Message data payload: " + data);

            Data feedbackData = new Data();
            feedbackData.setFeedbackId(Integer.valueOf(data.get("feedbackId")));
            feedbackData.setFeedbackType(FeedbackType.valueOf(data.get("feedbackType")));
            feedbackData.setText(data.get("text"));

            sendNotification(feedbackData);
        }
    }

    private void sendNotification(Data feedbackData) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra(Constants.FEEDBACK_DATA_KEY, feedbackData);

        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_pfiva_notification);
        notificationBuilder.setContentTitle("PFIVA");
        notificationBuilder.setContentText(feedbackData.getText());
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

    }
}
