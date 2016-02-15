package com.example.showtime.app.service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.tools.MovieDbException;

import java.util.List;

public class MovieService {

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
}
