package com.f.use.weexdemo.common;


import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/12/27 0027.
 * presenter的基类
 */

public abstract class BasePresenter<T extends IBaseView> implements IBasePresenter {

    protected T mView;

    public BasePresenter(T mView) {
        this.mView = mView;
    }

    // 获得内存卡资源？？？？
    protected String getResourceDir(Context context) {
        File environmentPath = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            environmentPath = Environment.getExternalStorageDirectory();
            if (context != null) {
                environmentPath = new File(environmentPath, "Android/data/" + context.getPackageName());
            }
        } else {
            if (context != null) {
                environmentPath = context.getFilesDir();
            }
        }

        File baseDirectory = new File(environmentPath, "weipei");

        if (!baseDirectory.exists()) {
            baseDirectory.mkdirs();
        }

        String resourceDir = baseDirectory.getPath();

        return resourceDir;
    }

}
