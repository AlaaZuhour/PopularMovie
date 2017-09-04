package com.example.alaazuhouer.popularmoive.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.alaazuhouer.popularmoive.data.MovieContract;

/**
 * Created by alaazuhouer on 02/09/17.
 */

public class MovieSyncUtils {
    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context) {
        if (sInitialized) return;

        sInitialized = true;
        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                String[] projection = {MovieContract.MovieEntry.COLUMN_MOVIE_ID};
                Cursor cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        projection,null,null,null);

                if(cursor == null || cursor.getCount() == 0){
                    startImmediateSync(context);
                }
//

            }
        });
        checkForEmpty.start();
    }

    private static void startImmediateSync(Context context) {
        Intent intent = new Intent(context, MovieIntentService.class);
        context.startService(intent);
    }

}
