package com.example.alaazuhouer.popularmoive;

/**
 * Created by alaazuhouer on 02/09/17.
 */

public class MovieVideos {
    private int movieId ;
    private String key;
    private String name;

    public MovieVideos(int movieId, String key, String name) {
        this.movieId = movieId;
        this.key = key;
        this.name = name;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
