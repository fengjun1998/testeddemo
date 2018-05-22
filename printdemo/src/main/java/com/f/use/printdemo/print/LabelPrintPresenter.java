package com.f.use.printdemo.print;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.f.use.printdemo.R;
import com.f.use.printdemo.common.BasePresenter;
import com.f.use.printdemo.printUtils.DeviceConnFactoryManager;
import com.f.use.printdemo.printUtils.PrinterCommand;
import com.f.use.printdemo.printUtils.ThreadPool;
import com.f.use.printdemo.utils.Logger;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Vector;

import static com.f.use.printdemo.printUtils.DeviceConnFactoryManager.CONN_STATE_FAILED;

/**
 * Created by fj on 2017/11/21.
 * 标签打印
 */

public class LabelPrintPresenter extends BasePresenter<IPrintContract.IPrintView> implements IPrintContract.ILabelPrintPresenter {
    private static final int MESSAGE_PRINTER_OPEN_FAILED = R.id.printer_open_failed;
    private static final int MESSAGE_PRINT_SUCCESS = 2;
    private static final int MESSAGE_FAIL = 1;

    private boolean mIsStartPrint;
    private Handler mAsyncHandler;
    private ThreadPool threadPool;
    private boolean isLabelDeviceConn;


    private Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_FAIL) {
                if (mView != null) {
                    mIsStartPrint = false;
                    String message = (String) msg.obj;
                    mView.onPrinterFailed(message);
                }
            } else if (msg.what == MESSAGE_PRINTER_OPEN_FAILED) {
                if (mView != null) {
                    mIsStartPrint = false;
                    String message = (String) msg.obj;
                    Logger.e("test,打印失败~");
                    mView.onOpenPrinterFailed(message);
                }
            } else if (msg.what == MESSAGE_PRINT_SUCCESS) {
                if (mView != null) {
                    mIsStartPrint = false;
                    mView.onPrinterSuccess();
                }
            }
        }
    };

    /**
     * 连接状态断开
     */
    private static final int CONN_STATE_DISCONN = 0x007;
    /**
     * 使用打印机指令错误
     */
    private static final int MESSAGE_UPDATE_PARAMETER = 0x009;
    private static final int PRINTER_COMMAND_ERROR = 0x008;
    private static final int CONN_MOST_DEVICES = 0x11;
    private static final int CONN_PRINTER = 0x12;
    private int id = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONN_STATE_DISCONN:
                    if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null) {
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].closePort(id);
                    }
                    break;
                case PRINTER_COMMAND_ERROR:
                    mView.showToastMessage(mView.getContext().getString(R.string.str_choice_printer_command));
                    break;
                case CONN_PRINTER:
                    mView.showToastMessage(mView.getContext().getString(R.string.str_cann_printer));
                    break;
                case MESSAGE_UPDATE_PARAMETER:
                    String strIp = msg.getData().getString("Ip");
                    String strPort = msg.getData().getString("Port");
                    //初始化端口信息
                    new DeviceConnFactoryManager.Build()
                            //设置端口连接方式
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.WIFI)
                            //设置端口IP地址
                            .setIp(strIp)
                            //设置端口ID（主要用于连接多设备）
                            .setId(id)
                            //设置连接的热点端口号
                            .setPort(Integer.parseInt(strPort))
                            .build();
                    threadPool = ThreadPool.getInstantiation();
                    threadPool.addTask(new Runnable() {
                        @Override
                        public void run() {
                            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    public LabelPrintPresenter(IPrintContract.IPrintView mView) {
        super(mView);
        EventBus.getDefault().register(this);
        initData();
    }

    @Override
    public void unRegisterEvent() {
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
        HandlerThread newThread = new HandlerThread(LabelPrintPresenter.class.getSimpleName());
        newThread.start();
        mAsyncHandler = new Handler(newThread.getLooper());
    }


    private boolean[] openLabelPort(final BluetoothDevice device, boolean isCreatePrinter) {
        final boolean[] isOpenPort = {false};
        mAsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                //初始化话DeviceConnFactoryManager
                if (device != null && StringUtils.isNotEmpty(device.getAddress())) {
                    mView.showToastMessage("正在连接打印机端口，请等待…");
                    new DeviceConnFactoryManager.Build()
                            .setId(id)
                            //设置连接方式
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                            //设置连接的蓝牙mac地址
                            .setMacAddress(device.getAddress())
                            .build();
                    //打开端口
                    isOpenPort[0] = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                } else {
                    mView.showToastMessage("请连接打印机");
                    isOpenPort[0] = false;
                }
            }
        });
        if (isCreatePrinter && !isOpenPort[0]) {
            sendOpenFailMessage();
            return isOpenPort;
        }
        return isOpenPort;
    }

    @Subscribe
    public void onEvent(LabelConnEvent event) {
        Intent intent = event.intent;
        int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
        int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
        switch (state) {
            case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                if (id == deviceId) {
                    mView.showToastMessage("连接状态：未连接");
                }
                break;
            case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                mView.showToastMessage("连接状态：连接中");
                break;
            case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                isLabelDeviceConn = true;
                mView.showToastMessage("连接状态：已连接");
                EventBus.getDefault().post(new PrintLabelEvent());
                break;
            case CONN_STATE_FAILED:
                mView.showToastMessage("该设备不支持串口");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean getLabelDeviceConnectState() {
        return isLabelDeviceConn;
    }

    @Override
    public void closeLabelPort() {
        DeviceConnFactoryManager.closeAllPort();
        if (threadPool != null) {
            threadPool.stopThreadPool();
        }
    }

    // todo 测试打印
    @Override
    public void postPrintLabelTest(final BluetoothDevice device) {
        if (isLabelDeviceConn) {
            sendPrintLabelTest();
        } else {
            openLabelPort(device, false);
        }

    }

    private void sendPrintLabelTest() {

        if (mIsStartPrint) {
            sendFailMessage("正在打印中");
            return;
        }
        mAsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
                        !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
                    mHandler.obtainMessage(CONN_PRINTER).sendToTarget();
                    return;
                }

                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.TSC) {
                    boolean isPrintSuccess = startPrintTestLabel();
                    if (isPrintSuccess) {
                        sendPrintSuccessMessage();
                    }
                } else {
                    mHandler.obtainMessage(PRINTER_COMMAND_ERROR).sendToTarget();
                }
            }
        });
    }

    public boolean startPrintTestLabel() {
        boolean isSuccess = false;
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(72, 50); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);// 开启带Response的打印，用于连续打印
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区

        int xStart = 20;
        int mul1TextSize = 30;// 小字号
        int mul2TextSize = 100;// 大字号

        tsc.addText(xStart, mul2TextSize, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "标签打印");

        tsc.addText(xStart, mul1TextSize + mul2TextSize, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                "测试测试测试测试");

        tsc.addPrint(1, 1); // 打印标签
//        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);

        Vector<Byte> datas = tsc.getCommand();
        // 发送数据
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null) {
            isSuccess = false;
        }
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(datas);
        isSuccess = true;
        return true;
    }

    private void sendOpenFailMessage() {
        Message handlerMessage = mainHandler.obtainMessage();
        handlerMessage.what = MESSAGE_PRINTER_OPEN_FAILED;
        mainHandler.sendMessage(handlerMessage);
    }

    private void sendFailMessage(String message) {
        Message handlerMessage = mainHandler.obtainMessage();
        handlerMessage.what = MESSAGE_FAIL;
        handlerMessage.obj = message;
        mainHandler.sendMessage(handlerMessage);
    }

    private void sendPrintSuccessMessage() {
        Message handlerMessage = mainHandler.obtainMessage();
        handlerMessage.what = MESSAGE_PRINT_SUCCESS;
        mainHandler.sendMessage(handlerMessage);
    }

}
