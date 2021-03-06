package com.example.showtime.app;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.widget.CalendarView;
import com.example.showtime.app.model.DatabaseHelper;
import com.example.showtime.app.service.MovieService;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import info.movito.themoviedbapi.model.Multi;


/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MovieListFragment} and the item details
 * (if present) is a {@link MovieDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link MovieListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class MovieListActivity extends AppCompatActivity
        implements MovieListFragment.Callbacks, HistoryListFragment.Callbacks,
        SearchView.OnQueryTextListener, MenuItemCompat.OnActionExpandListener {
        public static String language = "en";
        public static String calendarLanguage = "Calendar";
        public static String historyLanguage = "History";
        public static String deleteAllLanguage = "Delete All";
        public static String changeLanguage = "Switch En/Fr";
        public static String searchLanguage = "Search...";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_list);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((MovieListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.movie_list))
                    .setActivateOnItemClick(true);
        }
    }

    /**
     * Callback method from {@link MovieListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id, String mediaType) {

        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Intent intent = new Intent(this, NotificationActivity.class);

            Bundle arguments = new Bundle();
            arguments.putString(MovieDetailFragment.ARG_ITEM_ID, id);
            arguments.putString(MovieDetailFragment.ARG_ITEM_TYPE, mediaType);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MovieDetailActivity.class);
            detailIntent.putExtra(MovieDetailFragment.ARG_ITEM_ID, id);
            detailIntent.putExtra(MovieDetailFragment.ARG_ITEM_TYPE, mediaType);
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(searchItem, this);

        final MenuItem calendarButton = menu.findItem(R.id.action_calendar);
        CalendarView calendarView = (CalendarView) MenuItemCompat.getActionView(calendarButton);
        calendarButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MovieListActivity.this, CalendarActivity.class);
                startActivity(intent);
                return true;
            }
        });

        final MenuItem deleteAll = menu.findItem(R.id.action_delete_all);
        deleteAll.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                DisplayListFragment listFragment = ((DisplayListFragment) getSupportFragmentManager().findFragmentById(R.id.movie_list));
                listFragment.deleteAllFromDB();
                return true;
            }
        });

        final MenuItem historyButton = menu.findItem(R.id.action_history);
        historyButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment newFragment = new HistoryListFragment();
                transaction.replace(R.id.movie_list, newFragment).commit();
                return true;
            }
        });
        final MenuItem languageButton = menu.findItem(R.id.action_languages);
        if(language.equals("en")) {
            searchView.setQueryHint("Search...");
            calendarButton.setTitle("Calendar");
            historyButton.setTitle("History");
            deleteAll.setTitle("Delete All");
            languageButton.setTitle("Switch En/Fr");
        }
        else{
            searchView.setQueryHint("Rechercher...");
            calendarButton.setTitle("Calendrier");
            historyButton.setTitle("Historique");
            deleteAll.setTitle("Tout Supprimer");
            languageButton.setTitle("Changer Fr/En");
        }
        languageButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (language.equals("fr"))
                {
                    language = "en";
                    calendarLanguage = "Calendar";
                    historyLanguage = "History";
                    deleteAllLanguage = "Delete All";
                    changeLanguage = "Switch En/Fr";
                    searchLanguage = "Search...";
                    MovieDetailFragment.addLanguage = "Add";
                    MovieDetailFragment.removeLanguage = "Remove";
                    MovieDetailFragment.addNotesLanguage = "Add Notes";
                    MovieDetailFragment.saveNotesLanguage = "Save Modifications";
                    MovieDetailFragment.exportLanguage = "Export";
                }
                else
                {
                    language = "fr";
                    calendarLanguage = "Calendrier";
                    historyLanguage = "Historique";
                    deleteAllLanguage = "Tout Supprimer";
                    changeLanguage = "Changer En/Fr";
                    searchLanguage = "Rechercher...";
                    MovieDetailFragment.addLanguage = "Ajouter";
                    MovieDetailFragment.removeLanguage = "Supprimer";
                    MovieDetailFragment.addNotesLanguage = "Ajouter des notes";
                    MovieDetailFragment.saveNotesLanguage = "Sauver les modifications";
                    MovieDetailFragment.exportLanguage = "Exporter";
                }


                calendarButton.setTitle(calendarLanguage);
                historyButton.setTitle(historyLanguage);
                languageButton.setTitle(changeLanguage);
                deleteAll.setTitle(deleteAllLanguage);
                searchView.setQueryHint(searchLanguage);

                Log.d("mla", "onMenuItemClick: languagechange to -->" + language);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onQuerySelected(String query) {
        onQueryTextSubmit(query);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("MovieListActivity", "Search Submitted");

        if (query.length() > 2) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment newFragment = new MovieListFragment(); //your search fragment
            Bundle args = new Bundle();
            args.putString("query", query);
            newFragment.setArguments(args);

            transaction.replace(R.id.movie_list, newFragment)
                    .addToBackStack(null)
                    .commit();
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Log.d("MovieListActivity", "Search Text Change");
        onQueryTextSubmit(s);
        return false;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment newFragment = new MovieListFragment();
        transaction.replace(R.id.movie_list, newFragment)
                .addToBackStack(null)
                .commit();
        return true;
    }
}
