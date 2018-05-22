package com.f.use.printdemo.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by fj on 2017/7/27.
 * 获取系统权限提示
 */

public class PermissionDialogUtils {

    // 提示拍照权限的设置
    public static void cameraPermissionSetDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提醒");
        builder.setMessage("当前应用缺少拍照权限，请点击设置打开权限后再回来");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", "com.yipei.weipeilogistics", null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    // 提示拍照位置权限的设置
    public static void locationPermissionSetDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提醒");
        builder.setMessage("当前应用缺少获取位置权限，请点击设置打开权限后再回来");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", "com.yipei.weipeilogistics", null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    // 提示拨打电话权限的设置
    public static void phonePermissionSetDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提醒");
        builder.setMessage("当前应用缺少拨打电话的权限，请点击设置打开权限后再回来");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", "com.yipei.weipeilogistics", null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    // 提示存储权限的设置
    public static void storagePermissionSetDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提醒");
        builder.setMessage("当前应用缺少读写文件的权限，请点击设置打开权限后再回来");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", "com.yipei.weipeilogistics", null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

}
