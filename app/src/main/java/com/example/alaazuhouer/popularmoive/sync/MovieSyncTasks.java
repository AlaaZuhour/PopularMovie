package com.example.alaazuhouer.popularmoive.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.alaazuhouer.popularmoive.FetchMoivesData;
import com.example.alaazuhouer.popularmoive.data.MovieContract;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by alaazuhouer on 02/09/17.
 */

public class MovieSyncTasks {
    public static int[] moviesId ;
    public static void syncMovies(Context context){
        URL url = FetchMoivesData.buildUrl("","popular");
        ContentValues[] contentValues;
        String jsonString;

        try {
            jsonString=FetchMoivesData.getResponseFromHttpUrl(url);
            contentValues=FetchMoivesData.getMovieJsonFromString(jsonString);
            if(contentValues != null && contentValues.length != 0){
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(MovieContract.MovieEntry.CONTENT_URI,null,null);
                contentResolver.bulkInsert(MovieContract.MovieEntry.CONTENT_URI,contentValues);
            }
            url = FetchMoivesData.buildUrl("","top_rated");
            jsonString=FetchMoivesData.getResponseFromHttpUrl(url);
            contentValues=FetchMoivesData.getMovieJsonFromString(jsonString);
            if(contentValues != null && contentValues.length != 0){
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(MovieContract.TopRatedMovieEntry.CONTENT_URI,null,null);
                contentResolver.bulkInsert(MovieContract.TopRatedMovieEntry.CONTENT_URI,contentValues);
            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException ej) {
            ej.printStackTrace();

        }

    }

    public static void syncTrailersAndReviews(final Context context){

              String[] projection = {MovieContract.MovieEntry.COLUMN_MOVIE_ID};
              String[] projection1 = {MovieContract.TopRatedMovieEntry.COLUMN_MOVIE_ID};
                Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        projection,null,null,null);
                Cursor cursor1 = context.getContentResolver().query(MovieContract.TopRatedMovieEntry.CONTENT_URI,
                        projection1,null,null,null);
                int j=0;
                if(cursor != null && cursor.getCount() != 0){
                    moviesId = new int[45];
                    if(cursor.moveToFirst()){
                      do {
                          moviesId[j] = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                          j++;
                      }while (cursor.moveToNext() );
                    }
                }

        if(cursor1 != null && cursor1.getCount() != 0){
            if(cursor1.moveToFirst()){
                do {
                    moviesId[j] = cursor1.getInt(cursor1.getColumnIndex(MovieContract.TopRatedMovieEntry.COLUMN_MOVIE_ID));
                    j++;
                }while (cursor1.moveToNext() );
            }
        }

        if(moviesId != null && moviesId.length !=0){
            for(int i=0;i<moviesId.length;i++){
                URL url= FetchMoivesData.buildUrl(moviesId[i]+"","videos");
                String json = null;
                try {
                    json = FetchMoivesData.getResponseFromHttpUrl(url);
                    ContentValues[] contentValues =FetchMoivesData.getTrailerJsonFromString(json);
                    if(contentValues != null && contentValues.length !=0){
                        context.getContentResolver().bulkInsert(MovieContract.TrailersEntry.CONTENT_URI,contentValues);
                    }

                   url = FetchMoivesData.buildUrl(moviesId[i]+"","reviews");
                    json = FetchMoivesData.getResponseFromHttpUrl(url);
                    ContentValues[] contentValues1 =FetchMoivesData.getReviewJsonFromString(json);
                    if(contentValues1 != null && contentValues1.length !=0){
                        context.getContentResolver().bulkInsert(MovieContract.ReviewsEntry.CONTENT_URI,contentValues1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
