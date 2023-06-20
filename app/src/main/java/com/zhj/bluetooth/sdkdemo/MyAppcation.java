package com.zhj.bluetooth.sdkdemo;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;
import android.util.Log;

import com.zhj.zhjsdkcustomized.ble.BleReceiver;


public class MyAppcation extends Application {

    private static  MyAppcation app;
    private boolean connected;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        //腾讯bugly
//        CrashReport.initCrashReport(getApplicationContext(), "42dbea86c2", true);
    }
    public static MyAppcation getInstance() {
        return app;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        Log.d("ggg4","connected:" + connected);
        this.connected = connected;
    }
}
