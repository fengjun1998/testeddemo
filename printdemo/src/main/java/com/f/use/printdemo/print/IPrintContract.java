package com.f.use.printdemo.print;


import android.bluetooth.BluetoothDevice;

import com.f.use.printdemo.common.IBasePresenter;
import com.f.use.printdemo.common.IBaseView;
import com.f.use.printdemo.common.IMessageView;

import java.util.List;

/**
 * Created by f_ on 2018/5/22.
 * 打印
 */

public interface IPrintContract {

    // 打印-界面
    interface IPrintView extends IBaseView, IMessageView {

        /**
         * 让用户去设置蓝牙打印
         */
        public void configBluetooth();


        public void onClosePrinterFailed(String message);

        public void onClosePrinterSuccess();

        public void onOpenPrinterSuccess();

        /**
         * 打开打印机失败的回调方法
         *
         * @param message
         */
        public void onOpenPrinterFailed(String message);

        /**
         * 打印机打印成功的回调方法
         */
        public void onPrinterSuccess();

        /**
         * 打印机打印失败的回调方法
         *
         * @param message
         */
        public void onPrinterFailed(String message);

        public int getLogoResource();

        public void onScanStart();

        public void onScanCancel();

        public void onNoteTemplateConfig();


    }

    // 扫描蓝牙
    interface IManageDevicePresenter {

        /**
         * 开始扫描周围的蓝牙设备
         */
        public void startScanDevices();

        /**
         * 取消扫描周围的蓝牙设备
         */
        public void cancelScanDevices();

        /**
         * 判断该设备是不是目标打印机
         *
         * @param device
         * @return
         */
        public boolean isTargetDevice(BluetoothDevice device);

        /**
         * 解除配对指定的设备
         *
         * @param device
         * @return
         */
        public boolean unbindDevice(BluetoothDevice device);


        /**
         * 获取配对的设备列表
         *
         * @return
         */
        public List<BluetoothDevice> getBondedDevices();

        /**
         * 判断是否打开了蓝牙开关
         *
         * @return
         */
        public boolean isBluetoothEnabled();

        /**
         * 让手机和蓝牙设备进行配对
         *
         * @param device
         * @return
         */
        public boolean bindDevice(BluetoothDevice device);

        /**
         * 打开蓝牙设置
         *
         * @return
         */
        public boolean enableBluetooth();


        /**
         * 存设为默认的打印机的MAC地址到本地缓存
         *
         * @param device
         */
        public void saveDeviceInfoToPreference(final BluetoothDevice device);


        public void saveLabelDeviceInfoToPreference(final BluetoothDevice device);

        /**
         * 根据MAC地址找到对应的设备
         *
         * @param deviceAddress
         * @return
         */
        public BluetoothDevice findDeviceByAddress(String deviceAddress);

    }

    // 标签打印
    interface ILabelPrintPresenter extends IBasePresenter {

        void closeLabelPort();

        boolean getLabelDeviceConnectState();

        // 标签打印测试
        void postPrintLabelTest(final BluetoothDevice device);

        void unRegisterEvent();
    }

}
