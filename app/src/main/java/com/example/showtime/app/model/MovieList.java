package com.example.showtime.app.model;

import info.movito.themoviedbapi.model.MovieDb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 20/03/16.
 */
public class MovieList {

    public static List<Movie> toMovieList(List<MovieDb> movieDbList) {
        List<Movie> movies = new ArrayList<>();
        for (MovieDb movie : movieDbList)
            movies.add(new Movie(movie));
        return movies;
    }

}
