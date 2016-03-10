package com.example.showtime.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.showtime.app.model.DatabaseHelper;
import com.example.showtime.app.model.Movie;
import com.example.showtime.app.service.GoogleCalendar;
import com.example.showtime.app.service.MovieService;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import info.movito.themoviedbapi.model.MovieDb;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment implements Button.OnClickListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private Movie mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    private DatabaseHelper databaseHelper;

    View rootView = null;

    private DatabaseHelper getHelper() {
        if (databaseHelper == null)
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        return databaseHelper;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            /*
      The dummy content this fragment is presenting.
     */
            String movieId = getArguments().getString(ARG_ITEM_ID);
            getItemLists gl = new getItemLists();
            gl.execute(movieId);
        }
    }

    private Movie getFromDatabaseOrOnline(String movieId) {
        int id = Integer.parseInt(movieId);
        Movie movie;
        if (isInDatabase(id)) {
            movie = getHelper().getMovie(id);
        } else {
            MovieDb movieDb = MovieService.getMovieDetailsById(id);
            movie = new Movie(movieDb);
        }
        return movie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        rootView.findViewById(R.id.add).setOnClickListener(this);
        rootView.findViewById(R.id.export_btn).setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View v) {
        Button clicked = ((Button) v);
        if (clicked.getId() == R.id.add) {
            try {
                createOrDeleteMovie(mItem);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (clicked.getId() == R.id.export_btn) {
            String releaseDate = mItem.getReleaseDate();
            try {
                GregorianCalendar cal = new GregorianCalendar();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(releaseDate);
                cal.setTime(date);

                Intent addToCalendar = GoogleCalendar.addToCalendarIntent(mItem.getTitle(), cal);
                startActivity(addToCalendar);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void createOrDeleteMovie(Movie movie) throws SQLException {
        if (!isInDatabase(mItem.getId())) {
            getHelper().createMovie(mItem);
            setRemoveMovieAttributes(movie);
        } else {
            getHelper().deleteMovie(mItem.getId());
            setAddMovieAttributes(movie);
        }
    }

    public boolean isInDatabase(int id) {
        return getHelper().movieExists(id);
    }

    public void setAddMovieAttributes(Movie result) {
        ((TextView) rootView.findViewById(R.id.title)).setText(result.getTitle());
        ((TextView) rootView.findViewById(R.id.release_date)).setText(result.getReleaseDate());
        ((TextView) rootView.findViewById(R.id.description)).setText(result.getOverview());
        ((Button) rootView.findViewById(R.id.add)).setText("Add");
    }

    public void setRemoveMovieAttributes(Movie result) {
        ((TextView) rootView.findViewById(R.id.title)).setText(result.getTitle());
        ((TextView) rootView.findViewById(R.id.release_date)).setText(result.getReleaseDate());
        ((TextView) rootView.findViewById(R.id.description)).setText(result.getOverview());
        ((Button) rootView.findViewById(R.id.add)).setText("Remove");
    }

    private class getItemLists extends
            AsyncTask<String, String, Movie> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Movie doInBackground(String... params) {
            return getFromDatabaseOrOnline(params[0]);
        }

        @Override
        protected void onPostExecute(Movie result) {
            super.onPostExecute(result);
            mItem = result;

            if (!isInDatabase(result.getId()))
                setAddMovieAttributes(result);
            else
                setRemoveMovieAttributes(result);
        }
    }
}