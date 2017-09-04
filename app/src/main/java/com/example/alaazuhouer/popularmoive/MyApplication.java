package com.example.alaazuhouer.popularmoive;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by alaazuhouer on 04/09/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

     // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

     // Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );

      // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

       // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }
}
