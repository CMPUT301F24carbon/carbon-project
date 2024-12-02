package com.example.carbon_project.View;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.carbon_project.Controller.MainActivity;
import com.example.carbon_project.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel")
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(android.R.drawable.ic_notification_overlay) // Replace with your notification icon
                    .setPriority(NotificationCompat.PRIORITY_HIGH) // Ensure the notification is high priority
                    .setAutoCancel(true)  // Automatically dismiss when tapped
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC); // Make notification visible to everyone

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // For Android O and above, create a notification channel if not already created
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        "notification_channel",
                        "Default Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                channel.setDescription("This is a default notification channel");
                notificationManager.createNotificationChannel(channel);
            }

            // Show the notification
            notificationManager.notify(0, builder.build());
        }
    }
}

