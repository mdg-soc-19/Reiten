package com.example.reiten.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.reiten.Common.Common;
import com.example.reiten.CustomerCall;
import com.example.reiten.R;
import com.example.reiten.State;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        ///
        if(State.symbol.equals("D")){
        LatLng customer_location = new Gson().fromJson(remoteMessage.getNotification().getBody(),LatLng.class);

        Intent intent = new Intent(getBaseContext(), CustomerCall.class);
        intent.putExtra("lat",customer_location.latitude);
        intent.putExtra("lng",customer_location.longitude);
        intent.putExtra("customer",remoteMessage.getNotification().getTitle());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
        else if(State.symbol.equals("C"))
        { final RemoteMessage remoteMessage1=remoteMessage;
            if (remoteMessage.getNotification().getTitle().equals("Cancel")) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyFirebaseMessaging.this, "" + remoteMessage1.getNotification().getBody(), Toast.LENGTH_SHORT).show();
                    }
                });
                LocalBroadcastManager.getInstance(MyFirebaseMessaging.this).sendBroadcast(new Intent(Common.CANCEL_BROADCAST_STRING));
            }
            if (remoteMessage.getNotification().getTitle().equals("Accept")) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyFirebaseMessaging.this, "" + remoteMessage1.getNotification().getBody(), Toast.LENGTH_SHORT).show();
                    }
                });}else if (remoteMessage.getNotification().getTitle().equals("Arrived")) {
                showArrivedNotification(remoteMessage.getNotification().getBody());
            }
        }
    }
    private void showArrivedNotification(String body) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    "channel1",
                    "Arrived",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Notice");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            Notification notification = new NotificationCompat.Builder(this, "channel1")
                    .setSmallIcon(R.drawable.profile)
                    .setContentTitle("Arrived")
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .build();
            NotificationManagerCompat notificationManager;
            notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, notification);
        }
    }
}
