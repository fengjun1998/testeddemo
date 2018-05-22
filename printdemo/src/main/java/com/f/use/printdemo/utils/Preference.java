package com.f.use.printdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by fj on 2017/1/6.
 * 本地缓存工具类
 */

public class Preference {

    // 打印
    public static final String KEY_DEFAULT_PRINTER_TYPE = "default_printer_TYPE";
    public static final String KEY_LABEL_DEVICE_ADDRESS = "label_device_address";
    public static final String KEY_RECEIPT_DEVICE_ADDRESS = "device_address";

    public static final String RETURNED_CANCEL_TIME = "returned_cancel_time";
    private static final String PREFERENCE_NAME = "weipeiLogistics";
    public static final String KEY_CAMERA_FILE = "camera_file";
    public static final String TOKEN = "token";

    public static final String KEY_DEFAULT_RECEIPT_PRINT_SETTING = "default_receipt_print_setting";
    public static final String KEY_IS_NEED_PRINT_SETTING = "is_need_print_setting";
    public static final String KEY_IS_CREATE_PRINT_CORNER = "is_create_print_corner";
    public static final String KEY_IS_DETAIL_PRINT_CORNER = "is_detail_print_corner";
    public static final String KEY_DEFAULT_PRINT_SETTING = "default_print_setting";
    public static final String KEY_DEFAULT_CREATE_PAGE = "default_create_page";
    public static final String KEY_DEFAULT_DETAIL_PAGE = "default_detail_page";
    public static final String KEY_CREATE_PRINT_INFO = "create_print_info";
    public static final String KEY_DETAIL_PRINT_INFO = "detail_print_info";
    public static final String KEY_DEFAULT_PAGE = "default_page";


    public static final String KEY_DELIVERY_WAY = "delivery_way";

    private static SharedPreferences preferences;

    public static void init(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
    }

    public static void put(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String get(String key) {
        return preferences.getString(key, null);
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(String key) {
        return preferences.getLong(key, 0);
    }

    public static int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key) {
        return preferences.getBoolean(key, true);
    }

    public static boolean getBoolean(String key, boolean isDefault) {
        return preferences.getBoolean(key, isDefault);
    }


    public static void putObject(Context context, Object object, String objectName) {
        SharedPreferences preferences = context.getSharedPreferences("base64", Activity.MODE_PRIVATE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean isSuccess = false;
        try {

            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            //  String oAuth_Base64 = new String(Base64.encodeBase64(baos.toByteArray()));
            String oAuth_Base64 = new String(baos.toByteArray());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(objectName, oAuth_Base64);

            isSuccess = editor.commit();
        } catch (IOException e) {
            // TODO Auto-generated
        }
    }

    public static Object getObject(Context context, String objectName) {
        Object object = null;
        SharedPreferences preferences = context.getSharedPreferences("base64", Activity.MODE_PRIVATE);
        String productBase64 = preferences.getString(objectName, "");

//        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());
        byte[] base64 = productBase64.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {

                object = bis.readObject();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return object;
    }

    //清除缓存
    public static void clear() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
    }

    //清除缓存
    public static void clear(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
