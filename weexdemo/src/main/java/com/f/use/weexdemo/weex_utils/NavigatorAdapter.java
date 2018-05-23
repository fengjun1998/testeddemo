package com.f.use.weexdemo.weex_utils;

import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;

/**
 * Created by fj on 2017/11/28.
 * adapter
 */

public class NavigatorAdapter implements IActivityNavBarSetter {

    @Override
    public boolean push(String param) {
        return false;
    }

    @Override
    public boolean pop(String param) {
        return false;
    }

    @Override
    public boolean setNavBarRightItem(String param) {
        return false;
    }

    @Override
    public boolean clearNavBarRightItem(String param) {
        return false;
    }

    @Override
    public boolean setNavBarLeftItem(String param) {
        return false;
    }

    @Override
    public boolean clearNavBarLeftItem(String param) {
        return false;
    }

    @Override
    public boolean setNavBarMoreItem(String param) {
        return false;
    }

    @Override
    public boolean clearNavBarMoreItem(String param) {
        return false;
    }

    @Override
    public boolean setNavBarTitle(String param) {
        return false;
    }
}

