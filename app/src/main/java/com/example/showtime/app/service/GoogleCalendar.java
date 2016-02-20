package com.example.showtime.app.service;

import android.content.Intent;
import android.provider.CalendarContract;

import java.util.Calendar;

/**
 * Created by michael on 20/02/16.
 */
public class GoogleCalendar {

    public static Intent addToCalendarIntent(String title, Calendar startDateTime, Calendar endDateTime) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);

        intent.putExtra(CalendarContract.Events.TITLE, title);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Your Closest Cinema");

        // Set Dates
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                startDateTime.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                endDateTime.getTimeInMillis());

        // Making it private and shown as busy
        intent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        return intent;
    }

}
