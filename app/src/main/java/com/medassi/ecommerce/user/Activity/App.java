package com.medassi.ecommerce.user.Activity;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;



public class App extends Application {
    private static App instance;
    private final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
    }

    public static App getInstance() {
        return instance;
    }

    public static void setInstance(App instance) {
        App.instance = instance;
    }
   /* @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }*/

}
