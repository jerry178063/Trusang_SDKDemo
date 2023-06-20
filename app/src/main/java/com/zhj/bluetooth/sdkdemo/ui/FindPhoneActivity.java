package com.zhj.bluetooth.sdkdemo.ui;

import android.widget.EditText;

import com.zhj.bluetooth.sdkdemo.MyAppcation;
import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.zhjsdkcustomized.ble.DeviceCallbackWrapper;
import com.zhj.zhjsdkcustomized.ble.bluetooth.BluetoothLe;

import butterknife.BindView;

public class FindPhoneActivity extends BaseActivity {

    @BindView(R.id.etWeatherUnit)
    EditText etWeatherUnit;
    @BindView(R.id.etWeatherTemp)
    EditText etWeatherTemp;
    @BindView(R.id.etWeatherType)
    EditText etWeatherType;
    @Override
    protected int getContentView() {
        return R.layout.activity_weather;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.handband_search_mobile_phone));
        if(MyAppcation.getInstance().isConnected()) {
            BluetoothLe.getDefault().addDeviceCallback(new DeviceCallbackWrapper() {
                @Override
                public void findPhone() {
                    super.findPhone();
                }
            });
        }
    }
}
