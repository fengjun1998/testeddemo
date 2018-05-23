package com.f.use.weexdemo.weex;

import android.widget.Toast;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

/**
 * Created by fj on 2017/11/24.
 */

public class MyModule extends WXModule {

    @JSMethod
    public void printLog(String msg, JSCallback callback) {
        Toast.makeText(mWXSDKInstance.getContext(), msg, Toast.LENGTH_SHORT).show();
        //获取定位代码.....
//        Map<String, String> data = new HashMap<>();
//        data.put("param", "test");
//        data.put("y", "y");
        //通知一次
        callback.invoke("test");
        //持续通知
        callback.invokeAndKeepAlive("test");

        //invoke方法和invokeAndKeepAlive两个方法二选一
    }
}
