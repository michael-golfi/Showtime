package com.example.showtime.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.showtime.app.service.MovieService;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private MovieDb mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    View rootView = null;

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
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.title)).setText(mItem.getTitle());
        }

        return rootView;
    }

    private class getItemLists extends
            AsyncTask<String, String, MovieDb> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected MovieDb doInBackground(String... params) {
            int id = Integer.parseInt(params[0]);
            MovieDb results = MovieService.getMovieDetailsById(id);
            return results;
        }

        @Override
        protected void onPostExecute(MovieDb result) {
            super.onPostExecute(result);
            mItem = result;

            ((TextView) rootView.findViewById(R.id.title)).setText(mItem.getTitle());
            ((TextView) rootView.findViewById(R.id.release_date)).setText(mItem.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.description)).setText(mItem.getOverview());
        }
    }
}
