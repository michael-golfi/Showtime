package com.example.showtime.app;

/**
 * Created by alialnashashibi on 16-03-08.
 */

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;

import com.example.showtime.app.model.DatabaseHelper;
import com.example.showtime.app.model.Movie;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends FragmentActivity{
    CalendarView calendar;
    private List<Movie> movies = new ArrayList<>();
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    private DatabaseHelper databaseHelper = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendar = (CalendarView) findViewById(R.id.calendar);
        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();

        databaseHelper = getDatabaseHelper();

        try {
            Dao<Movie, Integer> moviesDao = databaseHelper.getMovieDao();
            movies = moviesDao.queryForAll();

            for(Movie movie: movies){
                try {
                    Date date = format.parse(movie.getReleaseDate());
                    ColorDrawable cd = new ColorDrawable(0xFFFF6666);
                    caldroidFragment.setBackgroundDrawableForDate(cd, date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), format.format(date),
                        Toast.LENGTH_SHORT).show();
                for(Movie movie: movies){
                    if (movie.getReleaseDate().equals(format.format(date))){
                        Intent detailIntent = new Intent(getApplicationContext(), MovieDetailActivity.class);
                        detailIntent.putExtra(MovieDetailFragment.ARG_ITEM_ID, Integer.toString(movie.getId()));
                        startActivity(detailIntent);
                    }
                }
            }

        };

        caldroidFragment.setCaldroidListener(listener);

    }
    private DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null)
            databaseHelper = OpenHelperManager.getHelper(getApplicationContext(), DatabaseHelper.class);
        return databaseHelper;
    }
}