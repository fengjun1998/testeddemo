package com.f.use.printdemo.widget;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.f.use.printdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/6 0006.
 * 打印机绑定提示弹出框
 */

public class BindDevicePopupWindow {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_icon)
    ImageView mIvIcon;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.li_button)
    LinearLayout mLiButton;

    private PopupWindow popupWindow;

    private Activity mContext;

    private View mView;


    public BindDevicePopupWindow(Activity context) {
        this.mContext = context;
        initPopupWindow();

    }


    private void initPopupWindow() {
        popupWindow = new PopupWindow();
        final Window window = mContext.getWindow();
        View view = LayoutInflater.from(mContext).inflate(R.layout.bind_device_popup_window, null);
        mView = view;

//        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
//        tvConfirm = (TextView) view.findViewById(R.id.tv_confirm);
//        NoScrollListView lvAppointmentSheet = (NoScrollListView) view.findViewById(R.id.lv_appointment_sheet);
//        lvAppointmentSheet.setAdapter(adapter);

        popupWindow.setContentView(view);
        ButterKnife.bind(this, view);
        popupWindow.setWidth(ActionBar.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
//        popupWindow.setBackgroundDrawable(dw);

        popupWindow.setOutsideTouchable(true);//设置非popuWindow区域可触
        popupWindow.setFocusable(true);//设置是否可以获得焦点
        popupWindow.setTouchable(true);//设置是否可以被触摸
        popupWindow.setAnimationStyle(R.style.popup_window_appointment);
        popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.color.white_main));
        //窗口消失时候回复的监听方法
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.alpha = 1f;
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setAttributes(lp);
            }
        });
    }

    public void show() {
        //产生背景变暗效果
        Window window = mContext.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.6f;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
        popupWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
    }


    public void onCancelListener() {
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public void onConfirmListener(View.OnClickListener listener) {
        mBtnConfirm.setOnClickListener(listener);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    public void displayDeviceInfo(BluetoothDevice device) {
        if (device == null) {
            return;
        }
        mTvName.setText(device.getName());
        mTvAddress.setText(device.getAddress());

    }


}
