package com.example.showtime.app;

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
import com.example.showtime.app.service.MovieService;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import info.movito.themoviedbapi.model.MovieDb;

import java.sql.SQLException;

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

    /**
     * The dummy content this fragment is presenting.
     */
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
            String movieId = getArguments().getString(ARG_ITEM_ID);
            getItemLists gfl = new getItemLists();
            gfl.execute(movieId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        rootView.findViewById(R.id.add).setOnClickListener(this);
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.title)).setText(mItem.getTitle());
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        try {
            getHelper().createMovie(mItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            int id = Integer.parseInt(params[0]);
            MovieDb result = MovieService.getMovieDetailsById(id);
            return new Movie(result);
        }

        @Override
        protected void onPostExecute(Movie result) {
            super.onPostExecute(result);
            mItem = result;

            ((TextView) rootView.findViewById(R.id.title)).setText(mItem.getTitle());
            ((TextView) rootView.findViewById(R.id.release_date)).setText(mItem.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.description)).setText(mItem.getOverview());
        }
    }
}
