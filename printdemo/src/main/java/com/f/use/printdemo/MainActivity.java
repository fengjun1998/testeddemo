package com.f.use.printdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.f.use.printdemo.print.ScanPrintActivity;
import com.f.test.library.utils.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_print)
    Button btnPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Logger.e("test,onCreate");
    }

    @OnClick(R.id.btn_print)
    public void print(View view){
        Logger.e("test,goto print activity");
        Intent intent = new Intent(this, ScanPrintActivity.class);
        startActivity(intent);
    }
}
