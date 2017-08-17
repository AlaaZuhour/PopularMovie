package com.example.alaazuhouer.popularmoive;

import android.os.Parcel;
import android.os.Parcelable;

class Movie implements Parcelable{


    private int id;
    private double voteAverage;
    private String title;
    private double popularity;
    private String posterPath;
    private String overview;
    private String releaseDate;

    Movie(int id, double vote_average, String title, double popularity,
          String poster_path, String overview, String release_date) {
        this.id = id;
        this.voteAverage = vote_average;
        this.title = title;
        this.popularity = popularity;
        this.posterPath = poster_path;
        this.overview = overview;
        this.releaseDate = release_date;
    }

    private Movie(Parcel in) {
        id = in.readInt();
        voteAverage = in.readDouble();
        title = in.readString();
        popularity = in.readDouble();
        posterPath= in.readString();
        overview = in.readString();
        releaseDate = in.readString();
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeDouble(voteAverage);
        parcel.writeString(title);
        parcel.writeDouble(popularity);
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
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
        return voteAverage;
    }

    String getTitle() {
        return title;
    }

    String getPoster_path() {
        return posterPath;
    }



    String getOverview() {
        return overview;
    }


    String getRelease_date() {
        return releaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }


}
