package com.example.alaazuhouer.popularmoive;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetilesActivity extends AppCompatActivity {

    private static final String IMAGE_URL="http:image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE="w342/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detiles);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        TextView title,rate,date,overview;
        ImageView movieImage;
        Intent intent = getIntent();
        title = (TextView) findViewById(R.id.movie_title);
        rate = (TextView) findViewById(R.id.rate);
        date = (TextView) findViewById(R.id.movie_date);
        overview = (TextView) findViewById(R.id.movie_overview);
        movieImage = (ImageView) findViewById(R.id.movie_image);

        if(intent != null && intent.hasExtra("movie_item")){
            Movie movie = intent.getParcelableExtra("movie_item");
            title.setText(movie.getTitle());
            String avgRate = getString(R.string.ratting)+movie.getVote_average();
            String movDate = getString(R.string.date)+movie.getRelease_date();
            rate.setText(avgRate);
            date.setText(movDate);
            overview.setText(movie.getOverview());
            Picasso.with(this).load(IMAGE_URL+IMAGE_SIZE+movie.getPoster_path()).resize(200,300).
                    into(movieImage);
        }

    }
}
