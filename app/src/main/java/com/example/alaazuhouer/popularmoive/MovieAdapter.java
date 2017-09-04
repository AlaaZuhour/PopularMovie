package com.example.alaazuhouer.popularmoive;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.alaazuhouer.popularmoive.data.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


 class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private static final String IMAGE_URL="http:image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE="w342/";
    private  Cursor mCursor;
     private OnMovieClickListener mOnMovieClickListener;
     private Context mContext;

    public MovieAdapter(@NonNull Context context, OnMovieClickListener listener) {
        mContext=context;
        mOnMovieClickListener = listener;
    }


     @Override
     public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item,parent,false);
         return new MovieAdapterViewHolder(view);
     }

     @Override
     public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
         if(mCursor.moveToPosition(position)){
             String posterPath = mCursor.getString(mCursor.getColumnIndex
                     (MovieContract.MovieEntry.COLUMN_POSTER_PATH));
             Picasso.with(mContext).load(IMAGE_URL + IMAGE_SIZE +posterPath).into(holder.movieImage);
         }

     }

     void swapCursor(Cursor newCursor) {
         this.mCursor = newCursor;
         notifyDataSetChanged();
     }

     @Override
     public int getItemCount() {
         return mCursor != null ? mCursor.getCount() : 0;
     }


     public interface OnMovieClickListener{
        void onMovieClick(Movie movie);
    }

     public class MovieAdapterViewHolder extends RecyclerView.ViewHolder{
         ImageView movieImage ;
         public MovieAdapterViewHolder(View itemView) {
             super(itemView);
             movieImage = (ImageView) itemView.findViewById(R.id.movie_image);
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     if(mCursor.moveToPosition(getAdapterPosition())) {
                         int id = mCursor.getInt(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
                         double voteAverage = mCursor.getDouble(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));
                         String title = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
                         double popularity = mCursor.getDouble(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY));
                         String posterPath = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
                         String overview = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
                         String releaseDate = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
                         boolean isFavorit = mCursor.getInt(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVORIT)) > 0;
                         Movie movie = new Movie(id, voteAverage, title, popularity, posterPath, overview, releaseDate, isFavorit);
                         mOnMovieClickListener.onMovieClick(movie);
                     }
                 }
             });
         }
     }
 }
