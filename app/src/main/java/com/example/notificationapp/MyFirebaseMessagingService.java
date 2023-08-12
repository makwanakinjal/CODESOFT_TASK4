package com.example.notificationapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService  extends FirebaseMessagingService {

    private static final String channelId = "notification_channel";
    private static final String channelName = "com.example.notificationapp";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            generateNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private RemoteViews getRemoteView(String title, String message) {
        RemoteViews remoteView = new RemoteViews("com.example.windowpushnotification", R.layout.notification);
        remoteView.setTextViewText(R.id.title, title);
        remoteView.setTextViewText(R.id.message, message);
        remoteView.setImageViewResource(R.id.logo, R.drawable.chat);
        return remoteView;
    }

    private void generateNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.chat)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000}) // Vibrate time
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        builder.setContent(getRemoteView(title, message));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }
}
