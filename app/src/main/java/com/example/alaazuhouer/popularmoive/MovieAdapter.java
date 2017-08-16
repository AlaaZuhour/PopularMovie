package com.example.alaazuhouer.popularmoive;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


 class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String IMAGE_URL="http:image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE="w342/";
    private final ArrayList<Movie> movieArrayList;

    public MovieAdapter(@NonNull Context context, @NonNull ArrayList<Movie> objects) {
        super(context, 0, objects);
        movieArrayList=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
        }
        Log.d("hi","im in getView");
        ImageView movieImage = convertView.findViewById(R.id.movie_image);
        if(movie.getPoster_path() != null)
            Picasso.with(getContext()).load(IMAGE_URL + IMAGE_SIZE + movie.getPoster_path()).into(movieImage);

        convertView.setTag(position);
        return convertView;
    }

    @Override
    public int getCount() {

        return movieArrayList.size();
    }

}
