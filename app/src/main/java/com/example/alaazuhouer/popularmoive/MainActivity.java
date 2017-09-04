package com.example.alaazuhouer.popularmoive;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.alaazuhouer.popularmoive.data.MovieContract;
import com.example.alaazuhouer.popularmoive.sync.MovieSyncTasks;
import com.example.alaazuhouer.popularmoive.sync.MovieSyncUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        MovieAdapter.OnMovieClickListener{
    private RecyclerView mRecyclerView;
    private static final int MOVIE_LOADER_ID = 22;
    private TextView errorView;
    private Cursor mCursor;
    private MovieAdapter mMovieAdapter;
    private int sortOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorView = (TextView) findViewById(R.id.error_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.movies_list);
        int ot = getResources().getConfiguration().orientation;
        GridLayoutManager layoutManager =
                new GridLayoutManager(this,ot == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this,this);
        mRecyclerView.setAdapter(mMovieAdapter);

        if(savedInstanceState == null) {
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
            MovieSyncUtils.initialize(this);
        }
        else{
            Cursor cursor;
            sortOrder = savedInstanceState.getInt("sort_order");
            switch (sortOrder){
                case 1:
                    cursor = this.getContentResolver().query(MovieContract.TopRatedMovieEntry.CONTENT_URI,
                            null,null,null, null);
                    mMovieAdapter.swapCursor(cursor);
                    break;
                case 2:
                    cursor = this.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,null,null, MovieContract.MovieEntry.COLUMN_POPULARITY+" DESC");
                    mMovieAdapter.swapCursor(cursor);
                    break;
                case 3:
                    String[] selectionArg = {1+""};
                    cursor = this.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null, MovieContract.MovieEntry.COLUMN_FAVORIT+" = ?",selectionArg, null);
                    mMovieAdapter.swapCursor(cursor);
                    break;
            }
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("sort_order",sortOrder);
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
            sortOrder = 2;
            Cursor cursor = this.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,null,null, MovieContract.MovieEntry.COLUMN_POPULARITY+" DESC");
            mMovieAdapter.swapCursor(cursor);
            return true;
        }

        if (id == R.id.top_rated) {
            sortOrder = 1;
            Cursor cursor = this.getContentResolver().query(MovieContract.TopRatedMovieEntry.CONTENT_URI,
                    null,null,null, null);
            mMovieAdapter.swapCursor(cursor);
            return true;
        }
        if(id == R.id.favorite){
            sortOrder =3;
            String[] selectionArg = {1+""};
            Cursor cursor = this.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null, MovieContract.MovieEntry.COLUMN_FAVORIT+" = ?",selectionArg, null);
            mMovieAdapter.swapCursor(cursor);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI,
                null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       if(data != null) {
           mCursor=data;
           mMovieAdapter.swapCursor(data);
           if(data.getCount() != 0)
               showMovieDataView();
       }else {
           showErrorMessage(getString(R.string.error_message_text));
       }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       mMovieAdapter.swapCursor(null);
    }
    private void showMovieDataView() {
        errorView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String message) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(message);
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent =new Intent(MainActivity.this,DetilesActivity.class);
        intent.putExtra("movie_item",movie);
        startActivity(intent);
    }
}
