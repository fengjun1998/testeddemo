package com.f.use.printdemo.print;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.f.use.printdemo.R;
import com.f.use.printdemo.common.BaseActivity;
import com.f.use.printdemo.utils.FullyLinearLayoutManager;
import com.f.use.printdemo.utils.PermissionDialogUtils;
import com.f.use.printdemo.utils.PrintCache;
import com.f.use.printdemo.widget.BindDevicePopupWindow;
import com.github.ybq.android.spinkit.SpinKitView;
import com.mikepenz.itemanimators.ScaleXAnimator;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by f_ on 2018/5/22.
 * 扫描打印
 */

@RuntimePermissions
public class ScanPrintActivity extends BaseActivity implements IPrintContract.IPrintView {
    @BindView(R.id.tv_connect_tip)
    TextView tvConnectTip;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_test)
    TextView tvTest;
    @BindView(R.id.tv_scan)
    TextView tvScan;
    @BindView(R.id.cb_scan)
    CheckBox cbScan;
    @BindView(R.id.rv_un_bond_device_list)
    RecyclerView rvUnBondDeviceList;
    @BindView(R.id.li_bonded)
    LinearLayout liBonded;
    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    private UnBondDeviceAdapter unBondDeviceAdapter;
    private ManageDevicePresenter devicePresenter;
    private LabelPrintPresenter printPresenter;

    private final List<BluetoothDevice> mNoneBondDeviceList = new ArrayList<BluetoothDevice>();
    private BluetoothDevice mCurrentDevice;

    // 扫描蓝牙
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                handleFoundDevice(intent);
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                if (state == BluetoothAdapter.STATE_ON) {
                    displayBondedDevices();
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int bondState = device.getBondState();
                if (bondState == BluetoothDevice.BOND_BONDED) {
                    unBondDeviceAdapter.addData(mCurrentDevice);
                    if (!mNoneBondDeviceList.contains(mCurrentDevice)) {
                        mNoneBondDeviceList.add(mCurrentDevice);
                    }

                    unBondDeviceAdapter.removeData(device);
                    mNoneBondDeviceList.remove(device);

                    mCurrentDevice = device;
                    displayCurrentBindDevice(mCurrentDevice);

                    PrintCache.setLabelPrintDevice(mCurrentDevice);
                    PrintCache.setLabelPrinterAddress(mCurrentDevice.getAddress());

                } else if (bondState == BluetoothDevice.BOND_NONE) {
                    mCurrentDevice = null;
                    PrintCache.setLabelPrintDevice(null);
                    PrintCache.setLabelPrinterAddress(null);
                    displayCurrentBindDevice(null);
                    unBondDeviceAdapter.addData(device);
                    if (!mNoneBondDeviceList.contains(device)) {
                        mNoneBondDeviceList.add(device);
                    }
                }
            }
        }
    };

    private void handleFoundDevice(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        BluetoothDevice labelPrintDevice = PrintCache.getLabelPrintDevice();
        if (!device.equals(mCurrentDevice)) {
            if (device.equals(labelPrintDevice)) {
                return;
            }
            if (!mNoneBondDeviceList.contains(device)) {
                mNoneBondDeviceList.add(device);
            }
            unBondDeviceAdapter.addData(device);
        } else {
            unBondDeviceAdapter.removeData(device);
            if (mNoneBondDeviceList.contains(device)) {
                mNoneBondDeviceList.remove(device);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.activity_scan_print);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        if (devicePresenter.isBluetoothEnabled()) {
            startScanDevices();
        } else {
            cancelScanDevices();
        }
        initView();
    }

    private void initData() {
        unBondDeviceAdapter = new UnBondDeviceAdapter(this);
        devicePresenter = new ManageDevicePresenter(this);
        printPresenter = new LabelPrintPresenter(this);
        registerBluetoothReceiver();
    }

    private void registerBluetoothReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction("com.print.test");
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void initView() {
        rvUnBondDeviceList.setLayoutManager(new FullyLinearLayoutManager(this));
        rvUnBondDeviceList.setAdapter(unBondDeviceAdapter);
        rvUnBondDeviceList.setItemAnimator(new ScaleXAnimator());

        displayBondedDevices();
    }

    @OnClick(R.id.cb_scan)
    public void scanBlueToothDevice(View view) {
        boolean isCheck = cbScan.isChecked();
        boolean isEnable = devicePresenter.isBluetoothEnabled();
        if (!isEnable) {
            cbScan.setChecked(false);
            return;
        }
        if (isCheck) {
            startScanDevices();
            cbScan.setChecked(true);
        } else {
            cancelScanDevices();
            cbScan.setChecked(false);
        }
    }

    private void startScanDevices() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ScanPrintActivityPermissionsDispatcher.enableBluetoothOnTopSdkWithCheck(this);
        } else {
            enableBluetooth();
        }

        cbScan.setChecked(true);
        spinKit.setVisibility(View.VISIBLE);
        mNoneBondDeviceList.clear();
        unBondDeviceAdapter.clear();
        devicePresenter.startScanDevices();

    }

    private void cancelScanDevices() {
        if (!devicePresenter.isBluetoothEnabled()) {
            devicePresenter.enableBluetooth();
            return;
        }
        cbScan.setChecked(false);
        spinKit.setVisibility(View.GONE);
        devicePresenter.cancelScanDevices();
    }

    /**
     * 当SDK的level>23的时候需要ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION的权限
     */
    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void enableBluetoothOnTopSdk() {
        if (!devicePresenter.isBluetoothEnabled()) {
            devicePresenter.enableBluetooth();
            return;
        }
    }

    public void enableBluetooth() {
        if (!devicePresenter.isBluetoothEnabled()) {
            devicePresenter.enableBluetooth();
            return;
        }
    }

    @Subscribe
    public void onEvent(BondEvent event) {
        final BluetoothDevice device = event.device;
        final BindDevicePopupWindow popupWindow = new BindDevicePopupWindow(this);
        popupWindow.displayDeviceInfo(device);
        popupWindow.onCancelListener();
        popupWindow.onConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();

                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    unBondDeviceAdapter.addData(mCurrentDevice);
                    if (!mNoneBondDeviceList.contains(mCurrentDevice)) {
                        mNoneBondDeviceList.add(mCurrentDevice);
                    }

                    unBondDeviceAdapter.removeData(device);
                    mNoneBondDeviceList.remove(device);
                    mCurrentDevice = device;
                    displayCurrentBindDevice(mCurrentDevice);

                    PrintCache.setLabelPrintDevice(mCurrentDevice);
                    PrintCache.setLabelPrinterAddress(mCurrentDevice.getAddress());
                    devicePresenter.saveLabelDeviceInfoToPreference(mCurrentDevice);
                } else {

                    boolean isSuccess = devicePresenter.bindDevice(device);
                    if (isSuccess) {

                    } else {
                        showToastMessage("绑定设备失败", false);
                    }
                }
            }
        });
        popupWindow.show();
    }

    @OnClick(R.id.tv_test)
    public void test(View view) {
        BluetoothDevice device = mCurrentDevice;
        printPresenter.postPrintLabelTest(device);
    }

    @Subscribe
    public void onEvent(PrintLabelEvent event) {
        printPresenter.postPrintLabelTest(mCurrentDevice);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ScanPrintActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @OnPermissionDenied({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void onPermissionDenied() {
        PermissionDialogUtils.locationPermissionSetDialog(this);
    }


    @OnNeverAskAgain({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void onNeverAsk() {
        PermissionDialogUtils.locationPermissionSetDialog(this);
    }

    private void displayBondedDevices() {
        List<BluetoothDevice> deviceSet = devicePresenter.getBondedDevices();
        List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();
        if (deviceSet != null) {
            deviceList.addAll(deviceSet);
        }

        String defaultDeviceAddress = null;
        defaultDeviceAddress = PrintCache.getLabelPrinterAddress();

        if (StringUtils.isNotEmpty(defaultDeviceAddress)) {
            BluetoothDevice device = devicePresenter.findDeviceByAddress(defaultDeviceAddress);
            if (deviceList.contains(device)) {
                mCurrentDevice = device;
                displayCurrentBindDevice(mCurrentDevice);
            } else {
                mCurrentDevice = null;
                displayCurrentBindDevice(null);
                PrintCache.setLabelPrintDevice(null);
                PrintCache.setLabelPrinterAddress(null);
            }
        } else {
            mCurrentDevice = null;
            displayCurrentBindDevice(null);
            PrintCache.setLabelPrintDevice(null);
            PrintCache.setLabelPrinterAddress(null);
        }

    }

    private void displayCurrentBindDevice(BluetoothDevice device) {
        if (device != null) {
            tvName.setText(device.getName());
            tvAddress.setText(device.getAddress());
            liBonded.setVisibility(View.VISIBLE);
        } else {
            liBonded.setVisibility(View.GONE);
        }
    }


    @Override
    public void configBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        // 设置蓝牙可见性，最多300秒
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(intent);
    }

    @Override
    public void onClosePrinterFailed(String message) {

    }

    @Override
    public void onClosePrinterSuccess() {

    }

    @Override
    public void onOpenPrinterSuccess() {

    }

    @Override
    public void onOpenPrinterFailed(String message) {
        showToastMessage(message, false);
    }

    @Override
    public void onPrinterSuccess() {

    }

    @Override
    public void onPrinterFailed(String message) {
        showToastMessage(message, false);
    }

    @Override
    public int getLogoResource() {
        return 0;
    }

    @Override
    public void onScanStart() {
        spinKit.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScanCancel() {
        spinKit.setVisibility(View.GONE);
    }

    @Override
    public void onNoteTemplateConfig() {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        devicePresenter.cancelScanDevices();
        printPresenter.closeLabelPort();
        printPresenter.unRegisterEvent();
        unregisterReceiver(mBroadcastReceiver);
        EventBus.getDefault().unregister(this);
    }
}
