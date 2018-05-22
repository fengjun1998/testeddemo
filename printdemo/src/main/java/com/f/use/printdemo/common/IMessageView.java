package com.f.use.printdemo.common;

/**
 * Created by Administrator on 2016/12/27 0027.
 * 显示提示信息的接口
 */

public interface IMessageView {

    // 显示toast提示
    void showToastMessage(String message, boolean isLong);

    // 显示toast提示
    void showToastMessage(String message);

    // 显示加载loading框
    void showLoadingDialog();

    // 隐藏加载loading框
    void dismissLoadingDialog();

}
