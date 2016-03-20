package com.example.showtime.app.model;


import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Multi;


/**
 * Created by txrdelage on 07/02/16.
 */
@DatabaseTable(tableName = "movies")
public class Movie implements MaterialElement {
    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String title;

    @DatabaseField
    private String notes;

    @DatabaseField
    private String posterPath;

    @DatabaseField
    private String releaseDate;

    @DatabaseField
    private String overview;

    @DatabaseField
    private MediaType mediaType;

    public Movie() {
    }

    public Movie(MovieDb movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.posterPath = movie.getPosterPath();
        this.releaseDate = movie.getReleaseDate();
        this.overview = movie.getOverview();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        Log.d("Movie", "Retrieving notes:" + notes);
        return this.notes;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String toString() {
        return "M: " + title + " - " + releaseDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public MediaType getMediaType() {
        return mediaType;
    }
}
