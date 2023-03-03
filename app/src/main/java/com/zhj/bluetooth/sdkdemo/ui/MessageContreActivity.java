package com.zhj.bluetooth.sdkdemo.ui;

import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_NOTICE;

import android.bluetooth.BluetoothGattCharacteristic;
import android.view.View;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.views.ItemToggleLayout;
import com.zhj.zhjsdkcustomized.bean.AppNotice;
import com.zhj.zhjsdkcustomized.bean.DeviceState;
import com.zhj.zhjsdkcustomized.ble.BleCallback;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.BluetoothLe;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageContreActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_message;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.message_info_title));
        //Please turn on the message switch in the device status before sending
        DeviceState deviceState = new DeviceState();
        deviceState.isNotice = 1;
//        BleSdkWrapper.setDeviceState(deviceState, new OnLeWriteCharacteristicListener() {
//            @Override
//            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//            }
//
//            @Override
//            public void onFailed(WriteBleException e) {
//
//            }
//        } );

        BleSdkWrapper.getNotice(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_GET_NOTICE) {
                    AppNotice appNotice = (AppNotice) handlerBleDataResult.data;
                }
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        } );

    }
    //English
    @OnClick(R.id.tvSendMessageEh)
    void sendMessageEh(){
        /**
         * CATEGORYID_INCOMING = 0x00,//Incoming call
         * CATEGORYID_SMS = 0x01,
         * CATEGORYID_WEIXIN = 0x02
         * CATEGORYID_MQQ = 0x03,
         * CATEGORYID_FACEBOOK = 0x04,
         * CATEGORYID_SKYPE = 0x05,
         * CATEGORYID_TWITTER = 0x06,
         * CATEGORYID_WHATISAPP = 0x07,
         * CATEGORYID_LINE = 0x08 ,
         * CATEGORYID_EMAIL = 0x09 ,
         * CATEGORYID_INSTAGRAM = 0x0A ,
         * CATEGORYID_LINKEDIN = 0x0B ,
         * CATEGORYID_UNKNOW = 0xFF,  //Custom message
         */


    }
    //French
    @OnClick(R.id.tvSendMessageFr)
    void sendMessageFr(){

    }
    //Spanish
    @OnClick(R.id.tvSendMessageEs)
    void sendMessageEs(){

    }
    //German
    @OnClick(R.id.tvSendMessageDe)
    void sendMessageDe(){

    }

}
