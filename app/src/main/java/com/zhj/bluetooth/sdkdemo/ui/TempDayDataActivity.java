package com.zhj.bluetooth.sdkdemo.ui;

import android.widget.TextView;

import com.zhj.bluetooth.sdkdemo.R;
import com.zhj.bluetooth.sdkdemo.base.BaseActivity;
import com.zhj.zhjsdkcustomized.bean.TempInfo;
import com.zhj.zhjsdkcustomized.ble.BleCallback;
import com.zhj.zhjsdkcustomized.ble.BleSdkWrapper;
import com.zhj.zhjsdkcustomized.ble.HandlerBleDataResult;
import com.zhj.zhjsdkcustomized.ble.bluetooth.OnLeWriteCharacteristicListener;
import com.zhj.zhjsdkcustomized.ble.bluetooth.exception.WriteBleException;

import butterknife.BindView;

public class TempDayDataActivity extends BaseActivity {
    @BindView(R.id.tvTemp)
    TextView tvTemp;

    @Override
    protected int getContentView() {
        return R.layout.activity_current_temp;
    }

    @Override
    protected void initView() {
        super.initView();
        titleName.setText(getResources().getString(R.string.main_current_temp));
        //The unit of body temperature is 0.01 degrees, and 3600 represents 36 degrees
    }
}
