package com.zhj.bluetooth.sdkdemo.ui;

import android.bluetooth.BluetoothGattCharacteristic;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.zhjsdkcustomized.bean.DeviceState;
import com.zhj.zhjsdkcustomized.ble.BleCallback;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class DeviceStateActivity extends BaseActivity {

    @BindView(R.id.tvScreenLight)
    TextView tvScreenLight;
    @BindView(R.id.tvScreenTime)
    TextView tvScreenTime;
    @BindView(R.id.tvScreenTheme)
    TextView tvScreenTheme;
    @BindView(R.id.tvLanguage)
    TextView tvLanguage;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.tvTimeFormat)
    TextView tvTimeFormat;
    @BindView(R.id.tvUpHander)
    TextView tvUpHander;
    @BindView(R.id.tvMusic)
    TextView tvMusic;
    @BindView(R.id.tvNotice)
    TextView tvNotice;
    @BindView(R.id.tvHandHabits)
    TextView tvHandHabits;

    @Override
    protected int getContentView() {
        return R.layout.activity_device_state;
    }
    private DeviceState deviceState;
    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.device_state_title));
        BleSdkWrapper.getDeviceState(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                deviceState = (DeviceState) handlerBleDataResult.data;
                if(deviceState != null) {
                    tvScreenLight.setText(getResources().getString(R.string.device_state_screen_brightness) + deviceState.screenLight);
                    tvScreenTime.setText(getResources().getString(R.string.device_state_bright_duration) + deviceState.screenTime);
                    tvScreenTheme.setText(getResources().getString(R.string.device_state_theme) + deviceState.theme);
                    //  0x00: English 0x01: Chinese 0x02: Russian 0x03: Ukrainian 0x04: French 0x05: Spanish
                    //  0x06: Portuguese 0x07: German 0x08: Japan 0x09: Poland 0x0A: Italy
                    //0x0B: Romania 0x0C: Traditional Chinese 0x0D: Korean
                    tvLanguage.setText(getResources().getString(R.string.device_state_language) + deviceState.language);
                    //0x00: Metric 0x01: English
                    tvUnit.setText(getResources().getString(R.string.device_state_unit) + deviceState.unit);
                    //0x00:24 hour system 0x01:12 hour system
                    tvTimeFormat.setText(getResources().getString(R.string.device_state_time_system) + deviceState.timeFormat);
                    //0x00: Off 0x01: On
                    tvUpHander.setText(getResources().getString(R.string.device_state_handle_up) + deviceState.upHander);
                    //0x00: Off 0x01: On
                    tvMusic.setText(getResources().getString(R.string.device_state_music_control) + deviceState.isMusic);
                    //0x00: Off 0x01: On
                    tvNotice.setText(getResources().getString(R.string.device_state_messagr_swith) + deviceState.isNotice);
                    //0x01: left 0x02: right other: invalid
                    tvHandHabits.setText(getResources().getString(R.string.device_state_hand_hibits) + deviceState.handHabits);
                    tvHandHabits.setText(getResources().getString(R.string.body_temperature_unit) + deviceState.tempUnit);
                }
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        } );
        rightText.setText(getResources().getString(R.string.scan_device_set));
        rightText.setOnClickListener(view -> {
            //Set temperature unit
            if(deviceState != null) {
                deviceState.screenLight = 80;
                deviceState.tempUnit = deviceState.tempUnit == 0 ? 1 : 0;
                BleSdkWrapper.setDeviceState(deviceState, new OnLeWriteCharacteristicListener() {
                    @Override
                    public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                        showToast(getResources().getString(R.string.toast_set_success));
                        DeviceStateActivity.this.finish();
                    }

                    @Override
                    public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

                    }

                    @Override
                    public void onFailed(WriteBleException e) {

                    }
                });
            }
        });
//
    }
}
