package com.zhj.bluetooth.sdkdemo.ui;


import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_ANSWER_RINGING;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_ENTER_CAMERA;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_EXIT_CAMERA;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_HEARTRATE_DETECTION_STATUS;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_REALTIME_TEMP;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_TEMP_DETECTION_STATUS;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SEND_MESSAGE_P;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SET_HEARTRATE_DETECTION_STATUS;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SET_SEDENTARY;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SET_TARGET;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SET_TEMP_DETECTION_STATUS;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_SWITCH_CUSTOM_DIAL;
import static com.zhj.zhjsdkcustomized.ble.CmdHelper.CMD_GET_HART_OPEN;

import android.util.Log;

import com.google.gson.Gson;
import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.zhjsdkcustomized.bean.Goal;
import com.zhj.zhjsdkcustomized.bean.LongSit;
import com.zhj.zhjsdkcustomized.bean.TempInfo;
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
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getDeviceInfo(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DEVICE_INFO){
                    Log.d("gfderr3","Get device information");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getDeviceState(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DEVICE_STATE){
                    Log.d("gfderr3","Get device status");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getDevicePower(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DEVICE_POWER){
                    Log.d("gfderr3","Get device power");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getAlarmList(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_ALARM_CLOCK){
                    Log.d("gfderr3","Get device alarm clock");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getSedentary(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_SEDENTARY){
                    Log.d("gfderr3","Get device sedentary");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getDialInfo(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DIAL_INFO){
                    Log.d("gfderr3","Get the device dial");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.getNotice(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_NOTICE){
                    Log.d("gfderr3","Get device CODE_GET_NOTICE");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });

//        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.enterCamera(new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                Log.d("gfder3","handlerBleDataResult.bluetooth_code:" + handlerBleDataResult.bluetooth_code);
//                if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_ENTER_CAMERA){
//                    Log.d("gfderr3","Get device CODE_ENTER_CAMERA");
//
//
//                }
//
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//
//            }
//        });
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.exitCamera(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_EXIT_CAMERA){
                    Log.d("gfderr3","Send device CODE_EXIT_CAMERA");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.findPhone(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_FIND_PHONE){
                    Log.d("gfderr3","Send device CODE_FIND_PHONE");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.answerRingingCallToDevice(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_ANSWER_RINGING){
                    Log.d("gfderr3","Send device CODE_ANSWER_RINGING");
                }

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.endRingingCallToDevice(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_END_RINGING){
                    Log.d("gfderr3","Send device CODE_END_RINGING");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.startHeartTest(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == BleSdkWrapper.BLUETOOTH_CODE.CODE_START_HEART_TEST){
                    Log.d("gfderr3","Send device CODE_START_HEART_TEST");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        BleSdkWrapper.getRealtimeTemp(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_GET_REALTIME_TEMP) {
                    TempInfo tempInfo = (TempInfo) handlerBleDataResult.data;
                    Log.d("gfderr3", "temp:" + new Gson().toJson(tempInfo));
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        BleSdkWrapper.getHeartRateDetectionStatus(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_GET_HEARTRATE_DETECTION_STATUS) {
                    Log.d("gfderr3", "temp:" + handlerBleDataResult.data);
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        BleSdkWrapper.setHeartRateDetectionStatus(true, 1, new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_SET_HEARTRATE_DETECTION_STATUS) {
                    Log.d("gfderr3", "temp_set:" + handlerBleDataResult.data);
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        Goal goal = new Goal();
        goal.sleepstate = 1;
        goal.goalSleep = 1;
        goal.stepstate = 1;
        goal.goalStep = 10000;
        goal.calstate = 1;
        goal.goalCal = 1;
        goal.distancestate = 100;
        goal.goalDistanceKm = 1;
        BleSdkWrapper.setTarget(goal, new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_SET_TARGET){
                    Log.d("gfderr3","CODE_SET_TARGET");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        BleSdkWrapper.getTempDetectionStatus(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_GET_TEMP_DETECTION_STATUS){
                    Log.d("gfderr3","CODE_GET_TEMP_DETECTION_STATUS");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
        BleSdkWrapper.setTempDetectionStatus(true, 1, new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_SET_TEMP_DETECTION_STATUS){
                    Log.d("gfderr3","CODE_SET_TEMP_DETECTION_STATUS");
                }
            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });

//        BleSdkWrapper.switchCustomDial(1, new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                if(handlerBleDataResult.bluetooth_code == CODE_SWITCH_CUSTOM_DIAL){
//
//                }
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//
//            }
//        });
        //        BleSdkWrapper.deleteDial(1, new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                if(handlerBleDataResult.bluetooth_code == CODE_DELETE_DIAL){
//
//                }
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//
//            }
//        });
        LongSit longSit = new LongSit();
        longSit.setOnOff(true);
//        BleSdkWrapper.setSedentary(longSit, new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                if(handlerBleDataResult.bluetooth_code == CODE_SET_SEDENTARY){
//
//                }
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//
//            }
//        });
    }

}
