package com.example.alaazuhouer.popularmoive.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by alaazuhouer on 03/09/17.
 */

public class MovieIntentService extends IntentService {
    public MovieIntentService() {
        super("MovieIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        MovieSyncTasks.syncMovies(this);
        MovieSyncTasks.syncTrailersAndReviews(this);

    }
}
