package com.example.alaazuhouer.popularmoive;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>{
    private ArrayList<Movie> movieArrayList;
    private GridView gridView;
    private static final int MOVIE_LOADER_ID = 22;
    private TextView errorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            movieArrayList = new ArrayList<>();
            if(isDeviceOnline()) {
                getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
            }else {
                showErrorMessage(getString(R.string.no_connection_message));
            }
        }
        else {
            movieArrayList = savedInstanceState.getParcelableArrayList("movies");
        }
        errorView = (TextView) findViewById(R.id.error_view);
        gridView = (GridView) findViewById(R.id.movies_list);



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movies",movieArrayList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.popularity) {
            FetchMoivesData.sortBy="popular";
            if(isDeviceOnline()) {
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,null,this);
            }else {
                showErrorMessage(getString(R.string.no_connection_message));
            }
            return true;
        }

        if (id == R.id.top_rated) {
            FetchMoivesData.sortBy="top_rated";
            if(isDeviceOnline()) {
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,null,this);
            }else {
                showErrorMessage(getString(R.string.no_connection_message));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {
            ArrayList<Movie> movieData;
            @Override
            protected void onStartLoading() {
                if(movieData != null)
                    deliverResult(movieData);
                else
                    forceLoad();
            }

            @Override
            public ArrayList<Movie> loadInBackground() {
                URL url = FetchMoivesData.buildUrl();
                String jsonString;

                try {
                    jsonString=FetchMoivesData.getResponseFromHttpUrl(url);
                    return  movieArrayList=FetchMoivesData.getJsonFromString(jsonString);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException ej) {
                    ej.printStackTrace();
                    return null;
                }
            }
            public void deliverResult(ArrayList<Movie> data) {
                movieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
       if(data != null) {
           movieArrayList = data;
           showMovieDataView();
       }else {
           showErrorMessage(getString(R.string.error_message_text));
       }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }
    private void showMovieDataView() {
        MovieAdapter movieAdapter;
        movieAdapter = new MovieAdapter(MainActivity.this, movieArrayList);
        gridView.setAdapter(movieAdapter);
        errorView.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.VISIBLE);
        gridView.setOnItemClickListener(new GridView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(MainActivity.this,DetilesActivity.class);
                intent.putExtra("movie_item",movieArrayList.get((int)view.getTag()));
                startActivity(intent);
            }
        });
    }

    private void showErrorMessage(String message) {
        gridView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(message);
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
