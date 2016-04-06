package com.cidadaoandroid.principal;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by gabri on 30/03/2016.
 */
public class CidadaoAplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
