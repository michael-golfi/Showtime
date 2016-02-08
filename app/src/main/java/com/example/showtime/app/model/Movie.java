package com.example.showtime.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;



/**
 * Created by txrdelage on 07/02/16.
 */
@DatabaseTable(tableName = "movies")
public class Movie {
    @DatabaseField
    private int tmdb_id;
    @DatabaseField
    private String movie_name;
    @DatabaseField
    private String release_date;

    public Movie(){}

    public Movie(int tmdb_id, String movie_name, String release_date){
        this.tmdb_id = tmdb_id;
        this.movie_name = movie_name;
        this.release_date= release_date;
    }

    public int getTmdb_id(){
        return tmdb_id;
    }

    public String getMovie_name(){
        return movie_name;
    }

    public  String getRelease_date(){
        return release_date;
    }
}
