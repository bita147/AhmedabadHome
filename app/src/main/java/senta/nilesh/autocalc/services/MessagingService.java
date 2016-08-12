package senta.nilesh.autocalc.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import senta.nilesh.autocalc.R;
import senta.nilesh.autocalc.activity.LoginActivity;

/**
 * Created by Nilesh Senta on 26-05-2016.
 */
public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Map data = message.getData();
        showNotification(data);
    }

    private void showNotification(Map data) {
        Intent myIntent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // Must Required
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(data.get("payBy") + " buy for you")
                .setContentText(data.get("itemDesc") + " of Rs. " + data.get("amt"))
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE) // For Heads-up Notification vibrate require API > 21 & setPriority Max or High
                .setSubText("Sender - " + data.get("userName"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        android.app.NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mgr.notify(1, builder.build());
    }
}
