package com.f.use.weexdemo.common;

import android.app.Application;

import com.f.use.weexdemo.weex.MyModule;
import com.f.use.weexdemo.weex_utils.ImageAdapter;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

/**
 * Created by f_ on 2018/5/22.
 * .
 */

public class WxApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initWx();
    }

    private void initWx() {
        InitConfig config = new InitConfig.Builder().setImgAdapter(new ImageAdapter(getApplicationContext())).build();
        WXSDKEngine.initialize(this, config);
        try {
            WXSDKEngine.registerModule("myModule", MyModule.class);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }
}
