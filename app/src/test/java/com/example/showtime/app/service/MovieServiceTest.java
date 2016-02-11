package com.example.showtime.app.service;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import org.junit.Test;

import static org.junit.Assert.*;

public class MovieServiceTest {

    @Test
    public void testGetMoviesByTitle() throws Exception {
        MovieResultsPage movies = MovieService.getMoviesByTitle("Superman");
        assert movies.getTotalResults() > 20;
        assert movies.getResults().get(0).getTitle().equals("Superman");
    }

    @Test
    public void testGetMoviesById() throws Exception {
        MovieDb movie = MovieService.getMovieById(5354);
        assert movie.getTitle().equals("Berlin am Meer");
    }
}