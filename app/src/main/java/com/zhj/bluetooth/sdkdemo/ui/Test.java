package com.zhj.bluetooth.sdkdemo.ui;

import android.bluetooth.BluetoothGattCharacteristic;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;
import com.zhj.zhjsdkcustomized.util.LogUtil;

import java.util.Deque;

public class Test extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_device_info;
    }


    @Override
    protected void initView() {
        super.initView();
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getDeviceInfo(MyListener);
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getDeviceState(MyListener);
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getDevicePower(MyListener);
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getAlarmList(MyListener);
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getSedentary(MyListener);
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getDialInfo(MyListener);
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getNotice(MyListener);
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.enterCamera(MyListener);
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.exitCamera(MyListener);
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.findPhone(MyListener);
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.answerRingingCallToDevice(MyListener);
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.endRingingCallToDevice(MyListener);
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.startHeartTest(MyListener);
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.sendMessage(1,"ssssss","sssss",MyListener);
    }

    private OnLeWriteCharacteristicListener MyListener = new OnLeWriteCharacteristicListener() {
        @Override
        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
            LogUtil.d("handlerBleDataResult.bluetooth_code:"+handlerBleDataResult.bluetooth_code);
            if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DEVICE_INFO){
                LogUtil.d("Get device information");
            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DEVICE_STATE){
                LogUtil.d("Get device status");
            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DEVICE_POWER){
                LogUtil.d("Get device power");
            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_ALARM_CLOCK){
                LogUtil.d("Get device alarm clock");
            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_SEDENTARY){
                LogUtil.d("Get device sedentary");
            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DIAL_INFO){
                LogUtil.d("Get the device dial");
            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_NOTICE){
                LogUtil.d("Get device messages");
            }else if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_SEND_MESSAGE){
                LogUtil.d("Send device message");
            }
        }

        @Override
        public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

        }

        @Override
        public void onFailed(WriteBleException e) {

        }
    };

}
