package com.example.showtime.app.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import com.example.showtime.app.MovieDetailFragment;
import com.example.showtime.app.R;


public class NotifyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Intent intent = new Intent(this, NotifyService.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification n = new Notification.Builder(this)
                .setContentTitle("Movie: " + MovieDetailFragment.myMovie.getTitle() + ", will be released Today")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pIntent).build();

        MovieDetailFragment.myMovie.getTitle();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }
}
