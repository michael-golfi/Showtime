package com.example.showtime.app.model;

import info.movito.themoviedbapi.model.Multi;

/**
 * Created by michael on 14/03/16.
 */
public interface MaterialElement extends Multi {

    int getId();

    String getTitle();

    String getOverview();

    String getReleaseDate();

    String getPosterPath();

    String getNotes();

    void setNotes(String notes);

    void setId(int id);

    void setTitle(String title);

    void setOverview(String overview);

    void setReleaseDate(String releaseDate);
}
