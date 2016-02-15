package com.example.showtime.app.service;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import org.junit.Test;

public class MovieServiceTest {

    @Test
    public void testGetMoviesByTitle() throws Exception {
        MovieResultsPage movies = MovieService.getMoviesByTitle("Superman");
        assert movies.getTotalResults() > 20;
        assert movies.getResults().get(0).getTitle().equals("Superman");
    }

    @Test
    public void testGetMoviesById() throws Exception {
        MovieDb movie = MovieService.getMovieDetailsById(5354);
        assert movie.getTitle().equals("Berlin am Meer");
    }

    @Test
    public void testGetMoviesByGenre() throws Exception {
        MovieResultsPage movies = MovieService.getMoviesByGenre("action");
        assert movies.getResults().size() > 0;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovieGenreDoesNotExist() throws Exception {
        MovieResultsPage movies = MovieService.getMoviesByGenre("sdfsdfsdf");
    }

    @Test
    public void testGetMovieByGenreOrTitle() throws Exception {
        MovieResultsPage movies = MovieService.searchForMovies("action");

        MovieDb deadpool = null;
        for (MovieDb movie : movies.getResults())
            if (movie.getTitle().equals("Deadpool"))
                deadpool = movie;

        assert deadpool != null;

        movies = MovieService.searchForMovies("Deadpool");
        MovieDb deadpool2 = movies.getResults().get(0);
        assert deadpool.getTitle().equals(deadpool2.getTitle());
    }

    @Test
    public void testGetMovieByNotTitleNotGenre() throws Exception {
        MovieResultsPage movies = MovieService.searchForMovies("dfsdfdf");
        assert movies.getResults().size() == 0;
    }
}