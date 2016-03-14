package com.example.showtime.app.model;

import com.j256.ormlite.field.DatabaseField;
import info.movito.themoviedbapi.model.Multi;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class TvShow implements MaterialElement {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String title, overview, releaseDate;

    @DatabaseField
    private MediaType mediaType = MediaType.TV_SERIES;

    public TvShow() {

    }

    public TvShow(TvSeries series) {
        this.id = series.getId();
        this.title = series.getName();
        this.overview = series.getOverview();
        this.releaseDate = series.getFirstAirDate();
    }

    @Override
    public MediaType getMediaType() {
        return MediaType.TV_SERIES;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getOverview() {
        return overview;
    }

    @Override
    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "TV: " + title + " - " + releaseDate;
    }
}
