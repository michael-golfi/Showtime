package com.example.showtime.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.showtime.app.model.DatabaseHelper;
import com.example.showtime.app.model.MaterialElement;
import com.example.showtime.app.model.Movie;
import com.example.showtime.app.model.TvShow;
import com.example.showtime.app.service.GoogleCalendar;
import com.example.showtime.app.service.LoadMoviePoster;
import com.example.showtime.app.service.MovieService;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import info.movito.themoviedbapi.model.Multi;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public static final String ARG_ITEM_TYPE = "item_type";

    private MaterialElement mItem;
    // TODO: fix this
    public static MaterialElement myMovie;

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

        if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey(ARG_ITEM_TYPE)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            /*
      The dummy content this fragment is presenting.
     */
            String movieId = getArguments().getString(ARG_ITEM_ID);
            String mediaType = getArguments().getString(ARG_ITEM_TYPE);
            getItemLists gl = new getItemLists();
            gl.execute(movieId, mediaType);
        }
    }

    private MaterialElement getFromDatabaseOrOnline(String movieId, String mediaType) {
        int id = Integer.parseInt(movieId);
        MaterialElement materialElement;

        if (mediaType.equals(Multi.MediaType.MOVIE.toString()) && movieIsInDatabase(id)) {
            materialElement = getHelper().getMovie(id);
        } else if (mediaType.equals(Multi.MediaType.MOVIE.toString()))
            materialElement = MovieService.getMovieDetailsById(id);
        else if (mediaType.equals(Multi.MediaType.TV_SERIES.toString()) && tvShowIsInDatabase(id)) {
            materialElement = getHelper().getTvShow(id);
        } else if (mediaType.equals(Multi.MediaType.TV_SERIES.toString()))
            materialElement = MovieService.getTvShowDetailsById(id);
        else
            materialElement = null;

        return materialElement;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        rootView.findViewById(R.id.add).setOnClickListener(this);
        rootView.findViewById(R.id.export_btn).setOnClickListener(this);
        rootView.findViewById(R.id.save_notes).setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View v) {
        Button clicked = ((Button) v);
        if (clicked.getId() == R.id.add) {
            try {
                createOrDeleteMaterialElement(mItem);
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
        } else if (clicked.getId() == R.id.save_notes) {
            try {
                EditText notes_field = ((EditText) rootView.findViewById(R.id.notes_field));
                String notes = notes_field.getText().toString();
                getHelper().updateNotes(mItem.getId(), notes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createOrDeleteMaterialElement(MaterialElement materialElement) throws SQLException {
        if (materialElement.getMediaType() == Multi.MediaType.MOVIE)
            if (!movieIsInDatabase(mItem.getId())) {
                getHelper().createMovie((Movie) mItem);
                setRemoveMovieAttributes(materialElement);
            } else if (movieIsInDatabase(mItem.getId())) {
                getHelper().deleteMovie(mItem.getId());
                setAddMovieAttributes(materialElement);
            }

        if (materialElement.getMediaType() == Multi.MediaType.TV_SERIES)
            if (!tvShowIsInDatabase(mItem.getId())) {
                getHelper().createTv((TvShow) mItem);
                setRemoveMovieAttributes(materialElement);
            } else if (tvShowIsInDatabase(mItem.getId())) {
                getHelper().deleteTvShow(mItem.getId());
                setAddMovieAttributes(mItem);
            }
    }

    public boolean movieIsInDatabase(int id) {
        return getHelper().movieExists(id);
    }

    public boolean tvShowIsInDatabase(int id) {
        return getHelper().tvShowExists(id);
    }

    public void setAddMovieAttributes(MaterialElement result) {
        ((TextView) rootView.findViewById(R.id.title)).setText(result.getTitle());
        ((TextView) rootView.findViewById(R.id.release_date)).setText(result.getReleaseDate());
        ((TextView) rootView.findViewById(R.id.description)).setText(result.getOverview());
        ((Button) rootView.findViewById(R.id.add)).setText("Add");

        ImageView imageView = (ImageView) rootView.findViewById(R.id.posterImage);
        if (imageView != null) {
            LoadMoviePoster loadPoster = new LoadMoviePoster(result.getPosterPath(), imageView);
            loadPoster.execute();
        }
        ((EditText) rootView.findViewById(R.id.notes_field)).setVisibility(View.GONE);
        ((Button) rootView.findViewById(R.id.save_notes)).setVisibility(View.GONE);
    }

    public void setRemoveMovieAttributes(MaterialElement result) {
        ((TextView) rootView.findViewById(R.id.title)).setText(result.getTitle());
        ((TextView) rootView.findViewById(R.id.release_date)).setText(result.getReleaseDate());
        ((TextView) rootView.findViewById(R.id.description)).setText(result.getOverview());
        ((Button) rootView.findViewById(R.id.add)).setText("Remove");
        ImageView imageView = (ImageView) rootView.findViewById(R.id.posterImage);
        if (imageView != null) {
            LoadMoviePoster loadPoster = new LoadMoviePoster(result.getPosterPath(), imageView);
            loadPoster.execute();

            ((EditText) rootView.findViewById(R.id.notes_field)).setText(result.getNotes());
        }
    }

    private class getItemLists extends
            AsyncTask<String, String, MaterialElement> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected MaterialElement doInBackground(String... params) {
            return getFromDatabaseOrOnline(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(MaterialElement result) {
            super.onPostExecute(result);
            mItem = result;
            myMovie = mItem;

            if (!movieIsInDatabase(result.getId()))
                setAddMovieAttributes(result);
            else
                setRemoveMovieAttributes(result);
        }
    }
}