package com.f.test.library.utils;


import android.util.Log;

/**
 * Created by Administrator on 2016/12/28 0028.
 * log工具类
 */

public class Logger {

    public final static void i() {
        Log.i(tag(), "");
    }

    public final static void i(String msg) {
        Log.i(tag(), msg);
    }

    public final static void d(String msg) {
        Log.d(tag(), msg);
    }

    public final static void e(String msg) {
        Log.e(tag(), msg);
    }

    public final static void w(String msg) {
        Log.w(tag(), msg);
    }

    private final static String tag() {
        Throwable t = new Throwable();
        StackTraceElement[] stackTrace = t.getStackTrace();
        StackTraceElement caller = stackTrace[2];
        String className = caller.getClassName();
        int index = className.lastIndexOf(".");
        String name = className.substring(index + 1);

        String tag = name + "." + caller.getMethodName();
        return tag;
    }

}
