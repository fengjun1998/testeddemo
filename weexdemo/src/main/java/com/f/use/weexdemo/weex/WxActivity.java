package com.f.use.weexdemo.weex;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.f.test.library.utils.Logger;
import com.f.use.weexdemo.R;
import com.f.use.weexdemo.common.BaseActivity;
import com.f.use.weexdemo.weex_utils.Constant;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.RenderContainer;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.ui.component.NestedContainer;
import com.taobao.weex.utils.WXFileUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by f_ on 2018/5/22.
 * weex
 */

public class WxActivity extends BaseActivity implements IWXRenderListener, WXSDKInstance.NestedInstanceInterceptor {

    private static final String credit_index = "http://pusher.spa.dev.weipeiapp.com/dist/indexa.js";
    private static final String BLANK_NOTE_PAGE_NAME = "com.f.use.weexdemo";
    public static final String WXPAGE = "wxpage";

    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;


    private HashMap mConfigMap = new HashMap<String, Object>();
    private WXSDKInstance mWXSDKInstance;
    private Uri mUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx);
        ButterKnife.bind(this);
        initWXLoad();
        renderPageByURL(credit_index);
    }

    private void initWXLoad() {
        Logger.e("test,initWXLoad");
        mWXSDKInstance = new WXSDKInstance(this);
        mWXSDKInstance.registerRenderListener(this);
    }

    private void renderPageByURL(String mUrl) {
        Logger.e("test,renderPageByURL");
        mUri = getIntent().getData();
        Bundle bundle = getIntent().getExtras();
        if (mUri == null && bundle == null) {
            mUri = Uri.parse(mUrl + Constant.WEEX_SAMPLES_KEY);
        }
        if (bundle != null) {
            String bundleUrl = bundle.getString("bundleUrl");

            if (bundleUrl != null) {
                mConfigMap.put("bundleUrl", bundleUrl + Constant.WEEX_SAMPLES_KEY);
                mUri = Uri.parse(bundleUrl + Constant.WEEX_SAMPLES_KEY);
            }
        } else {
            mConfigMap.put("bundleUrl", mUri.toString() + Constant.WEEX_SAMPLES_KEY);
        }

        if (WXPAGE.equals(mUri.getScheme()) || TextUtils.equals("true", mUri.getQueryParameter("_wxpage"))) {
            mUri = mUri.buildUpon().scheme("http").build();
            loadWXFromService(mUri.toString());
        } else if (TextUtils.equals("http", mUri.getScheme()) || TextUtils.equals("https", mUri.getScheme())) {
            String weexTpl = mUri.getQueryParameter(Constant.WEEX_TPL_KEY);
            String url = TextUtils.isEmpty(weexTpl) ? mUri.toString() : weexTpl;
            loadWXFromService(url);
        } else {
            loadWXFromLocal(false);
        }
    }

    private void loadWXFromService(final String url) {
        Logger.e("test,loadWXFromService");
        pbProgress.setVisibility(View.VISIBLE);

        if (mWXSDKInstance != null) {
            mWXSDKInstance.destroy();
        }

        RenderContainer renderContainer = new RenderContainer(this);
        flContainer.addView(renderContainer);

        mWXSDKInstance = new WXSDKInstance(this);
        mWXSDKInstance.setRenderContainer(renderContainer);
        mWXSDKInstance.registerRenderListener(this);
        mWXSDKInstance.setNestedInstanceInterceptor(this);
        mWXSDKInstance.setBundleUrl(url);
        mWXSDKInstance.setTrackComponent(true);

        Map<String, Object> options = new HashMap<>();
        options.put(WXSDKInstance.BUNDLE_URL, url);
        mWXSDKInstance.renderByUrl(BLANK_NOTE_PAGE_NAME, url, options, null, WXRenderStrategy.APPEND_ONCE);
    }

    private void loadWXFromLocal(boolean reload) {
        Logger.e("test,loadWXFromLocal");
        if (reload && mWXSDKInstance != null) {
            mWXSDKInstance.destroy();
            mWXSDKInstance = null;
        }
        if (mWXSDKInstance == null) {
            RenderContainer renderContainer = new RenderContainer(this);

            mWXSDKInstance = new WXSDKInstance(this);
            mWXSDKInstance.setRenderContainer(renderContainer);
            mWXSDKInstance.registerRenderListener(this);
            mWXSDKInstance.setNestedInstanceInterceptor(this);
            mWXSDKInstance.setTrackComponent(true);
        }
        flContainer.post(new Runnable() {
            @Override
            public void run() {
                Activity ctx = WxActivity.this;
                Rect outRect = new Rect();
                ctx.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
                mConfigMap.put("bundleUrl", mUri.toString());
                String path = "file".equals(mUri.getScheme()) ? assembleFilePath(mUri) : mUri.toString();
                mWXSDKInstance.render("TAG", WXFileUtils.loadAsset(path, WxActivity.this),
                        mConfigMap, null, WXRenderStrategy.APPEND_ASYNC);
            }
        });
    }

    private String assembleFilePath(Uri uri) {
        Logger.e("test,assembleFilePath");
        if (uri != null && uri.getPath() != null) {
            return uri.getPath().replaceFirst("/", "");
        }
        return "";
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        Logger.e("test,onViewCreated");
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        flContainer.addView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        Logger.e("test,onRenderSuccess");
        pbProgress.setVisibility(View.GONE);
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
        Logger.e("test,onRefreshSuccess");
        pbProgress.setVisibility(View.GONE);
    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        Logger.e("test,onException");
        pbProgress.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(errCode) && errCode.contains("|")) {
            String[] errCodeList = errCode.split("\\|");
            String code = errCodeList[1];
            String codeType = errCode.substring(0, errCode.indexOf("|"));

            if (TextUtils.equals("1", codeType)) {
                String errMsg = "codeType:" + codeType + "\n" + " errCode:" + code + "\n" + " ErrorInfo:" + msg;
                degradeAlert(errMsg);
                return;
            } else {
                Toast.makeText(getApplicationContext(), "errCode:" + errCode + " Render ERROR:" + msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void degradeAlert(String errMsg) {
        Logger.e("test,degradeAlert");
        new AlertDialog.Builder(this)
                .setTitle("Downgrade success")
                .setMessage(errMsg)
                .setPositiveButton("OK", null)
                .show();

    }

    @Override
    public void onCreateNestInstance(WXSDKInstance instance, NestedContainer container) {
        Logger.e("test,Nested Instance created.");
    }

    @Override
    protected void onDestroy() {
        Logger.e("test,onDestroy");
        super.onDestroy();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityDestroy();
        }
    }
}
