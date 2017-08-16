package com.example.alaazuhouer.popularmoive;

import android.net.Uri;
import android.util.Log;

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

 class FetchMoivesData {

    private static final String TAG = FetchMoivesData.class.getSimpleName();
    private static final String MOIVE_BASE_URL="http://api.themoviedb.org/3/movie/";
    private static final String API_KEY_PARAM="api_key";
    static String sortBy="popular";
    private static final String API_KEY="";

    static URL buildUrl(){
        Uri builtUri = Uri.parse(MOIVE_BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(API_KEY_PARAM,API_KEY)
                .build();
        URL url= null;
        try {
            url= new URL(builtUri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String getResponseFromHttpUrl(URL url) throws IOException {
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
    static ArrayList<Movie> getJsonFromString(String jsonString) throws JSONException {

        int id;
        double vote_average,popularity;
        String title,poster_path,overview,release_date;

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray moviesArray = jsonObject.getJSONArray("results");
        ArrayList<Movie> moviesList = new ArrayList<>();

        for(int i=0;i<moviesArray.length();i++){
            JSONObject movieData = moviesArray.getJSONObject(i);
            id=movieData.getInt("id");
            vote_average= movieData.getDouble("vote_average");
            title = movieData.getString("title");
            popularity = movieData.getDouble("popularity");
            poster_path = movieData.getString("poster_path");
            overview = movieData.getString("overview");
            release_date = movieData.getString("release_date");
            moviesList.add(new Movie(id,vote_average,title,popularity,poster_path,overview,release_date));

        }
      return moviesList;
    }
}
