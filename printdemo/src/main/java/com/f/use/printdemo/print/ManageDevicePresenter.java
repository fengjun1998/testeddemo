package com.f.use.printdemo.print;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.HandlerThread;

import com.f.use.printdemo.common.BasePresenter;
import com.f.use.printdemo.utils.ClsUtils;
import com.f.use.printdemo.utils.Logger;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by f_ on 2018/5/22.
 * 扫描蓝牙
 */

public class ManageDevicePresenter extends BasePresenter<IPrintContract.IPrintView> implements IPrintContract.IManageDevicePresenter {

    private BluetoothAdapter mAdapter;

    private boolean isEnable;

    private Handler mAsyncHandler;

    private HandlerThread mAsyncThread;

    public ManageDevicePresenter(IPrintContract.IPrintView mView) {
        super(mView);
        initData();
    }


    private void initData() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        HandlerThread newThread = new HandlerThread(ManageDevicePresenter.class.getSimpleName());
        newThread.start();
        mAsyncHandler = new Handler(newThread.getLooper());
    }


    @Override
    public void startScanDevices() {
        boolean isEnable = isBluetoothEnabled();
        if (!isEnable) {
            return;
        }

        boolean isSuccess = mAdapter.startDiscovery();
        Logger.e("startScanDevices() -- isSuccess is " + isSuccess);
    }

    @Override
    public void cancelScanDevices() {
        boolean isEnable = enableBluetooth();
        Logger.e("test,startScanDevices() -- isEnable is " + isEnable);
        if (!isEnable) {
            return;
        }

        if (!mAdapter.isDiscovering())
            return;
        mAdapter.cancelDiscovery();
    }

    @Override
    public boolean isTargetDevice(BluetoothDevice device) {
        return true;
    }

    @Override
    public boolean unbindDevice(BluetoothDevice device) {
        boolean isEnable = enableBluetooth();
        Logger.e("startScanDevices() -- isEnable is " + isEnable);
        if (!isEnable) {
            return false;
        }
        boolean isResult = false;
        try {
            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                Method unbindMethod = BluetoothDevice.class.getMethod("removeBond");
                isResult = (Boolean) unbindMethod.invoke(device);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.e("unbindDevice() - isResult is " + isResult);
        return isResult;
    }

    @Override
    public List<BluetoothDevice> getBondedDevices() {
        if (mAdapter == null) {
            return null;
        }

        List<BluetoothDevice> resultList = new ArrayList<BluetoothDevice>();
        Set<BluetoothDevice> tempSet = mAdapter.getBondedDevices();
        if (tempSet != null && !tempSet.isEmpty()) {
            for (BluetoothDevice device : tempSet) {
                if (isTargetDevice(device)) {
                    resultList.add(device);
                }
            }
        }

        return resultList;
    }


    @Override
    public boolean isBluetoothEnabled() {
        if (mAdapter == null) {
            return false;
        }
        return mAdapter.isEnabled();
    }

    @Override
    public boolean bindDevice(BluetoothDevice device) {
        boolean isEnable = enableBluetooth();
        Logger.e("test,startScanDevices() -- isEnable is " + isEnable);
        if (!isEnable) {
            return false;
        }

        cancelScanDevices();
        boolean isResult = false;
        try {
            if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                ClsUtils.createBond(device.getClass(), device);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return isResult;
    }

    @Override
    public boolean enableBluetooth() {
        boolean isEnable = isBluetoothEnabled();
        if (isEnable) {
            return true;
        }
        if (mAdapter == null) {
            return false;
        } else {
            return mAdapter.enable();
        }

    }

    @Override
    public void saveDeviceInfoToPreference(final BluetoothDevice device) {
        mAsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                //保存打印机到本地
            }
        });

    }


    public void saveLabelDeviceInfoToPreference(final BluetoothDevice device) {
        mAsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                // 保存打印机到本地
            }
        });
    }


    @Override
    public BluetoothDevice findDeviceByAddress(String deviceAddress) {

        List<BluetoothDevice> deviceList = getBondedDevices();

        Logger.e("findDeviceByAddress() -- deviceList is " + deviceList);

        if (deviceList == null || deviceList.isEmpty()) {
            return null;
        }

        if (StringUtils.isEmpty(deviceAddress)) {
            return null;
        }

        for (BluetoothDevice device : deviceList) {
            if (deviceAddress.equals(device.getAddress())) {
                return device;
            }
        }

        return null;


    }
}
