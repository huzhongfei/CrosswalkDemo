package com.woobo.crosswalkdemo.common.app;

import android.app.Application;

/**
 * @author sanji
 */
public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MySCrashHandler.getInstance().init(this);
    }


}
