package com.f.use.printdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by f_ on 2018/5/22.
 * application
 */

public class PrintApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Context getContext() {
        return mContext;
    }
}
