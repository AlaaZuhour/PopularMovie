package com.example.alaazuhouer.popularmoive;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.alaazuhouer.popularmoive.data.MovieContract;
import com.squareup.picasso.Picasso;

import static android.R.drawable.star_big_off;

public class DetilesActivity extends AppCompatActivity {

    private static final String IMAGE_URL="http:image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE="w342/";
    private Movie movie;
    private ScrollView mScrollView ;
    private  int[] position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detiles);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        TextView title,rate,date,overview;
        ImageView movieImage;
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        ListView trailerList = (ListView) findViewById(R.id.trailers_list);
        ListView reviewList = (ListView) findViewById(R.id.reviews_list);
        Intent intent = getIntent();
        title = (TextView) findViewById(R.id.movie_title);
        rate = (TextView) findViewById(R.id.rate);
        date = (TextView) findViewById(R.id.movie_date);
        overview = (TextView) findViewById(R.id.movie_overview);
        movieImage = (ImageView) findViewById(R.id.movie_image);

        if(intent != null && intent.hasExtra("movie_item")){
            movie = intent.getParcelableExtra("movie_item");
            title.setText(movie.getTitle());
            String avgRate = getString(R.string.ratting)+movie.getVote_average();
            String movDate = getString(R.string.date)+movie.getRelease_date();
            rate.setText(avgRate);
            date.setText(movDate);
            overview.setText(movie.getOverview());
            Picasso.with(this).load(IMAGE_URL+IMAGE_SIZE+movie.getPoster_path()).resize(200,300).
                    into(movieImage);
        }
        Uri trailerUri = MovieContract.TrailersEntry.CONTENT_URI.buildUpon().appendPath(""+movie.getId()).build();
        final Cursor trailerCursor = this.getContentResolver().query(trailerUri,null,null,null,null);
        Log.d("trailer",trailerCursor.getCount()+"");
        CursorAdapter trailerAdapter = new CursorAdapter(this,trailerCursor) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                return LayoutInflater.from(context).inflate(R.layout.trailer_item,viewGroup, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
               TextView trailerView = (TextView) view.findViewById(R.id.trailer_name);
               // if(cursor.moveToFirst()) {
                  //  do {
                        trailerView.setText(cursor.getString(cursor.getColumnIndex(MovieContract.TrailersEntry.COLUMN_NAME)));
                   // }while (cursor.moveToNext());
               // }
            }

            @Override
            public int getCount() {
                return trailerCursor.getCount();
            }
        };
        trailerList.setAdapter(trailerAdapter);
        trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(trailerCursor.moveToPosition(i)){
                    String key = trailerCursor.getString(
                            trailerCursor.getColumnIndex(MovieContract.TrailersEntry.COLUMN_KEY));
                    Intent applicationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+key));
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + key));
                    try {
                        startActivity(applicationIntent);
                    } catch (ActivityNotFoundException ex) {
                        startActivity(browserIntent);
                    }
                }
            }
        });

        Uri reviewUri = MovieContract.ReviewsEntry.CONTENT_URI.buildUpon().appendPath(""+movie.getId()).build();
        final Cursor reviewCursor = this.getContentResolver().query(reviewUri,null,null,null,null);
        CursorAdapter reviewAdapter = new CursorAdapter(this,reviewCursor,0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                return LayoutInflater.from(context).inflate(R.layout.review_item,viewGroup, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView reviewView = (TextView) view.findViewById(R.id.review_content);
                reviewView.setText(cursor.getString(cursor.getColumnIndex(MovieContract.ReviewsEntry.COLUMN_CONTENT)));
            }

            @Override
            public int getCount() {
                return reviewCursor.getCount();
            }
        };
        reviewList.setAdapter(reviewAdapter);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putIntArray("SCROLL_POSITION",
                new int[]{ mScrollView.getScrollX(), mScrollView.getScrollY()});
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getIntArray("SCROLL_POSITION");
        if(position != null)
            mScrollView.post(new Runnable() {
                public void run() {
                    mScrollView.scrollTo(position[0], position[1]);
                }
            });
    }

    @Override
    protected void onResume() {
        if(position != null)
            mScrollView.post(new Runnable() {
                public void run() {
                    mScrollView.scrollTo(position[0], position[1]);
                }
            });
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detiles_menu, menu);
        if (movie.isFavorit()) {
            menu.getItem(0).setIcon(getResources().getDrawable(android.R.drawable.star_big_on));
            menu.getItem(0).setTitle((getString(R.string.pref_favorit)));
        } else {
            menu.getItem(0).setIcon(getResources().getDrawable(android.R.drawable.star_big_off));
            menu.getItem(0).setTitle(getString(R.string.pref_not_favorit));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String title = (String) item.getTitle();
        Drawable on = getResources().getDrawable(android.R.drawable.star_big_on,null);
        Drawable off = getResources().getDrawable(android.R.drawable.star_big_off,null);
        if(id == R.id.favorite_detile){
            if (title.equals(getString(R.string.pref_favorit))){
                movie.setFavorit(false);
                item.setIcon(off);
                item.setTitle(getString(R.string.pref_not_favorit));
                title = (String) item.getTitle();
                ContentValues contentValues1 = new ContentValues();
                String[] selections = {"" + movie.getId()};
                contentValues1.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_FAVORIT, 0);
                int row = this.getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, contentValues1,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ", selections);
                movie.setFavorit(false);
                item.setIcon(getResources().getDrawable(android.R.drawable.star_big_off));
                return true;
            }
            else if(title.equals(getString(R.string.pref_not_favorit))) {
                movie.setFavorit(true);
                item.setIcon(on);
                item.setTitle(getString(R.string.pref_favorit));
                title = (String) item.getTitle();
                ContentValues contentValues1 = new ContentValues();
                String[] selections = {"" + movie.getId()};
                contentValues1.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
                contentValues1.put(MovieContract.MovieEntry.COLUMN_FAVORIT, 1);
                int row = this.getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, contentValues1,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ", selections);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
