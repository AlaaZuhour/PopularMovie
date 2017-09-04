package com.example.alaazuhouer.popularmoive;

/**
 * Created by alaazuhouer on 02/09/17.
 */

public class MovieReview {

    private int movieId;
    private String author;
    private String content;

    public MovieReview(int movieId, String author, String content) {
        this.movieId = movieId;
        this.author = author;
        this.content = content;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
