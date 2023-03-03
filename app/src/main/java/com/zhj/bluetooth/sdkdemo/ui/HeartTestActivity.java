package com.zhj.bluetooth.sdkdemo.ui;

import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_END_HEART_TEST;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_START_HEART_TEST;

import android.bluetooth.BluetoothGattCharacteristic;
import android.widget.Button;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.bluetooth.sdkdemo.util.CustomToggleButton;
import com.zhj.zhjsdkcustomized.ble.BleCallback;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;
import com.zhj.zhjsdkcustomized.util.LogUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class    HeartTestActivity extends BaseActivity {

    @BindView(R.id.mSwitch)
    CustomToggleButton mSwitch;
    @BindView(R.id.tvTag)
    TextView tvTag;

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
        BleSdkWrapper.startHeartTest(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_START_HEART_TEST) {
                    LogUtil.d("Heart rate monitoring has started");
                }
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }

    @OnClick(R.id.endHeart)
    void endHeart(){
        BleSdkWrapper.endHeartTest(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if(handlerBleDataResult.bluetooth_code == CODE_END_HEART_TEST) {
                    LogUtil.d("Heart rate monitoring has ended");
                }
            }

            @Override
            public void onSuccessCharac(BluetoothGattCharacteristic bluetoothGattCharacteristic) {

            }

            @Override
            public void onFailed(WriteBleException e) {

            }
        });
    }
}
