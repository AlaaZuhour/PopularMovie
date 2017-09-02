package com.example.alaazuhouer.popularmoive.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alaazuhouer on 01/09/17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY="com.example.alaazuhouer.popularmoive";
    public static final Uri CONTENT_BASE_URL = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "review";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                CONTENT_BASE_URL.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";


    }

    public static final class ReviewsEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                CONTENT_BASE_URL.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String TABLE_NAME = "reviews";

        // Foreign Key of Movie as movie_id
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";

    }

    public static final class TrailersEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                CONTENT_BASE_URL.buildUpon().appendPath(PATH_TRAILER).build();
        public static final String TABLE_NAME = "trailers";

        // Foreign Key of Movie as movie_id
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";


    }

}
