package com.example.showtime.app.service;

/**
 * Created by michael on 14/02/16.
 */
public class Genres {
    public static int getGenreId(String genre) {
        switch (genre.toLowerCase()) {
            case "action":
                return 28;
            case "adventure":
                return 12;
            case "animation":
                return 16;
            case "comedy":
                return 35;
            case "crime":
                return 80;
            case "documentary":
                return 99;
            case "drama":
                return 18;
            case "family":
                return 10751;
            case "fantasy":
                return 14;
            case "foreign":
                return 10769;
            case "history":
                return 36;
            case "horror":
                return 27;
            case "music":
                return 10402;
            case "mystery":
                return 9648;
            case "romance":
                return 10749;
            case "scifi":
                return 878;
            case "tv":
                return 10770;
            case "thriller":
                return 53;
            case "war":
                return 10752;
            case "western":
                return 37;
            default:
                return -1;
        }
    }
}
