package com.f.use.printdemo.utils;


import android.bluetooth.BluetoothDevice;

public class PrintCache {
    private static BluetoothDevice labelPrintDevice;
    private static String LabelPrinterAddress;

    public static BluetoothDevice getLabelPrintDevice() {
        return labelPrintDevice;
    }

    public static void setLabelPrintDevice(BluetoothDevice labelPrintDevice) {
        PrintCache.labelPrintDevice = labelPrintDevice;
    }

    public static String getLabelPrinterAddress() {
        return LabelPrinterAddress;
    }

    public static void setLabelPrinterAddress(String labelPrinterAddress) {
        LabelPrinterAddress = labelPrinterAddress;
    }
}
