package com.zhj.bluetooth.sdkdemo.ui;

import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_REALTIME_HEARTRATE;
import static com.zhj.zhjsdkcustomized.ble.bluetooth.BleManager.FLAT_START_GET_HEART_RATE;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.zhjsdkcustomized.bean.HealthHeartRate;
import com.zhj.zhjsdkcustomized.bean.HealthSport;
import com.zhj.zhjsdkcustomized.ble.BleCallback;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.HealthHrDataHandler;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;
import com.zhj.zhjsdkcustomized.util.BaseCmdUtil;

import butterknife.BindView;

public class StepsDataActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_steps;
    }

    @BindView(R.id.tvSteps)
    TextView tvSteps;
    @BindView(R.id.tvCal)
    TextView tvCal;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvFz)
    TextView tvFz;
    @BindView(R.id.tvSs)
    TextView tvSs;

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.steps_current_title));
        //Get current steps
        BleSdkWrapper.getRealtimeSteps(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {

            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }


    private void getCurrentRate(){
        //Get the current heart rate
        BleSdkWrapper.getRealtimeHeartRate(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == CODE_GET_REALTIME_HEARTRATE) {
                    Log.d("FFG3232r3", "getRealtimeHeartRate:" + handlerBleDataResult.bluetooth_code);
//                                        HealthHeartRate healthHeartRate = (HealthHeartRate) handlerBleDataResult.data;
//                                        Log.d("FFG3232r3", "data:" + healthHeartRate.toString());
                }
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
                HealthHrDataHandler healthHrDataHandler=new HealthHrDataHandler();
                byte[] to = new byte[20];
                BaseCmdUtil.copy(bluetoothGattCharacteristic.getValue(), to);
                int flag_start=to[0]&255;
                if(flag_start == FLAT_START_GET_HEART_RATE) {
                    HealthHeartRate healthHeartRate = healthHrDataHandler.handlerCurrent(to);
                    //Get heart rate value
                    Log.d("FFG3232r3", "heart_data:" + healthHeartRate.getSilentHeart());
                }
            }

            @Override
            public void onFailed(WriteBleException e) {
                Log.d("FFG3232r3", "e:" + e);
            }
        });
    }
}
