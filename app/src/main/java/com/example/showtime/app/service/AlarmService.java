package com.example.showtime.app.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.showtime.app.model.Alarm;

/**
 * Created by Jonti on 3/10/2016.
 */
public class AlarmService extends Service
{

    public void onCreate()
    {

        super.onCreate();
        Alarm alarm = new Alarm();
        alarm.SetAlarm(this);
        Log.d("AlarmService", "created!");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("AlarmService", "startcommand!");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}