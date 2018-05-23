package com.f.use.printdemo.printUtils;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by f_ on 2018/5/22.
 * 扫描蓝牙
 */

public class ClsUtils {

    public static BluetoothDevice remoteDevice=null;
    /**
     * 与设备配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    @SuppressWarnings("unchecked")
    static public boolean createBond(@SuppressWarnings("rawtypes") Class btClass, BluetoothDevice btDevice)
            throws Exception
    {
        Method createBondMethod = btClass.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    /**
     * 与设备解除配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    @SuppressWarnings("unchecked")
    static public boolean removeBond(Class btClass, BluetoothDevice btDevice)
            throws Exception
    {
        Method removeBondMethod = btClass.getMethod("removeBond");
        Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

    @SuppressWarnings("unchecked")
    static public boolean setPin(Class btClass, BluetoothDevice btDevice,
                                 String str) throws Exception
    {
        try
        {
            Method removeBondMethod = btClass.getDeclaredMethod("setPin",
                    new Class[]
                            {byte[].class});
            Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
                    new Object[]
                            {str.getBytes()});
            Log.d("returnValue", "setPin is success " +btDevice.getAddress()+ returnValue.booleanValue());
        }
        catch (SecurityException e)
        {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;

    }

    // 取消用户输入
    @SuppressWarnings("unchecked")
    static public boolean cancelPairingUserInput(Class btClass,
                                                 BluetoothDevice device)

            throws Exception
    {
        Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
        // cancelBondProcess()
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        Log.d("returnValue", "cancelPairingUserInput is success " + returnValue.booleanValue());
        return returnValue.booleanValue();
    }

    // 取消配对
    @SuppressWarnings("unchecked")
    static public boolean cancelBondProcess(Class btClass,
                                            BluetoothDevice device)

            throws Exception
    {
        Method createBondMethod = btClass.getMethod("cancelBondProcess");
        Boolean returnValue = (Boolean) createBondMethod.invoke(device);
        return returnValue.booleanValue();
    }

    static public boolean setParingConfirmation(Class btClass, BluetoothDevice device, boolean confirm)throws Exception {
        Method method = btClass.getMethod("setPairingConfirmation",Boolean.class);
        Boolean returnValue = (Boolean)method.invoke(device,confirm);
        return returnValue.booleanValue();
    }


    /**
     *
     * @param clsShow
     */
    @SuppressWarnings("unchecked")
    static public void printAllInform(Class clsShow)
    {
        try
        {
            // 取得所有方法
            Method[] hideMethod = clsShow.getMethods();
            int i = 0;
            for (; i < hideMethod.length; i++)
            {
                //Log.e("method name", hideMethod.getName() + ";and the i is:"
                //      + i);
            }
            // 取得所有常量
            Field[] allFields = clsShow.getFields();
            for (i = 0; i < allFields.length; i++)
            {
                //Log.e("Field name", allFields.getName());
            }
        }
        catch (SecurityException e)
        {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        }
        catch (IllegalArgumentException e)
        {
            // throw new RuntimeException(e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
