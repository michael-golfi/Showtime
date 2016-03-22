package com.example.showtime.app.model;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;
import info.movito.themoviedbapi.model.tv.TvSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael on 20/03/16.
 */
public class MaterialElementList {

    public static List<MaterialElement> movieListToMaterialElementList(List<Movie> movies) {
        List<MaterialElement> materialElements = new ArrayList<>();
        if(movies != null) {
            for (Movie movie : movies)
                materialElements.add(movie);
        }
        return materialElements;
    }

    public static List<MaterialElement> tvShowListToMaterialElementList(List<TvShow> movies) {
        List<MaterialElement> materialElements = new ArrayList<>();
        for (TvShow show : movies)
            materialElements.add(show);
        return materialElements;
    }

    public static List<MaterialElement> multiListToMaterialElementList(List<Multi> movies) {
        List<MaterialElement> materialElements = new ArrayList<>();
        for (Multi movie : movies) {
            if (movie.getMediaType() == Multi.MediaType.MOVIE)
                materialElements.add(new Movie((MovieDb) movie));
            else if (movie.getMediaType() == Multi.MediaType.TV_SERIES)
                materialElements.add(new TvShow((TvSeries) movie));
            else {
                // Add Nothing
            }
        }
        return materialElements;
    }
}
