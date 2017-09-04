package com.example.alaazuhouer.popularmoive.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by alaazuhouer on 01/09/17.
 */

public class MovieProvider extends ContentProvider {

    static final int MOVIE = 100;
    static final int TRAILER = 200;
    static final int REVIEW = 300;
    static final int TOP_RATED_MOVIE = 400;
    static final int REVIEW_WITH_ID = 301;
    static final int TRAILER_WITH_ID = 201;
    private MovieDBHelper movieDBHelper;
    public static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uri = new UriMatcher(UriMatcher.NO_MATCH);
        uri.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_MOVIE,MOVIE);
        uri.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_TRAILER,TRAILER);
        uri.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_TRAILER+"/#",TRAILER_WITH_ID);
        uri.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_REVIEW,REVIEW);
        uri.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_REVIEW+"/#",REVIEW_WITH_ID);
        uri.addURI(MovieContract.CONTENT_AUTHORITY,MovieContract.PATH_TOP_RATED,TOP_RATED_MOVIE);

        return uri;
    }

    @Override
    public boolean onCreate() {
        movieDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase sqLiteDatabase = movieDBHelper.getWritableDatabase();
        int rowInserted = 0;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                sqLiteDatabase.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long id = sqLiteDatabase.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (id != -1)
                            rowInserted++;
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                }finally {
                    sqLiteDatabase.endTransaction();
                }
                if (rowInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowInserted;
            case TOP_RATED_MOVIE:
                sqLiteDatabase.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long id = sqLiteDatabase.insert(MovieContract.TopRatedMovieEntry.TABLE_NAME, null, value);
                        if (id != -1)
                            rowInserted++;
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                }finally {
                    sqLiteDatabase.endTransaction();
                }
                if (rowInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowInserted;
            case TRAILER:
                sqLiteDatabase.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long id = sqLiteDatabase.insert(MovieContract.TrailersEntry.TABLE_NAME, null, value);
                        if (id != -1)
                            rowInserted++;
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                }finally {
                    sqLiteDatabase.endTransaction();
                }
                if (rowInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowInserted;
            case REVIEW:
                sqLiteDatabase.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long id = sqLiteDatabase.insert(MovieContract.ReviewsEntry.TABLE_NAME, null, value);
                        if (id != -1)
                            rowInserted++;
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                }finally {
                    sqLiteDatabase.endTransaction();
                }
                if (rowInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowInserted;
            default:
                return super.bulkInsert(uri, values);

        }

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteDatabase sqLiteDatabase = movieDBHelper.getReadableDatabase();
        Cursor retCursor = null;
        switch (sUriMatcher.match(uri)){
            case MOVIE:
                retCursor = sqLiteDatabase.query(MovieContract.MovieEntry.TABLE_NAME,
                        strings,s,strings1,null,null,s1);
                break;
            case TOP_RATED_MOVIE:
                retCursor = sqLiteDatabase.query(MovieContract.TopRatedMovieEntry.TABLE_NAME,
                        strings,s,strings1,null,null,s1);
                break;
            case TRAILER_WITH_ID:
                String id = uri.getLastPathSegment();
                String[] selectionArg = {id};
                retCursor = sqLiteDatabase.query(MovieContract.TrailersEntry.TABLE_NAME,
                        strings,MovieContract.TrailersEntry.COLUMN_MOVIE_ID+" = ? ",selectionArg,
                        null,null,s1);
                break;
            case REVIEW_WITH_ID:
                String id1 = uri.getLastPathSegment();
                String[] selectionArg1 = {id1};
                retCursor = sqLiteDatabase.query(MovieContract.ReviewsEntry.TABLE_NAME,
                        strings,MovieContract.ReviewsEntry.COLUMN_MOVIE_ID+" = ? ",selectionArg1,
                        null,null,s1);
                break;

        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == s) s = "1";

        switch (match) {
            case MOVIE: {
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, s, strings);
                break;
            }
            case TOP_RATED_MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.TopRatedMovieEntry.TABLE_NAME, s, strings);
                break;
            case TRAILER: {
                rowsDeleted = db.delete(
                        MovieContract.TrailersEntry.TABLE_NAME, s,strings);
                break;
            }
            case REVIEW: {
                rowsDeleted = db.delete(
                        MovieContract.ReviewsEntry.TABLE_NAME, s, strings);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Student: A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        // Oh, and you should notify the listeners here.

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Student: return the actual rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = movieDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, s,
                        strings);
                break;
            case TOP_RATED_MOVIE:
                rowsUpdated = db.update(MovieContract.TopRatedMovieEntry.TABLE_NAME, values, s,
                        strings);
                break;
            case TRAILER:
                rowsUpdated = db.update(MovieContract.TrailersEntry.TABLE_NAME, values, s,
                        strings);
                break;
            case REVIEW:
                rowsUpdated = db.update(MovieContract.ReviewsEntry.TABLE_NAME, values, s,
                        strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
