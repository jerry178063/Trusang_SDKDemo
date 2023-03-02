package com.zhj.bluetooth.sdkdemo.ui;


import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DEVICE_INFO;
import static com.zhj.zhjsdkcustomized.ble.BleSdkWrapper.BLUETOOTH_CODE.CODE_GET_DEVICE_POWER;

import android.bluetooth.BluetoothGattCharacteristic;
import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.zhjsdkcustomized.bean.BLEDevice;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class DeviceInfoActivity extends BaseActivity {
    @BindView(R.id.tvDeviceProduct)
    TextView tvDeviceProduct;
    @BindView(R.id.tvDeviceAddress)
    TextView tvDeviceAddress;
    @BindView(R.id.tvDeviceVersion)
    TextView tvDeviceVersion;
    @BindView(R.id.tvDevicePower)
    TextView tvDevicePower;
    @Override
    protected int getContentView() {
        return R.layout.activity_device_info;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.device_info_title));
        //The device name and signal value in the device information have been returned when searching for the device
        getPower();
        BleSdkWrapper.getDeviceInfo(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == CODE_GET_DEVICE_INFO){
                    BLEDevice bleDevice = (BLEDevice) handlerBleDataResult.data;
                    tvDeviceProduct.setText(getResources().getString(R.string.device_info_model)+bleDevice.mDeviceProduct);
                    tvDeviceAddress.setText(getResources().getString(R.string.device_info_address)+bleDevice.mDeviceAddress);
                    tvDeviceVersion.setText(getResources().getString(R.string.device_info_version)+bleDevice.mDeviceVersion);
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

    private void getPower(){
        //Get device power
        BleSdkWrapper.getDevicePower(new OnLeWriteCharacteristicListener() {
            @Override
            public void onSuccess(HandlerBleDataResult handlerBleDataResult) {
                if (handlerBleDataResult.bluetooth_code == CODE_GET_DEVICE_POWER){
                    int power = (int) handlerBleDataResult.data;
                    tvDevicePower.setText(getResources().getString(R.string.device_info_power)+power);
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
