package com.example.showtime.app.service;

import android.content.Intent;
import android.provider.CalendarContract;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by michael on 20/02/16.
 */
public class GoogleCalendar {

    public static Intent addToCalendarIntent(String title, Calendar releaseDate) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);

        intent.putExtra(CalendarContract.Events.TITLE, title);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Your Closest Cinema");

        intent.putExtra(CalendarContract.Events.ALL_DAY, true);

        // Set Dates
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                releaseDate.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                releaseDate.getTimeInMillis());

        // Making it private and shown as busy
        intent.putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE);
        intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

        return intent;
    }

}
