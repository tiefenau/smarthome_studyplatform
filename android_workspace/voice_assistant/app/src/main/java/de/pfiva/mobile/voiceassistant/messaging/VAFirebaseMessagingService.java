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

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import de.pfiva.data.model.FeedbackType;
import de.pfiva.data.model.notification.FeedbackData;
import de.pfiva.mobile.voiceassistant.Constants;
import de.pfiva.mobile.voiceassistant.R;
import de.pfiva.mobile.voiceassistant.activities.FeedbackActivity;

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
            Log.i(TAG, "Message data payload: " + data);

            FeedbackData feedbackData = new FeedbackData();
            feedbackData.setFeedbackId(Integer.valueOf(data.get("feedbackId")));
            feedbackData.setFeedbackType(FeedbackType.valueOf(data.get("feedbackType")));
            feedbackData.setText(data.get("text"));

            sendNotification(feedbackData);
        }

        //if(remoteMessage.getNotification() != null) {
          //  Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
           // sendNotification(remoteMessage.getNotification().getBody());
        //}
    }

    private void sendNotification(FeedbackData feedbackData) {
        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.FEEDBACK_DATA_KEY, feedbackData);
        //intent.putExtra(Intent.EXTRA_TEXT, messageBody);

        PendingIntent pendingIntent = PendingIntent
                .getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_name);
        notificationBuilder.setContentTitle("PFIVA");
        notificationBuilder.setContentText(feedbackData.getText());
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
}
