package com.zhj.bluetooth.sdkdemo.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.dialog.DialogHelperNew;
import com.zhj.bluetooth.sdkdemo.sevice.AssistService;
import com.zhj.bluetooth.sdkdemo.sevice.PhoneReceiver;
import com.zhj.bluetooth.sdkdemo.util.PhoneUtil;
import com.zhj.zhjsdkcustomized.ble.CmdHelper;
import com.zhj.zhjsdkcustomized.ble.DeviceCallbackWrapper;
import com.zhj.zhjsdkcustomized.ble.bluetooth.BluetoothLe;

import java.util.List;

public class TelephoAvtivity extends BaseActivity {

    private static final int REQUEST_NOTICE_PERMISSION_CODE = 0x12;
    private Dialog mDialog;
    @Override
    protected int getContentView() {
        return R.layout.activity_telepho;
    }

    @Override
    protected void initView() {
        super.initView();

        //Start service
        Intent assistIntent = new Intent(this, AssistService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(assistIntent);
        } else {
            startService(assistIntent);
        }

//        startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), REQUEST_NOTICE_PERMISSION_CODE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(1100,new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN});
            }
        }else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(1100, new String[]{Manifest.permission.CALL_PHONE});
            }
        }
        // If CALL_ PHONE permission is not granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.MODIFY_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS)!= PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.MEDIA_CONTENT_CONTROL)!= PackageManager.PERMISSION_GRANTED) {

            // Request permission
            // Permissions
            // RequestCode: application-specific request code to match and report to OnRequestPermissionsResultCallback # onRequestPermissionsResult (int, String [], int [])}
            // That is, the OnRequestPermissionResult() method of the following callback
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.ANSWER_PHONE_CALLS
//                    ,Manifest.permission.MODIFY_PHONE_STATE,Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.MEDIA_CONTENT_CONTROL},10010);

            requestPermissions(1200, new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.ANSWER_PHONE_CALLS
                    ,Manifest.permission.MODIFY_PHONE_STATE,Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.MEDIA_CONTENT_CONTROL});
            Log.d(TAG, "request_ Permission(): Applying for permission!");
        }else {
            Log.d(TAG, "request_ Permission(): You already have permission!");
        }
        BluetoothLe.getDefault().addDeviceCallback(deviceCallback);

    }
    DeviceCallbackWrapper deviceCallback = new DeviceCallbackWrapper(){
        @SuppressLint("MissingPermission")
        @Override
        public void answerRingingCall() {
            List<byte[]> datas2 = CmdHelper.spliteData(CmdHelper.answerRingingCallToDevice());
            BluetoothLe.getDefault().writeDataToCharacteristic(datas2);
            TelecomManager tm = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tm = (TelecomManager) TelephoAvtivity.this.getSystemService(Context.TELECOM_SERVICE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tm.acceptRingingCall();
            }
        }

        @Override
        public void endRingingCall() {
            List<byte[]> datas2 = CmdHelper.spliteData(CmdHelper.endRingingCallToDevice());
            BluetoothLe.getDefault().writeDataToCharacteristic(datas2);
            PhoneUtil.endCall(TelephoAvtivity.this);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_NOTICE_PERMISSION_CODE){
//            requestNotice();
        }
    }

    private void requestNotice(){
        if(!isNotificationEnabled()) {
            mDialog = DialogHelperNew.showRemindDialog(this, getResources().getString(R.string.permisson_notication_title),
                    getResources().getString(R.string.permisson_notication_tips), getResources().getString(R.string.permisson_location_open), view -> {
                        startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), REQUEST_NOTICE_PERMISSION_CODE);
                        mDialog.dismiss();
                    }, view -> {
                        mDialog.dismiss();
                        TelephoAvtivity.this.finish();
                    });
        }else{
            Log.d(TAG,"Listen to the bracelet broadcast");
            Intent assistIntent = new Intent(this, PhoneReceiver.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(assistIntent);
            } else {
                startService(assistIntent);
            }
        }
    }

    private boolean isNotificationEnabled() {
        ContentResolver contentResolver = getContentResolver();
        String enabledListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        if (!TextUtils.isEmpty(enabledListeners)) {
            return enabledListeners.contains(PhoneReceiver.class.getName());
        } else {
            return false;
        }
    }
}
