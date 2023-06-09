package com.zhj.bluetooth.sdkdemo.ui;

import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_END_HEART_TEST;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_HISTROY_SENSOR;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_REALTIME_HEARTRATE;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_START_HEART_TEST;
import static com.zhj.zhjsdkcustomized.ble.bluetooth.BleManager.FLAT_START_GET_HEART_RATE;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhj.bluetooth.sdkdemo.MyAppcation;
import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.util.CustomToggleButton;
import com.zhj.zhjsdkcustomized.bean.HealthHeartRate;
import com.zhj.zhjsdkcustomized.bean.SensorHistoryBean;
import com.zhj.zhjsdkcustomized.ble.BleCallback;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.ByteDataConvertUtil;
import com.zhj.zhjsdkcustomized.ble.CmdHelper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.HealthHrDataHandler;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;
import com.zhj.zhjsdkcustomized.util.BaseCmdUtil;
import com.zhj.zhjsdkcustomized.util.LogUtil;
import com.zhj.zhjsdkcustomized.util.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class    HeartTestActivity extends BaseActivity {

    @BindView(R.id.mSwitch)
    CustomToggleButton mSwitch;
    @BindView(R.id.tvTag)
    TextView tvTag;
    @BindView(R.id.tv_heart_rate_data)
    TextView tv_heart_rate_data;

    @Override
    protected int getContentView() {
        return R.layout.activity_heart_test;
    }

    @Override
    protected void initView() {
        super.initView();
        tvTag.setText(getResources().getString(R.string.heart_test_swith));
        titleName.setText(getResources().getString(R.string.heart_test_title));

    }


    @OnClick(R.id.startHeart)
    void startHeart(){
        if (MyAppcation.getInstance().isConnected()) {
            BleSdkWrapper.getRealtimeHeartRate(new OnLeWriteCharacteristicListener() {
                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {

                    if (handlerBleDataResult.bluetooth_code == CODE_GET_REALTIME_HEARTRATE) {
                        HealthHeartRate healthHeartRate = (HealthHeartRate) handlerBleDataResult.data;
//                                Log.d("FFG3232ffff", "heart_data_success1:" + healthHeartRate);
                        Log.d("hhh333s", "heart_data_success:" + TimeUtil.timeStamp2Date(healthHeartRate.getDate()) + "  " + healthHeartRate.getSilentHeart());
                    }
                }


                @Override
                public void onFailed(WriteBleException e) {
                    Log.d(TAG, "e:" + e);
                }
            });

            BleSdkWrapper.startHeartTest(new OnLeWriteCharacteristicListener() {
                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                    if (handlerBleDataResult.bluetooth_code == CODE_START_HEART_TEST) {
                        Log.d("hhh333s", "startHeartTest_success:" + handlerBleDataResult.data);
                        LogUtil.d("Heart rate monitoring has started");
                        Log.d(TAG, "CODE_START_HEART_TEST");
                    }
                }


                @Override
                public void onFailed(WriteBleException e) {

                }
            });
        }
    }

   @OnClick(R.id.historyHeart)
    void getHistoryHeartData(){
        Log.d("gf43r4f4","historyHeart:" + MyAppcation.getInstance().isConnected());
       if (MyAppcation.getInstance().isConnected()) {
           tv_heart_rate_data.setText("");
           BleSdkWrapper.getHistroySensor(2023, 5, 4, 1, 10, 10, 5260, new OnLeWriteCharacteristicListener() {
               @Override
               public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                   if (handlerBleDataResult.bluetooth_code == CODE_GET_HISTROY_SENSOR) {
                       SensorHistoryBean sensorHistoryBean = (SensorHistoryBean) handlerBleDataResult.data;
                       Log.d("hhh333s", "sensorHistoryBean:" + new Gson().toJson(sensorHistoryBean));
                       List<SensorHistoryBean.HeartList> samplingList = sensorHistoryBean.getHeartList();
                       Log.d(TAG, "samplingList_size:" + samplingList.size());
                       tv_heart_rate_data.setText("heart_list:" + new Gson().toJson(sensorHistoryBean.getHeartList()));
                   }
               }

               @Override
               public void onFailed(WriteBleException e) {
                   Log.d(TAG, "e:" + e);
                   tv_heart_rate_data.setText("no data");
               }
           });
       }
    }

    @OnClick(R.id.endHeart)
    void endHeart(){
        if (MyAppcation.getInstance().isConnected()) {
            BleSdkWrapper.endHeartTest(new OnLeWriteCharacteristicListener() {
                @Override
                public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                    if (handlerBleDataResult.bluetooth_code == CODE_END_HEART_TEST) {
                        LogUtil.d("Heart rate monitoring has ended");
                        Log.d("hhh333s", "Heart rate monitoring has ended");
                    }
                }


                @Override
                public void onFailed(WriteBleException e) {

                }
            });
        }
    }
}
