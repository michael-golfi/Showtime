package com.example.showtime.app.service;


import com.example.showtime.app.model.MaterialElement;
import com.example.showtime.app.model.Movie;
import com.example.showtime.app.model.TvShow;
import info.movito.themoviedbapi.model.Multi;
import org.junit.Test;

import java.util.List;

public class MovieServiceTest {

    @Test
    public void testGetMoviesByTitle() throws Exception {
        List<MaterialElement> movies = MovieService.getMaterialElementsByTitle("Superman");
        assert movies.size() > 10;
        assert movies.get(0).getTitle().equals("Superman");

        boolean containsTvShow = false, containsMovie = false;
        for (MaterialElement materialElement : movies)
            if (materialElement.getMediaType() == Multi.MediaType.TV_SERIES)
                containsTvShow = true;
            else if (materialElement.getMediaType() == Multi.MediaType.MOVIE)
                containsMovie = true;

        assert containsMovie;
        assert containsTvShow;
    }

    @Test
    public void testGetMoviesById() throws Exception {
        Movie movie = MovieService.getMovieDetailsById(5354);
        assert movie.getTitle().equals("Berlin am Meer");
    }

    @Test
    public void testGetTvShowById() throws Exception {
        TvShow tvShow = MovieService.getTvShowDetailsById(43136);
        assert tvShow.getTitle().equals("Superman");
    }

    @Test
    public void testGetMoviesByGenre() throws Exception {
        List<Movie> movies = MovieService.getMoviesByGenre("action");
        assert movies.size() > 0;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMovieGenreDoesNotExist() throws Exception {
        List<Movie> movies = MovieService.getMoviesByGenre("sdfsdfsdf");
    }

    @Test
    public void testGetMovieByGenreOrTitle() throws Exception {
        List<MaterialElement> movies = MovieService.searchForMovies("action");

        Movie deadpool = null;
        for (MaterialElement movie : movies)
            if (movie.getTitle().equals("Deadpool"))
                deadpool = (Movie) movie;

        assert deadpool != null;

        movies = MovieService.searchForMovies("Deadpool");
        Movie deadpool2 = (Movie) movies.get(0);
        assert deadpool.getTitle().equals(deadpool2.getTitle());
    }

    @Test
    public void testGetMovieByNotTitleNotGenre() throws Exception {
        List<MaterialElement> movies = MovieService.searchForMovies("dfsdfdf");
        assert movies.size() == 0;
    }

    @Test
    public void testGetMoviesByActor() throws Exception {
        List<MaterialElement> results = MovieService.getMoviesByActor("Leonardo Dicaprio");
        assert results.size() > 0;
    }

    @Test
    public void testGetMoviesByActorWithNonExistingName() {
        List<MaterialElement> results = MovieService.getMoviesByActor("Titanic");
        assert results.size() == 0;
    }

    @Test
    public void testGetMoviesByDate() {
        List<Movie> results = MovieService.getMoviesByDate("2003");
        assert results.size() == 20;
    }

    @Test
    public void testGetMoviesByDateWithNegativedDate() {
        List<Movie> results = MovieService.getMoviesByDate("-003");
        assert results == null;

        results = MovieService.getMoviesByDate("-2003");
        assert results == null;
    }

    @Test
    public void testGetMoviesByDateWithInvalidDate() {
        boolean assertIfTrue = false;

        try {
            List<Movie> results = MovieService.getMoviesByDate("a003");
        } catch (RuntimeException NumberFormatException) {
            assertIfTrue = true;
        }

        assert assertIfTrue;
    }

    @Test
    public void testGetMoviesByDirector() throws Exception {
        List<MaterialElement> results = MovieService.getMoviesByDirector("Martin Scorsese");
        assert results.size() > 0;
    }

    @Test
    public void testGetMoviesByDirectorWithNonExistingName() throws Exception {
        List<MaterialElement> results = MovieService.getMoviesByDirector("Shutter Island");
        assert results.size() == 0;
    }
}