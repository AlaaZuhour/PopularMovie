package com.example.alaazuhouer.popularmoive;

import android.os.Parcel;
import android.os.Parcelable;

class Movie implements Parcelable{


    private int id;
    private double vote_average;
    private String title;
    private double popularity;
    private String poster_path;
    private String overview;
    private String release_date;

    Movie(int id, double vote_average, String title, double popularity,
          String poster_path, String overview, String release_date) {
        this.id = id;
        this.vote_average = vote_average;
        this.title = title;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
    }

    private Movie(Parcel in) {
        id = in.readInt();
        vote_average = in.readDouble();
        title = in.readString();
        popularity = in.readDouble();
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeDouble(vote_average);
        parcel.writeString(title);
        parcel.writeDouble(popularity);
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(release_date);
    }
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    double getVote_average() {
        return vote_average;
    }

    String getTitle() {
        return title;
    }

    String getPoster_path() {
        return poster_path;
    }



    String getOverview() {
        return overview;
    }


    String getRelease_date() {
        return release_date;
    }


    @Override
    public int describeContents() {
        return 0;
    }


}
