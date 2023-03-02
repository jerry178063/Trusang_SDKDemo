package com.zhj.bluetooth.sdkdemo.sevice;

import android.app.Service;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;


import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.ByteDataConvertUtil;
import com.zhj.zhjsdkcustomized.ble.CmdHelper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.BluetoothLe;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;
import com.zhj.zhjsdkcustomized.util.BaseCmdUtil;

import java.util.List;

public class PhoneReceiver extends BroadcastReceiver {
    private static final String TAG = "FF4534";
    private int msgTypeMsg = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // If calling
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            String outNumber = this.getResultData();// Call number
            Log.i(TAG, "call OUT 1:" + phoneNumber);
            Log.i(TAG, "call OUT 2:" + outNumber);

        } else if ("android.intent.action.PHONE_STATE".equals(intent.getAction())) {
            // If it is a call
            TelephonyManager tManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            // Caller number
            String mIncomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.i(TAG, "call IN 1:" + state);
            Log.i(TAG, "call IN 2:" + mIncomingNumber);

            switch (tManager.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG, "**********************Incoming call detected!!!!*****");

                    BluetoothLe.getDefault().destroy(TAG);
                    BluetoothLe.getDefault().setOnWriteCharacteristicListener(TAG, new OnLeWriteCharacteristicListener() {
                        @Override
                        public void onSuccess(HandlerBleDataResult result) {
                        }

                        @Override
                        public void onSuccessCharac(BluetoothGattCharacteristic characteristic) {

                            byte[] to = new byte[20];
                            BaseCmdUtil.copy(characteristic.getValue(), to);
                            Log.d("FF4555","Reply 1: data demo:" + ByteDataConvertUtil.bytesToHexString(to));
                            if((to[0]&255) == 0x8A && (to[3]&255) == 0x00){
                                List<byte[]> datas = CmdHelper.setMessage2(1,mIncomingNumber  + "");
                                for (int i=0;i<datas.size();i++){
                                    Log.d("FF4555","Reply 2: data demo:" + ByteDataConvertUtil.bytesToHexString(to));
                                    Log.d("FF4555","Send the second stitch:" + ByteDataConvertUtil.bytesToHexString(datas.get(i)));
                                    BluetoothLe.getDefault().writeDataToCharacteristic(datas.get(i));
                                }
                                Log.d("FF4534","11111:");
                            }else if((to[0]&255) == 0x8A && (to[3]&255) == 0x01){
                                List<byte[]> datas2 = CmdHelper.setMessage2(2,mIncomingNumber + "");
                                for (int i=0;i<datas2.size();i++){
                                    Log.d("FF4555","Reply 3: data demo:" + ByteDataConvertUtil.bytesToHexString(to));
                                    Log.d("FF4555","Send the third stitch:" + ByteDataConvertUtil.bytesToHexString(datas2.get(i)));
                                    BluetoothLe.getDefault().writeDataToCharacteristic(datas2.get(i));
                                }
                                Log.d("FF4534","22222:" );
                            }else if((to[0]&255) == 0x8A && (to[3]&255) == 0x02){
                                Log.d("FF4555","Reply 4: data demo:" + ByteDataConvertUtil.bytesToHexString(to));
                                Log.d("FF4555","Send the fourth stitch:" + ByteDataConvertUtil.bytesToHexString(CmdHelper.END_MESSAGE));
                                BluetoothLe.getDefault().writeDataToCharacteristic(CmdHelper.END_MESSAGE);
                                Log.d("FF4534","33333:");
                            }else if((to[0]&255) == 0x8A && (to[3]&255) == 0x03){
                            }
                        }

                        @Override
                        public void onFailed(WriteBleException e) {
                            Log.d("FF4534","e:" + e);
                        }
                    });
                    Log.d("FF4555","Send the first stitch:" + ByteDataConvertUtil.bytesToHexString(CmdHelper.setMessageType(msgTypeMsg)));
                    BluetoothLe.getDefault().writeDataToCharacteristic(CmdHelper.setMessageType(msgTypeMsg));

//                    BluetoothLe.getDefault().writeDataToCharacteristic("telepho",CmdHelper.setMessageType(msgTypeMsg), new OnLeWriteCharacteristicListener() {
//
//                        @Override
//                        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                            Log.d("FF4534","回复第一针00:" + handlerBleDataResult.data);
//                        }
//
//                        @Override
//                        public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
//                            Log.d("FF4534","回复第一针:" + bluetoothGattCharacteristic.getValue());
//                        }
//
//                        @Override
//                        public void onFailed(WriteBleException e) {
//
//                        }
//                    });
//                    //获取当前步数
//                    BleSdkWrapper.getRealtimeSteps(new OnLeWriteCharacteristicListener() {
//                        @Override
//                        public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
//                            Log.d("FF4534","获取当前步数00:" + handlerBleDataResult.data);
//                        }
//
//                        @Override
//                        public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
//                        }
//
//                        @Override
//                        public void onFailed(WriteBleException e) {
//
//                        }
//                    });
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "**********************Phone answering detected!!!!************");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG, "**********************Hang-up detected!!!!*******************");
                    break;
            }
        }
    }
}
