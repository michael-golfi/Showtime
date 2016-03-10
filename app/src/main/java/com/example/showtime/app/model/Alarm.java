
package com.example.showtime.app.model;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import android.os.SystemClock;
import android.util.Log;;
import android.widget.Toast;

import com.example.showtime.app.MovieDetailFragment;
import com.example.showtime.app.MovieListFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


/**
 * Created by Jonti on 3/10/2016.
 */

public class Alarm extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        DatabaseHelper dbh = OpenHelperManager.getHelper(context, DatabaseHelper.class);




        Log.d("alarm","alarm doing databse shit");
        try {

           // Log.d("alarm","test 1");
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH)+1;
            int year = calendar.get(Calendar.YEAR);
          //  Log.d("alarm","test 2");
            Dao<Movie, Integer> moviesDao = dbh.getMovieDao();
            List<Movie> comingOut = moviesDao.queryForAll();
            for ( Movie m : comingOut) {
               // Log.d("alarm", "test 3");
                String mYear = m.getReleaseDate().substring(0, 4);
                String mMonth = m.getReleaseDate().substring(5, 7);
                String mDate = m.getReleaseDate().substring(8, 10);

                //Log.d("alarm", "year = " + mYear + ", our year is "+year);
                if (Integer.parseInt(mYear) == year) {
                   // Log.d("alarm", "month = " + mMonth + ", our month is =>" + month);
                    if (Integer.parseInt(mMonth) == month) {
                       // Log.d("alarm", "day = " + mDate + ", our day is =>" + day);
                        if (Math.abs(Integer.parseInt(mDate) - day) <= 7) {

                            Toast.makeText(context, "THERES A MOVIE ON YOUR LIST COMING OUT THIS WEEK!!", Toast.LENGTH_LONG).show();
                            Log.d("alarm", "TOAST!!");
                            break;
                        }
                    }
                }


            }
            } catch (SQLException e) {
            Log.d("alarm","test 4");
            e.printStackTrace();
        }

    }

    public void SetAlarm(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent("test");
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1000 * 60 , pi); // Millisec * Second * Minute
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent("test");
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}