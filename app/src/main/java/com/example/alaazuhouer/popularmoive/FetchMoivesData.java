package com.example.alaazuhouer.popularmoive;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.example.alaazuhouer.popularmoive.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

 public class FetchMoivesData {

    private static final String TAG = FetchMoivesData.class.getSimpleName();
    private static final String MOIVE_BASE_URL="http://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PARAM="api_key";
    private static final String API_KEY="";

    public static URL buildUrl(String id,String sortBy){
        Uri builtUri;
        if(id.equals("")) {
            builtUri = Uri.parse(MOIVE_BASE_URL).buildUpon()
                    .appendPath(sortBy)
                    .appendQueryParameter(API_KEY_PARAM, API_KEY)
                    .build();
        }
        else {
            builtUri = Uri.parse(MOIVE_BASE_URL).buildUpon()
                    .appendPath(id)
                    .appendPath(sortBy)
                    .appendQueryParameter(API_KEY_PARAM, API_KEY)
                    .build();
        }
        URL url= null;
        try {
            url= new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    public static ContentValues[] getMovieJsonFromString(String jsonString) throws JSONException {



        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray moviesArray = jsonObject.getJSONArray("results");
        ContentValues[] contentValues = new ContentValues[moviesArray.length()];

        for(int i=0;i<moviesArray.length();i++){
            ContentValues contentValues1 = new ContentValues();
            JSONObject movieData = moviesArray.getJSONObject(i);
            contentValues1.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,movieData.getInt("id"));
            contentValues1.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,movieData.getDouble("vote_average"));
            contentValues1.put(MovieContract.MovieEntry.COLUMN_TITLE,movieData.getString("title"));
            contentValues1.put(MovieContract.MovieEntry.COLUMN_POPULARITY,movieData.getDouble("popularity"));
            contentValues1.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movieData.getString("poster_path"));
            contentValues1.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,movieData.getString("overview"));
            contentValues1.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,movieData.getString("release_date"));
            contentValues1.put(MovieContract.MovieEntry.COLUMN_FAVORIT,0);
            contentValues[i] = contentValues1;
        }
      return contentValues;
    }

    public static ContentValues[] getTrailerJsonFromString(String jsonString) throws JSONException{
        JSONObject jsonObject = new JSONObject(jsonString);
        int id = jsonObject.getInt("id");
        JSONArray moviesArray = jsonObject.getJSONArray("results");
        ContentValues[] contentValues = new ContentValues[moviesArray.length()];

        for(int i=0;i<moviesArray.length();i++){
            ContentValues contentValues1 = new ContentValues();
            JSONObject movieData = moviesArray.getJSONObject(i);
            contentValues1.put(MovieContract.TrailersEntry.COLUMN_MOVIE_ID,id);
            contentValues1.put(MovieContract.TrailersEntry.COLUMN_KEY,movieData.getString("key"));
            contentValues1.put(MovieContract.TrailersEntry.COLUMN_NAME,movieData.getString("name"));
            contentValues[i] = contentValues1;
        }
        return contentValues;
    }
     public static ContentValues[] getReviewJsonFromString(String jsonString) throws JSONException{
         JSONObject jsonObject = new JSONObject(jsonString);
         int id = jsonObject.getInt("id");
         JSONArray moviesArray = jsonObject.getJSONArray("results");
         ContentValues[] contentValues = new ContentValues[moviesArray.length()];

         for(int i=0;i<moviesArray.length();i++){
             ContentValues contentValues1 = new ContentValues();
             JSONObject movieData = moviesArray.getJSONObject(i);
             contentValues1.put(MovieContract.ReviewsEntry.COLUMN_MOVIE_ID,id);
             contentValues1.put(MovieContract.ReviewsEntry.COLUMN_AUTHOR,movieData.getString("author"));
             contentValues1.put(MovieContract.ReviewsEntry.COLUMN_CONTENT,movieData.getString("content"));
             contentValues[i] = contentValues1;
         }
         return contentValues;
     }
}
