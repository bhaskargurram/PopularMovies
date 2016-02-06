package com.bhaskar.popularmovies;

/**
 * Created by bhaskar on 4/2/16.
 */

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        final Context context=this;
        Stetho.initialize(Stetho.newInitializerBuilder(context)
                .enableDumpapp(new DumperPluginsProvider() {
                    @Override
                    public Iterable<DumperPlugin> get() {
                        return new Stetho.DefaultDumperPluginsBuilder(context).finish();
                    }
                })
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }
}
