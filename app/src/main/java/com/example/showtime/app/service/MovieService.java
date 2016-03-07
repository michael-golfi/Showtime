package com.example.showtime.app.service;

import android.util.Log;

import com.example.showtime.app.model.Movie;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.example.showtime.app.model.Movie;
import com.uwetrottmann.tmdb.Tmdb;
import com.uwetrottmann.tmdb.entities.AppendToDiscoverResponse;
import com.uwetrottmann.tmdb.enumerations.SortBy;
import com.uwetrottmann.tmdb.services.DiscoverService;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbPeople;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCredit;
import info.movito.themoviedbapi.model.core.ResultsPage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieService {

    //Since we are only allowed 40 requests per second
    private static final int MAX_ALLOWED_REQUESTS = 20;

    private static Tmdb tmdb = new Tmdb();

    private static TmdbApi api = new TmdbApi("0279053766dc7d93871419292081d94f");

    public static MovieResultsPage searchForMovies(String query) {

        // Search by Genre if applicable.
        // Very primitive form of parsing a genre
        if (Genres.getGenreId(query) >= 0)
            return getMoviesByGenre(query);

        // Even more primitive way of parsing year
        if (query.startsWith("#"))
        {
            return getMoviesByDate(query.substring(1));
        }

        // By default, get movies by title
        return getMoviesByTitle(query);
    }

    public static List<Movie> searchForMoviesByPerson(String query){
        if(getMoviesByDirector(query).size() > 0){
            return getMoviesByDirector(query);
        }
        else{
            return getMoviesByActor(query);
        }
    }

    public static MovieResultsPage getMoviesByTitle(String query) {
        return api.getSearch().searchMovie(query, null, "en", false, 0);
    }

    public static MovieDb getMovieDetailsById(int id) {
        return api.getMovies().getMovie(id, "en");
    }

    public static MovieResultsPage getMoviesByGenre(String genre) throws IllegalArgumentException {
        int genreNumber = Genres.getGenreId(genre);

        if (genreNumber == -1)
            throw new IllegalArgumentException("No movies except for argument");

        return api.getGenre().getGenreMovies(genreNumber, "en", 0, false);
    }

    public static List<Movie> getMoviesByActor(String actor) {
        com.uwetrottmann.tmdb.entities.MovieResultsPage resultsPage;
        tmdb.setApiKey("0279053766dc7d93871419292081d94f");
        DiscoverService discoverService = tmdb.discoverService();
        List<Movie> movieResults = new ArrayList<>();
        TmdbPeople.PersonResultsPage actorsResults = api.getSearch().searchPerson(actor, false, 0);
        if(actorsResults.getResults().size() == 0){
            return movieResults;
        }
        int actor_id = actorsResults.getResults().get(0).getId();
        resultsPage = discoverService.discoverMovie(false,false,"en",1,
                null,null,null,null,null, SortBy.POPULARITY_DESC,
                null,null,null,null, new AppendToDiscoverResponse(actor_id),
                null,null,null,null,null,null);

        DateFormat df = new SimpleDateFormat("yyyy-MM-DD");
        for (com.uwetrottmann.tmdb.entities.Movie movie : resultsPage.results){
            Movie new_movie = new Movie();
            new_movie.setId(movie.id);
            new_movie.setTitle(movie.title);
            if(movie.release_date != null) {
                new_movie.setReleaseDate(df.format(movie.release_date));
            }
            else{
                new_movie.setReleaseDate("Date not provided");
            }
            new_movie.setOverview(movie.overview);
            movieResults.add(new_movie);
        }
        return movieResults;
    }

    public static List<Movie> getMoviesByDirector(String director) {
        com.uwetrottmann.tmdb.entities.MovieResultsPage resultsPage;
        tmdb.setApiKey("0279053766dc7d93871419292081d94f");
        DiscoverService discoverService = tmdb.discoverService();
        List<Movie> movieResults = new ArrayList<>();
        TmdbPeople.PersonResultsPage directorsResults = api.getSearch().searchPerson(director, false, 0);
        if(directorsResults.getResults().size() == 0){
            return movieResults;
        }
        int director_id = directorsResults.getResults().get(0).getId();
        resultsPage = discoverService.discoverMovie(false,false,"en",1,
                null,null,null,null,null, SortBy.POPULARITY_DESC,
                null,null,null,null, null,
                null,null,null,null,new AppendToDiscoverResponse(director_id),null);

        DateFormat df = new SimpleDateFormat("yyyy-MM-DD");
        for (com.uwetrottmann.tmdb.entities.Movie movie : resultsPage.results){
            Movie new_movie = new Movie();
            new_movie.setId(movie.id);
            new_movie.setTitle(movie.title);
            if(movie.release_date != null) {
                new_movie.setReleaseDate(df.format(movie.release_date));
            }
            else{
                new_movie.setReleaseDate("Date not provided");
            }
            new_movie.setOverview(movie.overview);
            movieResults.add(new_movie);
        }
        return movieResults;
    }

    public static MovieResultsPage getMoviesByDate(String query) {
        Log.d("MovieService", "getMoviesByDate year: " + query);
        MovieResultsPage movies = null;
        try
        {
            int year = Integer.parseInt(query);

            if (year > 0 && query.length() == 4)
            {
                try
                {
                    // Make sure that we only query movies that where released on the given year.
                    Discover discover = new Discover();
                    discover.primaryReleaseYear(year);

                    movies = api.getDiscover().getDiscover(discover);
                }
                catch (RuntimeException exception)
                {
                    Log.d("MovieService", "getMoviesByDate Failed to get movies from database");
                    throw  exception;
                }
            }
        }
        catch (NumberFormatException e)
        {
            Log.d("MovieService", "getMoviesByDate Not a valid integer");
            throw e;
        }

        return movies;
    }
}
