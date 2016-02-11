package com.example.showtime.app.service;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class MovieService {

    private static TmdbApi api = new TmdbApi("0279053766dc7d93871419292081d94f");

    public static MovieResultsPage getMoviesByTitle(String query) {
        return api.getSearch().searchMovie(query, null, "en", false, 0);
    }

    public static MovieDb getMovieDetailsById(int id) {
        return api.getMovies().getMovie(id, "en");
    }
}
