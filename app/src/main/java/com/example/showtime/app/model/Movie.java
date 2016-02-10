package com.example.showtime.app.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import info.movito.themoviedbapi.model.*;
import info.movito.themoviedbapi.model.keywords.Keyword;

import java.util.List;


/**
 * Created by txrdelage on 07/02/16.
 */
@DatabaseTable(tableName = "movies")
public class Movie {
    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String title;
/*    @DatabaseField
    private String originalTitle;

    @DatabaseField
    private float popularity;

    @DatabaseField
    private String backdropPath;
    @DatabaseField
    private String posterPath;*/

    @DatabaseField
    private String releaseDate;
/*    @DatabaseField
    private boolean adult;
    @DatabaseField
    private Collection belongsToCollection;
    @DatabaseField
    private long budget;
    @DatabaseField
    private List<Genre> genres;
    @DatabaseField
    private String homepage;*/

    @DatabaseField
    private String overview;
/*
    // todo still there??
    @DatabaseField
    private String imdbID;

    @DatabaseField
    private List<ProductionCompany> productionCompanies;
    @DatabaseField
    private List<ProductionCountry> productionCountries;

    @DatabaseField
    private long revenue;
    @DatabaseField
    private int runtime;

    @DatabaseField
    private List<Language> spokenLanguages;

    @DatabaseField
    private String tagline;
    @DatabaseField

    private float userRating;
    @DatabaseField
    private float voteAverage;
    @DatabaseField
    private int voteCount;

    @DatabaseField
    private String status;*/

    // Appendable responses

    /*@DatabaseField
    private List<AlternativeTitle> alternativeTitles;

    @DatabaseField
    private Credits credits;

    @DatabaseField
    private List<Artwork> images;

    // note: it seems to be a flaw in their api, because a paged result would be
    // more consistent
    @DatabaseField
    private List<Keyword> keywords;

    @DatabaseField
    private List<ReleaseInfo> releases;

    @DatabaseField
    private List<Video> videos;

    @DatabaseField
    private List<Translation> translations;

    @DatabaseField
    private List<MovieDb> similarMovies;

    @DatabaseField
    private List<Reviews> reviews;

    @DatabaseField
    private List<MovieList> lists;*/

    public Movie() {
    }

    public Movie(MovieDb movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
/*        this.originalTitle = movie.getOriginalTitle();
        this.popularity = movie.getPopularity();
        this.backdropPath = movie.getBackdropPath();
        this.posterPath = movie.getPosterPath();*/
        this.releaseDate = movie.getReleaseDate();
        /*this.adult = movie.isAdult();
        this.belongsToCollection = movie.getBelongsToCollection();
        this.budget = movie.getBudget();
        this.genres = movie.getGenres();
        this.homepage = movie.getHomepage();*/
        this.overview = movie.getOverview();
        /*this.imdbID = movie.getImdbID();
        this.productionCompanies = movie.getProductionCompanies();
        this.productionCountries = movie.getProductionCountries();
        this.revenue = movie.getRevenue();
        this.runtime = movie.getRuntime();
        this.spokenLanguages = movie.getSpokenLanguages();
        this.tagline = movie.getTagline();
        this.userRating = movie.getUserRating();
        this.voteAverage = movie.getVoteAverage();
        this.voteCount = movie.getVoteCount();
        this.status = movie.getStatus();

        /*this.credits = movie.getCredits();
        this.alternativeTitles = movie.getAlternativeTitles();
        this.images = movie.getImages();
        this.keywords = movie.getKeywords();
        this.releases = movie.getReleases();
        this.videos = movie.getVideos();
        this.translations = movie.getTranslations();
        this.similarMovies = movie.getSimilarMovies();
        this.reviews = movie.getReviews();
        this.lists = movie.getLists();*/
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

/*    public String getOriginalTitle() {
        return originalTitle;
    }

    public float getPopularity() {
        return popularity;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }*/

    public String getReleaseDate() {
        return releaseDate;
    }

    /*public boolean isAdult() {
        return adult;
    }

    public Collection getBelongsToCollection() {
        return belongsToCollection;
    }

    public long getBudget() {
        return budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }
*/
    public String getOverview() {
        return overview;
    }

    /*public String getImdbID() {
        return imdbID;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public List<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    public long getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public List<Language> getSpokenLanguages() {
        return spokenLanguages;
    }

    public String getTagline() {
        return tagline;
    }

    public float getUserRating() {
        return userRating;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getStatus() {
        return status;
    }

    /*public List<AlternativeTitle> getAlternativeTitles() {
        return alternativeTitles;
    }

    public Credits getCredits() {
        return credits;
    }

    public List<Artwork> getImages() {
        return images;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public List<ReleaseInfo> getReleases() {
        return releases;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public List<MovieDb> getSimilarMovies() {
        return similarMovies;
    }

    public List<Reviews> getReviews() {
        return reviews;
    }

    public List<MovieList> getLists() {
        return lists;
    }*/

    public  void setId(int id){
        this.id = id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public  void setReleaseDate (String releaseDate){
        this.releaseDate = releaseDate;
    }

    public  void setOverview (String overview){
        this.overview = overview;
    }


    public String toString(){
        return title + " - " + releaseDate;
    }
}
