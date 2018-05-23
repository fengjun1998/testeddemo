package com.f.use.weexdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.f.use.weexdemo.weex.WxActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_weex)
    Button btnWeex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_weex)
    public void gotoWeex(View view) {
        Intent intent = new Intent(this, WxActivity.class);
//        WXNavigatorModule.setIntentCategory(Constant.WEEX_INTENT_CATEGORY_PUSH_MESSAGE);
        startActivity(intent);
    }
}
