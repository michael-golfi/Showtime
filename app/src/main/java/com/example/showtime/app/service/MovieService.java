package com.example.showtime.app.service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbPeople;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.people.PersonCredit;

import java.util.ArrayList;
import java.util.List;

public class MovieService {

    //Since we are only allowed 40 requests per second
    private static final int MAX_ALLOWED_REQUESTS = 20;

    private static TmdbApi api = new TmdbApi("0279053766dc7d93871419292081d94f");

    public static MovieResultsPage searchForMovies(String query) {

        // Search by Genre if applicable.
        // Very primitive form of parsing a genre
        if (Genres.getGenreId(query) >= 0)
            return getMoviesByGenre(query);

        // By default, get movies by title
        return getMoviesByTitle(query);
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

    public static List<MovieDb> getMoviesByActor(String actor) {
        List<MovieDb> movieResults = new ArrayList<>();
        TmdbPeople.PersonResultsPage actorsResults = api.getSearch().searchPerson(actor, false, 0);
        if(actorsResults.getResults().size() == 0){
            return movieResults;
        }
        int actor_id = actorsResults.getResults().get(0).getId();
        //String query = "https://api.themoviedb.org/3/discover/movie?with_cast="+ actor_id +"&api_key=0279053766dc7d93871419292081d94f";
        List<PersonCredit> credits = api.getPeople().getPersonCredits(actor_id).getCast();
        for(int i=0; i < MAX_ALLOWED_REQUESTS; i++){
            movieResults.add(api.getMovies().getMovie(credits.get(i).getMovieId(), "en"));
        }
        return movieResults;
    }
}
