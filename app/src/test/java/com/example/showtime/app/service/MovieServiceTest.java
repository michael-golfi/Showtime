package com.example.showtime.app.service;


import com.example.showtime.app.model.Movie;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

import org.junit.Test;

import java.util.List;

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

    @Test
    public void testGetMoviesByActor() throws  Exception{
        List<MovieDb> results = MovieService.getMoviesByActor("Leonardo Dicaprio");
        assert results.size() > 0;
        System.out.println(results.get(1).getTitle());
        assert results.get(0).getTitle().equals("The Beach");
        assert results.get(1).getTitle().equals("The Aviator");

    }

    @Test
    public void testGetMoviesByActorOtherAPI() throws  Exception{
        List<Movie> results = MovieService.getMoviesByActorOtherApi("Leonardo Dicaprio");
        assert results.size() > 0;
        assert results.get(0).getTitle().equals("The Revenant");
        assert results.get(1).getTitle().equals("Django Unchained");

    }

    @Test
    public void testGetMoviesByActorWithNonExistingName(){
        List<MovieDb> results = MovieService.getMoviesByActor("Titanic");
        assert results.size() == 0;
    }

}